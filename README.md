| Description | HTTP method | URL | Request Body | Status Code | Response Example |
| ----------- | ----------- | --- | ------------ | ----------- | ---------------- |
| Create customer | POST | v1/customers/create | { "personalId": "XXXXXX", name": "AAAA", "surname": "BBBBB", "dob": "YYYY-MM-DD", "address": "RRRRR", "zipcode": "xxxxx", "city": "CCCC" } | 201 Created | { "call": "customers/create", "method:": "post", "personalId": "XXXXXX", name": "AAAA", "surname": "BBBBB", "dob": "YYYY-MM-DD", "address": "RRRRR", "zipcode": "xxxxx", "city": "CCCC", "loadTS": "YYYY-MM-DDTHH:MM:SS.sss" } |
| Get all customers | GET | v1/customers | N/A | 200 OK | [ { "personalId": "XXXXXX", name": "AAAA", "surname": "BBBBB", "dob": "YYYY-MM-DD", "address": "RRRRR", "zipcode": "xxxxx", "city": "CCCC", "loadTS": "YYYY-MM-DDTHH:MM:SS.sss" }, { "personalId": "XXXXXX", name": "AAAA", "surname": "BBBBB", "dob": "YYYY-MM-DD", "address": "RRRRR", "zipcode": "xxxxx", "city": "CCCC", "loadTS": "YYYY-MM-DDTHH:MM:SS.sss" } ] |
| Get by customer id | GET | v1/customers/personalId/xxxx | N/A | 200 OK | { { "personalId": "XXXXXX", name": "AAAA", "surname": "BBBBB", "dob": "YYYY-MM-DD", "address": "RRRRR", "zipcode": "xxxxx", "city": "CCCC", "loadTS": "YYYY-MM-DDTHH:MM:SS.sss" } | 

$message = @{ 
    items = @(
        @{ name = "Test01"; id = 100 }
    )
}
         
$JSON = $message | convertto-json 

$JSON

Invoke-WebRequest -uri "http://localhost:8080/create-order" -Method POST -Body $JSON -ContentType "application/json"

Invoke-WebRequest -uri "http://localhost:8080/item/100" 


$message = @{ 
    id = 10
    items = @(
        @{ name = "Test01"; id = 100 }
    )
}

$JSON = $message | convertto-json 

{
    "id":  10,
    "items":  [
                  {
                      "id":  100,
                      "name":  "Test01"
                  }
              ]
}

Invoke-WebRequest -uri "http://localhost:8080/item/10" 

## Docker with Jenkins and Nexus

We have created a docker-compose with a custom Jenkins image. To run the docker compose execute the following command:
```
docker-compose build --no-cache
docker-compose up
```

Both images have their own volumes in order to persist the plugins, settings and artifacts.

### Jenkins image

This image creates a custom jenkins image.  
Some of these settings can be set up either in the docker-compose or Dockerfile.  
By default the user and pass is: **admin** / **admin**  
Runs in port 8082  
It installs the default plugins and some extra plugins required by this project:

* sbt
* nexus platform
* docker

**Note**: Plugins can be checked in https://updates.jenkins.io/download/plugins/

### Nexus image

This will store the published artifacts after the build process. The port is defined in the docker-compose file.
It's running in the port 8081

### TODO
Create jenkins credentials to github using ssh
https://medium.com/@khandelwal12nidhi/setup-ssh-between-jenkins-and-github-6ec7c7933244

Install sbt-plugin, enable installation - global tools
nexus-platform plugin
Check nexus ip to be used from jenkins (docker container inspect nexus)
Create credentials file in Path.userHome/.sbt/sonatype_credentials with the following content
realm=Sonatype Nexus Repository Manager
host=localhost
user=admin
password=@N3xus144

add the following line to plugins.sbt
credentials += Credentials(Path.userHome / ".sbt" / "sonatype_credentials")

and the following lines to build.sbt
```
    publishTo := {
      val nexus = "http://localhost:8081/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "repository/maven-snapshots/")
      else
        Some("releases"  at nexus + "repository/maven-releases/")
    }
```