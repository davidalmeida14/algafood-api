ALTER TABLE restaurante add ativo tinyint(1) not null;
UPDATE restaurante set ativo = true;