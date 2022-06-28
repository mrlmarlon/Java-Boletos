package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescontosBoletoItauAPI {

    private String data_desconto;
    private String valor_desconto;
    private String percentual_desconto;
}
