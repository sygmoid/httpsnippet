package main

import (
	"fmt"
	"time"
	"strings"
	"net/http"
	"io/ioutil"
)

func main() {

	client := http.Client{
		Timeout: time.Duration(10 * time.Second),
	}

	url := "http://mockbin.com/har"

	payload := strings.NewReader("Hello World")

	req, _ := http.NewRequest("POST", url, payload)

	req.Header.Add("content-type", "text/plain")

	res, _ := client.Do(req)

	defer res.Body.Close()
	body, _ := ioutil.ReadAll(res.Body)

	fmt.Println(res)
	fmt.Println(string(body))

}