import os
from openai import OpenAI
from youtube_transcript_api import YouTubeTranscriptApi

# API 설정
api_key = os.getenv("OPEN_API_KEY")
# client = OpenAI(api_key=api_key)

client = OpenAI(base_url="http://127.0.0.1:1234/v1", api_key='lm-studio')


def get_video_id(url):
    return url.split("v=")[1].split("&")[0]


def get_transcript(video_id):
    ytt_api = YouTubeTranscriptApi()
    # transcript_list = ytt_api.list(video_id)
    # print(transcript_list)
    fetched_transcript = ytt_api.fetch(video_id, languages=['en'])

    return " ".join(snippet.text for snippet in fetched_transcript)


def summerise_text(transcript):
    response = client.responses.create(
        model='google/gemma-4-e4b',
        input=[
            {"role": "system", "content": "당신은 글 내용을 간단하고 명확하게 요약 정리하는 일의 전문가입니다."},
            {"role": "user", "content": f"제공되는 유튜브 스크립트 내용을 한글로 요약, 정리해 주세요.:\n{transcript}"}
        ],
        temperature=0.7,
        # max_output_tokens=1024
    )
    return response.output_text


def main():
    url = input("유튜브 링크 입력: ").strip()

    try:
        video_id = get_video_id(url)
        print("\n자막 가져오는 중... video_id: " + video_id)

        transcript = get_transcript(video_id)
        print(transcript)

        print("요약 중...")
        summary = summerise_text(transcript)

        print("\n==== 요약 결과 ====\n")
        print(summary)
    except Exception as e:
        print(f"Error summarising with ChatGPT: {e}")


if __name__ == "__main__":
    main()
