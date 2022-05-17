package br.com.java_brasil.boleto.service.bancos.banco_brasil_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ConfiguracaoBancoBrasilAPI implements Configuracao {

    private String clientId;
    private String clientSecret;
    private String developerKey;
    private String authorization;
    private AmbienteEnum ambiente;
    private String urlRegistroBoleto = "/boletos?gw-dev-app-key=";
    private String urlTokenProducao = "https://oauth.bb.com.br/oauth/token";
    private String urlTokenHomologacao = "https://oauth.hm.bb.com.br/oauth/token";
    private String urlBaseProducao = "https://api.bb.com.br/cobrancas/v2";
    private String urlBaseHomologacao = "https://api.hm.bb.com.br/cobrancas/v2";

    @Override
    public void verificaConfiguracoes() {
        if (StringUtils.isBlank(clientId) ||
                StringUtils.isBlank(clientSecret) ||
                StringUtils.isBlank(developerKey) ||
                ambiente == null ||
                StringUtils.isBlank(urlRegistroBoleto) ||
                StringUtils.isBlank(urlTokenProducao) ||
                StringUtils.isBlank(urlTokenHomologacao) ||
                StringUtils.isBlank(urlBaseProducao) ||
                StringUtils.isBlank(urlBaseHomologacao) ||
                StringUtils.isBlank(authorization)) {
            throw new BoletoException("Configuracoes invalidas.");
        }
    }

    public String getURLBase() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlBaseProducao : this.urlBaseHomologacao;
    }

    public String getURLToken() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlTokenProducao : this.urlTokenHomologacao;
    }
}
