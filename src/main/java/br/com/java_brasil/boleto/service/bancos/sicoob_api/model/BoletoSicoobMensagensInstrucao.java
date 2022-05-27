package br.com.java_brasil.boleto.service.bancos.sicoob_api.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoletoSicoobMensagensInstrucao {
    
    private Integer tipoInstrucao;
    private List<String> mensagens;
    
}
