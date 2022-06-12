//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 10:10:26 PM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="titulo" type="{Bergs.Boc.Bocswsxn}titulo" minOccurs="0"/>
 *         &lt;element name="ocorrencias" type="{Bergs.Boc.Bocswsxn}ocorrencias" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ambiente">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="(P|T)"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="retorno" type="{http://www.w3.org/2001/XMLSchema}short" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "titulo",
    "ocorrencias"
})
@XmlRootElement(name = "dados")
public class Dados {

    protected Titulo titulo;
    protected Ocorrencias ocorrencias;
    @XmlAttribute(name = "ambiente")
    protected String ambiente;
    @XmlAttribute(name = "retorno")
    protected Short retorno;

    /**
     * Obtém o valor da propriedade titulo.
     * 
     * @return
     *     possible object is
     *     {@link Titulo }
     *     
     */
    public Titulo getTitulo() {
        return titulo;
    }

    /**
     * Define o valor da propriedade titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link Titulo }
     *     
     */
    public void setTitulo(Titulo value) {
        this.titulo = value;
    }

    /**
     * Obtém o valor da propriedade ocorrencias.
     * 
     * @return
     *     possible object is
     *     {@link Ocorrencias }
     *     
     */
    public Ocorrencias getOcorrencias() {
        return ocorrencias;
    }

    /**
     * Define o valor da propriedade ocorrencias.
     * 
     * @param value
     *     allowed object is
     *     {@link Ocorrencias }
     *     
     */
    public void setOcorrencias(Ocorrencias value) {
        this.ocorrencias = value;
    }

    /**
     * Obtém o valor da propriedade ambiente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmbiente() {
        return ambiente;
    }

    /**
     * Define o valor da propriedade ambiente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmbiente(String value) {
        this.ambiente = value;
    }

    /**
     * Obtém o valor da propriedade retorno.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getRetorno() {
        return retorno;
    }

    /**
     * Define o valor da propriedade retorno.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setRetorno(Short value) {
        this.retorno = value;
    }

}
