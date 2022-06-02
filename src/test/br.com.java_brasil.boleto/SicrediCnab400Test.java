package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.sicredi_cnab400.ConfiguracaoSicrediCnab400;
import br.com.java_brasil.boleto.service.bancos.sicredi_cnab400.SicrediUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class SicrediCnab400Test {

    private BoletoService boletoService;

    @BeforeEach
    public void configuraTeste() {
        ConfiguracaoSicrediCnab400 configuracao = new ConfiguracaoSicrediCnab400();
        boletoService = new BoletoService(BoletoBanco.SICREDI_CNAB400, configuracao);
    }

    @Test
    @DisplayName("Testa Erro Configuracoes")
    void testaErroConfiguracoes() {
        //Não há campos na configuração para teste
    }

    @Test
    @DisplayName("Testa Impressão Boleto")
    void testeImprimirBoleto() {
        // Model Null
        assertThrows(NullPointerException.class, () -> boletoService.imprimirBoleto(null));
    }

    @Test
    @DisplayName("Testa Valida e Envia Boleto")
    void testaEnvioBoleto() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        // teste Sucesso (Não implementado)
        BoletoModel boletoModel = preencheBoleto();
        Throwable exception =
                assertThrows(BoletoException.class, () -> boletoService.enviarBoleto(boletoModel));
        assertEquals("Esta função não está disponível para este banco.", exception.getMessage());
    }

    @Test
    @DisplayName("Testa Fator Data")
    void testaFatorData() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        // teste até 21/02/2025
        String retorno = SicrediUtil.fatorData(LocalDate.of(2025, 2, 21));
        assertEquals("9999", retorno);

        // teste após 21/02/2025
        retorno = SicrediUtil.fatorData(LocalDate.of(2025, 2, 22));
        assertEquals("1000", retorno);
    }

    @Test
    @DisplayName("Testa Gera Nosso Número")
    void testaGeraNossoNumero() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        String nossoNumero = SicrediUtil.gerarNossoNumero(
                "2",
                "08875",
                LocalDate.of(2022, 6, 1));
        assertEquals("22208875", nossoNumero);
    }

    @Test
    @DisplayName("Testa Gera Dígito Nosso Número")
    void testaGeraDigitoNossoNumero() {
        final Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(Level.DEBUG);

        Integer digitoGerado = SicrediUtil.gerarDigitoNossoNumero(
                "2",
                "08875",
                LocalDate.of(2022, 6,1),
                "0663",
                "59770",
                "7");
        assertEquals(4, digitoGerado);
    }

    private BoletoModel preencheBoleto() {
        BoletoModel boleto = new BoletoModel();
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setAgencia("0663");
        beneficiario.setCarteira(null);
        beneficiario.setConta("86654");
        beneficiario.setDocumento("00000000000101");
        beneficiario.setNomeBeneficiario("EMPRESA DE TESTE");
        beneficiario.setDigitoAgencia(null);
        beneficiario.setPostoDaAgencia("7");
        beneficiario.setDigitoConta("0");
        beneficiario.setNossoNumero("22208875");
        beneficiario.setDigitoNossoNumero("4");
        beneficiario.setNumeroConvenio(null);

        Endereco enderecoBenef = new Endereco();
        enderecoBenef.setBairro("CENTRO");
        enderecoBenef.setCep("96030400");
        enderecoBenef.setCidade("PELOTAS");
        enderecoBenef.setComplemento("");
        enderecoBenef.setLogradouro("RUA OLAVO BILAC");
        enderecoBenef.setNumero("722");
        enderecoBenef.setUf("RS");
        beneficiario.setEndereco(enderecoBenef);
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
        boleto.setDataVencimento(LocalDate.of(2022, 6, 30));
        List<InformacaoModel> locaisPagamento = new ArrayList<>();
        locaisPagamento.add(new InformacaoModel("PAGÁVEL PREFERENCIALMENTE EM CANAIS DA SUA INSTITUIÇÃO FINANCEIRA"));
        boleto.setLocaisDePagamento(locaisPagamento);
        boleto.setDataEmissao(LocalDate.now());
        boleto.setNumeroDocumento("000004852");
        boleto.setEspecieDocumento("DMI");
        boleto.setAceite(false);
        boleto.setEspecieMoeda("REAL");
        List<InformacaoModel> instrucoes = new ArrayList<>();
        instrucoes.add(new InformacaoModel("PROTESTO AUTOMÁTICO APÓS 5 DIAS DO VENCIMENTO"));
        boleto.setInstrucoes(instrucoes);
        boleto.setTipoJuros(TipoJurosEnum.PERCENTUAL_MENSAL);
        boleto.setDiasJuros(1);
        boleto.setValorPercentualJuros(BigDecimal.valueOf(9.9));
        boleto.setTipoMulta(TipoMultaEnum.PERCENTUAL);
        boleto.setDiasMulta(1);
        boleto.setValorPercentualMulta(BigDecimal.valueOf(2.00));
        boleto.setTipoDesconto(TipoDescontoEnum.PERCENTUAL_DIA);
        boleto.setProtesto(true);
        boleto.setDiasProtesto(5);
        boleto.setNegativacaoAutomatica(false);
        boleto.setTipoImpressao("A");
        boleto.setEspecieMoeda("REAL");

        return boleto;
    }


}
