package br.com.java_brasil.boleto.service.bancos.sicoob_api;

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
public class ConfiguracaoSicoobAPI implements Configuracao {

    @NotEmpty
    private String clientId;
    @NotEmpty
    private String cpfCnpj;
    @NotEmpty
    private String cooperativa;
    @NotEmpty
    private String contaCorrente;
    @NotNull
    private Integer numeroContrato;
    @NotEmpty
    private String basicToken;
    @NotNull
    private AmbienteEnum ambiente;
    @NotEmpty
    private String urlBaseHomologacao = "https://sandbox.sicoob.com.br";
    @NotEmpty
    private String urlAuthProducao = "https://api.sisbr.com.br/auth";
    @NotEmpty
    private String urlBaseProducao = "https://api.sisbr.com.br/cooperado";
    @NotEmpty
    private String urlToken = "/token";
    @NotEmpty
    private String urlRegistraBoleto = "/cobranca-bancaria/v1/boletos";
    @NotEmpty
    private String urlAuthorize = "/oauth2/authorize";
    @NotEmpty
    private String urlConsultaBoleto = "/cobranca-bancaria/v1/boletos";
    //Prenchido Pela Aplicação
    private String token;
    private String refreshToken;
    private LocalDateTime expiracaoToken;

    public String getURLBase() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlBaseProducao : this.urlBaseHomologacao;
    }

    public String getURLAuth() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlAuthProducao : this.urlBaseHomologacao;
    }

    @Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "especieDocumento"
//                "beneficiario.agencia",
//                "beneficiario.digitoAgencia",
//                "beneficiario.conta",
//                "beneficiario.digitoConta",
//                "beneficiario.documento",
//                "beneficiario.carteira",
//                "beneficiario.nossoNumero",
//                "pagador.nome",
//                "pagador.documento",
//                "pagador.codigo",
//                "pagador.endereco.logradouro",
//                "pagador.endereco.numero",
//                "pagador.endereco.bairro",
//                "pagador.endereco.cep",
//                "pagador.endereco.cidade",
//                "pagador.endereco.uf",
//                "valorBoleto",
//                "dataVencimento",
//                "beneficiario",
//                "pagador"
        );
    }

}
