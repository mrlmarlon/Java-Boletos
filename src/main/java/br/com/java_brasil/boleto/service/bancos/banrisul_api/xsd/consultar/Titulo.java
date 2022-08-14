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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de titulo complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="titulo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="beneficiario" type="{Bergs.Boc.Bocswsxn}beneficiario" minOccurs="0"/>
 *         &lt;element name="pagador" type="{Bergs.Boc.Bocswsxn}pagador" minOccurs="0"/>
 *         &lt;element name="instrucoes" type="{Bergs.Boc.Bocswsxn}instrucoes" minOccurs="0"/>
 *         &lt;element name="pag_parcial" type="{Bergs.Boc.Bocswsxn}pag_parcial" minOccurs="0"/>
 *         &lt;element name="mensagens" type="{Bergs.Boc.Bocswsxn}mensagens" minOccurs="0"/>
 *         &lt;element name="rateio" type="{Bergs.Boc.Bocswsxn}rateio" minOccurs="0"/>
 *         &lt;element name="operacoes" type="{Bergs.Boc.Bocswsxn}operacoes" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="nosso_numero" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="seu_numero" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="data_vencimento" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="valor_nominal" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="especie" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="data_emissao" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="valor_iof" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="id_titulo_empresa" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="codigo_barras" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="linha_digitavel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="situacao_banrisul" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="situacao_cip" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="situacao_pagamento" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="carteira" type="{http://www.w3.org/2001/XMLSchema}short" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "titulo", propOrder = {

})
public class Titulo {

    protected Beneficiario beneficiario;
    protected Pagador pagador;
    protected Instrucoes instrucoes;
    @XmlElement(name = "pag_parcial")
    protected PagParcial pagParcial;
    protected Mensagens mensagens;
    protected Rateio rateio;
    protected Operacoes operacoes;
    @XmlAttribute(name = "nosso_numero")
    protected Long nossoNumero;
    @XmlAttribute(name = "seu_numero")
    protected String seuNumero;
    @XmlAttribute(name = "data_vencimento")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataVencimento;
    @XmlAttribute(name = "valor_nominal")
    protected BigDecimal valorNominal;
    @XmlAttribute(name = "especie")
    protected Short especie;
    @XmlAttribute(name = "data_emissao")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataEmissao;
    @XmlAttribute(name = "valor_iof")
    protected BigDecimal valorIof;
    @XmlAttribute(name = "id_titulo_empresa")
    protected String idTituloEmpresa;
    @XmlAttribute(name = "codigo_barras")
    protected String codigoBarras;
    @XmlAttribute(name = "linha_digitavel")
    protected String linhaDigitavel;
    @XmlAttribute(name = "situacao_banrisul")
    protected String situacaoBanrisul;
    @XmlAttribute(name = "situacao_cip")
    protected String situacaoCip;
    @XmlAttribute(name = "situacao_pagamento")
    protected Short situacaoPagamento;
    @XmlAttribute(name = "carteira")
    protected Short carteira;

    /**
     * Obtém o valor da propriedade beneficiario.
     * 
     * @return
     *     possible object is
     *     {@link Beneficiario }
     *     
     */
    public Beneficiario getBeneficiario() {
        return beneficiario;
    }

    /**
     * Define o valor da propriedade beneficiario.
     * 
     * @param value
     *     allowed object is
     *     {@link Beneficiario }
     *     
     */
    public void setBeneficiario(Beneficiario value) {
        this.beneficiario = value;
    }

    /**
     * Obtém o valor da propriedade pagador.
     * 
     * @return
     *     possible object is
     *     {@link Pagador }
     *     
     */
    public Pagador getPagador() {
        return pagador;
    }

    /**
     * Define o valor da propriedade pagador.
     * 
     * @param value
     *     allowed object is
     *     {@link Pagador }
     *     
     */
    public void setPagador(Pagador value) {
        this.pagador = value;
    }

    /**
     * Obtém o valor da propriedade instrucoes.
     * 
     * @return
     *     possible object is
     *     {@link Instrucoes }
     *     
     */
    public Instrucoes getInstrucoes() {
        return instrucoes;
    }

    /**
     * Define o valor da propriedade instrucoes.
     * 
     * @param value
     *     allowed object is
     *     {@link Instrucoes }
     *     
     */
    public void setInstrucoes(Instrucoes value) {
        this.instrucoes = value;
    }

    /**
     * Obtém o valor da propriedade pagParcial.
     * 
     * @return
     *     possible object is
     *     {@link PagParcial }
     *     
     */
    public PagParcial getPagParcial() {
        return pagParcial;
    }

    /**
     * Define o valor da propriedade pagParcial.
     * 
     * @param value
     *     allowed object is
     *     {@link PagParcial }
     *     
     */
    public void setPagParcial(PagParcial value) {
        this.pagParcial = value;
    }

