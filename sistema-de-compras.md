# Market Place

O Market place é o microsserviço core do ecosistema da ZupEdu-Commerce é atráves deste que os clientes de todo Brasil e Mundo irão realizar a compra de diversos produtos.

A principal responsabilidade deste sistema é receber a requisição de compra, se integrar com os microsserviços de **_Gerenciamento de Usuarios_** e **_Catalogo de Produto_** para coletar as informações necessárias para realização de uma Venda. 

Inicialmente ZupEdu-Commerce ira aceitar pagamentos somente via cartão  de credito, e para que a venda seja realizada é necessário que o MarketPlace se connecte de maneira sincrona através do protocolo HTTP ao serviço de pagamentos, para submeter o pagamento. Cada Venda devera ser registrada no Banco de Dados independente se o pagamento for aprovado ou recusado. Caso o pagamento seja aprovado, é necessário que seja inserido um evento no topico de **_vendas_**.


## Restrições
- Toda compra deve conter os seguintes campos:
    - idUsuario;
    - Coleção de Produtos, onde um produto é composto por seu id e quantidade a ser comprada;
    - Informacoes para Pagamento;
    - Todo pagamento necessita de: 
        - Titular do cartão;
        - Numero do cartão;
        - Mes/Ano de validate no cartão.
        - Codigo de Segurança
- Todos os campos são obrigatórios;
- O campo produtos deve conter no minimo 1 produto;
- Sobre os dados de pagamentos:
    - numero do cartao de conter 16 caracteres;
    - a data de validade do cartão deve estar no futuro ou presente;
    - codigo de segurança deve conter 3 caracteres;

Abaixo tem um exemplo de payload
```json
{
    "usuario": 1,
    "produtos":[
        {
            "id":1,
            "quantidade":1
        }
    ],
    "pagamento":{
        "titular": "Jose Humberto Coimbra Jr",
        "numero": "5478659832154582",
        "validoAte": "2025-08",
        "codigoSeguranca": 429,
    }
}
```


Para que você realize a Venda ao usuario, é necessário que contenha o nome, cpf, endereco, data de nascimento e email para envio caso da notafiscal caso o pagamento seja aprovado. 

Para obter estas informações você precisará se conectar ao sistema de **_Gerenciamento de Usuarios_**, a documentação deste sistema se encontra no seguinte endereço: 
> http://localhost:9090/swagger-ui/index.html

Para conseguir submeter o pagamento da Venda, é necessário que o valor total seja computado. O sistema detentor das informações sobre os produtos é o microsserviço de **_Catalogo de Produto_**, e a sua documentação é encontrada no seguinte endereço: 

> http://localhost:8082/swagger-ui/index.html

Após computar o valor da Venda, é possivel submeter uma requisição ao Sistema de Pagamentos, para verificar se de fato a venda sera aprovada ou recusada. A documentação do **_Serviço de Pagamentos_** esta disponivel no seguinte endereço: 

> http://localhost:8081/swagger-ui/index.html


Caso o Pagamento seja aprovado, é necessário que um evento de Venda seja inserido no topico `Venda`, um exemplo de mensagem esta descrito abaixo.

```json
   {
    "codigoPedido": "a7056a61-47d6-4c63-9c09-2bbfe64cd22d"
    "comprador": {
        "nome":"Douglas Patrick Boaventura",
        "cpf": "09581234039",
        "endereco":"Av Jose Joao residencial caipira, n 78, Jardim Brogota - Fortaleza,CE - 35697-586",
        "email":"usuario.qualquer@email.com",
        "dataNascimento":"1998-01-25"
    },
    "itens":[
        {
            "id":1,
            "nome":"Xbox One",
            "quantidade":1,
            "valor": 4234.23
        }
    ],
    "pagamento":{
        "id":"4ed0c5f5-761f-4d62-9eb7-651cb15d7685",
        "forma":"Cartao de Credito",
        "status": "APROVADO"
    }
   }
```
