package br.com.java_brasil.boleto.service.bancos.sicoob_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobModel;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobModelConverter;
import br.com.java_brasil.boleto.util.RestUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.apache.http.HttpHeaders.*;

/**
 * Classe Generica para servir como base de Implementação
 */
@Slf4j
public class BancoSicoobAPI extends BoletoController {

    @Override
    public byte[] imprimirBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
        //TODO Implementar Impressão
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSicoobAPI configuracao = getConfiguracaoSicoob();
            validaAutenticacao(configuracao);

            BoletoSicoobModel request = BoletoSicoobModelConverter.montaBoletoRequest(boletoModel, configuracao);

            String json = RestUtil.ObjectToJson(Collections.singletonList(request));
            log.debug("Json Envio Boleto: " + json);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(CONTENT_TYPE, "application/json"),
                    new BasicHeader(AUTHORIZATION, "Bearer " + configuracao.getToken()),
            };

            CloseableHttpResponse response = RestUtil.post(configuracao.getURLBase() + configuracao.getUrlRegistraBoleto(), headers, json);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.debug("Retorno Envio Boleto: " + retorno);

            return null;
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    private ConfiguracaoSicoobAPI getConfiguracaoSicoob() {
        return (ConfiguracaoSicoobAPI) getConfiguracao();
    }

    @Override
    public BoletoModel alteraBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel consultaBoleto(@NonNull BoletoModel boletoModel) {
        ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) getConfiguracao();

        validaAutenticacao(configuracao);

        Header[] headers = {
                new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                new BasicHeader(AUTHORIZATION, "Bearer " + configuracao.getToken()),
        };

        CloseableHttpResponse response = RestUtil.get(configuracao.getURLBase() + configuracao.getUrlConsultaBoleto(), headers);

        try {
            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.debug("Retorno Consulta Boleto: " + retorno);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void validaAutenticacao(ConfiguracaoSicoobAPI configuracao) {

        try {
            Optional.ofNullable(configuracao.getToken()).orElseThrow(() -> new BoletoException("Token não pode ser vazio."));
            if (configuracao.getExpiracaoToken() == null || configuracao.getExpiracaoToken().isBefore(LocalDateTime.now())) {
                log.debug("Token existe porém está expirado. Executando RefreshToken.");
                Optional.ofNullable(configuracao.getRefreshToken()).orElseThrow(() -> new BoletoException("Refresh Token não pode ser vazio."));
                SicoobUtil.refreshToken(configuracao);
            }
        } catch (IOException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

}
