# Description
Demo application to test Vertx and Reactive Hibernate monitoring for AppDynamics

# Components
App expects to connect MSSQL (currently in Azure)

## Commands
curl -X POST -H 'content-type:application/json' -d '{"name":"david", "type":"DOG"}' localhost:8888/pets -vvvv
curl localhost:8888/pets -vvvv
curl -X DELETE localhost:8888/pets -vvvv

helm upgrade --install vertx-demo helm/vertx-demo -n cicd --set image.tag=20231104.5 --set image.repository=10.10.11.109:8082/david-machacek/vertx-demo

curl -X POST -H 'content-type:application/json' -d '{"name":"david", "type":"DOG"}' vertx-demo:8888/pets -vvvv
curl vertx-demo:8888/pets -vvvv


for n in {1..10}; do curl vertx-demo:8888/pets -vvvv; done