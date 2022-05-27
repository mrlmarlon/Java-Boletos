package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;

public class BoletoSicoobModelConverter {

    public static BoletoModel montaBoletoResponse(BoletoModel boletoModel, BoletoSicoobModel boletoSicoobModelResponse) {
//        boletoModel.setCodRetorno("0");
//        boletoModel.setMensagemRetorno("gerado com Sucesso");
//        boletoModel.setCodigoBarras(boletoSicoobResultadoResponse.getCodigoBarras());
//        boletoModel.setLinhaDigitavel(boletoSicoobResultadoResponse.getLinhaDigitavel());
//        boletoModel.setSituacaoBoleto(boletoSicoobResultadoResponse.getSituacaoBoleto());
//        boletoModel.setPdfBoleto(boletoSicoobResultadoResponse.getPdfBoleto());
        return boletoModel;
    }

    /**
     * Converte BoletoModel para o padrão de entrada da API
     *
     * @param boletoModel
     * @return
     */
    public static BoletoSicoobModel montaBoletoRequest(BoletoModel boletoModel, ConfiguracaoSicoobAPI configuracaoSicoobAPI) {
        BoletoSicoobModel resultado = new BoletoSicoobModel();

        resultado.setNumeroContrato(configuracaoSicoobAPI.getNumeroContrato());
        resultado.setModalidade(1);
        resultado.setNumeroContaCorrente(Integer.valueOf(configuracaoSicoobAPI.getContaCorrente()));
        resultado.setEspecieDocumento(boletoModel.getEspecieDocumento());
        resultado.setDataEmissao(BoletoUtil.getDataFormatoYYYYMMDD(LocalDate.now()));
        resultado.setSeuNumero(12345); //TODO criar
        resultado.setIdentificacaoEmissaoBoleto(2);
        resultado.setValor(boletoModel.getValorBoleto().setScale(2, RoundingMode.HALF_UP));
        resultado.setDataVencimento(BoletoUtil.getDataFormatoYYYYMMDD(boletoModel.getDataVencimento()));
        resultado.setNumeroParcela(1); //TODO criar
        resultado.setGerarPdf(true);

        preenchedadosPagador(boletoModel, resultado);
        preecheDadosBeneficiario(boletoModel, resultado);
        preencheDadosMultaJuros(boletoModel, resultado);
        preencheDesconto(boletoModel, resultado);


        return resultado;
    }

    private static void preencheDesconto(BoletoModel boletoModel, BoletoSicoobModel resultado) {
        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorDescontos())) {
            resultado.setTipoDesconto(1);
            resultado.setValorPrimeiroDesconto(boletoModel.getValorDescontos().setScale(2, RoundingMode.HALF_UP));
            resultado.setDataPrimeiroDesconto(BoletoUtil.getDataFormatoDDMMYYYY(boletoModel.getDataVencimento()));
        }else{
            resultado.setTipoDesconto(0);
        }
    }

    private static void preecheDadosBeneficiario(BoletoModel boletoModel, BoletoSicoobModel boletoRequest) {
        BoletoSicoobBeneficiarioFinal beneficiario = new BoletoSicoobBeneficiarioFinal();
        beneficiario.setNumeroCpfCnpj(boletoModel.getBeneficiario().getDocumento());
        beneficiario.setNome(boletoModel.getBeneficiario().getNomeBeneficiario());
        boletoRequest.setBeneficiarioFinal(beneficiario);
    }

    private static void preenchedadosPagador(BoletoModel boletoModel, BoletoSicoobModel boletoRequest) {
        BoletoSicoobPagador pagador = new BoletoSicoobPagador();

        pagador.setNumeroCpfCnpj(boletoModel.getPagador().getDocumento());
        pagador.setNome(BoletoUtil.limitarTamanhoString(boletoModel.getPagador().getNome(), 50));
        pagador.setEndereco(boletoModel.getPagador().getEndereco().getLogradouro());
        pagador.setBairro(boletoModel.getPagador().getEndereco().getBairro());
        pagador.setCidade(boletoModel.getPagador().getEndereco().getCidade());
        pagador.setCep(boletoModel.getPagador().getEndereco().getCep());
        pagador.setUf(boletoModel.getPagador().getEndereco().getUf());
        pagador.setEmail(StringUtils.isNotBlank(boletoModel.getPagador().getEmail()) ?
                Collections.singletonList(boletoModel.getPagador().getEmail()) :
                null);

        boletoRequest.setPagador(pagador);
    }

    private static void preencheDadosMultaJuros(BoletoModel boletoModel, BoletoSicoobModel boletoRequest) {
        if (boletoModel.getDiasJuros() != 0) {
            //TODO HA TAMBÉM O JUROSMORA, IMPLEMENTAR DEPOIS.
            boletoRequest.setTipoJurosMora(2);
            LocalDate dataJurosMora = LocalDate.parse(boletoRequest.getDataJurosMora());
//            boletoRequest.setValorMulta(dataJurosMora.plusDays(boletoModel.getDiasJuros()));
        }else{
            boletoRequest.setTipoJurosMora(0);
        }

        if (boletoModel.getDiasMulta() != 0) {
            //TODO fazer
//            boletoRequest.set(Long.valueOf(BoletoUtil.bigDecimalSemCasas(boletoModel.getPercentualMulta())));
//            boletoRequest.setQtdeDiasMulta(Integer.valueOf(String.valueOf(boletoModel.getDiasMulta())));
        }else{
            boletoRequest.setTipoMulta(0);
        }
    }

}
