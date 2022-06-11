//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.05.26 às 10:10:26 PM BRT 
//


package br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de ocorrencias complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="ocorrencias">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ocorrencia" type="{Bergs.Boc.Bocswsxn}ocorrencia" maxOccurs="5"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ocorrencias", propOrder = {
    "ocorrencia"
})
public class Ocorrencias {

    @XmlElement(required = true)
    protected List<Ocorrencia> ocorrencia;

    /**
     * Gets the value of the ocorrencia property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ocorrencia property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOcorrencia().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ocorrencia }
     * 
     * 
     */
    public List<Ocorrencia> getOcorrencia() {
        if (ocorrencia == null) {
            ocorrencia = new ArrayList<Ocorrencia>();
        }
        return this.ocorrencia;
    }

}
