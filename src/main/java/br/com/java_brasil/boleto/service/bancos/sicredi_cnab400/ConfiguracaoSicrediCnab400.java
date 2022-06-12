package br.com.java_brasil.boleto.service.bancos.sicredi_cnab400;

import java.util.Arrays;
import java.util.List;

import br.com.java_brasil.boleto.model.Configuracao;

public class ConfiguracaoSicrediCnab400 implements Configuracao {

	@Override
    public List<String> camposObrigatoriosBoleto() {
        return Arrays.asList(
                "beneficiario.conta",
                "beneficiario.documento",
                "tipoImpressao",
                "especieMoeda",
                "tipoDesconto",
                "tipoJuros",
                "beneficiario.nossoNumero",
                "beneficiario.digitoNossoNumero",
                "numeroDocumento",
                "dataVencimento",
                "valorBoleto",
                "especieDocumento",
                "aceite",
                "dataEmissao",
                "protesto",
                "negativacaoAutomatica",
                "pagador.documento",
                "pagador.nome",
                "pagador.endereco",
                "pagador.endereco.logradouro",
                "pagador.endereco.numero",
                "pagador.endereco.cidade",
                "pagador.endereco.uf",
                "pagador.endereco.cep",
                "beneficiario",
                "pagador");
    }

}
