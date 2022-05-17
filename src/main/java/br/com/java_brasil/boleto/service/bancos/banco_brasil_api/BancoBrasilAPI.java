package br.com.java_brasil.boleto.service.bancos.banco_brasil_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoController;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.service.bancos.banco_brasil_api.model.*;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.RestUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.*;

@Slf4j
public class BancoBrasilAPI extends BoletoController {

    @Override
    public byte[] imprimirBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {

        try {

            BoletoBancoBrasilAPIRequest boletoBancoBrasilAPIRequest = montaBoletoRequest(boletoModel);
            return null;

        } catch (IOException e) {
            throw new BoletoException(e.getMessage(),e);
        }
    }




    /**
     * Converte BoletoModel para o padrão de entrada da API
     * @param boletoModel
     * @return
     */
    private BoletoBancoBrasilAPIRequest montaBoletoRequest(BoletoModel boletoModel) {
        BoletoBancoBrasilAPIRequest boletoRequest = BoletoBancoBrasilAPIRequest.builder()
                .numeroConvenio(boletoModel.getNumeroConvenio())
                .numeroCarteira(boletoModel.getNumeroCarteira())
                .numeroVariacaoCarteira(boletoModel.getNumeroVariacaoCarteira())
                .codigoModalidade(boletoModel.getCodigoModalidade())
                .dataEmissao(BoletoUtil.getDataFormatoDDMMYYYY(LocalDate.now()))
                .dataVencimento(BoletoUtil.getDataFormatoDDMMYYYY(boletoModel.getDataVencimento()))
                .valorOriginal(boletoModel.getValorBoleto())
                .valorAbatimento(boletoModel.getValorDescontos())
                .codigoAceite(boletoModel.getCodigoAceite())
                .codigoTipoTituloCobranca(boletoModel.getCodigoTipoTituloCobranca())
                .indicadorPermissaoRecebimentoParcial(boletoModel.getIndicadorPermissaoRecebimentoParcial())
                .numeroTituloBeneficiario(boletoModel.getNumeroTituloBeneficiario())
                .numeroTituloCliente(boletoModel.getNumeroTituloCliente())
                .pagador(Pagador.builder()
                        .tipoInscricao(boletoModel.getPagador().getTipoInscricao())
                        .numeroInscricao(boletoModel.getPagador().getNumeroInscricao())
                        .nome(boletoModel.getPagador().getNome())
                        .cep(Integer.valueOf(boletoModel.getPagador().getEndereco().getCep()))
                        .cidade(boletoModel.getPagador().getEndereco().getCidade())
                        .bairro(boletoModel.getPagador().getEndereco().getBairro())
                        .uf(boletoModel.getPagador().getEndereco().getUf())
                        .build())
                .build();

        return boletoRequest;
    }

    private BoletoModel montaBoletoResponse(BoletoModel boletoModel, BoletoBancoBrasilAPIResponse boletoBancoBrasilAPIResponse) {
        boletoModel.setCodigoCliente(boletoBancoBrasilAPIResponse.getCodigoCliente());
        boletoModel.setLinhaDigitavel(boletoBancoBrasilAPIResponse.getLinhaDigitavel());
        boletoModel.setCodigoBarras(boletoBancoBrasilAPIResponse.getCodigoBarraNumerico());
        boletoModel.setNumeroContratoCobranca(boletoBancoBrasilAPIResponse.getNumeroContratoCobranca());
        return boletoModel;
    }

    @Override
    public BoletoModel alteraBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

    @Override
    public BoletoModel consultaBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Não implementado!");
    }

}
