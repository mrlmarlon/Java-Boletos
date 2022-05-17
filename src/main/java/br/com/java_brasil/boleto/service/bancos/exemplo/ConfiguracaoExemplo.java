package br.com.java_brasil.boleto.service.bancos.exemplo;

import br.com.java_brasil.boleto.model.Configuracao;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ConfiguracaoExemplo implements Configuracao {

    @NotEmpty
    private String usuario;
    @NotEmpty
    private String senha;

    @Override
    public List<String> camposObrigatoriosBoleto() {
        return new ArrayList<>();
    }
}
