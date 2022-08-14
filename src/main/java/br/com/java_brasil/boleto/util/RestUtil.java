package br.com.java_brasil.boleto.util;

import br.com.java_brasil.boleto.exception.RestException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.CertificateException;

public final class RestUtil {

    /**
     * Construtor privado para garantir o Singleton.
     */
    private RestUtil() {
    }

    /**
     * Envia comando POST
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static CloseableHttpResponse post(String url, Header[] headers, String json) {
        return post(url, headers, json, null, null);
    }

    /**
     * Envia comando POST
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static CloseableHttpResponse post(String url, Header[] headers, String json, String caminhoCertificado, String senhaCertificado) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeaders(headers);
        if (StringUtils.isNotBlank(caminhoCertificado)) {
            return enviaComandoCertificado(httpPost, caminhoCertificado, senhaCertificado);
        } else {
            return enviaComando(httpPost);
        }
    }

    /**
     * Envia comando PUT
     *
     * @param url
     * @param headers
     * @param json
     * @return
     */
    public static CloseableHttpResponse put(String url, Header[] headers, String json) {
        HttpPut httpPut = new HttpPut(url);
        StringEntity entity = new StringEntity(json, "UTF-8");
        httpPut.setEntity(entity);
        httpPut.setHeaders(headers);
        return enviaComando(httpPut);
    }

    /**
     * Envia comando GET
     *
     * @param url
     * @param headers
     * @return
     */
    public static CloseableHttpResponse get(String url, Header[] headers) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(headers);
        return enviaComando(httpGet);
    }

    /**
     * Envia comando DEL
     *
     * @param url
     * @param headers
     * @return
     */
    public static CloseableHttpResponse del(String url, Header[] headers) {
        HttpDelete httpDelete = new HttpDelete(url);
        httpDelete.setHeaders(headers);
        return enviaComando(httpDelete);
    }

    /**
     * Envia Comando
     *
     * @param request Recebe o comando Get,Put,Post ou Del
     * @return Body de retorno
     */
    public static CloseableHttpResponse enviaComando(HttpUriRequest request) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD).build()).build();

            return client.execute(request);
        } catch (IOException e) {
            throw new RestException(e.getMessage(), e);
        }
    }

    /**
     * Envia Comando
     *
     * @param request Recebe o comando Get,Put,Post ou Del
     * @return Body de retorno
     */
    public static CloseableHttpResponse enviaComandoCertificado(HttpUriRequest request, String caminhoCertificado, String senha) {
        try {

            KeyStore keyStore;
            try (InputStream keyStoreStream = Files.newInputStream(Paths.get(caminhoCertificado))) {
                keyStore = KeyStore.getInstance("PKCS12");
                keyStore.load(keyStoreStream, StringUtils.isNotBlank(senha) ? senha.toCharArray() : null);
            } catch (CertificateException e) {
                throw new RestException(e.getMessage(), e);
            }

            SSLContext sslContext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore,
                            StringUtils.isNotBlank(senha) ? senha.toCharArray() : null)
                    .build();

            CloseableHttpClient client = HttpClientBuilder.create()
                    .setSSLContext(sslContext)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.STANDARD).build()).build();
            return client.execute(request);

        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | KeyManagementException e) {
            throw new RestException(e.getMessage(), e);
        }
    }

    /**
     * Valida a response
     *
     * @param response
     * @return
     */
    public static String validaResponseERetornaBody(CloseableHttpResponse response) throws IOException {
        if (response.getStatusLine().getStatusCode() != 200 &&
                response.getStatusLine().getStatusCode() != 201 &&
                response.getStatusLine().getStatusCode() != 202 &&
                response.getStatusLine().getStatusCode() != 207) {
            String erro = response.getStatusLine().getStatusCode() + " - " + EntityUtils.toString(response.getEntity(), "UTF-8");
            throw new RestException(erro);
        }
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    /**
     * Converte o objeto para String Json
     *
     * @param objeto
     * @return
     * @throws JsonProcessingException
     */
    public static String ObjectToJson(Object objeto) throws JsonProcessingException {
        return new ObjectMapper().registerModule(new JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(objeto);
    }

    /**
     * Converte o String Json para Objeto
     *
     * @param objeto
     * @return
     * @throws JsonProcessingException
     */
    public static <T> T JsonToObject(String json, Class<T> clazz) throws IOException {
        if (StringUtils.isBlank(json)) {
            return null;
        } else {
            return new ObjectMapper().registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false)
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, false)
                    .readValue(json, clazz);
        }
    }

}
