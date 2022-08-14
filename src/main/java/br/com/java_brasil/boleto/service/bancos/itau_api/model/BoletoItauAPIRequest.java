package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoItauAPIRequest {

    private String etapa_processo_boleto;
    private BeneficiarioBoletoItauAPI beneficiario;
    private DadoBoletoItauAPI dado_boleto;
    private DadosQrCodeBoletoItauAPI dados_qrcode;

}
