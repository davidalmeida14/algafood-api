package com.algaworks.algafood.api.model.view;

/**
 * Interface que serve como filtro para mapear o JSON que será renderizado pelo Controller.
 * Apenas os atributos anotados com @JsonView(Interface.class) serão renderizados
 */
public interface RestauranteResumoView {

    public interface Resumo { }
    public interface ApenasIdENome{}
}
