var data = "foo=bar";

var xhr = new XMLHttpRequest();
xhr.withCredentials = true;

xhr.addEventListener("readystatechange", function () {
  if (this.readyState === this.DONE) {
    console.log(this.responseText);
  }
});

xhr.open("POST", "http://mockbin.com/har?baz=abc&foo=bar&foo=baz&key=value");
xhr.setRequestHeader("Cookie", "foo=bar; bar=baz");
xhr.setRequestHeader("content-type", "application/x-www-form-urlencoded");
xhr.setRequestHeader("accept", "application/json");

xhr.send(data);
