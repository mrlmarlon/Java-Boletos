package br.com.java_brasil.boleto.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
public class Beneficiario implements Serializable {

    private String agencia;
    private String digitoAgencia;
    private String postoDaAgencia;

    private String conta;
    private String digitoConta;

    private String carteira;
    private String variacaoCarteira;
    private String nossoNumero;
    private String digitoNossoNumero;

    private String nomeBeneficiario;
    private String documento;
    private Endereco endereco;

    private String numeroConvenio;

    public boolean isClienteCpf() {
        return StringUtils.isNotBlank(documento) && documento.length() <= 11;
    }
}
