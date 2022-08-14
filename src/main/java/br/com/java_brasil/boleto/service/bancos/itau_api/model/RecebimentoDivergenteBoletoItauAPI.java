package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecebimentoDivergenteBoletoItauAPI {

    private String codigo_tipo_autorizacao;
    private String codigo_tipo_recebimento;
    private String valor_minimo;
    private String percentual_minimo;
    private String valor_maximo;
    private String percentual_maximo;
}
