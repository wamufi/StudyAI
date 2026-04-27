# import os
from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()
summarizer = pipeline("summarization", model="facebook/bart-large-cnn")


class TextRequest(BaseModel):
    text: str


@app.get("/")
def root():
    return {"msg": "hello"}


@app.post("/summarize")
def summarize(req: TextRequest):
    # text = req.text
    # summary = text[:100]
    # return {"summary": summary}
    result = summarizer(req.text, max_length=60, min_length=20)
    return {"summary": result[0]["summary_text"]}
