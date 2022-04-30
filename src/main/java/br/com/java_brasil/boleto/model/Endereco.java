package br.com.java_brasil.boleto.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Endereco implements Serializable {

    private String logradouro;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;

}
