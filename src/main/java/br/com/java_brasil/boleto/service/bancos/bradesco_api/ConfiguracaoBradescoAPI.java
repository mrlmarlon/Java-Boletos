package br.com.java_brasil.boleto.service.bancos.bradesco_api;

import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ConfiguracaoBradescoAPI implements Configuracao {

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String cpfCnpj;
    @NotNull
    private AmbienteEnum ambiente;
    @NotEmpty
    private String caminhoCertificado;
    @NotEmpty
    private String urlRegistroBoleto = "/v1/boleto/registrarBoleto";
    @NotEmpty
    private String urlToken = "/auth/server/v1.1/token";
    @NotEmpty
    private String urlBaseProducao = "https://openapi.bradesco.com.br";
    @NotEmpty
    private String urlBaseHomologacao = "https://proxy.api.prebanco.com.br";

    @NotEmpty

    public String getURLBase() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlBaseProducao : this.urlBaseHomologacao;
    }

    @Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "beneficiario.agencia",
                "beneficiario.digitoAgencia",
                "beneficiario.conta",
                "beneficiario.digitoConta",
                "beneficiario.documento",
                "beneficiario.carteira",
                "beneficiario.nossoNumero",
                "pagador.nome",
                "pagador.documento",
                "pagador.codigo",
                "pagador.endereco.logradouro",
                "pagador.endereco.numero",
                "pagador.endereco.bairro",
                "pagador.endereco.cep",
                "pagador.endereco.cidade",
                "pagador.endereco.uf",
                "valorBoleto",
                "dataVencimento",
                "beneficiario",
                "pagador");
    }

}
