package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoletoSicoobEnvioResponse {
    private List<BoletoSicoobResultado> resultado;
}