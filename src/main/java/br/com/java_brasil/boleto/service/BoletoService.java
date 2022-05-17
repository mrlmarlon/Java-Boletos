package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.BoletoBanco;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.util.ValidaUtils;
import lombok.NonNull;

public class BoletoService implements MetodosGenericos {

    private final BoletoController controller;

    public BoletoService(@NonNull BoletoBanco banco, @NonNull Configuracao configuracao) {
        ValidaUtils.validaConfiguracao(configuracao);
        this.controller = banco.getController();
        this.controller.setConfiguracao(configuracao);
    }

    @Override
    public byte[] imprimirBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.imprimirBoleto(boletoModel);
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        ValidaUtils.validaBoletoModel(boletoModel, this.controller.getConfiguracao().camposObrigatoriosBoleto());
        return this.controller.enviarBoleto(boletoModel);
    }

    @Override
    public BoletoModel alteraBoleto(@NonNull BoletoModel boletoModel) {
        ValidaUtils.validaBoletoModel(boletoModel, this.controller.getConfiguracao().camposObrigatoriosBoleto());
        return this.controller.alteraBoleto(boletoModel);
    }

    @Override
    public BoletoModel consultaBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.consultaBoleto(boletoModel);
    }

    public Configuracao getConfiguracao(){
        return this.controller.getConfiguracao();
    }
}
