CREATE TABLE PEDIDO (
	Id BigInt NOT NULL auto_increment,
	Subtotal float not null,
	Taxafrete float not null,
	Valortotal float not null,
	Datacriacao date,
	Dataconfirmacao date,
	Dataentrega date,
	Statuspedido varchar(10),
	endereco_cep varchar(100),
	endereco_logradouro varchar(100),
	endereco_numero varchar(100),
	endereco_complemento varchar(100),
	endereco_bairro varchar(100),
	endereco_cidade_id bigint,
	PRIMARY KEY (Id)
)
ALTER TABLE PEDIDO ADD CONSTRAINT FK_CIDADE_PEDIDO FOREIGN KEY (endereco_cidade_id) REFERENCES CIDADE (Id)