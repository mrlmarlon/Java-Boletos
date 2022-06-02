package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.emitir.Beneficiario;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.emitir.Dados;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.emitir.Titulo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

public class BancoBanrisulEmitirTitulo {


    /**
     * <p>Monta o XML para Emitir um boleto.</p>
     *
     * Irá retornar byte[] em formato PDF codificado (alfanumérico de Base64)
     *
     * @param boletoModel BoletoModel contendo os dados do boleto a ser alterado
     * @param ambiente    AmbienteEnum com o ambiente HOMOLOGACAO, PRODUCAO
     * @return String no formato XML do boleto
     */
    public String montaXmlEmitirBoleto(BoletoModel boletoModel, AmbienteEnum ambiente) throws Exception {
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

        return BanrisulUtil.transformarObjetoParaXml(dados, "EmitirBoleto");
    }

    /**
     * Valida a response
     *
     * @param xmlRetorno String contendo o xml retornado pelo Webservice
     * @return byte[] em formato PDF
     */
    public byte[] validaXmlRetorno(String xmlRetorno) throws Exception {
        xmlRetorno = BanrisulUtil.extrairNoXmlRetorno(xmlRetorno);

        Dados dadosEmitir = BanrisulUtil.transformarXmlParaObjeto(xmlRetorno, Dados.class);
        if (dadosEmitir.getRetorno() == 3) {
            StringBuilder erro = new StringBuilder();
            dadosEmitir.getOcorrencias().getOcorrencia().forEach(ocorrencia -> {
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
        return Base64.decodeBase64(dadosEmitir.getTitulo().getBoleto().getBytes());
    }
}
