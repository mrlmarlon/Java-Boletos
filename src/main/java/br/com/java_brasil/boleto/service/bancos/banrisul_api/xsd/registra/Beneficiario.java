//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 11:22:58 AM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.registra;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de beneficiario complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="beneficiario">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="codigo" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="tipo_pessoa" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cpf_cnpj" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="nome" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="nome_fantasia" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "beneficiario")
public class Beneficiario {

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

}
