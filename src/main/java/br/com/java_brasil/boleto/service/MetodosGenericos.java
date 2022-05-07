package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.BoletoModel;
import lombok.NonNull;

public interface MetodosGenericos {

    byte[] imprimirBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel alteraBoleto(@NonNull BoletoModel boletoModel);

    BoletoModel consultaBoleto(@NonNull BoletoModel boletoModel);
}
