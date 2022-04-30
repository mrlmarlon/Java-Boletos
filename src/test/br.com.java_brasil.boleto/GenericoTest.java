package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.model.enums.BoletoBanco;
import br.com.java_brasil.boleto.service.BoletoService;
import lombok.extern.java.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
final class GenericoTest {

    private BoletoService boletoService;
    private BoletoBanco boletoBanco;

    @BeforeEach
    public void configuraTeste(){
        boletoBanco = BoletoBanco.GENERICO; //TODO Altere aqui com seu Banco
        boletoService = new BoletoService(boletoBanco);
    }

    @Test
    @DisplayName("Testa Enum")
    void testeEnum() {
        String descricao = boletoBanco.getDescricao();
        assertEquals("Generico", descricao);
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Efetura o Teste de Consulta")
    void testeConsulta() {
        boletoService.consultaBoleto();
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Impressão Boleto")
    void testeImprimirBoleto() {
        boletoService.imprimirBoleto(null);
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Criação de Boleto")
    void testeCriarBoleto() {
        boletoService.criarBoleto(null);
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Encio de Boleto")
    void testeEnviarBoleto() {
        boletoService.enviarBoleto(null);
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Cria Linha Digitavel")
    void testeCriarLinhaDigitavel() {
        boletoService.criarLinhaDigitavel();
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Criar Nosso Numero")
    void testeCriarNossoNumero() {
        boletoService.criarNossoNumero();
        //TODO Criar Testes Aqui
    }

    @Test
    @DisplayName("Testa Consulta Boleto")
    void testeConsultaBoleto() {
        boletoService.consultaBoleto();
        //TODO Criar Testes Aqui
    }
}
