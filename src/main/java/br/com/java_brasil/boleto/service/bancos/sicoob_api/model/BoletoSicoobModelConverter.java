package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.model.BoletoBradescoAPIRequest;
import br.com.java_brasil.boleto.service.bancos.bradesco_api.model.BoletoBradescoAPIResponse;
import br.com.java_brasil.boleto.service.bancos.sicoob_api.ConfiguracaoSicoobAPI;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

public class BoletoSicoobModelConverter {

    /**
     * Converte BoletoModel para o padrão de entrada da API
     *
     * @param boletoModel
     * @return
     */
    public static BoletoSicoobAPIRequest montaBoletoRequest(ConfiguracaoSicoobAPI configuracaoSicoobAPI, BoletoModel boletoModel) {
        BoletoSicoobAPIRequest boletoRequest = new BoletoSicoobAPIRequest();

        boletoRequest.setNumeroContrato(configuracaoSicoobAPI.getNumeroContrato());
        boletoRequest.setModalidade(1);
        boletoRequest.setNumeroContaCorrente(Integer.valueOf(configuracaoSicoobAPI.getContaCorrente()));
        boletoRequest.setEspecieDocumento(boletoModel.getEspecieDocumento());


        return boletoRequest;
    }


}
