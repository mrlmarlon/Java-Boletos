package br.com.java_brasil.boleto.service.bancos.bradesco_cnab400;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
import br.com.java_brasil.boleto.model.enums.TipoMultaEnum;
import br.com.java_brasil.boleto.util.BoletoUtil;
import br.com.java_brasil.boleto.util.JasperUtil;
import br.com.java_brasil.boleto.util.ValidaUtils;
import lombok.NonNull;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.print.PrintService;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class BancoBradescoCnab400 extends BoletoController {

    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        try {
            return imprimir(boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora,
                                            PrintService printService) {
        try {
            imprimirDesktop(boletoModel, diretoImpressora, printService);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }

    }

    @Override
    public byte[] imprimirBoletoBanco(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel enviarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel alterarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel consultarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public BoletoModel baixarBoleto(@NonNull BoletoModel boletoModel) {
        throw new BoletoException("Esta função não está disponível para este banco.");
    }

    @Override
    public String gerarArquivoRemessa(@NonNull List<RemessaRetornoModel> remessaRetornoModel) {
        return gerarArquivo(remessaRetornoModel);
    }

    @Override
    public List<RemessaRetornoModel> importarArquivoRetorno(@NonNull String arquivo) {
        return importarArquivo(arquivo);
    }

    public String gerarArquivo(List<RemessaRetornoModel> list) {
        list.forEach(boleto -> {
            ValidaUtils.validaBoletoModel(boleto.getBoleto(), this.getConfiguracao().camposObrigatoriosBoleto());
        });

        Integer contador = 1;
        StringBuilder linhaArquivo = new StringBuilder();

        //Registro Header Label
        linhaArquivo.append("0"); //Identificação do Registro - deve ser "0"
        linhaArquivo.append("1"); //Identificação do Arquivo-Remessa - deve ser "1"
        linhaArquivo.append("REMESSA"); //Literal Remessa
        linhaArquivo.append("01"); //Código de Serviço - deve ser “01”
        linhaArquivo.append(StringUtils.rightPad("COBRANCA", 15, ' ')); //Literal Serviço - "COBRANCA"
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getCodigosEmpresa(), 20, "0")); // Código da Empresa - Será fornecido pelo Bradesco, quando do cadastramento Vide Obs.
        linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(list.get(0).getBoleto().getBeneficiario().getNomeBeneficiario(), 30), 30, " ")); //Nome da Empresa
        linhaArquivo.append("237"); //Número do Bradesco na Câmara de Compensação - "237"
        linhaArquivo.append(StringUtils.rightPad("BRADESCO", 15, ' ')); //Nome do Banco por Extenso - "BRADESCO"
        linhaArquivo.append(BoletoUtil.getDataFormato(LocalDate.now(), "ddMMyy")); //Data da Gravação do Arquivo - Formato DDMMAA
        linhaArquivo.append(StringUtils.repeat(" ", 8)); //Deixar em Branco (sem preenchimento) 8 posições
        linhaArquivo.append("MX"); //Identificação do Sistema - "MX"
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getNumeroRemessa(), 7, '0')); //Deve ser maior que zero: número do último arquivo remessa + 1; (7 posições)
        linhaArquivo.append(StringUtils.repeat(" ", 277)); //Deixar em Branco (sem preenchimento) 277 posições
        linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0")); //Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);
        contador++;

        //Registro de Transação (Tipo 1)
        for (RemessaRetornoModel boleto : list) {
            linhaArquivo.append("1"); //Identificação do Registro - deve ser "1"
            linhaArquivo.append(StringUtils.repeat("0", 5)); //Agência de Débito (opcional) - preencher com zeros caso não for informar o dado
            linhaArquivo.append(StringUtils.repeat("0", 1)); //Dígito da Agência de Débito (opcional) - preencher com espaço em branco caso não for informar o dado
            linhaArquivo.append(StringUtils.repeat("0", 5)); //Razão da Conta-Corrente (opcional) - preencher com zeros caso não for informar o dado
            linhaArquivo.append(StringUtils.repeat("0", 7)); //Conta-Corrente (opcional) - preencher com zeros caso não for informar o dado
            linhaArquivo.append(StringUtils.repeat("0", 1)); //Dígito da Conta-Corrente (opcional) - preencher com espaço em branco caso não for informar o dado
            linhaArquivo.append("0"); //Literal "0"
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getCarteira(), 3, "0")); //Códigos da Carteira
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getAgencia(), 5, "0")); //Códigos da Agência Beneficiários, sem o Dígito
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 7, "0")); //Contas-Corrente
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDigitoConta(), 1, "0")); //Dígitos da Conta
            linhaArquivo.append(StringUtils.repeat(" ", 25)); //Nº Controle do Participante
            linhaArquivo.append(StringUtils.repeat("0", 3)); //Código do Banco a ser debitado na Câmara de Compensação

            if (!boleto.getBoleto().getTipoMulta().equals(TipoMultaEnum.ISENTO)
                    && BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualMulta())) {
                linhaArquivo.append("2"); // Campo de Multa - Se = 2 considerar percentual de multa. Se = 0, sem multa
                linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualMulta(), 2, 4)); // Percentual de Multa
            } else {
                linhaArquivo.append("0");
                linhaArquivo.append(StringUtils.repeat("0", 4));
            }

            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getBeneficiario().getNossoNumero(), 11, "0")); //Nosso Número
            linhaArquivo.append(boleto.getBoleto().getBeneficiario().getDigitoNossoNumero()); //Digito Nosso Número
            linhaArquivo.append(StringUtils.repeat("0", 10)); //Desconto Bonificação por dia
            linhaArquivo.append("2"); //Condição para Emissão da Papeleta de Cobrança - "2" = Cliente emite e o Banco somente processa o registro
            linhaArquivo.append(" "); //Ident. se emite Boleto para Débito Automático - deixar em branco
            linhaArquivo.append(StringUtils.repeat(" ", 10)); //Identificação da Operação do Banco - deixar em branco
            linhaArquivo.append(" "); //Indicador Rateio Crédito (opcional) - deixar em branco
            linhaArquivo.append("2"); //Endereçamento para Aviso do Débito Automático em Conta-Corrente (opcional) - "2" = não emite aviso.
            linhaArquivo.append(StringUtils.repeat(" ", 2)); //Quantidade de Pagamentos - deixar em branco
            linhaArquivo.append(StringUtils.leftPad(boleto.getInstrucao(), 2, "0"));// Identificação da Ocorrência
            linhaArquivo.append(StringUtils.rightPad(boleto.getBoleto().getNumeroDocumento(), 10, " ")); //Número do Documento
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataVencimento(), "ddMMyy")); // Data de Vencimento
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorBoleto(), 2, 13)); //Valor do título com 13 posições - Alinhado à direita e zeros à esquerda;
            linhaArquivo.append(StringUtils.repeat("0", 3)); //Banco Encarregado da Cobrança - preencher com zeros
            linhaArquivo.append(StringUtils.repeat("0", 5)); //Agência Depositária - preencher com zeros
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getEspecieDocumento(), 2, "0")); //Espécie do título
            linhaArquivo.append("N"); //Identificação - sempre "N"
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataEmissao(), "ddMMyy"));//Data Emissão
            linhaArquivo.append(StringUtils.repeat("0", 4)); //Instruções - preencher com zeros
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getJuroDeMora(), 2, 13)); //Valor a ser Cobrado por Dia de Atraso
            linhaArquivo.append(StringUtils.repeat("0", 6)); //Data Limite P/Concessão de Desconto - preencher com zeros
            linhaArquivo.append(StringUtils.repeat("0", 13)); //Valor do Desconto  - preencher com zeros
            linhaArquivo.append(StringUtils.repeat("0", 13)); //Valor do IOF - preencher com zeros
            linhaArquivo.append(StringUtils.repeat("0", 13)); //Valor do Abatimento a ser Concedido ou Cancelado - preencher com zeros
            linhaArquivo.append(boleto.getBoleto().getPagador().isClienteCpf() ? "01" : "02"); //Identificação do Tipo de Inscrição do Pagador - 1 = CPF e 2 = CNPJ
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getDocumento(), 14, "0")); //Documento do Pagador
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getPagador().getNome(), 40), 40, " ")); //Nome

            String enderecoCompleto = boleto.getBoleto().getPagador().getEndereco().getLogradouro();
            enderecoCompleto = enderecoCompleto + ", " + boleto.getBoleto().getPagador().getEndereco().getNumero();
            if (boleto.getBoleto().getPagador().getEndereco().getComplemento() != null) {
                enderecoCompleto = enderecoCompleto + " " + boleto.getBoleto().getPagador().getEndereco().getComplemento();
            }
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(enderecoCompleto, 40), 40, " ")); //Endereço completo

            linhaArquivo.append(StringUtils.repeat(" ", 12)); //deve ser em branco
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getEndereco().getCep(), 8, "0")); //CEP
            linhaArquivo.append(StringUtils.repeat(" ", 60)); //deve ser em branco
            linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0")); //Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
            linhaArquivo.append((char) 13);
            linhaArquivo.append((char) 10);
            contador++;
        }

        //Registro Trailler
        linhaArquivo.append("9");//Identificação do registro trailer
        linhaArquivo.append(StringUtils.repeat(" ", 393));//Deixar em Branco (sem preenchimento) com 384 posições
        linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0")); //Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        String arquivoRemessa = linhaArquivo.toString();
        StringBuilder sbRemessa = new StringBuilder();
        for (int i = 0; i < arquivoRemessa.length(); i++) {
            char ch = arquivoRemessa.charAt(i);
            if (BradescoUtil.isASCIIBradesco(ch)) {
                sbRemessa.append(ch);
            } else {
                sbRemessa.append((char) 32);
            }
        }

        return sbRemessa.toString();
    }

    private List<RemessaRetornoModel> importarArquivo(String arquivo) {
        List<RemessaRetornoModel> list = new ArrayList<>();
        String numeroRetorno = null;
        try (Scanner scanner = new Scanner(arquivo)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.length() > 1) {
                    if (linha.charAt(0) == '0') { //Header
                        if (!linha.substring(1, 2).equals("2")) { //2 = RETORNO
                            throw new BoletoException("Formato do aquivo inválido.");
                        }
                        if (!linha.substring(76, 79).equals("748")) { //Número do Bradesco
                            throw new BoletoException("Número do Banco inválido.");
                        }
                        numeroRetorno = linha.substring(108, 113);
                    }
                    if (linha.charAt(0) == '1') { //Detalhe
                        RemessaRetornoModel remessaRetornoModel = new RemessaRetornoModel();
                        remessaRetornoModel.setBoleto(new BoletoModel());
                        remessaRetornoModel.getBoleto().setPagador(new Pagador());
                        remessaRetornoModel.getBoleto().setBeneficiario(new Beneficiario());
                        remessaRetornoModel.getBoleto().setCodRetorno(numeroRetorno);
                        remessaRetornoModel.getBoleto().getPagador().setDocumento(linha.substring(3, 17));
                        remessaRetornoModel.getBoleto().getBeneficiario().setNossoNumero(linha.substring(70, 81));
                        remessaRetornoModel.getBoleto().getBeneficiario().setDigitoNossoNumero(linha.substring(81, 82));
                        remessaRetornoModel.setOcorrencia(linha.substring(108, 110));
                        remessaRetornoModel.getBoleto().setNumeroDocumento(linha.substring(116, 126));
                        remessaRetornoModel.getBoleto().setDataVencimento(BoletoUtil.formataStringPadraoDDMMYYYYParaLocalDate(linha.substring(146, 152)));//Formato: DDMMAAAA
                        remessaRetornoModel.getBoleto().setValorBoleto(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(152, 165)));
                        remessaRetornoModel.setDespesaCobranca(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(175, 188)));
                        remessaRetornoModel.setMotivoOcorrencia(linha.substring(318, 328));
                        remessaRetornoModel.setAbatimentoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(227, 240)));
                        remessaRetornoModel.setDataOcorrencia(BoletoUtil.formataStringPadraoDDMMYYYYParaLocalDate(linha.substring(110, 116)));//Formato: DDMMAAAA
                        remessaRetornoModel.setDescontoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(240, 253)));
                        remessaRetornoModel.setJuroDeMora(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(266, 279)));
                        remessaRetornoModel.setValorEfetivamentePago(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(253, 266)));

                        list.add(remessaRetornoModel);
                    }
                }
            }
        }

        return list;
    }

    private byte[] imprimir(BoletoModel boletoModel) throws Exception {
        HashMap<String, Object> parametros = getParametrosRelatorio();

        preparaValidaBoletoImpressao(boletoModel);

        byte[] byteRelatorio = JasperUtil.geraRelatorio(Arrays.asList(boletoModel),
                parametros,
                "BoletoBradesco",
                this.getClass(),
                new HashMap<>());

        return byteRelatorio;
    }

    private void imprimirDesktop(BoletoModel boletoModel, boolean diretoImpressora,
                                 PrintService printService) throws Exception {
        try {
            HashMap<String, Object> parametros = getParametrosRelatorio();

            preparaValidaBoletoImpressao(boletoModel);

            JasperUtil.geraRelatorioDescktop(Arrays.asList(boletoModel), parametros, "BoletoBradesco", this.getClass(),
                    new HashMap<>(), diretoImpressora, printService);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }
    }

    private HashMap<String, Object> getParametrosRelatorio() throws IOException {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/logo/bradesco.png");
        Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
        parametros.put("LogoBanco", image);
        return parametros;
    }

    private void preparaValidaBoletoImpressao(BoletoModel boletoModel) {
        boletoModel.getBeneficiario().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiario().getDocumento()));
        boletoModel.getPagador().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getPagador().getDocumento()));
        if (boletoModel.getBeneficiarioFinal() != null) {
            boletoModel.getBeneficiarioFinal().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiarioFinal().getDocumento()));
        }

        StringBuilder codigoBarras = new StringBuilder();
        codigoBarras.append("2379");
        codigoBarras.append("X"); //Substituir pelo Digito verificador
        codigoBarras.append(BradescoUtil.fatorData(boletoModel.getDataVencimento()));
        codigoBarras.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

        StringBuilder campoLivre = new StringBuilder();
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
        campoLivre.append(boletoModel.getBeneficiario().getCarteira());
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getNossoNumero(), 11, "0"));
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 7, "0"));
        campoLivre.append("0");
        codigoBarras.append(campoLivre);

        Integer digitoVerificadorGeral = BradescoUtil.modulo11(codigoBarras.toString().replace("X", ""));
        boletoModel.setCodigoBarras(codigoBarras.toString().replace("X", digitoVerificadorGeral.toString()));

        StringBuilder linhaParte1 = new StringBuilder();
        linhaParte1.append("2379");
        linhaParte1.append(campoLivre.toString().substring(0, 5));
        linhaParte1.append(BradescoUtil.modulo10(linhaParte1.toString()));

        StringBuilder linhaParte2 = new StringBuilder();
        linhaParte2.append(campoLivre.toString().substring(5, 15));
        linhaParte2.append(BradescoUtil.modulo10(linhaParte2.toString()));

        StringBuilder linhaParte3 = new StringBuilder();
        linhaParte3.append(campoLivre.substring(15, 25));
        linhaParte3.append(BradescoUtil.modulo10(linhaParte3.toString()));

        StringBuilder linhaParte4 = new StringBuilder();
        linhaParte4.append(digitoVerificadorGeral);

        StringBuilder linhaParte5 = new StringBuilder();
        linhaParte5.append(codigoBarras.toString().substring(5, 19));

        StringBuilder linhaDigitavel = new StringBuilder();
        linhaDigitavel.append(linhaParte1.toString().substring(0, 5));
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte1.toString().substring(5, 10));
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte2.toString().substring(0, 5));
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte2.toString().substring(5, 11));
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte3.toString().substring(0, 5));
        linhaDigitavel.append(".");
        linhaDigitavel.append(linhaParte3.toString().substring(5, 11));
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte4.toString());
        linhaDigitavel.append("  ");

        linhaDigitavel.append(linhaParte5.toString());

        boletoModel.setLinhaDigitavel(linhaDigitavel.toString());

        validaDadosImpressao(boletoModel);
    }

    private void validaDadosImpressao(BoletoModel boleto) {
        ValidaUtils.validaBoletoModel(boleto,
                Arrays.asList("locaisDePagamento", "dataVencimento", "beneficiario.nomeBeneficiario",
                        "beneficiario.documento", "beneficiario.agencia",
                        "beneficiario.conta", "dataEmissao", "numeroDocumento", "especieDocumento", "aceite",
                        "beneficiario.nossoNumero", "beneficiario.digitoNossoNumero", "especieMoeda", "valorBoleto",
                        "pagador.nome", "pagador.documento", "pagador.endereco.logradouro", "pagador.endereco.cep",
                        "linhaDigitavel", "codigoBarras", "pagador.endereco.numero", "pagador.endereco.bairro",
                        "pagador.endereco.cidade", "pagador.endereco.uf"));
    }
}
