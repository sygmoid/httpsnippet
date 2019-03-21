var client = new RestClient("http://mockbin.com/har?key=value");
var request = new RestRequest(Method.POST);
request.AddHeader("accept", "application/json");
request.AddHeader("content-type", "application/x-www-form-urlencoded");
request.AddCookie("foo", "bar");
request.AddCookie("bar", "baz");
IRestResponse response = client.Execute(request);