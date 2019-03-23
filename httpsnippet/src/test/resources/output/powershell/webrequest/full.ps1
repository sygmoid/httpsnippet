$headers=@{}
$headers.Add("accept", "application/json")
$headers.Add("content-type", "application/x-www-form-urlencoded")
$session = New-Object Microsoft.PowerShell.Commands.WebRequestSession
$cookie = New-Object System.Net.Cookie
$cookie.Name = 'foo'
$cookie.Value = 'bar'
$cookie.Domain = 'null'
$session.Cookies.Add($cookie)
$cookie = New-Object System.Net.Cookie
$cookie.Name = 'bar'
$cookie.Value = 'baz'
$cookie.Domain = 'null'
$session.Cookies.Add($cookie)
$response = Invoke-WebRequest -Uri 'http://mockbin.com/har?baz=abc&foo=bar&foo=baz&key=value' -Method POST -Headers $headers -WebSession $session