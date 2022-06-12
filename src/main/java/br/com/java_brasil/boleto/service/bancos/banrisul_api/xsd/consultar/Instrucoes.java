//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 10:12:45 PM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de instrucoes complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="instrucoes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="juros" type="{Bergs.Boc.Bocswsxn}juros"/>
 *         &lt;element name="multa" type="{Bergs.Boc.Bocswsxn}multa" minOccurs="0"/>
 *         &lt;element name="desconto" type="{Bergs.Boc.Bocswsxn}desconto" minOccurs="0"/>
 *         &lt;element name="abatimento" type="{Bergs.Boc.Bocswsxn}abatimento" minOccurs="0"/>
 *         &lt;element name="protesto" type="{Bergs.Boc.Bocswsxn}protesto" minOccurs="0"/>
 *         &lt;element name="baixa" type="{Bergs.Boc.Bocswsxn}baixa" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "instrucoes", propOrder = {

})
public class Instrucoes {

    @XmlElement(required = true)
    protected Juros juros;
    protected Multa multa;
    protected Desconto desconto;
    protected Abatimento abatimento;
    protected Protesto protesto;
    protected Baixa baixa;

    /**
     * Obtém o valor da propriedade juros.
     * 
     * @return
     *     possible object is
     *     {@link Juros }
     *     
     */
    public Juros getJuros() {
        return juros;
    }

    /**
     * Define o valor da propriedade juros.
     * 
     * @param value
     *     allowed object is
     *     {@link Juros }
     *     
     */
    public void setJuros(Juros value) {
        this.juros = value;
    }

    /**
     * Obtém o valor da propriedade multa.
     * 
     * @return
     *     possible object is
     *     {@link Multa }
     *     
     */
    public Multa getMulta() {
        return multa;
    }

    /**
     * Define o valor da propriedade multa.
     * 
     * @param value
     *     allowed object is
     *     {@link Multa }
     *     
     */
    public void setMulta(Multa value) {
        this.multa = value;
    }

    /**
     * Obtém o valor da propriedade desconto.
     * 
     * @return
     *     possible object is
     *     {@link Desconto }
     *     
     */
    public Desconto getDesconto() {
        return desconto;
    }

    /**
     * Define o valor da propriedade desconto.
     * 
     * @param value
     *     allowed object is
     *     {@link Desconto }
     *     
     */
    public void setDesconto(Desconto value) {
        this.desconto = value;
    }

    /**
     * Obtém o valor da propriedade abatimento.
     * 
     * @return
     *     possible object is
     *     {@link Abatimento }
     *     
     */
    public Abatimento getAbatimento() {
        return abatimento;
    }

    /**
     * Define o valor da propriedade abatimento.
     * 
     * @param value
     *     allowed object is
     *     {@link Abatimento }
     *     
     */
    public void setAbatimento(Abatimento value) {
        this.abatimento = value;
    }

    /**
     * Obtém o valor da propriedade protesto.
     * 
     * @return
     *     possible object is
     *     {@link Protesto }
     *     
     */
    public Protesto getProtesto() {
        return protesto;
    }

    /**
     * Define o valor da propriedade protesto.
     * 
     * @param value
     *     allowed object is
     *     {@link Protesto }
     *     
     */
    public void setProtesto(Protesto value) {
        this.protesto = value;
    }

    /**
     * Obtém o valor da propriedade baixa.
     * 
     * @return
     *     possible object is
     *     {@link Baixa }
     *     
     */
    public Baixa getBaixa() {
        return baixa;
    }

    /**
     * Define o valor da propriedade baixa.
     * 
     * @param value
     *     allowed object is
     *     {@link Baixa }
     *     
     */
    public void setBaixa(Baixa value) {
        this.baixa = value;
    }

}
