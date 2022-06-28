package br.com.java_brasil.boleto.service.bancos.itau_api;

import br.com.java_brasil.boleto.model.Configuracao;
import br.com.java_brasil.boleto.model.enums.AmbienteEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class ConfiguracaoItauAPI implements Configuracao {

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String clientSecret;
    @NotEmpty
    private String caminhoCertificado;
    @NotEmpty
    private String senhaCertificado;
    @NotNull
    private AmbienteEnum ambiente;
    @NotEmpty
    private String urlRegistroBoletoHomologacao = "https://devportal.itau.com.br/sandboxapi/pix_recebimentos_conciliacoes_v2_ext/v2/boletos_pix";
    @NotEmpty
    private String urlRegistroBoletoProducao = "https://secure.api.itau/pix_recebimentos_conciliacoes/v2/boletos_pix";
    @NotEmpty
    private String urlTokenHomologacao = "https://devportal.itau.com.br/api/jwt";
    @NotEmpty
    private String urlTokenProducao = "https://sts.itau.com.br/api/oauth/token";

    //Prenchido Pela Aplicação
    private String token;
    private LocalDateTime expiracaoToken;

    public String getURLToken() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlTokenProducao : this.urlTokenHomologacao;
    }

    public String getURLRegistroBoleto() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlRegistroBoletoProducao : this.urlRegistroBoletoHomologacao;
    }

    @Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "beneficiario",
                "beneficiario.agencia",
                "beneficiario.conta",
                "beneficiario.digitoConta",
                "beneficiario.carteira",
                "valorBoleto",
                "dataVencimento",
                "dataEmissao",
                "especieDocumento",
                "numeroBoleto",
                "pagador",
                "pagador.nome",
                "pagador.documento",
                "pagador.endereco",
                "pagador.endereco.logradouro",
                "pagador.endereco.bairro",
                "pagador.endereco.cidade",
                "pagador.endereco.uf",
                "pagador.endereco.cep",
                "beneficiario.documento"
        );
    }

}
