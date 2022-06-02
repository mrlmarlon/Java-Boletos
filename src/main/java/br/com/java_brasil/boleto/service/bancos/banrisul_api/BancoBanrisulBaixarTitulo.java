package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar.Beneficiario;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar.Dados;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar.Titulo;
import org.apache.commons.lang3.StringUtils;

public class BancoBanrisulBaixarTitulo {


    /**
     * <p>Monta o XML para Baixar um boleto.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do boleto a ser alterado
     * @param ambiente    AmbienteEnum com o ambiente HOMOLOGACAO, PRODUCAO
     * @return String no formato XML do boleto
     */
    public String montaXmlBaixarTitulo(BoletoModel boletoModel, AmbienteEnum ambiente) throws Exception {
        Dados dados = new Dados();
        dados.setAmbiente(ambiente.equals(AmbienteEnum.PRODUCAO) ? "P" : "T");

        Titulo titulo = new Titulo();

        if (!StringUtils.isBlank(boletoModel.getBeneficiario().getNossoNumero())) {
            titulo.setNossoNumero(Long.valueOf(boletoModel.getBeneficiario().getNossoNumero()));
        } else if (!StringUtils.isBlank(boletoModel.getCodigoBarras())) {
            titulo.setCodigoBarras(boletoModel.getCodigoBarras());
        } else {
            titulo.setLinhaDigitavel(boletoModel.getLinhaDigitavel());
        }

        //Opcional
        if (boletoModel.getBeneficiario() != null
                && !StringUtils.isBlank(boletoModel.getBeneficiario().getAgencia())
                && !StringUtils.isBlank(boletoModel.getBeneficiario().getConta())) {
            Beneficiario beneficiario = new Beneficiario();
            String codigoBeneficiario = StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0");
            codigoBeneficiario = codigoBeneficiario + StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 9, "0");
            beneficiario.setCodigo(Long.parseLong(codigoBeneficiario));

            titulo.setBeneficiario(beneficiario);
        }
        dados.setTitulo(titulo);

        return BanrisulUtil.transformarObjetoParaXml(dados, "BaixarTitulo");
    }

    /**
     * Valida a response
     *
     * @param xmlRetorno  String contendo o xml retornado pelo Webservice
     * @param boletoModel BoletoModel com os dados do boleto
     * @return BoletoModel incluindo os dados retornado pelo Webservice
     */
    public BoletoModel validaXmlRetorno(String xmlRetorno, BoletoModel boletoModel) throws Exception {
        xmlRetorno = BanrisulUtil.extrairNoXmlRetorno(xmlRetorno);

        Dados dadosBaixar = BanrisulUtil.transformarXmlParaObjeto(xmlRetorno, Dados.class);
        if (dadosBaixar.getRetorno() == 3) {
            StringBuilder erro = new StringBuilder();
            dadosBaixar.getOcorrencias().getOcorrencia().forEach(ocorrencia -> {
                erro.append(ocorrencia.getCodigo());
                erro.append(" - ");
                erro.append(ocorrencia.getMensagem());
                if (ocorrencia.getComplemento() != null) {
                    erro.append(" - ");
                    erro.append(ocorrencia.getComplemento());
                }
                erro.append("\n");
            });
            throw new BoletoException(erro.toString());
        }

        boletoModel.setCodRetorno(dadosBaixar.getRetorno().toString());
        return boletoModel;
    }
}
