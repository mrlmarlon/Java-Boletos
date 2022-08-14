package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.InformacaoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BoletoItauModelConverter {

    /**
     * Converte BoletoModel para o padrão de entrada da API
     *
     * @param boletoModel
     * @return
     */
    public static BoletoItauAPIRequest montaBoletoRequest(BoletoModel boletoModel, AmbienteEnum ambienteEnum) {
        BoletoItauAPIRequest boletoRequest = new BoletoItauAPIRequest();
        boletoRequest.setEtapa_processo_boleto(ambienteEnum.equals(AmbienteEnum.PRODUCAO) ? "efetivacao" : "simulacao");
        preencheDadosBenefeciario(boletoModel, boletoRequest);
        preencheDadosBoleto(boletoModel, boletoRequest);

        return boletoRequest;
    }

    public static BoletoModel montaBoletoResponse(BoletoModel boletoModel, BoletoItauAPIResponse boletoItauAPIResponse) {
        boletoModel.setCodRetorno("200");
        boletoModel.setMensagemRetorno("Registrado com Sucesso");
        boletoModel.setCodigoBarras(boletoItauAPIResponse.getDado_boleto().getDados_individuais_boleto().get(0).getCodigo_barras());
        boletoModel.setLinhaDigitavel(boletoItauAPIResponse.getDado_boleto().getDados_individuais_boleto().get(0).getNumero_linha_digitavel());

        if (boletoItauAPIResponse.getDados_qrcode() != null) {
            boletoModel.setPixTxidQrCode(boletoItauAPIResponse.getDados_qrcode().getTxid());
            boletoModel.setPixUrlQrCode(boletoItauAPIResponse.getDados_qrcode().getLocation());
            boletoModel.setPixCopiaCola(boletoItauAPIResponse.getDados_qrcode().getEmv());
            boletoModel.setPixBase64(boletoItauAPIResponse.getDados_qrcode().getBase64());
        }

        return boletoModel;
    }

    private static void preencheDadosIndividuais(BoletoModel boletoModel, DadoBoletoItauAPI dadosBoleto) {
        DadosIndividuaisBoletoItauAPI dadosIndividuais = new DadosIndividuaisBoletoItauAPI();
        dadosIndividuais.setNumero_nosso_numero(StringUtils.leftPad(boletoModel.getNumeroBoleto(), 8, '0'));
        dadosIndividuais.setData_vencimento(BoletoUtil.getDataFormatoYYYYMMDD(boletoModel.getDataVencimento()));
        dadosIndividuais.setValor_titulo(StringUtils.leftPad(
                BoletoUtil.valorSemPontos(boletoModel.getValorBoleto(), 2)
                , 17, '0'));
        dadosBoleto.setDados_individuais_boleto(Collections.singletonList(dadosIndividuais));
    }

    private static void preencheDadosBoleto(BoletoModel boletoModel, BoletoItauAPIRequest boletoRequest) {
        DadoBoletoItauAPI dadosBoleto = new DadoBoletoItauAPI();
        dadosBoleto.setTipo_boleto("a vista");
        dadosBoleto.setCodigo_carteira(boletoModel.getBeneficiario().getCarteira());
        dadosBoleto.setCodigo_tipo_vencimento("3");
        dadosBoleto.setValor_total_titulo(StringUtils.leftPad(
                BoletoUtil.valorSemPontos(boletoModel.getValorBoleto(), 2)
                , 17, '0'));
        dadosBoleto.setCodigo_especie(boletoModel.getEspecieDocumento());
        dadosBoleto.setData_emissao(BoletoUtil.getDataFormatoYYYYMMDD(boletoModel.getDataEmissao()));

        preencheDadosPagador(boletoModel, dadosBoleto);
        preencheDadosIndividuais(boletoModel, dadosBoleto);
        preencheJurosMulta(boletoModel, dadosBoleto);
        preencheDesconto(boletoModel, dadosBoleto);
        preencheMensagens(boletoModel, dadosBoleto);

        boletoRequest.setDado_boleto(dadosBoleto);

    }

    private static void preencheMensagens(BoletoModel boletoModel, DadoBoletoItauAPI dadosBoleto) {
        if (!boletoModel.getDescricoes().isEmpty()) {
            List<MensagemCobrancaBoletoItauAPI> listaMensagens = new ArrayList<>();
            for (InformacaoModel descricoes : boletoModel.getDescricoes()) {
                MensagemCobrancaBoletoItauAPI mensagem = new MensagemCobrancaBoletoItauAPI();
                mensagem.setMensagem(descricoes.getInformacao());
                listaMensagens.add(mensagem);
            }
            dadosBoleto.setLista_mensagem_cobranca(listaMensagens);
        }
    }

    private static void preencheDesconto(BoletoModel boletoModel, DadoBoletoItauAPI dadosBoleto) {
        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorDescontos())) {
            DescontoBoletoItauAPI descontoBoletoItauAPI = new DescontoBoletoItauAPI();
            descontoBoletoItauAPI.setCodigo_tipo_desconto("01");

            DescontosBoletoItauAPI descontos = new DescontosBoletoItauAPI();
            descontos.setValor_desconto(StringUtils.leftPad(
                    BoletoUtil.valorSemPontos(boletoModel.getValorDescontos(), 2)
                    , 17, '0'));
            descontoBoletoItauAPI.setDescontos(Collections.singletonList(descontos));
            dadosBoleto.setDesconto(descontoBoletoItauAPI);
        }
    }

    private static void preencheJurosMulta(BoletoModel boletoModel, DadoBoletoItauAPI dadosBoleto) {
        if (boletoModel.getDiasJuros() != 0) {
            JurosBoletoItauAPI juros = new JurosBoletoItauAPI();
            juros.setData_juros(BoletoUtil.getDataFormatoYYYYMMDD(
                    boletoModel.getDataVencimento().plusDays(boletoModel.getDiasJuros())));
            juros.setCodigo_tipo_juros("90");
            juros.setPercentual_juros(StringUtils.leftPad(
                    BoletoUtil.valorSemPontos(boletoModel.getPercentualJuros(), 5)
                    , 12, '0'));
            dadosBoleto.setJuros(juros);
        }

        if (boletoModel.getDiasMulta() != 0) {
            MultaBoletoItauAPI multa = new MultaBoletoItauAPI();
            multa.setCodigo_tipo_multa("02");
            multa.setData_multa(BoletoUtil.getDataFormatoYYYYMMDD(
                    boletoModel.getDataVencimento().plusDays(boletoModel.getDiasMulta())));
            multa.setPercentual_multa(StringUtils.leftPad(
                    BoletoUtil.valorSemPontos(boletoModel.getPercentualMulta(), 5)
                    , 12, '0'));
            dadosBoleto.setMulta(multa);
        }
    }

    private static void preencheDadosPagador(BoletoModel boletoModel, DadoBoletoItauAPI dadosBoleto) {

        PagadorBoletoItauAPI pagador = new PagadorBoletoItauAPI();
        PessoaBoletoItauAPI pessoa = new PessoaBoletoItauAPI();
        pessoa.setNome_pessoa(StringUtils.abbreviate(boletoModel.getPagador().getNome(), null, 50));

        TipoPessoaBoletoItauAPI tipoPessoa = new TipoPessoaBoletoItauAPI();
        if (boletoModel.getPagador().isClienteCpf()) {
            tipoPessoa.setCodigo_tipo_pessoa("F");
            tipoPessoa.setNumero_cadastro_pessoa_fisica(boletoModel.getPagador().getDocumento());
        } else {
            tipoPessoa.setCodigo_tipo_pessoa("J");
            tipoPessoa.setNumero_cadastro_nacional_pessoa_juridica(boletoModel.getPagador().getDocumento());
        }
        pessoa.setTipo_pessoa(tipoPessoa);
        pagador.setPessoa(pessoa);

        EnderecoBoletoItauAPI endereco = new EnderecoBoletoItauAPI();
        endereco.setNome_logradouro(StringUtils.abbreviate(boletoModel.getPagador().getEndereco().getLogradouro(), null, 45));
        endereco.setNome_bairro(boletoModel.getPagador().getEndereco().getBairro());
        endereco.setNome_cidade(boletoModel.getPagador().getEndereco().getCidade());
        endereco.setSigla_UF(boletoModel.getPagador().getEndereco().getUf());
        endereco.setNumero_CEP(boletoModel.getPagador().getEndereco().getCep());
        pagador.setEndereco(endereco);
        dadosBoleto.setPagador(pagador);
    }

    private static void preencheDadosBenefeciario(BoletoModel boletoModel, BoletoItauAPIRequest boletoRequest) {
        BeneficiarioBoletoItauAPI beneficiario = new BeneficiarioBoletoItauAPI();
        beneficiario.setId_beneficiario(
                StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, '0') +
                        StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, '0') +
                        boletoModel.getBeneficiario().getDigitoConta());
        boletoRequest.setBeneficiario(beneficiario);
    }
}
