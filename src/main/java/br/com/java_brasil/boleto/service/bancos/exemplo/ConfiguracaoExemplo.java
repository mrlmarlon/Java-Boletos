package br.com.java_brasil.boleto.service.bancos.exemplo;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.Configuracao;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ConfiguracaoExemplo implements Configuracao {

    private String usuario;
    private String senha;

    @Override
    public void verificaConfiguracoes() {
        if (StringUtils.isBlank(usuario) || StringUtils.isBlank(senha)) {
            throw new BoletoException("Configuracoes invalidas.");
        }
    }
}
