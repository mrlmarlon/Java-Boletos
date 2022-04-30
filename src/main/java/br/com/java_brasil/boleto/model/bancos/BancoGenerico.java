package br.com.java_brasil.boleto.model.bancos;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.controller.BoletoController;
import lombok.extern.java.Log;

import java.util.List;

/**
 * Classe Generica para servir como base de Implementação
 */
@Log
public class BancoGenerico implements BoletoController {

    @Override
    public void imprimirBoleto(List<BoletoModel> lista) {
        log.info("Imprimiu Boleto");
    }

    @Override
    public void criarBoleto(List<BoletoModel> lista) {
        log.info("Criou Boleto");

    }

    @Override
    public void enviarBoleto(List<BoletoModel> lista) {
        log.info("Enviou Boleto");

    }

    @Override
    public void criarLinhaDigitavel() {
        log.info("Criou Linha Digitavel");

    }

    @Override
    public void criarNossoNumero() {
        log.info("Criou nosso número");

    }

    @Override
    public void consultaBoleto() {
        log.info("Consultou Boleto");
    }
}
