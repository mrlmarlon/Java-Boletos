package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SacadorAvalistaBoletoItauAPI {

    private PessoaBoletoItauAPI pessoa;
    private EnderecoBoletoItauAPI endereco;
}
