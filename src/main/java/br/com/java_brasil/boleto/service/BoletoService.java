package br.com.java_brasil.boleto.service;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.controller.MetodosGenericos;
import br.com.java_brasil.boleto.model.enums.BoletoBanco;

import java.util.List;

public class BoletoService implements MetodosGenericos {

    private final BoletoBanco banco;

    public BoletoService(BoletoBanco banco) {this.banco = banco;}

    @Override
    public void imprimirBoleto(List<BoletoModel> lista) {
        banco.getController().imprimirBoleto(lista);
    }

    @Override
    public void criarBoleto(List<BoletoModel> lista) {

    }

    @Override
    public void enviarBoleto(List<BoletoModel> lista) {

    }

    @Override
    public void criarLinhaDigitavel() {

    }

    @Override
    public void criarNossoNumero() {
        banco.getController().criarNossoNumero();
    }

    @Override
    public void consultaBoleto() {
        banco.getController().consultaBoleto();
    }
}
