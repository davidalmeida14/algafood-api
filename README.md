# algafood-api
- O Algafood é uma API desenvolvida durante o treinamento Especialista Spring REST, que tem como principal papel fornecer recursos para gestão de restaurantes, pedidos, pagamentos.

# Gerenciamento de Cozinhas

Através do recurso de cozinhas, é possível realizar todo o gerencimaneto das cozinhas que seus restaurantes poderão pertencer.

* GET /cozinhas - Listar todas as cozinhas
* GET /cozinhas/{id} - Listar cozinha com identificador específico.
* POST /cozinhas - Cadastrar uma nova cozinha
* PUT /cozinhas/{id} - Atualizar cozinha
* DELETE /cozinhas/{id} - Exclui uma cozinha específica

Constraints:
 - Não pode-se excluir cozinhas que estão em uso por restaurantes.

# Gerenciamento de restaurantes

Através de um recurso de restaurantes, é possível realizar o cadastro, atualização, exclusão e manipulação em geral dos restaurantes.

* GET /restaurantes - Listar todos os restaurantes
* GET /restaurantes/{id} - Buscar restaurante através de seu identificador.
* POST /restaurantes - Cadastro de um novo restaurante.
* PUT /restaurantes/{id} - Atualizar um restaurante
* DELETE /restaurantes/{id} - Exclui restaurante específico por id

