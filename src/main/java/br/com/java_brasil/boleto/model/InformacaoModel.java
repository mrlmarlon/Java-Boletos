package br.com.java_brasil.boleto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class InformacaoModel implements Serializable {
    protected String informacao;

    @Override
    public String toString() {
        return informacao;
    }

}