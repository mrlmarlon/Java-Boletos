package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.BanrisulUtil;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.ConfiguracaoBanrisulAPI;
import br.com.java_brasil.boleto.util.ValidaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

final class BanrisulApiTest {

    private BoletoService boletoService;

    @BeforeEach
    public void configuraTeste() {
        Logger rootLog = Logger.getLogger("");
        rootLog.setLevel( Level.CONFIG );
        rootLog.getHandlers()[0].setLevel( Level.CONFIG );

        ConfiguracaoBanrisulAPI configuracao = new ConfiguracaoBanrisulAPI();
        configuracao.setAmbiente(AmbienteEnum.HOMOLOGACAO);
        configuracao.setCaminhoCertificado("D:/Temp/dacar.jks");
        configuracao.setSenhaCertificado("12345678");
        configuracao.setUrlBaseProducao("solicitar ao banco");
        configuracao.setUrlBaseHomologacao("https://ww20.banrisul.com.br/boc/link/Bocswsxn_CobrancaOnlineWS.asmx");
        boletoService = new BoletoService(BoletoBanco.BANRISUL_API, configuracao);
    }

    @Test
    @DisplayName("Testa Erro Configuracoes")
    void testaErroConfiguracoes() {
        ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) boletoService.getConfiguracao();
        configuracao.setCaminhoCertificado(null);
        Throwable exception =
                assertThrows(BoletoException.class, () -> ValidaUtils.validaConfiguracao(configuracao));
        assertEquals("Campo caminhoCertificado não pode estar vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Testa Valida e Envia Boleto")
    void testaEnvioBoleto() {
        // teste Sucesso (Não implementado)
        BoletoModel boletoModel = preencheBoleto();
        boletoModel = boletoService.enviarBoleto(boletoModel);
    }

    @Test
    @DisplayName("Testa Fator Data")
    void testaFatorData() {
        // teste até 21/02/2025
        String retorno = BanrisulUtil.fatorData(LocalDate.of(2025, 2, 21));
        assertEquals("9999", retorno);

        // teste após 21/02/2025
        retorno = BanrisulUtil.fatorData(LocalDate.of(2025, 2, 22));
        assertEquals("1000", retorno);
    }

    @Test
    @DisplayName("Testa Gera Dígito Nosso Número")
    void testaGeraDigitoNossoNumero() {
        String digitoGerado = BanrisulUtil.geraDigitoNossoNumero("22200593");
        assertEquals("78", digitoGerado);
    }

    private BoletoModel preencheBoleto() {
        BoletoModel boleto = new BoletoModel();
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setAgencia("0475");
        beneficiario.setCarteira(null);
        beneficiario.setConta("2187670");
        beneficiario.setDocumento("00000000000101");
        beneficiario.setNomeBeneficiario("EMPRESA DE TESTE");
        beneficiario.setDigitoAgencia(null);
        beneficiario.setPostoDaAgencia(null);
        beneficiario.setDigitoConta("77");
        beneficiario.setNossoNumero("22200593");
        beneficiario.setDigitoNossoNumero("78");
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
        boleto.setEspecieDocumento("2");
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
