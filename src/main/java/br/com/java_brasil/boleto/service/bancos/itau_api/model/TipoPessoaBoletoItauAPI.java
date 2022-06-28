package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TipoPessoaBoletoItauAPI {

    private String codigo_tipo_pessoa;
    private String numero_cadastro_pessoa_fisica;
    private String numero_cadastro_nacional_pessoa_juridica;
}
