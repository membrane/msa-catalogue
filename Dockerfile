FROM openjdk

COPY target/*.jar .

CMD ["java","-jar","catalogue-0.0.1-SNAPSHOT.jar","--spring.cloud.consul.host=consul"]