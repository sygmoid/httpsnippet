import Foundation

let headers = ["Cookie": "foo=bar;bar=baz","content-type": "application/x-www-form-urlencoded","accept": "application/json"]

let postData = NSMutableData(data: "foo=bar".data(using: String.Encoding.utf8)!)

let request = NSMutableURLRequest(url: NSURL(string: "http://mockbin.com/har?key=value")! as URL,
                                        cachePolicy: .useProtocolCachePolicy,
                                    timeoutInterval: 10)
request.httpMethod = "POST"
request.allHTTPHeaderFields = headers
request.httpBody = postData as Data

let session = URLSession.shared
let dataTask = session.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
 if (error != nil) {
  print(error)
 } else {
  let httpResponse = response as? HTTPURLResponse
  print(httpResponse)
 }
})

dataTask.resume()