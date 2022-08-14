package br.com.java_brasil.boleto.service.bancos.banrisul_api;

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
public class ConfiguracaoBanrisulAPI implements Configuracao {
    @NotNull
    private AmbienteEnum ambiente;
    @NotEmpty
    private String caminhoCertificado;
    @NotNull
    private String senhaCertificado;
    @NotEmpty
    private String urlBaseProducao = "?";
    @NotEmpty
    private String urlBaseHomologacao = "https://ww20.banrisul.com.br/boc/link/Bocswsxn_CobrancaOnlineWS.asmx";

    @NotEmpty
    public String getURLBase() {
        return this.ambiente.equals(AmbienteEnum.PRODUCAO) ? this.urlBaseProducao : this.urlBaseHomologacao;
    }

    @Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "beneficiario.agencia",
                "beneficiario.conta",
                "beneficiario.digitoConta",
                "beneficiario.documento",
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
