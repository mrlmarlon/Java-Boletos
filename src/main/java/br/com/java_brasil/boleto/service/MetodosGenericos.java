package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import lombok.NonNull;

import javax.print.PrintService;
import java.util.List;

public interface MetodosGenericos {

    byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel);

    void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService);

    byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel);

    BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel);

    String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel);

    List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo);

}
