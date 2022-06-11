package br.com.java_brasil.boleto.service.bancos.sicoob_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobBoleto;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobConsultaResponse;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobEnvioResponse;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobModelConverter;
import br.com.java_brasil.boleto.util.RestUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
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
        return Base64.decodeBase64(
                Optional.ofNullable(boletoModel.getImpressaoBase64()).orElseThrow(
                        () -> new BoletoException("Campo ImpressãoBase64 não está preenchido, consulte o boleto para receber a impressão.")
                ));
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSicoobAPI configuracao = getConfiguracaoSicoob();
            validaAutenticacao(configuracao);

            BoletoSicoobBoleto request = BoletoSicoobModelConverter.montaBoletoRequest(boletoModel, configuracao);

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
            BoletoSicoobEnvioResponse boletoSicoobResponse = RestUtil.JsonToObject(retorno, BoletoSicoobEnvioResponse.class);

            BoletoSicoobBoleto boletoResponse = boletoSicoobResponse.getResultado().get(0).getBoleto();

            if(boletoResponse == null){
                throw new BoletoException("Erro ao enviar boleto, código erro: "+ boletoSicoobResponse.getResultado().get(0).getStatus().getCodigo());
            }

            return BoletoSicoobModelConverter.montaBoletoResponse(boletoModel, boletoResponse);

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
        try {
            ConfiguracaoSicoobAPI configuracao = (ConfiguracaoSicoobAPI) getConfiguracao();

            validaAutenticacao(configuracao);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(AUTHORIZATION, "Bearer " + configuracao.getToken()),
            };

            String parametros = "?numeroContrato=" + configuracao.getNumeroContrato();
            parametros += "modalidade=1";

            if (boletoModel.getBeneficiario() != null &&
                    StringUtils.isNotBlank(boletoModel.getBeneficiario().getNossoNumero())) {
                parametros += "nossoNumero=" + boletoModel.getBeneficiario().getNossoNumero();
            } else if (StringUtils.isNotBlank(boletoModel.getLinhaDigitavel())) {
                parametros += "linhaDigitavel=" + boletoModel.getLinhaDigitavel();
            } else if (StringUtils.isNotBlank(boletoModel.getCodigoBarras())) {
                parametros += "linhaDigitavel=" + boletoModel.getCodigoBarras();
            }

            CloseableHttpResponse response = RestUtil.get(
                    configuracao.getURLBase() +
                            configuracao.getUrlConsultaBoleto() +
                            parametros, headers);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.debug("Retorno Consulta Boleto: " + retorno);
            BoletoSicoobConsultaResponse boletoSicoobResponse = RestUtil.JsonToObject(retorno, BoletoSicoobConsultaResponse.class);

            return BoletoSicoobModelConverter.montaBoletoResponse(boletoModel, boletoSicoobResponse.getResultado());

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
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
