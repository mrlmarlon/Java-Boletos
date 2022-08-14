package br.com.java_brasil.boleto.service.bancos.itau_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.bancos.itau_api.model.BoletoItauAPIRequest;
import br.com.java_brasil.boleto.service.bancos.itau_api.model.BoletoItauAPIResponse;
import br.com.java_brasil.boleto.service.bancos.itau_api.model.BoletoItauModelConverter;
import br.com.java_brasil.boleto.util.RestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import javax.print.PrintService;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.*;

/**
 * Classe Generica para servir como base de Implementação
 */
@Log
public class BancoItauAPI extends BoletoController {

    private static void capturaToken(ConfiguracaoItauAPI configuracao) throws IOException {

        Header[] headers = {
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(CONTENT_TYPE, "application/x-www-form-urlencoded"),
        };

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("grant_type", "client_credentials"));
        nvps.add(new BasicNameValuePair("client_id", configuracao.getClientId()));
        nvps.add(new BasicNameValuePair("client_secret", configuracao.getClientSecret()));

        HttpPost httpPost = new HttpPost(configuracao.getURLToken());
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        httpPost.setHeaders(headers);

        CloseableHttpResponse response = RestUtil.enviaComandoCertificado(httpPost
                , configuracao.getCaminhoCertificado(), configuracao.getSenhaCertificado());
        String retorno = RestUtil.validaResponseERetornaBody(response);
        log.config("Retorno Token Itau: " + retorno);

        JsonObject json = JsonParser.parseString(retorno).getAsJsonObject();
        String token = json.get("access_token").getAsString();
        LocalDateTime expires = LocalDateTime.now().plusSeconds(json.get("expires_in").getAsInt());
        log.config("Token Itau: " + token);
        log.config("Expiracao: " + expires);

        configuracao.setToken(token);
        configuracao.setExpiracaoToken(expires);
    }

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoItauAPI configuracao = (ConfiguracaoItauAPI) this.getConfiguracao();
            BoletoItauAPIRequest apiRequest = BoletoItauModelConverter.montaBoletoRequest(boletoModel, configuracao.getAmbiente());
            BoletoItauAPIResponse apiResponse = registraBoleto(apiRequest, configuracao);
            return BoletoItauModelConverter.montaBoletoResponse(boletoModel, apiResponse);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | InvalidKeyException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    private BoletoItauAPIResponse registraBoleto(BoletoItauAPIRequest boletoRequest, ConfiguracaoItauAPI configuracao) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException, SignatureException, InvalidKeyException {

        validaAutenticacao(configuracao);

        String json = RestUtil.ObjectToJson(boletoRequest);
        log.config("Json Envio Itau: " + json);

        Header[] headers = {
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(CONTENT_TYPE, "application/json"),
                getHeadAutorization(configuracao.getToken(), configuracao.getAmbiente()),
        };

        CloseableHttpResponse response = RestUtil.post(
                configuracao.getURLRegistroBoleto(),
                headers,
                json, configuracao.getCaminhoCertificado(), configuracao.getSenhaCertificado());

        String retorno = RestUtil.validaResponseERetornaBody(response);
        log.config("Retorno Envio Itau: " + retorno);

        JsonObject jsonRetorno = JsonParser.parseString(retorno).getAsJsonObject();
        retorno = jsonRetorno.get("data").getAsString();
        return RestUtil.JsonToObject(retorno, BoletoItauAPIResponse.class);
    }

    private BasicHeader getHeadAutorization(String token, AmbienteEnum ambienteEnum) {
        return ambienteEnum.equals(AmbienteEnum.PRODUCAO) ?
                new BasicHeader(AUTHORIZATION, "Bearer " + token) :
                new BasicHeader("x-sandbox-token", token);
    }

    private void validaAutenticacao(ConfiguracaoItauAPI configuracao) {
        try {
            if (StringUtils.isBlank(configuracao.getToken()) ||
                    configuracao.getExpiracaoToken() == null || configuracao.getExpiracaoToken().isBefore(LocalDateTime.now())) {
                log.config("Token está expirado. Gerando novo Token.");
                capturaToken(configuracao);
            }
        } catch (IOException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }
}
