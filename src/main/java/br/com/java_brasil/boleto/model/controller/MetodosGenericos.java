package br.com.java_brasil.boleto.model.controller;

import br.com.java_brasil.boleto.model.BoletoModel;

import java.util.List;

public interface MetodosGenericos {

    void imprimirBoleto(List<BoletoModel> lista);

    void criarBoleto(List<BoletoModel> lista);

    void enviarBoleto(List<BoletoModel> lista);

    void criarLinhaDigitavel();

    void criarNossoNumero();

    void consultaBoleto();

}
