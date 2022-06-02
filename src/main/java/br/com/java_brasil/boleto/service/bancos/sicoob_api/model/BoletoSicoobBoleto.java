package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobBoleto {

    //COLOQUEI NA ORDEM DO TXT QUE O SAMUEL GEROU NA NOITE DE 27/05/2022
    private Integer numeroContrato;
    private Integer modalidade;
    private Integer numeroContaCorrente;
    private Integer nossoNumero;
    private Integer seuNumero;
    private String especieDocumento;
    private String dataEmissao;
    private String identificacaoBoletoEmpresa;
    private String codigoBarras; //NOVO - 27/05/2022
    private String linhaDigitavel; //NOVO - 27/05/2022
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
    private BigDecimal valorMulta;
    private Integer tipoJurosMora;
    private String dataJurosMora;
    private BigDecimal valorJurosMora;
    private Integer numeroParcela;
    private Boolean aceite;
    private Integer codigoNegativacao;
    private Integer numeroDiasNegativacao;
    private Integer codigoProtesto;
    private Integer numeroDiasProtesto;
    private Integer quantidadeDiasFloat;
    
    private BoletoSicoobPagador pagador;
    private BoletoSicoobBeneficiarioFinal beneficiarioFinal;
    private BoletoSicoobMensagensInstrucao mensagensInstrucao;
    
    private boolean gerarPdf;
    private String pdfBoleto;
    private String situacaoBoleto;

    private List<BoletoSicoobRateioCreditos> rateioCreditos;
}