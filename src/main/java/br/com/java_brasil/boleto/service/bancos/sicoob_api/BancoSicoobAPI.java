package br.com.java_brasil.boleto.service.bancos.sicoob_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.InformacaoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobBoleto;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobConsultaResponse;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobEnvioResponse;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.model.BoletoSicoobModelConverter;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.JasperUtil;
import br.com.java_brasil.boleto.util.RestUtil;
import br.com.java_brasil.boleto.util.ValidaUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;

import javax.print.PrintService;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

import static org.apache.http.HttpHeaders.*;

/**
 * Classe Generica para servir como base de Implementação
 */
@Slf4j
public class BancoSicoobAPI extends BoletoController {

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        try {
            return imprimir(boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {

    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
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

            if (boletoResponse == null) {
                throw new BoletoException("Erro ao enviar boleto, código erro: " + boletoSicoobResponse.getResultado().get(0).getStatus().getCodigo());
            }

            return BoletoSicoobModelConverter.montaBoletoResponse(boletoModel, boletoResponse);

        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        return null;
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    private ConfiguracaoSicoobAPI getConfiguracaoSicoob() {
        return (ConfiguracaoSicoobAPI) getConfiguracao();
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
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

    private byte[] imprimir(BoletoModel boletoModel) throws Exception {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoSicoob.png");
        Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
        parametros.put("LogoBanco", image);

        preparaValidaBoletoImpressao(boletoModel);

        return JasperUtil.geraRelatorio(Collections.singletonList(boletoModel),
                parametros,
                "BoletoSicoob",
                this.getClass(),
                new HashMap<>());
    }

    private void preparaValidaBoletoImpressao(BoletoModel boletoModel) {
        boletoModel.getBeneficiario().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiario().getDocumento()));
        boletoModel.getPagador().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getPagador().getDocumento()));
        if (boletoModel.getBeneficiarioFinal() != null) {
            boletoModel.getBeneficiarioFinal().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiarioFinal().getDocumento()));
        }

        StringBuilder codigoBarras = new StringBuilder();
        codigoBarras.append("7569");
        codigoBarras.append("X"); //Substituir pelo Digito verificador
        codigoBarras.append(br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.fatorData(boletoModel.getDataVencimento()));
        codigoBarras.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

        StringBuilder campoLivre = new StringBuilder();
        campoLivre.append("1");
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
        campoLivre.append("01");
        campoLivre.append(boletoModel.getBeneficiario().getNumeroConvenio());
        campoLivre.append(boletoModel.getBeneficiario().getNossoNumero().substring(2));
        campoLivre.append(boletoModel.getBeneficiario().getDigitoNossoNumero());
        campoLivre.append("001");
        codigoBarras.append(campoLivre);

        Integer digitoVerificadorGeral = br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.modulo11(codigoBarras.toString().replace("X", ""));
        boletoModel.setCodigoBarras(codigoBarras.toString().replace("X", digitoVerificadorGeral.toString()));

        StringBuilder linhaParte1 = new StringBuilder();
        linhaParte1.append("75691");
        linhaParte1.append(boletoModel.getBeneficiario().getAgencia());
        linhaParte1.append(br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.modulo10(linhaParte1.toString()));

        StringBuilder linhaParte2 = new StringBuilder();
        linhaParte2.append("01");
        linhaParte2.append(boletoModel.getBeneficiario().getNumeroConvenio());
        linhaParte2.append(boletoModel.getBeneficiario().getNossoNumero().substring(2, 3));
        linhaParte2.append(br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.modulo10(linhaParte2.toString()));

        StringBuilder linhaParte3 = new StringBuilder();
        linhaParte3.append(boletoModel.getBeneficiario().getNossoNumero().substring(3));
        linhaParte3.append(boletoModel.getBeneficiario().getDigitoNossoNumero());
        linhaParte3.append("001");
        linhaParte3.append(br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.modulo10(linhaParte3.toString()));

        StringBuilder linhaParte4 = new StringBuilder();
        linhaParte4.append(digitoVerificadorGeral);

        StringBuilder linhaParte5 = new StringBuilder();
        linhaParte5.append(br.com.java_brasil.boleto.service.bancos.sicoob_cnab240.SicoobUtil.fatorData(boletoModel.getDataVencimento()));
        linhaParte5.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

        if (StringUtils.isEmpty(boletoModel.getLinhaDigitavel())) {
            montaLinhaDigitavel(boletoModel, linhaParte1, linhaParte2, linhaParte3, linhaParte4, linhaParte5);
        }

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualMulta())) {
            String instrucaoMulta = "APOS VENCIMENTO COBRAR MULTA DE "
                    + BoletoUtil.formatarCasasDecimais(boletoModel.getValorPercentualMulta(), 2) + "%";
            boletoModel.getInstrucoes().add(new InformacaoModel(instrucaoMulta));
        }

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualJuros())) {
            BigDecimal valorPercentualJuros = boletoModel.getValorPercentualJuros();
            BigDecimal valorMoraDia = boletoModel.getValorPercentualJuros();
            if (boletoModel.getTipoJuros().equals(TipoJurosEnum.PERCENTUAL_MENSAL)
                    && BoletoUtil.isNotNullEMaiorQZero(valorPercentualJuros)) {
                // Se informado percentual de juros mensal, deve dividir por 30 dias para se
                // obter o percentual diário
                valorPercentualJuros = valorPercentualJuros.divide(BigDecimal.valueOf(30), MathContext.DECIMAL32);
                valorMoraDia = boletoModel.getValorBoleto().multiply(valorPercentualJuros, MathContext.DECIMAL32);
                valorMoraDia = valorMoraDia.divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
            }

            String instrucaoJuros = "APOS VENCIMENTO COBRAR MORA DIARIA DE R$ "
                    + BoletoUtil.formatarCasasDecimais(valorMoraDia, 2);
            boletoModel.getInstrucoes().add(new InformacaoModel(instrucaoJuros));
        }

        validaDadosImpressao(boletoModel);
    }

    private void montaLinhaDigitavel(BoletoModel boletoModel, StringBuilder linhaParte1, StringBuilder linhaParte2, StringBuilder linhaParte3, StringBuilder linhaParte4, StringBuilder linhaParte5) {
        StringBuilder linhaDigitavel = new StringBuilder();
        linhaDigitavel.append(linhaParte1.toString(), 0, 5);
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte1.toString(), 5, 10);
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte2.toString(), 0, 5);
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte2.toString(), 5, 11);
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte3.toString(), 0, 5);
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte3.toString(), 5, 11);
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte4);
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte5);

        boletoModel.setLinhaDigitavel(linhaDigitavel.toString());
    }

    private void validaDadosImpressao(BoletoModel boleto) {
        ValidaUtils.validaBoletoModel(boleto,
                Arrays.asList("locaisDePagamento",
                        "dataVencimento",
                        "beneficiario.nomeBeneficiario",
                        "beneficiario.documento",
                        "beneficiario.agencia",
                        "beneficiario.numeroConvenio",
                        "beneficiario.conta",
                        "dataEmissao",
                        "numeroDocumento",
                        "especieDocumento",
                        "aceite",
                        "beneficiario.nossoNumero",
                        "beneficiario.digitoNossoNumero",
                        "especieMoeda",
                        "valorBoleto",
                        "pagador.nome",
                        "pagador.documento",
                        "pagador.endereco.logradouro",
                        "pagador.endereco.cep",
                        "linhaDigitavel",
                        "codigoBarras",
                        "pagador.endereco.numero",
                        "pagador.endereco.bairro",
                        "pagador.endereco.cidade",
                        "pagador.endereco.uf"));
    }

}
