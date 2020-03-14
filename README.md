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
docker container run -d -p 8081:8081 --name nexus -v nexus-data:/nexus-data sonatype/nexus3:latest

Password located into this persisted folder. File called: admin.password
user: admin/@N3xus144
https://hub.docker.com/r/sonatype/nexus3/

Docker:

docker pull jenkins/jenkins
docker volume create jenkins_home
docker container run -d -p 8082:8080 -p 50000:50000 --name my_jenkins -v jenkins_home:/var/jenkins_home jenkins/jenkins

user: docker/@D0cker144

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