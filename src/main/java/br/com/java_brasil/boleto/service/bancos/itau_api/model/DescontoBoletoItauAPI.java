package br.com.java_brasil.boleto.service.bancos.itau_api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DescontoBoletoItauAPI {

    private String codigo_tipo_desconto;
    private List<DescontosBoletoItauAPI> descontos;
}
