package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoBanco;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.exemplo_api.ConfiguracaoExemplo;
import br.com.java_brasil.boleto.util.ValidaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class ExemploTest {

    private BoletoService boletoService;

    @BeforeEach
    public void configuraTeste() {
        ConfiguracaoExemplo configuracao = new ConfiguracaoExemplo();
        configuracao.setUsuario("teste");
        configuracao.setSenha("123");
        boletoService = new BoletoService(BoletoBanco.EXEMPLO, configuracao);
    }

    @Test
    @DisplayName("Testa Erro Configuracoes")
    void testaErroConfiguracoes() {
        ConfiguracaoExemplo configuracao = (ConfiguracaoExemplo) boletoService.getConfiguracao();
        configuracao.setSenha(null);
        Throwable exception =
                assertThrows(BoletoException.class, () -> ValidaUtils.validaConfiguracao(configuracao));
        assertEquals("Campo senha não pode estar vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Testa Impressão Boleto")
    void testeImprimirBoleto() {
        // Model Null
        assertThrows(NullPointerException.class, () -> boletoService.imprimirBoletoBanco(null));

        // teste Sucesso (Não implementado)
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.imprimirBoletoBanco(new BoletoModel()));
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
       // assertThrows(NullPointerException.class, () -> boletoService.alteraBoleto(null));

        // teste Sucesso (Não implementado)
//        Throwable exception =
//                assertThrows(BoletoException.class, () -> boletoService.alteraBoleto(new BoletoModel()));
//        assertEquals("Não implementado!", exception.getMessage());

    }

    @Test
    @DisplayName("Testa Consulta de Boleto")
    void testeConsultarBoleto() {
        // Lista Nula
//        assertThrows(NullPointerException.class, () -> boletoService.consultaBoleto(null));
//
//        // teste Sucesso (Não implementado)
//        Throwable exception =
//                assertThrows(BoletoException.class, () -> boletoService.consultaBoleto(new BoletoModel()));
//        assertEquals("Não implementado!", exception.getMessage());

    }
}
