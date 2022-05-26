package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobAPIRequest {

    private Integer numeroContrato;
    private Integer modalidade;
    private Integer numeroContaCorrente;
    private String especieDocumento;

}