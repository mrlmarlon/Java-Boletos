package br.com.java_brasil.boleto.service.bancos.sicoob_cnab240;

import br.com.java_brasil.boleto.model.Configuracao;

import java.util.Arrays;
import java.util.List;

public class ConfiguracaoSicoobCnab240 implements Configuracao {
    @Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList("locaisDePagamento",
                "dataVencimento",
                "beneficiario.nomeBeneficiario",
                "beneficiario.documento",
                "beneficiario.agencia",
                "beneficiario.numeroConvenio",
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
                "linhaDigitavel",
                "codigoBarras",
                "pagador.endereco.numero",
                "pagador.endereco.bairro",
                "pagador.endereco.cidade",
                "pagador.endereco.uf");
    }
}
