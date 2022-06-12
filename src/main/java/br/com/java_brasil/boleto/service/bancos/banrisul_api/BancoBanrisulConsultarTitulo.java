package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Dados;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Instrucoes;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Mensagem;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Titulo;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

public class BancoBanrisulConsultarTitulo {


    /**
     * <p>Monta o XML para Consultar um boleto.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do boleto a ser alterado
     * @return String no formato XML do boleto
     */
    public String montaXmlConsultarTitulo(BoletoModel boletoModel) throws Exception {
        Dados dados = new Dados();

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
            br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Beneficiario beneficiario = new br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Beneficiario();
            String codigoBeneficiario = StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0");
            codigoBeneficiario = codigoBeneficiario + StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 9, "0");
            beneficiario.setCodigo(Long.parseLong(codigoBeneficiario));

            titulo.setBeneficiario(beneficiario);
        }
        dados.setTitulo(titulo);

        return BanrisulUtil.transformarObjetoParaXml(dados, "ConsultarTitulo");
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

        Dados dadosConsultar = BanrisulUtil.transformarXmlParaObjeto(xmlRetorno, Dados.class);
        if (dadosConsultar.getRetorno() == 3) {
            StringBuilder erro = new StringBuilder();
            dadosConsultar.getOcorrencias().getOcorrencia().forEach(ocorrencia -> {
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
        return montaRetornoConsulta(dadosConsultar, boletoModel);
    }

    /**
     * Monta um BoletoModel com base no retorna da consulta
     *
     * @param dados       Dados retornado na consulta
     * @param boletoModel BoletoModel com os dados do boleto
     * @return BoletoModel incluindo os dados retornado pelo Webservice
     */
    private BoletoModel montaRetornoConsulta(Dados dados, BoletoModel boletoModel) {
        boletoModel.setCodRetorno(dados.getRetorno().toString());
        boletoModel.setBeneficiario(getBeneficiario(dados.getTitulo()));
        boletoModel.setPagador(getPagador(dados.getTitulo()));

        boletoModel.setNumeroDocumento(dados.getTitulo().getSeuNumero());
        boletoModel.setDataVencimento(BoletoUtil.converteXMLGregorianCalendarData(dados.getTitulo().getDataVencimento()));
        boletoModel.setValorBoleto(dados.getTitulo().getValorNominal());
        boletoModel.setEspecieDocumento(StringUtils.leftPad(dados.getTitulo().getEspecie().toString(), 2, "0"));
        boletoModel.setDataEmissao(BoletoUtil.converteXMLGregorianCalendarData(dados.getTitulo().getDataEmissao()));
        boletoModel.setValorIof(dados.getTitulo().getValorIof());
        boletoModel.setCodigoBarras(dados.getTitulo().getCodigoBarras());
        boletoModel.setLinhaDigitavel(dados.getTitulo().getLinhaDigitavel());
        boletoModel.setAceite(dados.getTitulo().getPagador().getAceite().equals("A"));

        verificaInstrucoes(dados.getTitulo(), boletoModel);
        verificaPagParcial(dados.getTitulo(), boletoModel);

        if (dados.getTitulo().getMensagens() != null) {
            boletoModel.setInstrucoes(new ArrayList<>());
            for(Mensagem mensagem : dados.getTitulo().getMensagens().getMensagem()) {
                boletoModel.getInstrucoes().add(new InformacaoModel(mensagem.getTexto()));
            }
        }
        /*
         * Verificar demais informações se iremos recuperar
         *
         * dados.getTitulo().getSituacaoBanrisul();
                A = Ativo
                B = Baixado por pagamento
                D = Baixado por devolução
                L = Liquidado
                R = Reembolsado
                T = Transferido para CL
                P = Protestado

         * dados.getTitulo().getSituacaoCip();
                A = Aprovado
                R = Rejeitado
                P = Pendente
                E = Enviado
                N = Não centralizado

         * dados.getTitulo().getSituacaoPagamento();
                1 = Apto para pagamento
                2 = Pagamento em processamento
                3 = Crédito retido
                4 = Baixado no dia
                5 = Em processo de devolução automática
                6 = Em cartório
                7 = Pagamento indisponível, entre em contato com sua agência

         * dados.getTitulo().getOperacoes();
                Vários dados referente ao pagamento do título

         * dados.getTitulo().getRateio();
         */

        return boletoModel;
    }

    /**
     * <p>Retorna um Beneficiário.</p>
     *
     * @param titulo Título retornado pelo WebService
     * @return Beneficiario com base nos dados informados
     */
    private Beneficiario getBeneficiario(Titulo titulo) {
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.setNossoNumero(titulo.getNossoNumero().toString());
        beneficiario.setCarteira(titulo.getCarteira().toString());
        if (titulo.getBeneficiario().getTipoPessoa().equals("F")) {
            beneficiario.setDocumento(StringUtils.leftPad(titulo.getBeneficiario().getCpfCnpj().toString(), 11, "0"));
        } else {
            beneficiario.setDocumento(StringUtils.leftPad(titulo.getBeneficiario().getCpfCnpj().toString(), 14, "0"));
        }
        beneficiario.setNomeBeneficiario(titulo.getBeneficiario().getNome());

        return beneficiario;
    }

    /**
     * <p>Retorna um Pagador.</p>
     *
     * @param titulo Título retornado pelo WebService
     * @return Pagador com base nos dados informados
     */
    private Pagador getPagador(Titulo titulo) {
        Pagador pagador = new Pagador();
        if (titulo.getPagador().getTipoPessoa().equals("F")) {
            pagador.setDocumento(StringUtils.leftPad(String.valueOf(titulo.getPagador().getCpfCnpj()), 11, "0"));
        } else {
            pagador.setDocumento(StringUtils.leftPad(String.valueOf(titulo.getPagador().getCpfCnpj()), 14, "0"));
        }
        pagador.setNome(titulo.getPagador().getNome());
        pagador.setEndereco(new Endereco());
        pagador.getEndereco().setLogradouro(titulo.getPagador().getEndereco());
        pagador.getEndereco().setCep(StringUtils.leftPad(String.valueOf(titulo.getPagador().getCep()), 8, "0"));
        pagador.getEndereco().setCidade(titulo.getPagador().getCidade());
        pagador.getEndereco().setUf(titulo.getPagador().getUf());
        return pagador;
    }

    /**
     * <p>Seta as instruções.</p>
     *
     * @param titulo      Título retornado pelo WebService
     * @param boletoModel BoletoModel a ser preenchido
     */
    private void verificaInstrucoes(Titulo titulo, BoletoModel boletoModel) {
        Instrucoes instrucoes = titulo.getInstrucoes();
        if (instrucoes.getJuros() != null) {
            boletoModel.setDiasJuros(0);
            boletoModel.setTipoJuros(TipoJurosEnum.ISENTO);
            boletoModel.setValorPercentualJuros(BigDecimal.ZERO);
            if (instrucoes.getJuros().getCodigo() != 3) {
                LocalDate dataJuros = BoletoUtil.converteXMLGregorianCalendarData(instrucoes.getJuros().getData());
                boletoModel.setDiasJuros((int) (BoletoUtil.difEntreDatasEmDias(boletoModel.getDataVencimento(), dataJuros)));

                boletoModel.setTipoJuros(instrucoes.getJuros().getCodigo() == 1 ? TipoJurosEnum.VALOR_DIA : TipoJurosEnum.PERCENTUAL_MENSAL);
                boletoModel.setValorPercentualJuros(instrucoes.getJuros().getCodigo() == 1 ? instrucoes.getJuros().getValor() : instrucoes.getJuros().getTaxa());
            }
        }

        if (instrucoes.getMulta() != null) {
            boletoModel.setDiasMulta(0);
            boletoModel.setTipoMulta(instrucoes.getMulta().getCodigo() == 1 ? TipoMultaEnum.VALOR : TipoMultaEnum.PERCENTUAL);
            boletoModel.setValorPercentualMulta(instrucoes.getMulta().getValor());

            if (instrucoes.getMulta().getData() != null) {
                LocalDate dataMulta = BoletoUtil.converteXMLGregorianCalendarData(instrucoes.getMulta().getData());
                boletoModel.setDiasMulta((int) (BoletoUtil.difEntreDatasEmDias(boletoModel.getDataVencimento(), dataMulta)));
            }
        }

        if (instrucoes.getDesconto() != null) {
            switch (instrucoes.getDesconto().getCodigo()) {
                case 1:
                    boletoModel.setTipoDesconto(TipoDescontoEnum.VALOR_FIXO);
                    boletoModel.setValorPercentualDescontos(instrucoes.getDesconto().getValor());
                    break;
                case 2:
                    boletoModel.setTipoDesconto(TipoDescontoEnum.PERCENTUAL_FIXO);
                    boletoModel.setValorPercentualDescontos(instrucoes.getDesconto().getTaxa());
                    break;
                case 3:
                    boletoModel.setTipoDesconto(TipoDescontoEnum.VALOR_DIA);
                    boletoModel.setValorPercentualDescontos(instrucoes.getDesconto().getValor());
                default:
                    boletoModel.setTipoDesconto(TipoDescontoEnum.PERCENTUAL_DIA);
                    boletoModel.setValorPercentualDescontos(instrucoes.getDesconto().getTaxa());
            }
            if (instrucoes.getDesconto().getData() != null) {
                boletoModel.setDataLimiteParaDesconto(BoletoUtil.converteXMLGregorianCalendarData(instrucoes.getDesconto().getData()));
            }
        }

        if (instrucoes.getAbatimento() != null) {
            boletoModel.setValorDeducoes(instrucoes.getAbatimento().getValor());
        }

        if (instrucoes.getProtesto() != null) {
            boletoModel.setProtesto(instrucoes.getProtesto().getCodigo() == 1);
            boletoModel.setDiasProtesto(instrucoes.getProtesto().getPrazo());
        }

        if (instrucoes.getBaixa() != null) {
            boletoModel.setDiasParaBaixaDevolver(instrucoes.getBaixa().getPrazo());
        }
    }

    /**
     * <p>Verifica pagamento parcial.</p>
     *
     * @param titulo      Título retornado pelo WebService
     * @param boletoModel BoletoModel a ser preenchido
     */
    private void verificaPagParcial(Titulo titulo, BoletoModel boletoModel) {
        boletoModel.setAutorizaPagamentoParcial(false);
        boletoModel.setCodigoPagamentoParcial(3);
        boletoModel.setQuantidadePagamentoParcial(0);
        boletoModel.setTipoPagamentoParcial(1);
        boletoModel.setValorMinPagamentoParcial(BigDecimal.ZERO);
        boletoModel.setValorMaxPagamentoParcial(BigDecimal.ZERO);
        boletoModel.setPercentualMinPagamentoParcial(BigDecimal.ZERO);
        boletoModel.setPercentualMaxPagamentoParcial(BigDecimal.ZERO);

        if (titulo.getPagParcial() != null) {
            boletoModel.setAutorizaPagamentoParcial(titulo.getPagParcial().getAutoriza() == 2);
            boletoModel.setCodigoPagamentoParcial(titulo.getPagParcial().getCodigo());

            if (boletoModel.getCodigoPagamentoParcial() == 2) {
                boletoModel.setQuantidadePagamentoParcial(titulo.getPagParcial().getQuantidade());
                boletoModel.setTipoPagamentoParcial(titulo.getPagParcial().getTipo());
                if (boletoModel.getTipoPagamentoParcial() == 2) {
                    boletoModel.setValorMinPagamentoParcial(titulo.getPagParcial().getValorMin());
                    boletoModel.setValorMaxPagamentoParcial(titulo.getPagParcial().getValorMax());
                }
                if (boletoModel.getTipoPagamentoParcial() == 1) {
                    boletoModel.setPercentualMinPagamentoParcial(titulo.getPagParcial().getPercentualMin());
                    boletoModel.setPercentualMaxPagamentoParcial(titulo.getPagParcial().getPercentualMax());
                }
            }
        }
    }
}
