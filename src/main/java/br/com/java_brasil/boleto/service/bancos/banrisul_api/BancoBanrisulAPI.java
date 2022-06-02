package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.InformacaoModel;
import br.com.java_brasil.boleto.model.RemessaRetornoModel;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.JasperUtil;
import br.com.java_brasil.boleto.util.ValidaUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.print.PrintService;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Slf4j
public class BancoBanrisulAPI extends BoletoController {

    @Override
    public byte[] imprimirBoleto(@NonNull BoletoModel boletoModel) {
        try {
            return imprimir(boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }
    }

    @Override
    public void imprimirBoletoDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {
        try {
            imprimirDesktop(boletoModel, diretoImpressora, printService);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }
    }

    @Override
    public byte[] emitirBoleto(@NonNull BoletoModel boletoModel) {
        try {
            ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();
            String xmlEnvio = montaBoletoXml(boletoModel, "EmitirBoleto");
            String retorno = BanrisulUtil.comunicacaoHttpsURLConnection(xmlEnvio, configuracao);
            return new BancoBanrisulEmitirTitulo().validaXmlRetorno(retorno);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        try {
            ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();
            String xmlEnvio = montaBoletoXml(boletoModel, "RegistrarTitulo");
            String retorno = BanrisulUtil.comunicacaoHttpsURLConnection(xmlEnvio, configuracao);
            return new BancoBanrisulRegistrarTitulo().validaXmlRetorno(retorno, boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        try {
            ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();
            String xmlEnvio = montaBoletoXml(boletoModel, "AlterarTitulo");
            String retorno = BanrisulUtil.comunicacaoHttpsURLConnection(xmlEnvio, configuracao);
            return new BancoBanrisulAlterarTitulo().validaXmlRetorno(retorno, boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
        try {
            ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();
            String xmlEnvio = montaBoletoXml(boletoModel, "ConsultarTitulo");
            String retorno = BanrisulUtil.comunicacaoHttpsURLConnection(xmlEnvio, configuracao);
            return new BancoBanrisulConsultarTitulo().validaXmlRetorno(retorno, boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        try {
            ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();
            String xmlEnvio = montaBoletoXml(boletoModel, "BaixarTitulo");
            String retorno = BanrisulUtil.comunicacaoHttpsURLConnection(xmlEnvio, configuracao);
            return new BancoBanrisulBaixarTitulo().validaXmlRetorno(retorno, boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    private byte[] imprimir(BoletoModel boletoModel) throws Exception {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoBanrisul.jpg");
        Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
        parametros.put("LogoBanco", image);

        preparaValidaBoletoImpressao(boletoModel);

        byte[] byteRelatorio = JasperUtil.geraRelatorio(Arrays.asList(boletoModel),
                parametros,
                "BoletoBanrisul",
                this.getClass(),
                new HashMap<>());

        return byteRelatorio;
    }

    private void imprimirDesktop(BoletoModel boletoModel, boolean diretoImpressora,
                                 PrintService printService) throws Exception {
        try {
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

            InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoBanrisul.jpg");
            Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
            parametros.put("LogoBanco", image);

            preparaValidaBoletoImpressao(boletoModel);

            JasperUtil.geraRelatorioDescktop(Arrays.asList(boletoModel), parametros, "BoletoBanrisul", this.getClass(),
                    new HashMap<>(), diretoImpressora, printService);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoletoException(e.getMessage());
        }
    }

    /**
     * Converte BoletoModel para o XML entrada da API
     *
     * @param boletoModel BoletoModel contendo os dados do boleto
     * @param servico String com o serviço a ser executado
     * @return String XML
     */
    private String montaBoletoXml(BoletoModel boletoModel, String servico) throws Exception {
        ConfiguracaoBanrisulAPI configuracao = (ConfiguracaoBanrisulAPI) this.getConfiguracao();

        String xmlEntrada;
        switch (servico) {
            case "RegistrarTitulo":
                xmlEntrada = new BancoBanrisulRegistrarTitulo().montaXmlRegistrarTitulo(boletoModel, configuracao.getAmbiente());
                break;
            case "AlterarTitulo":
                xmlEntrada = new BancoBanrisulAlterarTitulo().montaXmlAlterarTitulo(boletoModel, configuracao.getAmbiente());
                break;
            case "ConsultarTitulo":
                xmlEntrada = new BancoBanrisulConsultarTitulo().montaXmlConsultarTitulo(boletoModel);
                break;
            case "BaixarTitulo":
                xmlEntrada = new BancoBanrisulBaixarTitulo().montaXmlBaixarTitulo(boletoModel, configuracao.getAmbiente());
                break;
            case "EmitirBoleto":
                xmlEntrada = new BancoBanrisulEmitirTitulo().montaXmlEmitirBoleto(boletoModel, configuracao.getAmbiente());
                break;
            default:
                throw new Exception("Serviço não encontrado");
        }

        String envelopeSoap = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<" + servico + " xmlns=\"Bergs.Boc.Bocswsxn\">" +
                "<xmlEntrada>" + xmlEntrada + "</xmlEntrada>" +
                "</" + servico + ">" +
                "</soap:Body>" +
                "</soap:Envelope>";

        return envelopeSoap;
    }

    private void preparaValidaBoletoImpressao(BoletoModel boletoModel) {
        boletoModel.getBeneficiario().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiario().getDocumento()));
        boletoModel.getPagador().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getPagador().getDocumento()));
        if (boletoModel.getBeneficiarioFinal() != null) {
            boletoModel.getBeneficiarioFinal().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiarioFinal().getDocumento()));
        }

        boletoModel.getBeneficiario().setAgencia(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
        boletoModel.getBeneficiario().setConta(StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, "0"));
        boletoModel.getBeneficiario().setDigitoConta(StringUtils.leftPad(boletoModel.getBeneficiario().getDigitoConta(), 2, "0"));
        boletoModel.getBeneficiario().getEndereco().setComplemento(
                boletoModel.getBeneficiario().getEndereco().getComplemento() == null ? "" :
                        boletoModel.getBeneficiario().getEndereco().getComplemento()
        );

        if(StringUtils.isBlank(boletoModel.getCodigoBarras())){
            StringBuilder codigoBarras = new StringBuilder();
            codigoBarras.append("0419");
            codigoBarras.append("X"); //Substituir pelo DAC
            codigoBarras.append(BanrisulUtil.fatorData(boletoModel.getDataVencimento()));
            codigoBarras.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

            StringBuilder campoLivre = new StringBuilder();
            campoLivre.append("21");
            campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
            campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, "0"));
            campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getNossoNumero(),8,"0"));
            campoLivre.append("40");

            Integer digito10 = BanrisulUtil.modulo10Banrisul(campoLivre.toString());
            codigoBarras.append(BanrisulUtil.modulo11Banrisul(campoLivre.toString(), digito10));

            String dac = BanrisulUtil.modulo11Dac(codigoBarras.toString().replace("X", ""));

            boletoModel.setCodigoBarras(codigoBarras.toString().replace("X", dac));
            if(StringUtils.isBlank(boletoModel.getLinhaDigitavel())){
                boletoModel.setLinhaDigitavel(codigoBarras.toString().replace("X", dac));
            }
        }

        if(StringUtils.isBlank(boletoModel.getLinhaDigitavel())){
            StringBuilder linhaParte1 = new StringBuilder();
            linhaParte1.append(boletoModel.getLinhaDigitavel().substring(0, 4));
            linhaParte1.append(boletoModel.getLinhaDigitavel().substring(19, 21));
            linhaParte1.append(boletoModel.getLinhaDigitavel().substring(21, 24));
            linhaParte1.append(BanrisulUtil.modulo10Banrisul(linhaParte1.toString()));

            StringBuilder linhaParte2 = new StringBuilder();
            linhaParte2.append(boletoModel.getLinhaDigitavel().substring(24, 25));
            linhaParte2.append(boletoModel.getLinhaDigitavel().substring(25, 32));
            linhaParte2.append(boletoModel.getLinhaDigitavel().substring(32, 34));
            linhaParte2.append(BanrisulUtil.modulo10Banrisul(linhaParte2.toString()));

            StringBuilder linhaParte3 = new StringBuilder();
            linhaParte3.append(boletoModel.getLinhaDigitavel().substring(34, 40));
            linhaParte3.append("40");
            linhaParte3.append(boletoModel.getLinhaDigitavel().substring(42, 44));
            linhaParte3.append(BanrisulUtil.modulo10Banrisul(linhaParte3.toString()));

            StringBuilder linhaParte4 = new StringBuilder();
            linhaParte4.append(boletoModel.getLinhaDigitavel().substring(4, 5));
            linhaParte4.append(boletoModel.getLinhaDigitavel().substring(5, 9));
            linhaParte4.append(boletoModel.getLinhaDigitavel().substring(9, 19));

            StringBuilder linhaDigitavel = new StringBuilder();
            linhaDigitavel.append(linhaParte1.substring(0, 5));
            linhaDigitavel.append(".");
            linhaDigitavel.append(linhaParte1.substring(5, 10));
            linhaDigitavel.append("  ");

            linhaDigitavel.append(linhaParte2.substring(0, 5));
            linhaDigitavel.append(".");
            linhaDigitavel.append(linhaParte2.substring(5, 11));
            linhaDigitavel.append("  ");

            linhaDigitavel.append(linhaParte3.substring(0, 5));
            linhaDigitavel.append(".");
            linhaDigitavel.append(linhaParte3.substring(5, 11));
            linhaDigitavel.append("  ");

            linhaDigitavel.append(linhaParte4.substring(0, 1));
            linhaDigitavel.append("  ");
            linhaDigitavel.append(linhaParte4.substring(1, 15));

            boletoModel.setLinhaDigitavel(linhaDigitavel.toString());
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

    private void validaDadosImpressao(BoletoModel boleto) {
        ValidaUtils.validaBoletoModel(boleto,
                Arrays.asList("locaisDePagamento", "dataVencimento", "beneficiario.nomeBeneficiario",
                        "beneficiario.documento", "beneficiario.agencia", "beneficiario.digitoConta",
                        "beneficiario.conta", "beneficiario.endereco", "beneficiario.endereco.logradouro",
                        "beneficiario.endereco.numero", "beneficiario.endereco.bairro", "beneficiario.endereco.cidade",
                        "beneficiario.endereco.uf", "beneficiario.endereco.cep","dataEmissao", "numeroDocumento", "especieDocumento", "aceite",
                        "beneficiario.nossoNumero", "beneficiario.digitoNossoNumero", "especieMoeda", "valorBoleto",
                        "pagador.nome", "pagador.documento", "pagador.endereco.logradouro", "pagador.endereco.cep",
                        "linhaDigitavel", "codigoBarras", "pagador.endereco.numero", "pagador.endereco.bairro",
                        "pagador.endereco.cidade", "pagador.endereco.uf"));
    }
}
