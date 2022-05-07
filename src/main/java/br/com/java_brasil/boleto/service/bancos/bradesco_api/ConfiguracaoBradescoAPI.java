package br.com.java_brasil.boleto.service.bancos.bradesco_api;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class ConfiguracaoBradescoAPI implements Configuracao {

    private String clientId;
    private String cpfCnpj;
    private AmbienteEnum ambiente;
    private String caminhoCertificado;
    private String urlRegistroBoleto = "/v1/boleto/registrarBoleto";
    private String urlToken = "/auth/server/v1.1/token";
    private String urlBaseProducao = "https://openapi.bradesco.com.br";
    private String urlBaseHomologacao = "https://proxy.api.prebanco.com.br";

    @Override
    public void verificaConfiguracoes() {
        if (StringUtils.isBlank(clientId) ||
                StringUtils.isBlank(cpfCnpj) ||
                ambiente == null ||
                StringUtils.isBlank(caminhoCertificado) ||
                StringUtils.isBlank(urlBaseProducao) ||
                StringUtils.isBlank(urlBaseHomologacao) ||
                StringUtils.isBlank(urlToken) ||
                StringUtils.isBlank(urlRegistroBoleto)) {
            throw new BoletoException("Configuracoes invalidas.");
        }
    }

    public String getURLBase() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlBaseProducao : this.urlBaseHomologacao;
    }
}
