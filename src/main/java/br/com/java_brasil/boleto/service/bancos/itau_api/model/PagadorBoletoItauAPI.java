package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagadorBoletoItauAPI {

    private String id_pagador;
    private PessoaBoletoItauAPI pessoa;
    private EnderecoBoletoItauAPI endereco;
    private Boolean pagador_eletronico_DDA;
    private Boolean praca_protesto;
}
