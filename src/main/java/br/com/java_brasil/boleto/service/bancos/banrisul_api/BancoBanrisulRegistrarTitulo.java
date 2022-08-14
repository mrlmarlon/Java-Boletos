package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.BoletoModel;
import br.com.java_brasil.boleto.model.InformacaoModel;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.registra.*;
import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

public class BancoBanrisulRegistrarTitulo {

    /**
     * <p>Monta o XML para registrar(Enviar) um boleto.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do boleto a ser registrado(Enviado)
     * @param ambiente    AmbienteEnum com o ambiente HOMOLOGACAO, PRODUCAO
     * @return String no formato XML do boleto
     */
    public String montaXmlRegistrarTitulo(BoletoModel boletoModel, AmbienteEnum ambiente) throws Exception {
        Dados dados = new Dados();
        dados.setAmbiente(ambiente.equals(AmbienteEnum.PRODUCAO) ? "P" : "T");
        dados.setTitulo(getTitulo(boletoModel));

        return BanrisulUtil.transformarObjetoParaXml(dados, "RegistrarTitulo");
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

        Dados dadosRegistro = BanrisulUtil.transformarXmlParaObjeto(xmlRetorno, Dados.class);
        if (dadosRegistro.getRetorno() == 3) {
            StringBuilder erro = new StringBuilder();
            dadosRegistro.getOcorrencias().getOcorrencia().forEach(ocorrencia -> {
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

        boletoModel.setCodRetorno(dadosRegistro.getRetorno().toString());
        boletoModel.getBeneficiario().setNossoNumero(dadosRegistro.getTitulo().getNossoNumero().toString());
        boletoModel.setCodigoBarras(dadosRegistro.getTitulo().getCodigoBarras());
        boletoModel.setLinhaDigitavel(dadosRegistro.getTitulo().getLinhaDigitavel());

        return boletoModel;

    }

    /**
     * <p>Retorna um Titulo com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return Titulo com base nos dados informados
     */
    private Titulo getTitulo(BoletoModel boletoModel) {
        Titulo titulo = new Titulo();
        titulo.setBeneficiario(getBeneficiario(boletoModel));
        titulo.setPagador(getPagador(boletoModel));
        titulo.setInstrucoes(getInstrucoes(boletoModel));
        titulo.setPagParcial(getPagParcial(boletoModel));
        titulo.setMensagens(getMensagens(boletoModel));
        //titulo.setRateio(getRateio(boletoModel));
        titulo.setNossoNumero(Long.valueOf(boletoModel.getBeneficiario().getNossoNumero()));
        titulo.setSeuNumero(boletoModel.getNumeroDocumento());
        titulo.setDataVencimento(BoletoUtil.converteDataXMLGregorianCalendar(BoletoUtil.obterInicioDoDia(boletoModel.getDataVencimento())));
        titulo.setValorNominal(boletoModel.getValorBoleto());
        titulo.setEspecie(Short.parseShort(boletoModel.getEspecieDocumento()));
        titulo.setDataEmissao(BoletoUtil.converteDataXMLGregorianCalendar(BoletoUtil.obterInicioDoDia(boletoModel.getDataEmissao())));
        titulo.setValorIof(boletoModel.getValorIof());
        return titulo;
    }


    /**
     * <p>Retorna um Beneficiário com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return Beneficiario com base nos dados informados
     */
    private Beneficiario getBeneficiario(BoletoModel boletoModel) {
        Beneficiario beneficiario = new Beneficiario();
        String codigoBeneficiario = StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0");
        codigoBeneficiario = codigoBeneficiario + StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, "0");
        codigoBeneficiario = codigoBeneficiario + StringUtils.leftPad(boletoModel.getBeneficiario().getDigitoConta(), 2, "0");
        beneficiario.setCodigo(Long.parseLong(codigoBeneficiario));
        return beneficiario;
    }

    /**
     * <p>Retorna um Pagador com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return Pagador com base nos dados informados
     */
    private Pagador getPagador(BoletoModel boletoModel) {
        Pagador pagador = new Pagador();
        pagador.setTipoPessoa(boletoModel.getPagador().isClienteCpf() ? "F" : "J");
        pagador.setCpfCnpj(Long.parseLong(boletoModel.getPagador().getDocumento()));
        pagador.setNome(BoletoUtil.limitarTamanhoString(boletoModel.getPagador().getNome(), 40));
        pagador.setEndereco(BoletoUtil.limitarTamanhoString(boletoModel.getPagador().getEndereco().getEnderecoCompleto(), 35));
        pagador.setCep(Integer.parseInt(boletoModel.getPagador().getEndereco().getCep()));
        pagador.setCidade(BoletoUtil.limitarTamanhoString(boletoModel.getPagador().getEndereco().getCidade(), 15));
        pagador.setUf(boletoModel.getPagador().getEndereco().getUf());
        pagador.setAceite(boletoModel.isAceite() ? "A" : "N");
        return pagador;
    }

    /**
     * <p>Retorna as Instruções com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return Intrucoes com base nos dados informados
     */
    private Instrucoes getInstrucoes(BoletoModel boletoModel) {
        Instrucoes instrucoes = new Instrucoes();
        Juros juros = new Juros();
        if (boletoModel.getDiasJuros() > 0) {
            juros.setData(BoletoUtil.converteDataXMLGregorianCalendar(
                    BoletoUtil.obterInicioDoDia(
                            BoletoUtil.adicionarDiasData(boletoModel.getDataVencimento(), boletoModel.getDiasJuros()))));
        }
        switch (boletoModel.getTipoJuros()) {
            case VALOR_DIA:
                juros.setCodigo((short) 1);
                juros.setValor(boletoModel.getValorPercentualJuros());
                break;
            case PERCENTUAL_MENSAL:
                juros.setCodigo((short) 2);
                juros.setTaxa(boletoModel.getValorPercentualJuros());
                break;
            default:
                juros.setCodigo((short) 3);
                juros.setData(null);
        }
        instrucoes.setJuros(juros);

        if (!boletoModel.getTipoMulta().equals(TipoMultaEnum.ISENTO)
                && BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualMulta())) {
            Multa multa = new Multa();
            if (boletoModel.getTipoMulta().equals(TipoMultaEnum.VALOR)) {
                multa.setCodigo((short) 1);
                multa.setValor(boletoModel.getValorPercentualMulta());
            } else {
                multa.setCodigo((short) 2);
                multa.setTaxa(boletoModel.getValorPercentualMulta());
            }
            if (boletoModel.getDiasMulta() > 0) {
                multa.setData(BoletoUtil.converteDataXMLGregorianCalendar(
                        BoletoUtil.obterInicioDoDia(
                                BoletoUtil.adicionarDiasData(boletoModel.getDataVencimento(), boletoModel.getDiasMulta()))));
            }
            instrucoes.setMulta(multa);
        }

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualDescontos())) {
            Desconto desconto = new Desconto();

            switch (boletoModel.getTipoDesconto()) {
                case VALOR_FIXO:
                    desconto.setCodigo((short) 1);
                    desconto.setValor(boletoModel.getValorPercentualDescontos());
                    break;
                case PERCENTUAL_FIXO:
                    desconto.setCodigo((short) 2);
                    desconto.setTaxa(boletoModel.getValorPercentualDescontos());
                    break;
                case VALOR_DIA:
                    desconto.setCodigo((short) 3);
                    desconto.setValor(boletoModel.getValorPercentualDescontos());
                default:
                    desconto.setCodigo((short) 5);
                    desconto.setTaxa(boletoModel.getValorPercentualDescontos());

            }
            if (boletoModel.getDataLimiteParaDesconto() != null) {
                desconto.setData(BoletoUtil.converteDataXMLGregorianCalendar(
                        BoletoUtil.obterInicioDoDia(boletoModel.getDataLimiteParaDesconto())));
            }
            instrucoes.setDesconto(desconto);
        }

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorDeducoes())) {
            Abatimento abatimento = new Abatimento();
            abatimento.setValor(boletoModel.getValorDeducoes());
            instrucoes.setAbatimento(abatimento);
        }

        Protesto protesto = new Protesto();
        protesto.setCodigo((short) 3);
        if (boletoModel.isProtesto()) {
            protesto.setCodigo((short) 1);
            protesto.setPrazo((short) boletoModel.getDiasProtesto());
        }
        instrucoes.setProtesto(protesto);

        if (boletoModel.getDiasParaBaixaDevolver() > 0) {
            Baixa baixa = new Baixa();
            baixa.setCodigo((short) 1);
            baixa.setPrazo((short) boletoModel.getDiasParaBaixaDevolver());
            instrucoes.setBaixa(baixa);
        }
        return instrucoes;
    }

    /**
     * <p>Retorna um PagParcial com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return PagParcial com base nos dados informados
     */
    private PagParcial getPagParcial(BoletoModel boletoModel) {
        PagParcial pagParcial = new PagParcial();
        pagParcial.setAutoriza((short) (boletoModel.isAutorizaPagamentoParcial() ? 2 : 1));
        pagParcial.setCodigo((short) boletoModel.getCodigoPagamentoParcial());

        if (boletoModel.getCodigoPagamentoParcial() == 2) {
            pagParcial.setQuantidade((short) boletoModel.getQuantidadePagamentoParcial());
            pagParcial.setTipo((short) boletoModel.getTipoPagamentoParcial());
            if (boletoModel.getTipoPagamentoParcial() == 2) {
                pagParcial.setValorMin(boletoModel.getValorMinPagamentoParcial());
                pagParcial.setValorMax(boletoModel.getValorMaxPagamentoParcial());
            }
            if (boletoModel.getTipoPagamentoParcial() == 1) {
                pagParcial.setPercentualMin(boletoModel.getPercentualMinPagamentoParcial());
                pagParcial.setPercentualMax(boletoModel.getPercentualMaxPagamentoParcial());
            }
        }
        return pagParcial;
    }

    /**
     * <p>Retorna as Mensagens com base no BoletoModel.</p>
     *
     * @param boletoModel BoletoModel contendo os dados do título
     * @return Mensagens com base nos dados informados
     */
    private Mensagens getMensagens(BoletoModel boletoModel) {
        if (boletoModel.getInstrucoes().isEmpty()) {
            return null;
        }
        Mensagens mensagens = new Mensagens();
        int contador = 0;
        for (InformacaoModel instrucao : boletoModel.getInstrucoes()) {
            contador++;
            Mensagem mensagem = new Mensagem();
            mensagem.setLinha((short) contador);
            mensagem.setTexto(BoletoUtil.limitarTamanhoString(instrucao.getInformacao(), 75));
            mensagens.getMensagem().add(mensagem);
            if (contador >= 9) {
                break;
            }
        }
        return mensagens;
    }
}
