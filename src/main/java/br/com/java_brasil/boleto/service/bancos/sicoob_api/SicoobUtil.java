package br.com.java_brasil.boleto.service.bancos.sicoob_api;

import br.com.java_brasil.boleto.util.RestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.*;

@Slf4j
public class SicoobUtil {

    public static String getUrlAutenticacao(ConfiguracaoSicoobAPI configuracao, String urlCallback) {

        return configuracao.getURLAuth() + configuracao.getUrlAuthorize() + "?response_type=code" +
                "&redirect_uri=" + urlCallback +
                "&client_id=" + configuracao.getClientId() +
                "&cooperativa=" + configuracao.getCooperativa() +
                "&contaCorrente=" + configuracao.getContaCorrente() +
                "&versaoHash=3&scope=cobranca_boletos_consultar%20cobranca_boletos_incluir%20cobranca_boletos_segunda_via";
    }

    public static void refreshToken(ConfiguracaoSicoobAPI configuracao) throws IOException {
        Header[] headers = getheaders(configuracao);

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("grant_type", "refresh_token"));
        nvps.add(new BasicNameValuePair("refresh_token", configuracao.getRefreshToken()));

        capturaToken(configuracao, nvps, headers);

    }

    private static void capturaToken(ConfiguracaoSicoobAPI configuracao, List<NameValuePair> nvps, Header[] headers) throws IOException {
        HttpPost httpPost = new HttpPost(configuracao.getURLAuth() + configuracao.getUrlToken());
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        httpPost.setHeaders(headers);
        CloseableHttpResponse response = RestUtil.enviaComando(httpPost);

        String retorno = RestUtil.validaResponseERetornaBody(response);
        log.debug("Retorno Token Sicoob: " + retorno);

        JsonObject json = JsonParser.parseString(retorno).getAsJsonObject();
        String token = json.get("access_token").getAsString();
        String refreshToken = json.get("refresh_token").getAsString();
        LocalDateTime expires = LocalDateTime.now().plusSeconds(json.get("expires_in").getAsInt());

        log.debug("Token Sicoob: " + token);
        log.debug("Refresh Token Sicoob: " + refreshToken);
        log.debug("Expiracao: " + expires);

        configuracao.setToken(token);
        configuracao.setRefreshToken(refreshToken);
        configuracao.setExpiracaoToken(expires);
    }

    private static Header[] getheaders(ConfiguracaoSicoobAPI configuracao) {
        return new BasicHeader[]{
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(CONTENT_TYPE, "application/x-www-form-urlencoded"),
                new BasicHeader(AUTHORIZATION, "Basic " + configuracao.getBasicToken()),
        };
    }

    public static void getToken(ConfiguracaoSicoobAPI configuracao, String code, String urlCallback) throws IOException {

        Header[] headers = getheaders(configuracao);

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
        nvps.add(new BasicNameValuePair("code", code));
        nvps.add(new BasicNameValuePair("redirect_uri", urlCallback));

        capturaToken(configuracao, nvps, headers);

    }
}
