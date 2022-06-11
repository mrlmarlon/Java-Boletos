//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 10:12:45 PM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de pagador complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="pagador">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="tipo_pessoa" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cpf_cnpj" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *       &lt;attribute name="nome" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="endereco" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="cep" use="required" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="cidade" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="uf" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="aceite" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pagador")
public class Pagador {

    @XmlAttribute(name = "tipo_pessoa", required = true)
    protected String tipoPessoa;
    @XmlAttribute(name = "cpf_cnpj", required = true)
    protected long cpfCnpj;
    @XmlAttribute(name = "nome", required = true)
    protected String nome;
    @XmlAttribute(name = "endereco", required = true)
    protected String endereco;
    @XmlAttribute(name = "cep", required = true)
    protected int cep;
    @XmlAttribute(name = "cidade", required = true)
    protected String cidade;
    @XmlAttribute(name = "uf", required = true)
    protected String uf;
    @XmlAttribute(name = "aceite", required = true)
    protected String aceite;

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
     */
    public long getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * Define o valor da propriedade cpfCnpj.
     * 
     */
    public void setCpfCnpj(long value) {
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
     * Obtém o valor da propriedade endereco.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndereco() {
        return endereco;
    }

    /**
     * Define o valor da propriedade endereco.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndereco(String value) {
        this.endereco = value;
    }

    /**
     * Obtém o valor da propriedade cep.
     * 
     */
    public int getCep() {
        return cep;
    }

    /**
     * Define o valor da propriedade cep.
     * 
     */
    public void setCep(int value) {
        this.cep = value;
    }

    /**
     * Obtém o valor da propriedade cidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCidade() {
        return cidade;
    }

    /**
     * Define o valor da propriedade cidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCidade(String value) {
        this.cidade = value;
    }

    /**
     * Obtém o valor da propriedade uf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUf() {
        return uf;
    }

    /**
     * Define o valor da propriedade uf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUf(String value) {
        this.uf = value;
    }

    /**
     * Obtém o valor da propriedade aceite.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAceite() {
        return aceite;
    }

    /**
     * Define o valor da propriedade aceite.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAceite(String value) {
        this.aceite = value;
    }

}
