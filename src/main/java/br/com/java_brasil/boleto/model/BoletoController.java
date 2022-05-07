package br.com.java_brasil.boleto.model;

import br.com.java_brasil.boleto.service.MetodosGenericos;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BoletoController implements MetodosGenericos {
    private Configuracao configuracao;
}