    /**
     * Obtém o valor da propriedade mensagens.
     * 
     * @return
     *     possible object is
     *     {@link Mensagens }
     *     
     */
    public Mensagens getMensagens() {
        return mensagens;
    }

    /**
     * Define o valor da propriedade mensagens.
     * 
     * @param value
     *     allowed object is
     *     {@link Mensagens }
     *     
     */
    public void setMensagens(Mensagens value) {
        this.mensagens = value;
    }

    /**
     * Obtém o valor da propriedade rateio.
     * 
     * @return
     *     possible object is
     *     {@link Rateio }
     *     
     */
    public Rateio getRateio() {
        return rateio;
    }

    /**
     * Define o valor da propriedade rateio.
     * 
     * @param value
     *     allowed object is
     *     {@link Rateio }
     *     
     */
    public void setRateio(Rateio value) {
        this.rateio = value;
    }

    /**
     * Obtém o valor da propriedade operacoes.
     * 
     * @return
     *     possible object is
     *     {@link Operacoes }
     *     
     */
    public Operacoes getOperacoes() {
        return operacoes;
    }

    /**
     * Define o valor da propriedade operacoes.
     * 
     * @param value
     *     allowed object is
     *     {@link Operacoes }
     *     
     */
    public void setOperacoes(Operacoes value) {
        this.operacoes = value;
    }

    /**
     * Obtém o valor da propriedade nossoNumero.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNossoNumero() {
        return nossoNumero;
    }

    /**
     * Define o valor da propriedade nossoNumero.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNossoNumero(Long value) {
        this.nossoNumero = value;
    }

    /**
     * Obtém o valor da propriedade seuNumero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSeuNumero() {
        return seuNumero;
    }

    /**
     * Define o valor da propriedade seuNumero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSeuNumero(String value) {
        this.seuNumero = value;
    }

    /**
     * Obtém o valor da propriedade dataVencimento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataVencimento() {
        return dataVencimento;
    }

    /**
     * Define o valor da propriedade dataVencimento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataVencimento(XMLGregorianCalendar value) {
        this.dataVencimento = value;
    }

    /**
     * Obtém o valor da propriedade valorNominal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorNominal() {
        return valorNominal;
    }

    /**
     * Define o valor da propriedade valorNominal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorNominal(BigDecimal value) {
        this.valorNominal = value;
    }

    /**
     * Obtém o valor da propriedade especie.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getEspecie() {
        return especie;
    }

    /**
     * Define o valor da propriedade especie.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setEspecie(Short value) {
        this.especie = value;
    }

    /**
     * Obtém o valor da propriedade dataEmissao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEmissao() {
        return dataEmissao;
    }

    /**
     * Define o valor da propriedade dataEmissao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEmissao(XMLGregorianCalendar value) {
        this.dataEmissao = value;
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
     * Obtém o valor da propriedade idTituloEmpresa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTituloEmpresa() {
        return idTituloEmpresa;
    }

    /**
     * Define o valor da propriedade idTituloEmpresa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTituloEmpresa(String value) {
        this.idTituloEmpresa = value;
    }

    /**
     * Obtém o valor da propriedade codigoBarras.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoBarras() {
        return codigoBarras;
    }

    /**
     * Define o valor da propriedade codigoBarras.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoBarras(String value) {
        this.codigoBarras = value;
    }

    /**
     * Obtém o valor da propriedade linhaDigitavel.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinhaDigitavel() {
        return linhaDigitavel;
    }

    /**
     * Define o valor da propriedade linhaDigitavel.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinhaDigitavel(String value) {
        this.linhaDigitavel = value;
    }

    /**
     * Obtém o valor da propriedade situacaoBanrisul.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSituacaoBanrisul() {
        return situacaoBanrisul;
    }

    /**
     * Define o valor da propriedade situacaoBanrisul.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSituacaoBanrisul(String value) {
        this.situacaoBanrisul = value;
    }

    /**
     * Obtém o valor da propriedade situacaoCip.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSituacaoCip() {
        return situacaoCip;
    }

    /**
     * Define o valor da propriedade situacaoCip.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSituacaoCip(String value) {
        this.situacaoCip = value;
    }

    /**
     * Obtém o valor da propriedade situacaoPagamento.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getSituacaoPagamento() {
        return situacaoPagamento;
    }

    /**
     * Define o valor da propriedade situacaoPagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setSituacaoPagamento(Short value) {
        this.situacaoPagamento = value;
    }

    /**
     * Obtém o valor da propriedade carteira.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getCarteira() {
        return carteira;
    }

    /**
     * Define o valor da propriedade carteira.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setCarteira(Short value) {
        this.carteira = value;
    }

}
