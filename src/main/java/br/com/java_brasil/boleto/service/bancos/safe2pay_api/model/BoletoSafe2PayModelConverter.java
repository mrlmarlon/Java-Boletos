package br.com.java_brasil.boleto.service.bancos.safe2pay_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.enums.SituacaoEnum;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.ConfiguracaoSafe2PayAPI;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.cancelamento.BoletoSafe2PayAPICancelarResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.consulta.BoletoSafe2PayAPIConsultaResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.envio.BoletoSafe2PayAPIEnvioResponse;
import br.com.java_brasil.boleto.service.bancos.safe2pay_api.model.envio.Payment;
import br.com.java_brasil.boleto.util.BoletoUtil;

public class BoletoSafe2PayModelConverter {

    /**
     * <p>Monta Payment com dado do Envio</p>
     *
     * @param boletoModel BoletoModel com os dados do boleto
     * @param configuracaoSafe2PayAPI Confogiração para comunicação com Webservicepouco.
     * @return Payment Objeto para requisicão de envio de boleto.
     */
    public static Payment montaEnviarBoletoRequest(BoletoModel boletoModel, ConfiguracaoSafe2PayAPI configuracaoSafe2PayAPI) {
        boolean sandbox = configuracaoSafe2PayAPI.isSandbox();
        Payment payment = new Payment(boletoModel, sandbox);
        return payment;
    }

    /**
     * <p>Monta BoletoModel com retorno do Envio</p>
     *
     * @param boletoModel BoletoModel com os dados do boleto
     * @param response incluindo os dados retornado pelo Webservicepouco.
     * @return boletoModel BoletoModel com os dados do boleto atualiuzado.
     */
    public static BoletoModel montaEnviarBoletoResponse(BoletoModel boletoModel, BoletoSafe2PayAPIEnvioResponse response) {
        boletoModel.setNumeroBoleto(response.getResponseDetail().getIdTransaction());
        boletoModel.setCodRetorno(response.getResponseDetail().getStatus());
        boletoModel.setMensagemRetorno(response.getResponseDetail().getMessage());
        boletoModel.setCodigoBarras(response.getResponseDetail().getBarcode());
        boletoModel.setUrlPdf(response.getResponseDetail().getBankSlipUrl());
        boletoModel.setSituacao(converteStatusSituacao(response.getResponseDetail().getStatus()));
        return boletoModel;
    }

    /**
     * <p>Monta BoletoModel com retorno da Consulta</p>
     *
     * @param boletoModel BoletoModel com os dados do boleto
     * @param response incluindo os dados retornado pelo Webservicepouco.
     * @return boletoModel BoletoModel com o numero do boleto a ser consultado!
     */
    public static BoletoModel montaConsultarBoletoResponse(BoletoModel boletoModel, BoletoSafe2PayAPIConsultaResponse response) {
        boletoModel.setNumeroBoleto(response.getResponseDetail().getIdTransaction());
        boletoModel.setCodRetorno(response.getResponseDetail().getStatus());
        boletoModel.setMensagemRetorno(response.getResponseDetail().getMessage());
        boletoModel.setSituacao(converteStatusSituacao(response.getResponseDetail().getStatus()));
        boletoModel.setUrlPdf(response.getResponseDetail().getPaymentObject().getBankSlipUrl());
        boletoModel.setCodigoBarras(response.getResponseDetail().getPaymentObject().getBarcode());
        boletoModel.setDataVencimento(BoletoUtil.formataStringPadraoYYYYMMDDParaLocalDate(response.getResponseDetail().getPaymentObject().getDueDate().replace("-", "")));
        return boletoModel;
    }

    /**
     * <p>Monta BoletoModel com retorno da Consulta</p>
     *
     * @param boletoModel BoletoModel com os dados do boleto
     * @param response incluindo os dados retornado pelo Webservicepouco.
     * @return boletoModel BoletoModel com o numero do boleto a ser consultado!
     */
    public static BoletoModel montaBaixaBoletoResponse(BoletoModel boletoModel, BoletoSafe2PayAPICancelarResponse response) {
//        boletoModel.setNumeroBoleto(response.getResponseDetail().getIdTransaction());
//        boletoModel.setCodRetorno(response.getResponseDetail().getStatus());
//        boletoModel.setMensagemRetorno(response.getResponseDetail().getMessage());
//        boletoModel.setSituacao(converteStatusSituacao(response.getResponseDetail().getStatus()));
//        boletoModel.setUrlPdf(response.getResponseDetail().getPaymentObject().getBankSlipUrl());
//        boletoModel.setCodigoBarras(response.getResponseDetail().getPaymentObject().getBarcode());
//        boletoModel.setDataVencimento(BoletoUtil.formataStringPadraoYYYYMMDDParaLocalDate(response.getResponseDetail().getPaymentObject().getDueDate().replace("-", "")));
        return boletoModel;
    }

    ///1	    Pendente	Iniciado uma transação pelo comprador, porém até o momento o Safe2Pay não recebeu nenhuma informação sobre o pagamento.
    ///2	    Processamento	Esta transação está em processamento e em breve deve retornar o status final da situação do pagamento.
    ///3	    Autorizado	A transação foi paga pelo comprador e o Safe2Pay já recebeu uma confirmação da instituição financeira responsável pelo processamento.
    ///7	    Baixado	O boleto bancário foi baixado automáticamente pela instituição financeira após 29 dias de seu vencimento.
    ///11	Liberado	A transação foi liberada por um usuário com perfil financeiro, entretanto até o momento não foi paga.
    ///12	Em cancelamento	A transação está em cancelamento até o período de baixa

    /**
     * <p>Verifica e converte o código de status da Safe2Pay para SituacaoEnum</p>
     *
     * @param status Status retornado pela Safe2Pay
     * @return SituacaoEnum
     */
    private static SituacaoEnum converteStatusSituacao(String status){
        if (status != null && (status.equals("7") || status.equals("11") || status.equals("12"))) {
            return SituacaoEnum.BAIXADO;
        } else if (status != null && status.equals("3")) {
            return SituacaoEnum.LIQUIDADO;
        } else {
            return SituacaoEnum.EM_ABERTO;
        }
    }

    /**
     * <p>Monta Payment com dado da Alteração</p>
     *
     * No momento somente é possivel alterar a data de vencimento
     *
     * @param boletoModel BoletoModel com a data de vencimento a ser modificada!
     * @return Payment Objeto para requisicão de envio de boleto.
     */
    public static Payment montaAlterarBoletoRequest(BoletoModel boletoModel) {
        Payment payment = new Payment(boletoModel.getNumeroBoleto(), boletoModel.getDataVencimento(), 1);
        return payment;
    }


}
