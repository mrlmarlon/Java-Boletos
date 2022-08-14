package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JurosBoletoItauAPI {

    private String data_juros;
    private String codigo_tipo_juros;
    private String valor_juros;
    private String percentual_juros;
}
