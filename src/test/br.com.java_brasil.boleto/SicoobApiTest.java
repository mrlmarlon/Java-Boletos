import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.SicoobUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class SicoobApiTest {

    public static final String URL_CALLBACK = "https://enunvar6fv55.x.pipedream.net";
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
        boletoService = new BoletoService(BoletoBanco.SICOOB_API, configuracao);
    }

    @Test
    void getUrlAutenticacao() {
        String urlCorreta = "https://sandbox.sicoob.com.br/oauth2/authorize?response_type=code&redirect_uri=https://enunvar6fv55.x.pipedream" +
                ".net&client_id=mgS6gZLbT93COP2LgKFaSHF7sMQa&cooperativa=0001&contaCorrente=700033690&versaoHash=3&scope=cobranca_boletos_consultar%20cobranca_boletos_incluir%20cobranca_boletos_segunda_via";

        String urlAutenticacao = SicoobUtil.getUrlAutenticacao(
                (ConfiguracaoSicoobAPI) boletoService.getConfiguracao(),
                URL_CALLBACK);
        assertEquals(urlCorreta, urlAutenticacao);
        System.out.println(urlAutenticacao);
    }

    @Test
    void getUrlToken() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        SicoobUtil.getToken(configuracao,
                "a2f239fd-e048-3e46-b008-87af436976a3",
                URL_CALLBACK);

        assertNotNull(configuracao.getToken());
        assertNotNull(configuracao.getRefreshToken());
        System.out.println(configuracao.getToken());
        System.out.println(configuracao.getRefreshToken());
    }

    @Test
    void testaConsultaBoleto() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        configuracao.setToken("f2ccbb0e-0947-3078-b8ac-74dd3c62b374");
        configuracao.setRefreshToken("42bdfb56-6bf5-348f-8c9d-fe3eb592ad4a");
        configuracao.setExpiracaoToken(LocalDateTime.now().plusDays(1));
        BoletoModel boletoModel = new BoletoModel();
        boletoService.consultaBoleto(boletoModel);
    }

    @Test
    void testaEnviaBoleto() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        configuracao.setToken("f2ccbb0e-0947-3078-b8ac-74dd3c62b374");
        configuracao.setRefreshToken("42bdfb56-6bf5-348f-8c9d-fe3eb592ad4a");
        configuracao.setExpiracaoToken(LocalDateTime.now().plusDays(1));
        BoletoModel boletoModel = preencheBoleto();
        boletoService.enviarBoleto(boletoModel);
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
        boleto.setEspecieDocumento("DM");
        boleto.setDataVencimento(LocalDate.of(2022, 5, 30));

        return boleto;
    }


}
