CURL *hnd = curl_easy_init();

curl_easy_setopt(hnd, CURLOPT_CUSTOMREQUEST, "POST");
curl_easy_setopt(hnd, CURLOPT_URL, "http://mockbin.com/har?key=value");

struct curl_slist *headers = NULL;
headers = curl_slist_append(headers, "accept: application/json");
headers = curl_slist_append(headers, "content-type: application/x-www-form-urlencoded");
curl_easy_setopt(hnd, CURLOPT_HTTPHEADER, headers);

curl_easy_setopt(hnd, CURLOPT_COOKIE, "foo=bar;bar=baz");

CURLcode ret = curl_easy_perform(hnd);