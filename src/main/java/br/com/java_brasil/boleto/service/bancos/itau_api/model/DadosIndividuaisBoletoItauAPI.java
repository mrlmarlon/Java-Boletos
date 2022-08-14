package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosIndividuaisBoletoItauAPI {

    private String id_boleto_individual;
    private String numero_nosso_numero;
    private String dac_titulo;
    private String data_vencimento;
    private String valor_titulo;
    private String texto_seu_numero;
    private String texto_uso_beneficiario;
    private String codigo_barras;
    private String numero_linha_digitavel;
    private String data_limite_pagamento;
    private String lista_mensagens_cobranca;

}
