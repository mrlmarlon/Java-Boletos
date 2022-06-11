package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.enums.SituacaoEnum;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

public class BoletoSicoobModelConverter {

    public static BoletoModel montaBoletoResponse(BoletoModel boletoModel, BoletoSicoobBoleto boletoResponse) {

        boletoModel.setCodigoBarras(boletoResponse.getCodigoBarras());
        boletoModel.setLinhaDigitavel(boletoResponse.getLinhaDigitavel());
        boletoModel.setImpressaoBase64(boletoResponse.getPdfBoleto());

        if (StringUtils.isBlank(boletoResponse.getSituacaoBoleto())) {
            boletoModel.setSituacao(SituacaoEnum.EM_ABERTO);
        } else {
            switch (boletoResponse.getSituacaoBoleto()) {
                case "Em Aberto":
                    boletoModel.setSituacao(SituacaoEnum.EM_ABERTO);
                    break;
                case "Baixado":
                    boletoModel.setSituacao(SituacaoEnum.BAIXADO);
                    break;
                case "Liquidado":
                    boletoModel.setSituacao(SituacaoEnum.LIQUIDADO);
                    break;

            }
        }

        return boletoModel;
    }

    /**
     * Converte BoletoModel para o padrão de entrada da API
     *
     * @param boletoModel
     * @return
     */
    public static BoletoSicoobBoleto montaBoletoRequest(BoletoModel boletoModel, ConfiguracaoSicoobAPI configuracaoSicoobAPI) {
        BoletoSicoobBoleto boleto = new BoletoSicoobBoleto();

        boleto.setNumeroContrato(configuracaoSicoobAPI.getNumeroContrato());
        boleto.setModalidade(1);
        boleto.setNumeroContaCorrente(Integer.valueOf(configuracaoSicoobAPI.getContaCorrente()));
        boleto.setEspecieDocumento(boletoModel.getEspecieDocumento());
        boleto.setDataEmissao(BoletoUtil.getDataFormatoYYYYMMDD(LocalDate.now()));
        boleto.setSeuNumero(Integer.valueOf(boletoModel.getNumeroBoleto()));
        boleto.setIdentificacaoEmissaoBoleto(2);
        boleto.setValor(boletoModel.getValorBoleto().setScale(2, RoundingMode.HALF_UP));
        boleto.setDataVencimento(BoletoUtil.getDataFormatoYYYYMMDD(boletoModel.getDataVencimento()));
        boleto.setNumeroParcela(Optional.ofNullable(boleto.getNumeroParcela()).orElse(1));
        boleto.setGerarPdf(true);

        preenchedadosPagador(boletoModel, boleto);
        preecheDadosBeneficiario(boletoModel, boleto);
        preencheDadosMultaJuros(boletoModel, boleto);
        preencheDesconto(boletoModel, boleto);

        return boleto;
    }

    private static void preencheDesconto(BoletoModel boletoModel, BoletoSicoobBoleto boleto) {
        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorDescontos())) {
            boleto.setTipoDesconto(1);
            boleto.setValorPrimeiroDesconto(boletoModel.getValorDescontos().setScale(2, RoundingMode.HALF_UP));
            boleto.setDataPrimeiroDesconto(BoletoUtil.getDataFormatoDDMMYYYY(boletoModel.getDataVencimento()));
        } else {
            boleto.setTipoDesconto(0);
        }
    }

    private static void preecheDadosBeneficiario(BoletoModel boletoModel, BoletoSicoobBoleto boletoRequest) {
        BoletoSicoobBeneficiarioFinal beneficiario = new BoletoSicoobBeneficiarioFinal();
        beneficiario.setNumeroCpfCnpj(boletoModel.getBeneficiario().getDocumento());
        beneficiario.setNome(boletoModel.getBeneficiario().getNomeBeneficiario());
        boletoRequest.setBeneficiarioFinal(beneficiario);
    }

    private static void preenchedadosPagador(BoletoModel boletoModel, BoletoSicoobBoleto boletoRequest) {
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

    private static void preencheDadosMultaJuros(BoletoModel boletoModel, BoletoSicoobBoleto boletoRequest) {
        if (boletoModel.getDiasJuros() != 0) {
            boletoRequest.setTipoJurosMora(2);
            boletoRequest.setValorJurosMora(boletoModel.getPercentualJuros().setScale(2, RoundingMode.HALF_UP));
            boletoRequest.setDataJurosMora(BoletoUtil.getDataFormatoYYYYMMDD(
                    boletoModel.getDataVencimento().plusDays(boletoModel.getDiasJuros())));
        } else {
            boletoRequest.setTipoJurosMora(0);
        }

        if (boletoModel.getDiasMulta() != 0) {
            boletoRequest.setTipoMulta(2);
            boletoRequest.setDataMulta(BoletoUtil.getDataFormatoYYYYMMDD(
                    boletoModel.getDataVencimento().plusDays(boletoModel.getDiasJuros())));
            boletoRequest.setValorMulta(boletoModel.getPercentualMulta().setScale(2, RoundingMode.HALF_UP));
        } else {
            boletoRequest.setTipoMulta(0);
        }
    }

}