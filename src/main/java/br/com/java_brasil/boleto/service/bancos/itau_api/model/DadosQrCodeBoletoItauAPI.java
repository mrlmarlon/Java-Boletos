package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DadosQrCodeBoletoItauAPI {

    private String chave;
    private String id_location;
    private String emv;
    private String base64;
    private String txid;
    private String location;
}
