package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BeneficiarioBoletoItauAPI {

    private String id_beneficiario;
    private String nome_cobranca;
    private EnderecoBoletoItauAPI endereco;
    private TipoPessoaBoletoItauAPI tipo_pessoa;
}
