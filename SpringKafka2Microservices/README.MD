# Experimento usando SpringBoot com Kafka e Docker

Simulei produtor e consumidor usando Kafka como plataforma distribuída de Streaming, com todo o ambiente dentro do container docker. Usei docker-compose para facilitar a criação e inicialização dos containeres do Kafka (e sua dependencia do Zookeeper), junto com os dois microsserviços que criei como produtor e consumidor.

### pre-requisitos de ambiente:
ter instalado o docker, docker-compose, Java 11 e Maven

## para compilar o projeto do consumidor:
`cd SpringKafka2Microservices/springKafkaConsumer`
`mvn package`

## para compilar o projeto do produtor:
`cd SpringKafka2Microservices/springKafkaProducer`
`mvn package`

## para criar os containeres e executar toda a solução:
`cd SpringKafka2Microservices`
`docker-compose up`

## para parar os containeres da solução:
`cd SpringKafka2Microservices`
`docker-compose down`
