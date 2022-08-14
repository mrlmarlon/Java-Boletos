package br.com.java_brasil.boleto.model;

import br.com.java_brasil.boleto.model.enums.SituacaoEnum;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
//TODO Refatorar campos realmente necessarios
public class BoletoModel implements Serializable {

    protected String numeroDocumento;
    protected LocalDate dataVencimento;
    protected BigDecimal valorBoleto;
    protected BigDecimal valorDescontos = BigDecimal.ZERO;
    protected BigDecimal valorDeducoes = BigDecimal.ZERO;
    protected BigDecimal valorMulta = BigDecimal.ZERO;
    protected BigDecimal valorAcrescimos = BigDecimal.ZERO;
    protected BigDecimal valorCobrado = BigDecimal.ZERO;
    protected BigDecimal percentualJuros;
    protected BigDecimal percentualMulta;
    protected String especieDocumento;
    protected LocalDate dataEmissao;
    protected BigDecimal valorIof;
    protected boolean aceite;
    protected TipoJurosEnum tipoJuros;
    protected int diasJuros;
    protected BigDecimal valorPercentualJuros;
    protected TipoMultaEnum tipoMulta;
    protected int diasMulta;
    protected BigDecimal valorPercentualMulta;
    protected TipoDescontoEnum tipoDesconto;
    protected LocalDate dataLimiteParaDesconto;
    protected BigDecimal valorPercentualDescontos = BigDecimal.ZERO;
    protected LocalDate dataLimiteParaDesconto2;
    protected BigDecimal valorPercentualDescontos2 = BigDecimal.ZERO;
    protected LocalDate dataLimiteParaDesconto3;
    protected BigDecimal valorPercentualDescontos3 = BigDecimal.ZERO;
    protected boolean protesto;
    protected int diasProtesto;
    protected boolean negativacaoAutomatica;
    protected int numeroDiasNegativacao;
    protected int diasParaBaixaDevolver;
    protected String especieMoeda;
    protected int codigoEspecieMoeda;
    protected Integer parcela;
    protected String numeroBoleto;
    protected Pagador pagador;
    protected Pagador beneficiarioFinal; //Avalista
    protected Beneficiario beneficiario;
    protected List<InformacaoModel> instrucoes = Collections.emptyList();
    protected List<InformacaoModel> descricoes = Collections.emptyList();
    protected List<InformacaoModel> locaisDePagamento = Collections.emptyList();
    protected String codigoBarras;
    protected String linhaDigitavel;
    protected boolean autorizaPagamentoParcial;
    protected int codigoPagamentoParcial;
    protected int quantidadePagamentoParcial;
    protected int tipoPagamentoParcial;
    protected BigDecimal valorMinPagamentoParcial;
    protected BigDecimal valorMaxPagamentoParcial;
    protected BigDecimal percentualMinPagamentoParcial;
    protected BigDecimal percentualMaxPagamentoParcial;
    protected String tipoImpressao;
    protected int numeroDaParcelaCarne;
    protected int numeroTotalDeParcelasCarne;
    protected String codRetorno;
    protected String mensagemRetorno;
    protected String localPagamento;
    protected String impressaoBase64;
    protected SituacaoEnum situacao;
    protected String pixTxidQrCode;
    protected String pixUrlQrCode;
    protected String pixCopiaCola;
    protected String urlPdf;
    protected String pixBase64;
    protected String codigosEmpresa;

    private Long numeroConvenio;
    private Integer numeroCarteira;
    private Integer numeroVariacaoCarteira;
    private Integer codigoModalidade;
    private String codigoAceite;
    private Integer codigoTipoTituloCobranca;
    private String indicadorPermissaoRecebimentoParcial;
    private String numeroTituloBeneficiario;
    private String numeroTituloCliente;
    private String linhaDigitavel;
    private Long codigoCliente;
    private Long numeroContratoCobranca;

}
