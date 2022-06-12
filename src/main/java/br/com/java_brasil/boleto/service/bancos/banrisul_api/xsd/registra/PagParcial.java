//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 11:22:58 AM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.registra;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de pag_parcial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="pag_parcial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="autoriza" use="required" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="codigo" use="required" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="quantidade" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="tipo" type="{http://www.w3.org/2001/XMLSchema}short" />
 *       &lt;attribute name="valor_min" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="valor_max" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="percentual_min" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="percentual_max" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pag_parcial")
public class PagParcial {

    @XmlAttribute(name = "autoriza", required = true)
    protected short autoriza;
    @XmlAttribute(name = "codigo", required = true)
    protected short codigo;
    @XmlAttribute(name = "quantidade")
    protected Short quantidade;
    @XmlAttribute(name = "tipo")
    protected Short tipo;
    @XmlAttribute(name = "valor_min")
    protected BigDecimal valorMin;
    @XmlAttribute(name = "valor_max")
    protected BigDecimal valorMax;
    @XmlAttribute(name = "percentual_min")
    protected BigDecimal percentualMin;
    @XmlAttribute(name = "percentual_max")
    protected BigDecimal percentualMax;

    /**
     * Obtém o valor da propriedade autoriza.
     * 
     */
    public short getAutoriza() {
        return autoriza;
    }

    /**
     * Define o valor da propriedade autoriza.
     * 
     */
    public void setAutoriza(short value) {
        this.autoriza = value;
    }

    /**
     * Obtém o valor da propriedade codigo.
     * 
     */
    public short getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     */
    public void setCodigo(short value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade quantidade.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getQuantidade() {
        return quantidade;
    }

    /**
     * Define o valor da propriedade quantidade.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setQuantidade(Short value) {
        this.quantidade = value;
    }

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setTipo(Short value) {
        this.tipo = value;
    }

    /**
     * Obtém o valor da propriedade valorMin.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorMin() {
        return valorMin;
    }

    /**
     * Define o valor da propriedade valorMin.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorMin(BigDecimal value) {
        this.valorMin = value;
    }

    /**
     * Obtém o valor da propriedade valorMax.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValorMax() {
        return valorMax;
    }

    /**
     * Define o valor da propriedade valorMax.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValorMax(BigDecimal value) {
        this.valorMax = value;
    }

    /**
     * Obtém o valor da propriedade percentualMin.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentualMin() {
        return percentualMin;
    }

    /**
     * Define o valor da propriedade percentualMin.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentualMin(BigDecimal value) {
        this.percentualMin = value;
    }

    /**
     * Obtém o valor da propriedade percentualMax.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentualMax() {
        return percentualMax;
    }

    /**
     * Define o valor da propriedade percentualMax.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentualMax(BigDecimal value) {
        this.percentualMax = value;
    }

}
