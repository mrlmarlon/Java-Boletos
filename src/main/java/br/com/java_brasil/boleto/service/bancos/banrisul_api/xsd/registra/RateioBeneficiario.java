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
 * <p>Classe Java de rateio_beneficiario complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="rateio_beneficiario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="codigo" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="tipo_pessoa" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cpf_cnpj" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="nome" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nome_fantasia" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="valor" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="percentual" type="{http://www.w3.org/2001/XMLSchema}decimal" />
 *       &lt;attribute name="parcela" type="{http://www.w3.org/2001/XMLSchema}short" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rateio_beneficiario")
public class RateioBeneficiario {

    @XmlAttribute(name = "codigo", required = true)
    protected long codigo;
    @XmlAttribute(name = "tipo_pessoa")
    protected String tipoPessoa;
    @XmlAttribute(name = "cpf_cnpj")
    protected Long cpfCnpj;
    @XmlAttribute(name = "nome")
    protected String nome;
    @XmlAttribute(name = "nome_fantasia")
    protected String nomeFantasia;
    @XmlAttribute(name = "valor")
    protected BigDecimal valor;
    @XmlAttribute(name = "percentual")
    protected BigDecimal percentual;
    @XmlAttribute(name = "parcela")
    protected Short parcela;

    /**
     * Obtém o valor da propriedade codigo.
     * 
     */
    public long getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     */
    public void setCodigo(long value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade tipoPessoa.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * Define o valor da propriedade tipoPessoa.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoPessoa(String value) {
        this.tipoPessoa = value;
    }

    /**
     * Obtém o valor da propriedade cpfCnpj.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * Define o valor da propriedade cpfCnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCpfCnpj(Long value) {
        this.cpfCnpj = value;
    }

    /**
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade nomeFantasia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * Define o valor da propriedade nomeFantasia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeFantasia(String value) {
        this.nomeFantasia = value;
    }

    /**
     * Obtém o valor da propriedade valor.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * Define o valor da propriedade valor.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValor(BigDecimal value) {
        this.valor = value;
    }

    /**
     * Obtém o valor da propriedade percentual.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentual() {
        return percentual;
    }

    /**
     * Define o valor da propriedade percentual.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentual(BigDecimal value) {
        this.percentual = value;
    }

    /**
     * Obtém o valor da propriedade parcela.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getParcela() {
        return parcela;
    }

    /**
     * Define o valor da propriedade parcela.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setParcela(Short value) {
        this.parcela = value;
    }

}
