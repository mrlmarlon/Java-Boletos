package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoBanco;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.exemplo.ConfiguracaoExemplo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class ExemploTest {

    private BoletoService boletoService;
    private BoletoBanco boletoBanco;

    @BeforeEach
    public void configuraTeste() {
        ConfiguracaoExemplo configuracao = new ConfiguracaoExemplo();
        configuracao.setUsuario("teste");
        configuracao.setSenha("123");
        boletoBanco = BoletoBanco.EXEMPLO; //TODO Altere aqui com seu Banco
        boletoService = new BoletoService(boletoBanco, configuracao);
    }

    @Test
    @DisplayName("Testa Configuracoes")
    void testeConfiguracoes() {
        //Configuracao Sucesso
        ConfiguracaoExemplo configuracao = new ConfiguracaoExemplo();
        configuracao.setUsuario("teste");
        configuracao.setSenha("123");
        configuracao.verificaConfiguracoes();

        //Configuracao Erro
        configuracao.setSenha(null);
        Throwable exception =
                assertThrows(BoletoException.class, configuracao::verificaConfiguracoes);
        assertEquals("Configuracoes invalidas.", exception.getMessage());
    }

    @Test
    @DisplayName("Testa Enum")
    void testeEnum() {
        String descricao = boletoBanco.getDescricao();
        assertEquals("Exemplo", descricao);
    }

    @Test
    @DisplayName("Testa Impressão Boleto")
    void testeImprimirBoleto() {
        // Model Null
        assertThrows(NullPointerException.class, () -> boletoService.imprimirBoleto(null));

        // teste Sucesso (Não implementado)
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.imprimirBoleto(new BoletoModel()));
        assertEquals("Não implementado!", exception.getMessage());

    }

    @Test
    @DisplayName("Testa Envio de Boleto")
    void testeEnviarBoleto() {
        // Lista Nula
        assertThrows(NullPointerException.class, () -> boletoService.enviarBoleto(null));

        // teste Sucesso (Não implementado)
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.enviarBoleto(new BoletoModel()));
        assertEquals("Não implementado!", exception.getMessage());

    }

    @Test
    @DisplayName("Testa Altera de Boleto")
    void testeAlterarBoleto() {
        // Lista Nula
        assertThrows(NullPointerException.class, () -> boletoService.alteraBoleto(null));

        // teste Sucesso (Não implementado)
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.alteraBoleto(new BoletoModel()));
        assertEquals("Não implementado!", exception.getMessage());

    }

    @Test
    @DisplayName("Testa Consulta de Boleto")
    void testeConsultarBoleto() {
        // Lista Nula
        assertThrows(NullPointerException.class, () -> boletoService.consultaBoleto(null));

        // teste Sucesso (Não implementado)
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.consultaBoleto(new BoletoModel()));
        assertEquals("Não implementado!", exception.getMessage());

    }
}
