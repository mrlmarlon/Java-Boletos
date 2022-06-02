//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 10:12:45 PM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de operacoes complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="operacoes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="data_baixa" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="data_pagamento" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="data_credito" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="data_reembolso" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="valor_juros_recebido" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_juros_pago" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_multa_recebido" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_desconto_utilizado" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_abatimento_utilizado" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_iof" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="custas_cartorio" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="ressarcimento_cartorio" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_registro" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_baixa_liquidacao" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_aponte_cartorio" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_interbancaria" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_manutencao_mensal" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_sustacao" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="tarifa_diversas" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_pagamento" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_reembolso" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_cobrado" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_creditado_debitado" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operacoes")
public class Operacoes {

    @XmlAttribute(name = "data_baixa")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataBaixa;
    @XmlAttribute(name = "data_pagamento")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataPagamento;
    @XmlAttribute(name = "data_credito")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataCredito;
    @XmlAttribute(name = "data_reembolso")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataReembolso;
    @XmlAttribute(name = "valor_juros_recebido")
    protected BigDecimal valorJurosRecebido;
    @XmlAttribute(name = "valor_juros_pago")
    protected BigDecimal valorJurosPago;
    @XmlAttribute(name = "valor_multa_recebido")
    protected BigDecimal valorMultaRecebido;
    @XmlAttribute(name = "valor_desconto_utilizado")
    protected BigDecimal valorDescontoUtilizado;
    @XmlAttribute(name = "valor_abatimento_utilizado")
    protected BigDecimal valorAbatimentoUtilizado;
    @XmlAttribute(name = "valor_iof")
    protected BigDecimal valorIof;
    @XmlAttribute(name = "custas_cartorio")
    protected BigDecimal custasCartorio;
    @XmlAttribute(name = "ressarcimento_cartorio")
    protected BigDecimal ressarcimentoCartorio;
    @XmlAttribute(name = "tarifa_registro")
    protected BigDecimal tarifaRegistro;
    @XmlAttribute(name = "tarifa_baixa_liquidacao")
    protected BigDecimal tarifaBaixaLiquidacao;
    @XmlAttribute(name = "tarifa_aponte_cartorio")
    protected BigDecimal tarifaAponteCartorio;
    @XmlAttribute(name = "tarifa_interbancaria")
    protected BigDecimal tarifaInterbancaria;
    @XmlAttribute(name = "tarifa_manutencao_mensal")
    protected BigDecimal tarifaManutencaoMensal;
    @XmlAttribute(name = "tarifa_sustacao")
    protected BigDecimal tarifaSustacao;
    @XmlAttribute(name = "tarifa_diversas")
    protected BigDecimal tarifaDiversas;
    @XmlAttribute(name = "valor_pagamento")
    protected BigDecimal valorPagamento;
    @XmlAttribute(name = "valor_reembolso")
    protected BigDecimal valorReembolso;
    @XmlAttribute(name = "valor_cobrado")
    protected BigDecimal valorCobrado;
    @XmlAttribute(name = "valor_creditado_debitado")
    protected BigDecimal valorCreditadoDebitado;

