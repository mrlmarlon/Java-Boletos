package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.alterar.Beneficiario;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.alterar.Dados;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.alterar.Titulo;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

public class BancoBanrisulAlterarTitulo {


    /**
     * <p>Monta o XML para Alterar um boleto.</p>
     *
     * Até o momento somente é permitido alterar a data de vencimento
     *
     * @param boletoModel BoletoModel contendo os dados do boleto a ser alterado
     * @param ambiente    AmbienteEnum com o ambiente HOMOLOGACAO, PRODUCAO
     * @return String no formato XML do boleto
     */
    public String montaXmlAlterarTitulo(BoletoModel boletoModel, AmbienteEnum ambiente) throws Exception {
        Dados dados = new Dados();
        dados.setAmbiente(ambiente.equals(AmbienteEnum.PRODUCAO) ? "P" : "T");
        dados.setTipoAlteracao((short) 6);

        Titulo titulo = new Titulo();

        if (!StringUtils.isBlank(boletoModel.getBeneficiario().getNossoNumero())) {
            titulo.setNossoNumero(Long.valueOf(boletoModel.getBeneficiario().getNossoNumero()));
        } else if (!StringUtils.isBlank(boletoModel.getCodigoBarras())) {
            titulo.setCodigoBarras(boletoModel.getCodigoBarras());
        } else {
            titulo.setLinhaDigitavel(boletoModel.getLinhaDigitavel());
        }

        titulo.setDataVencimento(BoletoUtil.converteDataXMLGregorianCalendar(BoletoUtil.obterInicioDoDia(boletoModel.getDataVencimento())));

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

        return BanrisulUtil.transformarObjetoParaXml(dados, "AlterarTitulo");
    }

    /**
     * Valida a response
     *
     * @param xmlRetorno String contendo o xml retornado pelo Webservice
     * @param boletoModel BoletoModel com os dados do boleto
     * @return BoletoModel incluindo os dados retornado pelo Webservice
     */
    public BoletoModel validaXmlRetorno(String xmlRetorno, BoletoModel boletoModel) throws Exception {
        xmlRetorno = BanrisulUtil.extrairNoXmlRetorno(xmlRetorno);

        Dados dadosAlterar = BanrisulUtil.transformarXmlParaObjeto(xmlRetorno, Dados.class);
        if (dadosAlterar.getRetorno() == 3) {
            StringBuilder erro = new StringBuilder();
            dadosAlterar.getOcorrencias().getOcorrencia().forEach(ocorrencia -> {
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

        boletoModel.setCodRetorno(dadosAlterar.getRetorno().toString());
        return boletoModel;
    }
}
