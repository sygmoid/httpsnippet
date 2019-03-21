import Foundation

let request = NSMutableURLRequest(url: NSURL(string: "http://mockbin.com/har?key=value")! as URL,
                                        cachePolicy: .useProtocolCachePolicy,
                                    timeoutInterval: 10)
request.httpMethod = "GET"

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