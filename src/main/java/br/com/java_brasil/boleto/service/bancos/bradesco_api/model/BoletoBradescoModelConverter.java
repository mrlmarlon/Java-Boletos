package br.com.java_brasil.boleto.service.bancos.bradesco_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

public class BoletoBradescoModelConverter {

    /**
     * Converte BoletoModel para o padrão de entrada da API
     *
     * @param boletoModel
     * @return
     */
    public static BoletoBradescoAPIRequest montaBoletoRequest(BoletoModel boletoModel) {
        BoletoBradescoAPIRequest boletoRequest = new BoletoBradescoAPIRequest();

        preecheDadosBeneficiario(boletoModel, boletoRequest);

        preencheDadosBoleto(boletoModel, boletoRequest);

        preencheDadosMultaJuros(boletoModel, boletoRequest);

        preenchedadosPagador(boletoModel, boletoRequest);

        return boletoRequest;
    }

    private static void preencheDadosBoleto(BoletoModel boletoModel, BoletoBradescoAPIRequest boletoRequest) {
        boletoRequest.setDtEmissaoTitulo(BoletoUtil.getDataFormatoDDMMYYYY(LocalDate.now()));
        boletoRequest.setDtVencimentoTitulo(BoletoUtil.getDataFormatoDDMMYYYY(boletoModel.getDataVencimento()));
        boletoRequest.setVlNominalTitulo(Long.valueOf(BoletoUtil.valorSemPontos(boletoModel.getValorBoleto(), 2)));

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorDescontos())) {
            boletoRequest.setVlDesconto1(Long.valueOf(BoletoUtil.valorSemPontos(boletoModel.getValorDescontos(), 2)));
            boletoRequest.setDataLimiteDesconto1(BoletoUtil.getDataFormatoDDMMYYYY(boletoModel.getDataVencimento()));
        }
    }

    private static void preecheDadosBeneficiario(BoletoModel boletoModel, BoletoBradescoAPIRequest boletoRequest) {
        boletoRequest.setAgenciaDestino(Integer.valueOf(boletoModel.getBeneficiario().getAgencia()));
        boletoRequest.setNuCPFCNPJ(Integer.valueOf(boletoModel.getBeneficiario().getDocumento().substring(0, 8)));
        boletoRequest.setFilialCPFCNPJ(Integer.valueOf(boletoModel.getBeneficiario().getDocumento().substring(8, 12)));
        boletoRequest.setCtrlCPFCNPJ(Integer.valueOf(boletoModel.getBeneficiario().getDocumento().substring(boletoModel.getBeneficiario().getDocumento().length() - 2)));
        boletoRequest.setIdProduto(Integer.valueOf(boletoModel.getBeneficiario().getCarteira()));
        boletoRequest.setNuNegociacao(
                Long.valueOf(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, '0') +
                        "0000000" +
                        StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, '0')));
    }

    private static void preenchedadosPagador(BoletoModel boletoModel, BoletoBradescoAPIRequest boletoRequest) {
        boletoRequest.setControleParticipante(boletoModel.getPagador().getCodigo());
        boletoRequest.setNuCliente(boletoModel.getPagador().getCodigo());
        boletoRequest.setNomePagador(BoletoUtil.limitarTamanhoString(boletoModel.getPagador().getNome(), 70));
        boletoRequest.setLogradouroPagador(boletoModel.getPagador().getEndereco().getLogradouro());
        boletoRequest.setNuLogradouroPagador(boletoModel.getPagador().getEndereco().getNumero());
        boletoRequest.setComplementoLogradouroPagador(BoletoUtil.limitarTamanhoString(
                Optional.ofNullable(boletoModel.getPagador().getEndereco().getComplemento()).orElse(""), 15));
        boletoRequest.setCepPagador(Integer.valueOf(BoletoUtil.manterApenasNumeros(boletoModel.getPagador().getEndereco().getCep()).substring(0, 5)));
        boletoRequest.setComplementoCepPagador(Integer.valueOf(BoletoUtil.manterApenasNumeros(boletoModel.getPagador().getEndereco().getCep()).substring(5)));
        boletoRequest.setBairroPagador(boletoModel.getPagador().getEndereco().getBairro());
        boletoRequest.setMunicipioPagador(boletoModel.getPagador().getEndereco().getCidade());
        boletoRequest.setUfPagador(boletoModel.getPagador().getEndereco().getUf());
        boletoRequest.setCdIndCpfcnpjPagador(boletoModel.getPagador().isClienteCpf() ? 1 : 2);
        boletoRequest.setNuCpfcnpjPagador(Long.valueOf(StringUtils.leftPad(boletoModel.getPagador().getDocumento(), 14, '0')));
        boletoRequest.setEndEletronicoPagador(Optional.ofNullable(boletoModel.getPagador().getEmail()).orElse(""));
    }

    private static void preencheDadosMultaJuros(BoletoModel boletoModel, BoletoBradescoAPIRequest boletoRequest) {
        if (boletoModel.getDiasJuros() != 0) {
            boletoRequest.setPercentualJuros(Long.valueOf(BoletoUtil.bigDecimalSemCasas(boletoModel.getPercentualJuros())));
            boletoRequest.setQtdeDiasJuros(boletoModel.getDiasJuros());
        }

        if (boletoModel.getDiasMulta() != 0) {
            boletoRequest.setPercentualMulta(Long.valueOf(BoletoUtil.bigDecimalSemCasas(boletoModel.getPercentualMulta())));
            boletoRequest.setQtdeDiasMulta(Integer.valueOf(String.valueOf(boletoModel.getDiasMulta())));
        }
    }
}
