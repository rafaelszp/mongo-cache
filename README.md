# mongo cache

## Escopo

Este exemplo é de uma aplicação JAX-RS que consulta informações direto do db NoSQL Mongodb, 
que neste caso é utilizado somente como cache.

## Fora do escopo
Não faz parte do escopo deste projeto:

1. Modelagem e implementação de rotinas referentes ao banco principal(produção);
1. Definição de estratégias de cache;
    - Ver: https://zubialevich.blogspot.com/2018/08/caching-strategies.html
    - Ver: https://codeahoy.com/2017/08/11/caching-strategies-and-how-to-choose-the-right-one/
1. Tratar sobre DTOs, VO's e outras estratégias de mapeamento;
1. Detalhes de funcionamento e demais características do Mongodb
    - Ver: https://mongodb.github.io/mongo-java-driver/3.11/driver/tutorials/
    - Ver: https://docs.mongodb.com/manual/reference/
    
## Detalhes da aplicação exemplo

1. Existe um scheduler para esquentar o cache
    - Ver: `sfieg.mongocache.users.UserCacheWarmer`;
1. Acesso ao database MongoDB é tratado na classe `sfieg.mongocache.mongo.MongoResource`  por meio de CDI;
1. Operações na collection `users` são tratadas na classe `sfieg.mongocache.users.UserService`
1. Recurso REST descrito na classe `sfieg.mongocache.users.UserEndpoint`
1. Configurações do projeto em `src/main/resources/project-defaults.yml`


## Rodando a aplicação

1. **Maven**:
    ```shell script
    mvn thorntail:run 
    ```
2. **IDE**: crie uma configuração para rodar classe `org.wildfly.swarm.Swarm`