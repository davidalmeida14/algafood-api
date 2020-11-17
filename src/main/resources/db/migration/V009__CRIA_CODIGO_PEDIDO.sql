alter table pedido add codigo varchar (36) not null after id;
alter table pedido add constraint uk_pedido_codigo UNIQUE (codigo);