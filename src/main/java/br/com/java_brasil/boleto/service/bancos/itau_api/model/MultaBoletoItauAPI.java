package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultaBoletoItauAPI {

    private String data_multa;
    private String codigo_tipo_multa;
    private String valor_multa;
    private String percentual_multa;
}
