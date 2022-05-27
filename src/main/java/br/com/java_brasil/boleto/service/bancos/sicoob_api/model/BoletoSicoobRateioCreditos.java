package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobRateioCreditos {
    
    private Integer numeroBanco;
    private Integer numeroAgencia;
    private Integer numeroContaCorrente;
    private Boolean contaPrincipal;
    private Integer codigoTipoValorRateio;
    private BigDecimal valorRateio;
    private Integer codigoTipoCalculoRateio;
    private String numeroCpfCnpjTitular;
    private String nomeTitular;
    private Integer codigoFinalidadeTed;
    private String codigoTipoContaDestinoTed;
    private Integer quantidadeDiasFloat;
    private String dataFloatCredito;
    
}
