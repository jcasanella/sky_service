$message = @{ 
    items = @(
        @{ name = "Test01"; id = 100 }
    )
}
         
$JSON = $message | convertto-json 

$JSON

Invoke-WebRequest -uri "http://localhost:8080/create-order" -Method POST -Body $JSON -ContentType "application/json"

Invoke-WebRequest -uri "http://localhost:8080/item/100" 