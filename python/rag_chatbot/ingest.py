# PDF 문서 넣기
from typing import List
from langchain_community.document_loaders import PyPDFLoader
from langchain_text_splitters import RecursiveCharacterTextSplitter
from langchain_community.vectorstores import Chroma
from langchain_core.embeddings import Embeddings
from openai import OpenAI
import time

# Custom Embedding
class PDFEmbedding(Embeddings):
    def __init__(self, model: str = "nomic-ai/nomic-embed-text-v1.5-GGUF"):
        self.client = OpenAI(base_url="http://127.0.0.1:1234/v1", api_key='lm-studio')
        self.model = model
        
    def embed_documents(self, texts: List[str]) -> List[List[float]]:
        return self._embed(texts)

    def embed_query(self, text: str) -> List[float]:
        return self._embed([text])[0]

    def _embed(self, texts: List[str]) -> List[List[float]]:
        max_retries = 3
        for attempt in range(max_retries):
            try:
                response = self.client.embeddings.create(model=self.model, input=texts)
                return [e.embedding for e in response.data]

            except Exception as e:
                if attempt == max_retries - 1:
                    raise e
                time.sleep(1 * (attempt + 1))


# ingest.py
def run():
    loader = PyPDFLoader("../data/rag_sample.pdf")
    documents = loader.load()

    splitter = RecursiveCharacterTextSplitter(chunk_size=800, chunk_overlap=50)
    docs = splitter.split_documents(documents)

    embeddings = PDFEmbedding()

    db = Chroma.from_documents(docs, embedding=embeddings, persist_directory="./chroma")

    db.persist()
    print("ingest 완료")


if __name__ == "__main__":
    run()

