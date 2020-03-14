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


Nexus:

docker pull sonatype/nexus3
docker volume create --name nexus-data
docker container run -d -p 8081:8081 --name nexus -v //d/tmp/docker/nexus:/nexus-data sonatype/nexus3:latest

Password located into this persisted folder. File called: admin.password
user: admin
https://hub.docker.com/r/sonatype/nexus3/

Docker:

docker pull jenkins/jenkins
docker volume create jenkins_home
docker run -p 8082:8080 -p 50000:50000 --name my_jenkins -v jenkins_home:/var/jenkins_home jenkins/jenkins

user: docker/@D0cker144