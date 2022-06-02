package br.com.java_brasil.boleto.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//TODO Refatorar campos realmente necessarios
public class RemessaRetornoModel implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer numeroRemessaNoDia; //Se houver mais de uma altera o nome do arquivo(Sicredi)
    protected String numeroRemessa;
    protected String postagemTitulo;
    protected String impressaoTitulo;
    protected String instrucao;
    protected String campoAlterado;
    protected String ocorrencia;
    protected LocalDate dataOcorrencia;
    protected BigDecimal despesaCobranca;
    protected BigDecimal despesaCustasProtesto;
    protected BigDecimal abatimentoConcedido;
    protected BigDecimal descontoConcedido;
    protected BigDecimal valorEfetivamentePago;
    protected BigDecimal juroDeMora;
    protected BigDecimal multa;
    protected String confirmacaoProtesto;
    protected String motivoOcorrencia;
    protected LocalDate dataPrevisaoLancamento;
    protected String boletoDda;
    protected BoletoModel boleto;
}