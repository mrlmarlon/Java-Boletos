package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobModel {

    private Integer numeroContrato;
    private Integer modalidade;
    private Integer numeroContaCorrente;
    private String especieDocumento;
    private String dataEmissao;
    private Integer nossoNumero;
    private Integer seuNumero;
    private String identificacaoBoletoEmpresa;
    private Integer identificacaoEmissaoBoleto;
    private Integer identificacaoDistribuicaoBoleto;
    private BigDecimal valor;
    private String dataVencimento;
    private String dataLimitePagamento;
    private Integer valorAbatimento;
    private Integer tipoDesconto;
    private String dataPrimeiroDesconto;
    private BigDecimal valorPrimeiroDesconto;
    private String dataSegundoDesconto;
    private BigDecimal valorSegundoDesconto;
    private String dataTerceiroDesconto;
    private BigDecimal valorTerceiroDesconto;
    private Integer tipoMulta;
    private String dataMulta;
    private Integer valorMulta;
    private Integer tipoJurosMora;
    private String dataJurosMora;
    private Integer valorJurosMora;
    private Integer numeroParcela;
    private Boolean aceite;
    private Integer codigoNegativacao;
    private Integer numeroDiasNegativacao;
    private Integer codigoProtesto;
    private Integer numeroDiasProtesto;
    
    private BoletoSicoobPagador pagador;
    private BoletoSicoobBeneficiarioFinal beneficiarioFinal;
    private BoletoSicoobMensagensInstrucao mensagensInstrucao;
    
    private boolean gerarPdf;
    
    private BoletoSicoobRateioCreditos rateioCreditos;

}