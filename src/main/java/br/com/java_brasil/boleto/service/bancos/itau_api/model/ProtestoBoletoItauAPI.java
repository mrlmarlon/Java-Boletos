package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProtestoBoletoItauAPI {
    private Integer protesto;
    private Integer quantidade_dias_protesto;
}
