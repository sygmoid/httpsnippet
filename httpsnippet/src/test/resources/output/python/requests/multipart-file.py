import requests

url = "http://mockbin.com/har"

payload = {}

headers = {
	"content-type": "multipart/form-data"
}

response = requests.request("POST", url, data=payload, headers=headers)

print(response.text)