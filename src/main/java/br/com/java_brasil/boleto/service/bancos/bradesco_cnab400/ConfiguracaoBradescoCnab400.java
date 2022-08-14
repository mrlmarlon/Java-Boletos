package br.com.java_brasil.boleto.service.bancos.bradesco_cnab400;

import br.com.java_brasil.boleto.model.Configuracao;

import java.util.Arrays;
import java.util.List;

public class ConfiguracaoBradescoCnab400 implements Configuracao {

	@Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "locaisDePagamento",
                "dataVencimento",
                "beneficiario.nomeBeneficiario",
                "beneficiario.documento",
                "beneficiario.agencia",
                "beneficiario.conta",
                "dataEmissao",
                "numeroDocumento",
                "especieDocumento",
                "aceite",
                "beneficiario.nossoNumero",
                "beneficiario.digitoNossoNumero",
                "especieMoeda",
                "valorBoleto",
                "pagador.nome",
                "pagador.documento",
                "pagador.endereco.logradouro",
                "pagador.endereco.cep",
                "pagador.endereco.numero",
                "pagador.endereco.bairro",
                "pagador.endereco.cidade",
                "pagador.endereco.uf");
    }

}
