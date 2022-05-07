package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.ConfiguracaoBradescoAPI;
import br.com.java_brasil.boleto.service.bancos.exemplo.ConfiguracaoExemplo;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class BradescoApiTest {

    private BoletoService boletoService;
    private BoletoBanco boletoBanco;

    @BeforeEach
    public void configuraTeste() {
        ConfiguracaoBradescoAPI configuracao = new ConfiguracaoBradescoAPI();
        configuracao.setClientId("123");
        configuracao.setCpfCnpj("99999999999999");
        configuracao.setAmbiente(AmbienteEnum.HOMOLOGACAO);
        configuracao.setCaminhoCertificado("d:/teste/teste.pem");
        boletoBanco = BoletoBanco.BRADESCO_API; //TODO Altere aqui com seu Banco
        boletoService = new BoletoService(boletoBanco, configuracao);
    }

    @Test
    @DisplayName("Testa Configuracoes")
    void testeConfiguracoes() {
        //Configuracao Sucesso
        ConfiguracaoBradescoAPI configuracao = new ConfiguracaoBradescoAPI();
        configuracao.setClientId("123");
        configuracao.setCpfCnpj("99999999999999");
        configuracao.setAmbiente(AmbienteEnum.HOMOLOGACAO);
        configuracao.setCaminhoCertificado("d:/teste/teste.pem");
        configuracao.verificaConfiguracoes();

        //Configuracao Erro
        configuracao.setClientId(null);
        Throwable exception =
                assertThrows(BoletoException.class, configuracao::verificaConfiguracoes);
        assertEquals("Configuracoes invalidas.", exception.getMessage());
    }

    @Test
    void testaConsumo() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        ConfiguracaoBradescoAPI configuracao = new ConfiguracaoBradescoAPI();
        configuracao.setAmbiente(AmbienteEnum.HOMOLOGACAO);
        configuracao.setCaminhoCertificado("d:/teste/bradesco.pem");
        configuracao.setClientId("9c228ae2-6277-4a8c-a26b-51223a0aaa09");
        configuracao.setCpfCnpj("38052160005701");

        BoletoService boletoService = new BoletoService(BoletoBanco.BRADESCO_API, configuracao);
        BoletoModel retorno = boletoService.enviarBoleto(preencheBoleto());

        System.out.println(retorno.getCodRetorno() + " - " + retorno.getMensagemRetorno());
        System.out.println(retorno.getCodigoBarras());

//        byte[] bytes = boletoService.imprimirBoleto(retorno);
        // SALVAR PDF

    }

    private BoletoModel preencheBoleto() {
        BoletoModel boleto = new BoletoModel();
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setAgencia("3995");
        beneficiario.setDigitoAgencia("0");
        beneficiario.setDocumento("38052160005701");
        beneficiario.setConta("75557");
        beneficiario.setDigitoConta("5");
        beneficiario.setCarteira("9");
        beneficiario.setNossoNumero("2336835");
        boleto.setBeneficiario(beneficiario);

        Pagador pagador = new Pagador();
        pagador.setNome("SAMUEL BORGES DE OLIVEIRA");
        pagador.setDocumento("01713390108"); // <- PIX
        pagador.setCodigo("999");
        Endereco endereco = new Endereco();
        endereco.setLogradouro("Endereco Teste");
        endereco.setNumero("0");
        endereco.setBairro("Centro");
        endereco.setComplemento("Qd 0 Lote 0");
        endereco.setCep("75120683");
        endereco.setCidade("ANAPOLIS");
        endereco.setUf("GO");
        pagador.setEndereco(endereco);
        boleto.setPagador(pagador);

        boleto.setValorBoleto(BigDecimal.TEN);
        boleto.setDataVencimento(LocalDate.of(2022, 5, 30));

        return boleto;
    }


}
