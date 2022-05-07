package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.BoletoBanco;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.Configuracao;
import lombok.NonNull;

public class BoletoService implements MetodosGenericos {

    private final BoletoController controller;

    public BoletoService(@NonNull BoletoBanco banco, @NonNull Configuracao configuracao) {
        configuracao.verificaConfiguracoes();
        this.controller = banco.getController();
        this.controller.setConfiguracao(configuracao);
    }

    @Override
    public byte[] imprimirBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.imprimirBoleto(boletoModel);
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.enviarBoleto(boletoModel);
    }
}
