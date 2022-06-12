package br.com.java_brasil.boleto.service.bancos.exemplo_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import lombok.NonNull;

import javax.print.PrintService;
import java.util.List;

/**
 * Classe Generica para servir como base de Implementação
 */
public class BancoExemplo extends BoletoController {

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }
}