    /**
     * Obtém o valor da propriedade dataBaixa.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataBaixa() {
        return dataBaixa;
    }

    /**
     * Define o valor da propriedade dataBaixa.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataBaixa(XMLGregorianCalendar value) {
        this.dataBaixa = value;
    }

    /**
     * Obtém o valor da propriedade dataPagamento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPagamento() {
        return dataPagamento;
    }

    /**
     * Define o valor da propriedade dataPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPagamento(XMLGregorianCalendar value) {
        this.dataPagamento = value;
    }

    /**
     * Obtém o valor da propriedade dataCredito.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCredito() {
        return dataCredito;
    }

    /**
     * Define o valor da propriedade dataCredito.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCredito(XMLGregorianCalendar value) {
        this.dataCredito = value;
    }

    /**
     * Obtém o valor da propriedade dataReembolso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataReembolso() {
        return dataReembolso;
    }

    /**
     * Define o valor da propriedade dataReembolso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataReembolso(XMLGregorianCalendar value) {
        this.dataReembolso = value;
    }

    /**
     * Obtém o valor da propriedade valorJurosRecebido.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorJurosRecebido() {
        return valorJurosRecebido;
    }

    /**
     * Define o valor da propriedade valorJurosRecebido.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorJurosRecebido(BigDecimal value) {
        this.valorJurosRecebido = value;
    }

    /**
     * Obtém o valor da propriedade valorJurosPago.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorJurosPago() {
        return valorJurosPago;
    }

    /**
     * Define o valor da propriedade valorJurosPago.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorJurosPago(BigDecimal value) {
        this.valorJurosPago = value;
    }

    /**
     * Obtém o valor da propriedade valorMultaRecebido.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorMultaRecebido() {
        return valorMultaRecebido;
    }

    /**
     * Define o valor da propriedade valorMultaRecebido.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorMultaRecebido(BigDecimal value) {
        this.valorMultaRecebido = value;
    }

    /**
     * Obtém o valor da propriedade valorDescontoUtilizado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorDescontoUtilizado() {
        return valorDescontoUtilizado;
    }

    /**
     * Define o valor da propriedade valorDescontoUtilizado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorDescontoUtilizado(BigDecimal value) {
        this.valorDescontoUtilizado = value;
    }

    /**
     * Obtém o valor da propriedade valorAbatimentoUtilizado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorAbatimentoUtilizado() {
        return valorAbatimentoUtilizado;
    }

    /**
     * Define o valor da propriedade valorAbatimentoUtilizado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorAbatimentoUtilizado(BigDecimal value) {
        this.valorAbatimentoUtilizado = value;
    }

    /**
     * Obtém o valor da propriedade valorIof.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorIof() {
        return valorIof;
    }

    /**
     * Define o valor da propriedade valorIof.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorIof(BigDecimal value) {
        this.valorIof = value;
    }

    /**
     * Obtém o valor da propriedade custasCartorio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCustasCartorio() {
        return custasCartorio;
    }

    /**
     * Define o valor da propriedade custasCartorio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCustasCartorio(BigDecimal value) {
        this.custasCartorio = value;
    }

    /**
     * Obtém o valor da propriedade ressarcimentoCartorio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRessarcimentoCartorio() {
        return ressarcimentoCartorio;
    }

    /**
     * Define o valor da propriedade ressarcimentoCartorio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRessarcimentoCartorio(BigDecimal value) {
        this.ressarcimentoCartorio = value;
    }

    /**
     * Obtém o valor da propriedade tarifaRegistro.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaRegistro() {
        return tarifaRegistro;
    }

    /**
     * Define o valor da propriedade tarifaRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaRegistro(BigDecimal value) {
        this.tarifaRegistro = value;
    }

    /**
     * Obtém o valor da propriedade tarifaBaixaLiquidacao.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaBaixaLiquidacao() {
        return tarifaBaixaLiquidacao;
    }

    /**
     * Define o valor da propriedade tarifaBaixaLiquidacao.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaBaixaLiquidacao(BigDecimal value) {
        this.tarifaBaixaLiquidacao = value;
    }

    /**
     * Obtém o valor da propriedade tarifaAponteCartorio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaAponteCartorio() {
        return tarifaAponteCartorio;
    }

    /**
     * Define o valor da propriedade tarifaAponteCartorio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaAponteCartorio(BigDecimal value) {
        this.tarifaAponteCartorio = value;
    }

    /**
     * Obtém o valor da propriedade tarifaInterbancaria.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaInterbancaria() {
        return tarifaInterbancaria;
    }

    /**
     * Define o valor da propriedade tarifaInterbancaria.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaInterbancaria(BigDecimal value) {
        this.tarifaInterbancaria = value;
    }

    /**
     * Obtém o valor da propriedade tarifaManutencaoMensal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaManutencaoMensal() {
        return tarifaManutencaoMensal;
    }

    /**
     * Define o valor da propriedade tarifaManutencaoMensal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaManutencaoMensal(BigDecimal value) {
        this.tarifaManutencaoMensal = value;
    }

    /**
     * Obtém o valor da propriedade tarifaSustacao.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaSustacao() {
        return tarifaSustacao;
    }

    /**
     * Define o valor da propriedade tarifaSustacao.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaSustacao(BigDecimal value) {
        this.tarifaSustacao = value;
    }

    /**
     * Obtém o valor da propriedade tarifaDiversas.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTarifaDiversas() {
        return tarifaDiversas;
    }

    /**
     * Define o valor da propriedade tarifaDiversas.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTarifaDiversas(BigDecimal value) {
        this.tarifaDiversas = value;
    }

    /**
     * Obtém o valor da propriedade valorPagamento.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorPagamento() {
        return valorPagamento;
    }

    /**
     * Define o valor da propriedade valorPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorPagamento(BigDecimal value) {
        this.valorPagamento = value;
    }

    /**
     * Obtém o valor da propriedade valorReembolso.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorReembolso() {
        return valorReembolso;
    }

    /**
     * Define o valor da propriedade valorReembolso.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorReembolso(BigDecimal value) {
        this.valorReembolso = value;
    }

    /**
     * Obtém o valor da propriedade valorCobrado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorCobrado() {
        return valorCobrado;
    }

    /**
     * Define o valor da propriedade valorCobrado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorCobrado(BigDecimal value) {
        this.valorCobrado = value;
    }

    /**
     * Obtém o valor da propriedade valorCreditadoDebitado.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorCreditadoDebitado() {
        return valorCreditadoDebitado;
    }

    /**
     * Define o valor da propriedade valorCreditadoDebitado.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorCreditadoDebitado(BigDecimal value) {
        this.valorCreditadoDebitado = value;
    }

}
