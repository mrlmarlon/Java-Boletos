package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.util.ValidaUtils;
import lombok.NonNull;

import javax.print.PrintService;
import java.util.List;

public class BoletoService implements MetodosGenericos {

    private final BoletoController controller;

    public BoletoService(@NonNull BoletoBanco banco, @NonNull Configuracao configuracao) {
        ValidaUtils.validaConfiguracao(configuracao);
        this.controller = banco.getController();
        this.controller.setConfiguracao(configuracao);
    }

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        return this.controller.imprimirBoletoJasper(boletoModel);
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora,
                                            PrintService printService) {
        this.controller.imprimirBoletoJasperDesktop(boletoModel, diretoImpressora, printService);
    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        return this.controller.imprimirBoletoJasper(boletoModel);
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        ValidaUtils.validaBoletoModel(boletoModel, this.controller.getConfiguracao().camposObrigatoriosBoleto());
        return this.controller.enviarBoleto(boletoModel);
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        ValidaUtils.validaBoletoModel(boletoModel, this.controller.getConfiguracao().camposObrigatoriosBoleto());
        return this.controller.alterarBoleto(boletoModel);
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.consultarBoleto(boletoModel);
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        return this.controller.baixarBoleto(boletoModel);
    }

    public Configuracao getConfiguracao() {
        return this.controller.getConfiguracao();
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        return this.controller.gerarArquivoRemessa(remessaRetornoModel);
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        return this.controller.importarArquivoRetorno(arquivo);
    }
}
