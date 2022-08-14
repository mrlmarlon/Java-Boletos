package br.com.java_brasil.boleto.service.bancos.bradesco_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.model.BoletoBradescoAPIRequest;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.model.BoletoBradescoAPIResponse;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.model.BoletoBradescoModelConverter;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.RestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import javax.print.PrintService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import static org.apache.http.HttpHeaders.*;

/**
 * Classe Generica para servir como base de Implementação
 */
@Log
public class BancoBradescoAPI extends BoletoController {

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
        //TODO Implementar Impressão
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora,
                                            PrintService printService) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {

            BoletoBradescoAPIRequest boletoBradescoAPIRequest = BoletoBradescoModelConverter.montaBoletoRequest(boletoModel);
            BoletoBradescoAPIResponse boletoBradescoAPIResponse = registraBoleto(boletoBradescoAPIRequest);
            return BoletoBradescoModelConverter.montaBoletoResponse(boletoModel, boletoBradescoAPIResponse);

        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException | SignatureException | InvalidKeyException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    /**
     * Gera JWT API
     *
     * @param configuracao
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    private String gerarJWT(ConfiguracaoBradescoAPI configuracao) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        Date data = new Date();
        return Jwts.builder()
                .setAudience(configuracao.getURLBase() + configuracao.getUrlToken())
                .setSubject(configuracao.getClientId())
                .setIssuedAt(data)
                .setExpiration(new Date(data.getTime() + (1000 * 60 * 60 * 24)))
                .setId(String.valueOf(System.currentTimeMillis()))
                .signWith(
                        SignatureAlgorithm.RS256,
                        getKey(configuracao)
                )
                .compact();
    }

    private BoletoBradescoAPIResponse registraBoleto(BoletoBradescoAPIRequest boletoRequest) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException {

        ConfiguracaoBradescoAPI configuracao = (ConfiguracaoBradescoAPI) this.getConfiguracao();
        String token = getToken(configuracao);
        String nonce = String.valueOf(System.currentTimeMillis());
        String timestamp = BoletoUtil.converteData(LocalDateTime.now());
        String parametros = "";

        String json = RestUtil.ObjectToJson(boletoRequest);
        log.config("Json Envio Bradesco: " + json);

        String request = "POST\n" +
                configuracao.getUrlRegistroBoleto() + "\n" +
                parametros + "\n" +
                json + "\n" +
                token + "\n" +
                nonce + "\n" +
                timestamp + "\n" +
                "SHA256";
        log.config("Request Envio Bradesco: " + request);

        String assinatura = signSHA256RSA(configuracao, request);

        Header[] headers = {
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(CONTENT_TYPE, "application/json;charset=ISO-8859-1"),
                new BasicHeader(AUTHORIZATION, "Bearer " + token),
                new BasicHeader("X-Brad-Nonce", nonce),
                new BasicHeader("X-Brad-Timestamp", timestamp),
                new BasicHeader("X-Brad-Algorithm", "SHA256"),
                new BasicHeader("X-Brad-Signature", assinatura)
        };

        CloseableHttpResponse response = RestUtil.post(configuracao.getURLBase() + configuracao.getUrlRegistroBoleto(),
                headers, json);
        String retorno = RestUtil.validaResponseERetornaBody(response);
        log.config("Retorno Envio Bradesco: " + retorno);
        return RestUtil.JsonToObject(retorno, BoletoBradescoAPIResponse.class);
    }

    public String getToken(ConfiguracaoBradescoAPI configuracao) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {

        Header[] headers = {
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(CONTENT_TYPE, "application/x-www-form-urlencoded"),
        };

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("grant_type", "urn:ietf:params:oauth:grant-type:jwt-bearer"));
        nvps.add(new BasicNameValuePair("assertion", gerarJWT(configuracao)));

        HttpPost httpPost = new HttpPost(configuracao.getURLBase() + configuracao.getUrlToken());
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8));
        httpPost.setHeaders(headers);
        CloseableHttpResponse response = RestUtil.enviaComando(httpPost);

        String retorno = RestUtil.validaResponseERetornaBody(response);
        log.config("Retorno Token Bradesco: " + retorno);

        JsonObject json = JsonParser.parseString(retorno).getAsJsonObject();
        String token = json.get("access_token").getAsString();
        LocalDateTime expires = LocalDateTime.now().plusSeconds(json.get("expires_in").getAsInt());

        log.config("Token Bradesco: " + token);
        log.config("Expira: " + expires);

        return token;

    }

    private String signSHA256RSA(ConfiguracaoBradescoAPI configuracao, String input) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(getKey(configuracao));
        privateSignature.update(input.getBytes(StandardCharsets.UTF_8));
        byte[] s = privateSignature.sign();
        return Base64.getEncoder().encodeToString(s);
    }

    private PrivateKey getKey(ConfiguracaoBradescoAPI configuracao) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = FileUtils.openInputStream(new File(configuracao.getCaminhoCertificado()));
        String pk = IOUtils.toString(fileInputStream, StandardCharsets.UTF_8.name());
        String realPK = pk.replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "");

        byte[] b1 = Base64.getDecoder().decode(realPK);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b1);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(spec);
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

}
