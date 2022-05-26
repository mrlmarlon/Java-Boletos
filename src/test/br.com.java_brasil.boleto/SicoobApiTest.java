import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoBanco;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.BoletoService;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.ConfiguracaoBradescoAPI;
import br.com.java_brasil.boleto.service.bancos.exemplo.ConfiguracaoExemplo;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.SicoobUtil;
import br.com.java_brasil.boleto.util.ValidaUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(urlCorreta,urlAutenticacao);
        System.out.println(urlAutenticacao);
    }

    @Test
    void getUrlToken() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        SicoobUtil.getToken(configuracao,
                "ef108597-4033-3993-b330-08ee89e81cb3",
                URL_CALLBACK);

        assertNotNull(configuracao.getToken());
        assertNotNull(configuracao.getRefreshToken());
        System.out.println(configuracao.getToken());
        System.out.println(configuracao.getRefreshToken());
    }

    @Test
    void testaConsultaBoleto() throws IOException {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) boletoService.getConfiguracao();
        configuracao.setToken("37a65100-029a-3ae2-8c43-484e5ea75432");
        configuracao.setRefreshToken("b1aa789e-bf51-3103-8633-3b7349813d62");
        BoletoModel boletoModel = new BoletoModel();
        boletoService.consultaBoleto(boletoModel);
    }


}
