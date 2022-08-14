package br.com.java_brasil.boleto.model;

import br.com.java_brasil.boleto.service.bancos.banco_brasil_api.BancoBrasilAPI;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.BancoBanrisulAPI;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.BancoBradescoAPI;
import br.com.java_brasil.boleto.service.bancos.bradesco_cnab400.BancoBradescoCnab400;
import br.com.java_brasil.boleto.service.bancos.exemplo_api.BancoExemplo;
import br.com.java_brasil.boleto.service.bancos.itau_api.BancoItauAPI;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.BancoSafe2PayAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.BancoSicoobAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.BancoSicoobCnab240;
import br.com.java_brasil.boleto.service.bancos.sicredi_cnab400.BancoSicrediCnab400;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum BoletoBanco {

    EXEMPLO("Exemplo", BancoExemplo.class),
    BRADESCO_API("Bradesco API", BancoBradescoAPI.class),
    BANCO_BRASIL_API("Banco do Brasil API", BancoBrasilAPI.class);
    BANRISUL_API("Banrisul API", BancoBanrisulAPI.class),
    BRADESCO_CNAB400("Bradesco CNAB400", BancoBradescoCnab400.class),
    SICREDI_CNAB400("Sicredi CNAB400", BancoSicrediCnab400.class),
    SICOOB_CNAB240("Sicoob CNAB240", BancoSicoobCnab240.class),
    SICOOB_API("Sicoob API", BancoSicoobAPI.class),
    SAFE2PAY_API("Safe2Pay API", BancoSafe2PayAPI.class),
    ITAU_API("Itau API", BancoItauAPI.class);

    private final String descricao;
    private final Class<? extends BoletoController> controller;

    BoletoBanco(String descricao,
                Class<? extends BoletoController> controller) {
        this.descricao = descricao;
        this.controller = controller;
    }

    @SneakyThrows
    public BoletoController getController() {
        return controller.getConstructor().newInstance();
    }
}