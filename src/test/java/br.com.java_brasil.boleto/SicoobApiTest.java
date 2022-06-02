package br.com.java_brasil.boleto;

import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.SicoobUtil;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class SicoobApiTest {

    public static final String URL_CALLBACK = "http://localhost:8080/callback";
    private BoletoService boletoService;

    @BeforeEach
    public void configuraTeste() {
        ConfiguracaoSicoobAPI configuracao = new ConfiguracaoSicoobAPI();
        configuracao.setClientId("mgS6gZLbT93COP2LgKFaSHF7sMQa");
        configuracao.setCpfCnpj("38052160005701");
        configuracao.setContaCorrente("700033690");
        configuracao.setCooperativa("0001");
        configuracao.setNumeroContrato(123);
        configuracao.setBasicToken("bWdTNmdaTGJUOTNDT1AyTGdLRmFTSEY3c01RYTpzc3Y2bl9OeWpBYndPUWJhMWJNeldaVVk1bFlh");
        configuracao.setAmbiente(AmbienteEnum.HOMOLOGACAO);
        configuracao.setExpiracaoToken(LocalDateTime.now().plusDays(1));
        configuracao.setToken("a14b36f0-fb10-35c8-8462-7e545ceb34eb");
        configuracao.setRefreshToken("42bdfb56-6bf5-348f-8c9d-fe3eb592ad4a");
        boletoService = new BoletoService(BoletoBanco.SICOOB_API, configuracao);
    }

    //    @Test
    void getUrlAutenticacaoHomologacao() {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        String urlCorreta = "https://sandbox.sicoob.com.br/oauth2/authorize?response_type=code&redirect_uri=" + URL_CALLBACK +
                "&client_id=" + configuracao.getClientId() + "&cooperativa=" + configuracao.getCooperativa() +
                "&contaCorrente=" + configuracao.getContaCorrente() + "&versaoHash=3&scope" +
                "=cobranca_boletos_consultar" +
                "%20cobranca_boletos_incluir" +
                "%20cobranca_boletos_segunda_via";

        String urlAutenticacao = SicoobUtil.getUrlAutenticacao(configuracao, URL_CALLBACK);
        assertEquals(urlCorreta, urlAutenticacao);
        System.out.println(urlAutenticacao);
    }

    //    @Test
    void getToken() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        SicoobUtil.getToken(configuracao,
                "f1b98a64-52c0-395b-84d7-a8a9d83da9e9",
                URL_CALLBACK);

        assertNotNull(configuracao.getToken());
        assertNotNull(configuracao.getRefreshToken());
        System.out.println(configuracao.getToken());
        System.out.println(configuracao.getRefreshToken());
    }

    //    @Test
    void testaConsultaBoleto() {
        BoletoModel boletoModel = new BoletoModel();
        boletoModel.setCodigoBarras("12345678901234567890123456789012345678901234");
        boletoModel.setLinhaDigitavel("12345678901234567890123456789012345678901234567");
        BoletoModel boletoModelRetorno = boletoService.consultaBoleto(boletoModel);

        System.out.println(boletoModelRetorno.getSituacao());
    }

    //    @Test
    void testaEnviaBoleto() {
        BoletoModel boletoModel = preencheBoleto();
        BoletoModel boletoResponse = boletoService.enviarBoleto(boletoModel);
        System.out.println(boletoResponse);
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
        boleto.setEspecieDocumento("DM");
        boleto.setDataVencimento(LocalDate.of(2022, 5, 30));
        boleto.setNumeroBoleto("123");

        return boleto;
    }

}
