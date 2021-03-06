
## API definitions

In this liknk there's the [API definition](doc/api_definition.ods) for this project.
Just to sum up:

* **POST** `v1/customers/create` Create a new customer
* **GET** `v1/customers` Get all the customers
* **GET** `v1/customers/:id` Return customer by Id

TODO:
* Send correct msg answer following SPEC
* Change Date for the Java8 new date

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