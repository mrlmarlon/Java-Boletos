package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PessoaBoletoItauAPI {

    private String nome_pessoa;
    private String nome_fantasia;
    private TipoPessoaBoletoItauAPI tipo_pessoa;
    private String numero_cpf_cnpj;
}
