package br.com.java_brasil.boleto.service.bancos.safe2pay_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.ConfiguracaoSafe2PayAPI;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.envio.Payment;

public class BoletoSafe2PayModelConverter {

    public static Payment montaBoletoRequest(BoletoModel boletoModel, ConfiguracaoSafe2PayAPI configuracaoSafe2PayAPI) {
        boolean sandbox = configuracaoSafe2PayAPI.isSandbox();
        Payment payment = new Payment(boletoModel, sandbox);
        return payment;
    }

}
