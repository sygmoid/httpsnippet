import requests

url = "http://mockbin.com/har?key=value"

querystring = {"foo":["bar","baz"],"baz":["abc"]}

payload = {"foo":"bar"}

headers = {
	"Cookie": "foo=bar;bar=baz","content-type": "application/x-www-form-urlencoded","accept": "application/json"
}

response = requests.request("POST", url, data=payload, headers=headers, params=querystring)

print(response.text)