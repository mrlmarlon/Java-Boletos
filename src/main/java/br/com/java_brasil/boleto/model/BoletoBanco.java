package br.com.java_brasil.boleto.model;

import br.com.java_brasil.boleto.service.bancos.banrisul_api.BancoBanrisulAPI;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.BancoBradescoAPI;
import br.com.java_brasil.boleto.service.bancos.exemplo.BancoExemplo;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.BancoSicoobAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.BancoSicoobCnab240;
import br.com.java_brasil.boleto.service.bancos.sicredi_cnab400.BancoSicrediCnab400;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum BoletoBanco {

    EXEMPLO("Exemplo", BancoExemplo.class),
    BRADESCO_API("Bradesco API", BancoBradescoAPI.class),
    BANRISUL_API("Banrisul API", BancoBanrisulAPI.class),
    SICREDI_CNAB400("Sicredi CNAB400", BancoSicrediCnab400.class),
    SICOOB_CNAB240("Sicoob CNAB240", BancoSicoobCnab240.class);
    SICOOB_API("Sicoob API", BancoSicoobAPI.class);

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