package br.com.java_brasil.boleto.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagador {

    private String nome;
    private String documento;
    private Endereco endereco;
}
