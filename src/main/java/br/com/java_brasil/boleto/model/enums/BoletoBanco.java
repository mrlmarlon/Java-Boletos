package br.com.java_brasil.boleto.model.enums;

import br.com.java_brasil.boleto.model.bancos.BancoGenerico;
import br.com.java_brasil.boleto.model.controller.BoletoController;
import lombok.Getter;
import lombok.SneakyThrows;

@Getter
public enum BoletoBanco {

    GENERICO("Generico", BancoGenerico.class);

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