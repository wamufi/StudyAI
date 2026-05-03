from fastapi import FastAPI
from fastapi.responses import StreamingResponse
from langchain_community.vectorstores import Chroma
from rag_chatbot.ingest import PDFEmbedding
from openai import OpenAI

class RagService:

    def __init__(self):
        self.embedding = PDFEmbedding()
        self.db = Chroma(persist_directory="./chroma", embedding_function=self.embedding)
        self.llm = OpenAI(base_url="http://127.0.0.1:1234/v1", api_key='lm-studio')

    def retrieve(self, query: str, k: int = 4):
        docs = self.db.similarity_search(query, k=k)
        return docs
    
    def build_prompt(self, query: str, docs):
        context = "\n\n".join([d.page_content for d in docs])

        prompt = f"""
        너는 문서 기반 QA 시스템이다.
        context에 없는 내용은 절대 추측하지 마라.

        context:
        {context}

        question:
        {query} 
        """

        return prompt
    
    def stream_answer(self, prompt: str):
        response = self.llm.chat.completions.create(
            model='google/gemma-4-e4b',
            messages=[
                {"role": "system", "content": "You are a helpful assistant."},
                {"role": "user", "content": prompt}
            ],
            stream=True
        )

        for chunk in response:
            if chunk.choices[0].delta.content:
                yield chunk.choices[0].delta.content


# app.py
app = FastAPI()
rag = RagService()

@app.get("/rag")
def ask(q: str):
    docs = rag.retrieve(q)
    prompt = rag.build_prompt(q, docs)

    # 3. 스트리밍 응답
    return StreamingResponse(rag.stream_answer(prompt=prompt), media_type="text/plain")