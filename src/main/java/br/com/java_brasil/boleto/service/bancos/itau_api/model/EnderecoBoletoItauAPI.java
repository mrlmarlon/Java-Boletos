package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EnderecoBoletoItauAPI {

    private String nome_logradouro;
    private String numero;
    private String complemento;
    private String nome_bairro;
    private String nome_cidade;
    private String sigla_UF;
    private String numero_CEP;
}
