package br.com.java_brasil.boleto.service.bancos.safe2pay_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.BoletoSafe2PayModelConverter;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.cancelamento.BoletoSafe2PayAPICancelarResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.consulta.BoletoSafe2PayAPIConsultaResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.envio.BoletoSafe2PayAPIEnvioResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.envio.Payment;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.RestUtil;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import javax.print.PrintService;
import java.io.IOException;
import java.util.List;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;
import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Classe Generica para servir como base de Implementação
 */
@Log
public class BancoSafe2PayAPI extends BoletoController {

    private ConfiguracaoSafe2PayAPI getConfiguracaoSicoob() {
        return (ConfiguracaoSafe2PayAPI) getConfiguracao();
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSafe2PayAPI configuracao = getConfiguracaoSicoob();

            Payment request = BoletoSafe2PayModelConverter.montaEnviarBoletoRequest(boletoModel, configuracao);

            String json = RestUtil.ObjectToJson(request);
            log.config("Json Envio Boleto: " + json);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(CONTENT_TYPE, "application/json"),
                    new BasicHeader("x-api-key", configuracao.getToken())
            };

            CloseableHttpResponse response = RestUtil.post(configuracao.getUrlBaseBoleto() + configuracao.getUrlBoleto(), headers, json);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.config("Retorno Envio Boleto: " + retorno);
            BoletoSafe2PayAPIEnvioResponse boletoSafe2PayAPIEnvioResponse = RestUtil.JsonToObject(retorno, BoletoSafe2PayAPIEnvioResponse.class);

            if (boletoSafe2PayAPIEnvioResponse == null) {
                throw new BoletoException("Erro ao enviar boleto, retorno nulo.");
            }

            if (boletoSafe2PayAPIEnvioResponse.getHasError() != null && boletoSafe2PayAPIEnvioResponse.getHasError()) {
                throw new BoletoException("Erro ao enviar boleto, código erro: " + boletoSafe2PayAPIEnvioResponse.getErrorCode());
            }

            return BoletoSafe2PayModelConverter.montaEnviarBoletoResponse(boletoModel, boletoSafe2PayAPIEnvioResponse);

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }

    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSafe2PayAPI configuracao = getConfiguracaoSicoob();

            String numeroBoleto = boletoModel.getNumeroBoleto();

            log.config("numeroBoleto Consulta Boleto: " + numeroBoleto);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(CONTENT_TYPE, "application/json"),
                    new BasicHeader("x-api-key", configuracao.getToken())
            };

            String urlConsulta = configuracao.getUrlBaseTransacao().concat(configuracao.getUrlTransacao()).concat("/Get?Id=").concat(numeroBoleto);
            log.config("Url Consulta Boleto: " + urlConsulta);

            CloseableHttpResponse response = RestUtil.get(urlConsulta, headers);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.config("Retorno Envio Boleto: " + retorno);
            BoletoSafe2PayAPIConsultaResponse boletoSafe2PayAPIConsultaResponse = RestUtil.JsonToObject(retorno, BoletoSafe2PayAPIConsultaResponse.class);

            if (boletoSafe2PayAPIConsultaResponse == null) {
                throw new BoletoException("Erro ao consultar boleto, retorno nulo.");
            }

            if (boletoSafe2PayAPIConsultaResponse.getHasError() != null && boletoSafe2PayAPIConsultaResponse.getHasError()) {
                throw new BoletoException("Erro ao consultar boleto, código erro: " + boletoSafe2PayAPIConsultaResponse.getErrorCode());
            }

            return BoletoSafe2PayModelConverter.montaConsultarBoletoResponse(boletoModel, boletoSafe2PayAPIConsultaResponse);

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }

    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        try {
            String urlPdf = boletoModel.getUrlPdf();
            return BoletoUtil.downloadFile(urlPdf);
        } catch (IOException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        return imprimirBoletoBanco(boletoModel);
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSafe2PayAPI configuracao = getConfiguracaoSicoob();

            Payment request = BoletoSafe2PayModelConverter.montaAlterarBoletoRequest(boletoModel);

            String json = RestUtil.ObjectToJson(request);
            log.config("Json Alterar Boleto: " + json);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(CONTENT_TYPE, "application/json"),
                    new BasicHeader("x-api-key", configuracao.getToken())
            };

            CloseableHttpResponse response = RestUtil.post(configuracao.getUrlBaseBoleto() + configuracao.getUrlBoleto(), headers, json);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.config("Retorno Alterar Boleto: " + retorno);
            BoletoSafe2PayAPIEnvioResponse boletoSafe2PayAPIEnvioResponse = RestUtil.JsonToObject(retorno, BoletoSafe2PayAPIEnvioResponse.class);

            if (boletoSafe2PayAPIEnvioResponse == null) {
                throw new BoletoException("Erro ao Alterar boleto, retorno nulo.");
            }

            if (boletoSafe2PayAPIEnvioResponse.getHasError() != null && boletoSafe2PayAPIEnvioResponse.getHasError()) {
                throw new BoletoException("Erro ao Alterar boleto, código erro: " + boletoSafe2PayAPIEnvioResponse.getErrorCode());
            }

            return BoletoSafe2PayModelConverter.montaEnviarBoletoResponse(boletoModel, boletoSafe2PayAPIEnvioResponse);

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }

    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {

        try {
            ConfiguracaoSafe2PayAPI configuracao = getConfiguracaoSicoob();

            String numeroBoleto = boletoModel.getNumeroBoleto();

            log.config("numeroBoleto baixa Boleto: " + numeroBoleto);

            Header[] headers = {
                    new BasicHeader(USER_AGENT, "PostmanRuntime/7.26.8"),
                    new BasicHeader(CONTENT_TYPE, "application/json"),
                    new BasicHeader("x-api-key", configuracao.getToken())
            };

            String urlBaixa = configuracao.getUrlBaseTransacao().concat(configuracao.getUrlCancelamento()).concat("/?idTransaction=").concat(numeroBoleto);
            log.config("Url baixa Boleto: " + urlBaixa);

            CloseableHttpResponse response = RestUtil.del(urlBaixa, headers);

            String retorno = RestUtil.validaResponseERetornaBody(response);
            log.config("Retorno baixa Boleto: " + retorno);
            BoletoSafe2PayAPICancelarResponse boletoSafe2PayAPICancelarResponse = RestUtil.JsonToObject(retorno, BoletoSafe2PayAPICancelarResponse.class);

            if (boletoSafe2PayAPICancelarResponse == null) {
                throw new BoletoException("Erro ao baixar boleto, retorno nulo.");
            }

            if (boletoSafe2PayAPICancelarResponse.isHasError()) {
                throw new BoletoException("Erro ao baixar boleto, código erro: " + boletoSafe2PayAPICancelarResponse.getError());
            }

            return BoletoSafe2PayModelConverter.montaBaixaBoletoResponse(boletoModel, boletoSafe2PayAPICancelarResponse);

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }

    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {
        throw new BoletoException("Não implementado!");
    }


}
