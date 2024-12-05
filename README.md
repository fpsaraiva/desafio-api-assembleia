# api-assembleia

Implementação do desafio técnico referente à criação de uma solução bacckend para gerenciar sessões de votação em 
assembleias de cooperativas.

Tecnologias utilizadas:
* Java 21
* Spring
* Gradle
* PostgreSQL
* Apache Kafka
* Docker
* Springdoc OpenAPI
* jUnit
* Mockito

## Como rodar?

* Realizar o clone do repositório com o seguinte comando:
```
git clone https://github.com/fpsaraiva/desafio-api-assembleia.git
```

* Rodar o seguinte comando no terminal para subir o banco de dados localmente:
```
docker compose up -d
```

* Para iniciar a aplicação, rodar o seguinte comando no terminal:
```
./gradlew bootRun
```
Comando (no terminal também) para finalizar a aplicação: Ctrl + C

* Para acessar o Kafka e fazer a leitura dos eventos, referentes ao resultado da votação, executar o seguinte comando 
(no terminal):
```
docker exec -it kafka-api-assembleia kafka-console-consumer --bootstrap-server localhost:9092 --topic assembleia-topic --from-beginning
```

* Acessar a documentação da api em http://localhost:8080/swagger-ui/index.html

## Comentários sobre algumas tomadas de decisão

* Com o intuito de simplificar o design da solução, a votação é realizada dentro de uma sessão de votação. Cada sessão, 
por sua vez, é relacionada a uma pauta específica.
* Pelo mesmo motivo, não foi implementado CRUD completo nos endpoints de Pauta, Sessão e Voto.
* Foi implementado um cadastro para associados, de maneira a facilitar o registro de votação. Neste cadastro, o CPF 
não chega a ser validado.
* A criação de testes unitários esteve focada nas classes de Controller e Service.
* Na tarefa bônus 1, foi realizada a criação do cliente para integrar com sistema externo de validação de CPF. No entanto, 
como a API encontra-se fora do ar, a implementação do método no respectivo service não foi realizada.
* Na tarefa bônus 2, foi criado somente um produtor na aplicação. As mensagens produzidas podem ser lidas diretamente via 
Kafka.
* Tarefa bônus 4 - Versionamento da API: Caso a API seja mais estável, não apresentando tantas mudanças estruturais com
frequência (o contrato de múltiplos endpoints é alterado constantemente, por exemplo), utilizaria uma estratégia de
versionamento diretamente no caminho do URI.

## O que poderia melhorar?

* Na criação de uma sessão, adicionar uma verificação se já existe uma sessão aberta para aquela pauta. Caso positivo, 
permitir abrir uma sessão de votação somente após a sessão atual estiver fechada.
* Verificar a necessidade de implementar vínculo da entidade Associado à entidade Voto.
* Retornar o título da pauta, no momento de listar resultado de uma votação.
