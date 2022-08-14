package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobPagador {
    
    private String numeroCpfCnpj;
    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String cep;
    private String uf;
    private List<String> email;
            
    
}
