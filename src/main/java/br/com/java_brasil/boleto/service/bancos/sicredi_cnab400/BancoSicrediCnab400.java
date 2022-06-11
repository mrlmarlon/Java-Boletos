package br.com.java_brasil.boleto.service.bancos.sicredi_cnab400;

import br.com.java_brasil.boleto.exception.BoletoException;
import br.com.java_brasil.boleto.model.*;
import br.com.java_brasil.boleto.model.enums.TipoDescontoEnum;
import br.com.java_brasil.boleto.model.enums.TipoJurosEnum;
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

public class BancoSicrediCnab400 extends BoletoController {

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

        //CABEÇALHO (Tipo 0)
        linhaArquivo.append("0"); //A identicação do header deve ser “0”(zero)
        linhaArquivo.append("1"); //A identificação do arquivo de remessa deve ser "1"
        linhaArquivo.append("REMESSA"); //literal REMESSA
        linhaArquivo.append("01"); //O código de serviço de cobrança é “01”
        linhaArquivo.append("COBRANCA");//literal COBRANCA
        linhaArquivo.append(StringUtils.rightPad("", 7, ' '));
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 5, '0')); //Conta Corrente sem o DV ou conta beneficiário (5 posições)
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDocumento(), 14, '0')); //Informar CPF/CNPJ do beneficiário. Alinhado à direita e zeros à esquerda; (14 posições) *** PEGAR DA EMPRESA
        linhaArquivo.append(StringUtils.repeat(" ", 31));//Deixar em Brancos (sem preenchimento) 31 posiçoes
        linhaArquivo.append("748"); // numero do sicredi
        linhaArquivo.append(StringUtils.rightPad("SICREDI", 15, ' ')); //literal Sicredi
        linhaArquivo.append(BoletoUtil.getDataFormato(LocalDate.now(), "yyyyMMdd")); //O Formato da data de geração do arquivo deve estar no padrão: AAAAMMDD
        linhaArquivo.append(StringUtils.repeat(" ", 8)); //Deixar em Branco (sem preenchimento) 8 posições
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getNumeroRemessa(), 7, '0')); //Deve ser maior que zero: número do último arquivo remessa + 1; (7 posições)
        linhaArquivo.append(StringUtils.repeat(" ", 273)); //Deixar em Branco (sem preenchimento) 273 posições
        linhaArquivo.append("2.00"); //2.00 (o ponto deve ser colocado)
        linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, '0')); //Alinhado à direita e zeros à esquerda; (Número seqüencial do registro com 6 posições)
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);
        contador++;

        //DETALHE COM REGISTRO (Tipo 1)
        for (RemessaRetornoModel boleto : list) {
            linhaArquivo.append("1"); //Identificação do registro detalhe de estar “1”
            linhaArquivo.append("A"); //“A” - Sicredi Com Registro
            linhaArquivo.append("A"); //“A” – Simples
            linhaArquivo.append(boleto.getBoleto().getTipoImpressao()); //“A” – Normal “B” – Carnê
            linhaArquivo.append(" "); //Deixar em Branco
            linhaArquivo.append("H"); // H - Híbrido
            linhaArquivo.append(StringUtils.repeat(" ", 10));//Deixar em Branco (sem preenchimento) 12 posições
            linhaArquivo.append("A"); //“A” – Real
            linhaArquivo.append(boleto.getBoleto().getTipoDesconto().equals(TipoDescontoEnum.VALOR_FIXO) ? "A" : "B");
            linhaArquivo.append(boleto.getBoleto().getTipoJuros().equals(TipoJurosEnum.VALOR_DIA) ? "A" : "B");
            linhaArquivo.append(StringUtils.repeat(" ", 28));//Deixar em Branco (sem preenchimento) 28 posições
            linhaArquivo.append(boleto.getBoleto().getBeneficiario().getNossoNumero() + boleto.getBoleto().getBeneficiario().getDigitoNossoNumero()); //Nosso número Sicredi - com 9 posições
            linhaArquivo.append(StringUtils.repeat(" ", 6));// Deixar em Branco (sem preenchimento) 6 posições
            linhaArquivo.append(BoletoUtil.getDataFormato(LocalDate.now(), "yyyyMMdd"));//Data da Instrução - O Formato da data de instrução do arquivo deve estar no padrão: AAAAMMDD
            linhaArquivo.append(boleto.getInstrucao().equals("31") ? boleto.getCampoAlterado() : " ");//Campo alterado, quando instrução “31”
            linhaArquivo.append(boleto.getPostagemTitulo());//Postagem do título - “S” - Para postar o título diretamente ao pagador - “N” - Não postar e remeter o título para o beneficiário
            linhaArquivo.append(" "); //Deixar em Branco (sem preenchimento) 1 posição
            linhaArquivo.append(boleto.getImpressaoTitulo());//Emissão do bloqueto - “A” – Impressão é feita pelo Sicredi - “B” – Impressão é feita pelo Beneficiário
            if (boleto.getBoleto().getTipoImpressao().equals("B")) {
                linhaArquivo.append(StringUtils.leftPad(String.valueOf(boleto.getBoleto().getNumeroDaParcelaCarne()), 2, '0')); //Número da parcela do carnê com 2 posições - Quando o tipo de impressão for “B – Carnê” (posição 004).
                linhaArquivo.append(StringUtils.leftPad(String.valueOf(boleto.getBoleto().getNumeroTotalDeParcelasCarne()), 2, '0'));//Número total de parcelas do carnê com 2 posições - Quando o tipo de impressão for “B – Carnê” (posição 004).
            } else {
                linhaArquivo.append("00");//Número da parcela do carnê com 2 posições - Quando o tipo de impressão for “B – Carnê” (posição 004).
                linhaArquivo.append("00");//Número total de parcelas do carnê com 2 posições - Quando o tipo de impressão for “B – Carnê” (posição 004).
            }
            linhaArquivo.append(StringUtils.repeat(" ", 4));//Deixar em Branco (sem preenchimento) 4 posições
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos(), 2, 10));//Valor de desconto por dia de antecipação com 10 posições - Informar valor de desconto (alinhado à direita e zeros à esquerda) ou senão preencher com zeros.
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualMulta(), 2, 4));//% multa por pagamento em atraso com 4 posições - Alinhado à direita com zeros à esquerda, sem separador decimal ou preencher com zeros.
            linhaArquivo.append(StringUtils.repeat(" ", 12)); //Brancos (sem preenchimento) com 12 posições
            linhaArquivo.append(StringUtils.leftPad(boleto.getInstrucao(), 2, '0'));// Instrução com 2 posições - 01 - Cadastro de título; 02 - Pedido de baixa; 04 - Concessão de abatimento; 05 - Cancelamento de abatimento concedido; 06 - Alteração de vencimento; 09 - Pedido de protesto; 18 - Sustar protesto e baixar título; 19 - Sustar protesto e manter em carteira;
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getNumeroDocumento(), 10, '0'));//Seu número com 10 posições - 31 - Este campo nunca pode se repetir (Diferente de branco) - normalmente usado neste campo o número da nota fiscal gerada para o sacado.
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataVencimento(), "ddMMyy"));//A data de vencimento deve ser sete dias MAIOR que o campo 151 a 156 “Data de emissão”. Formato: DDMMAA
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorBoleto(), 2, 13));//Valor do título com 13 posições - Alinhado à direita e zeros à esquerda;
            linhaArquivo.append(StringUtils.repeat(" ", 2)); //Deixar em Branco (sem preenchimento)
            linhaArquivo.append(StringUtils.repeat(" ", 7)); //Deixar em Branco (sem preenchimento)
            linhaArquivo.append(boleto.getBoleto().getEspecieDocumento()); //Espécie de documento com 1 posição - Este campo só permite usar os seguintes códigos: A - Duplicata Mercantil por Indicação; B - Duplicata Rural; C - Nota Promissória; D - Nota Promissória Rural; E - Nota de Seguros; G – Recibo; H - Letra de Câmbio; I - Nota de Débito;J - Duplicata de Serviço por Indicação;K – Outros.O – Boleto Proposta Obs.: Se título possuir protesto automático, favor utilizar o código A, pois esta é uma espécie de documento que permite utilizar o protesto automático sem a utilização de um Sacador Avalista.
            linhaArquivo.append(boleto.getBoleto().isAceite() ? "S" : "N");// Aceite do título com 1 posição - “S” – sim - “N” – não
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataEmissao(), "ddMMyy"));//A data de emissão deve ser sete dias MENOR que o campo 121 a 126 “Data de vencimento”. Formato: DDMMAA
            linhaArquivo.append(boleto.getBoleto().isProtesto() ? "06" : "00");//Instrução de protesto automático com 2 posições - “00” - Não protestar automaticamente “06” - Protestar automaticamente
            linhaArquivo.append(StringUtils.leftPad(String.valueOf(boleto.getBoleto().getDiasProtesto()), 2, '0')); // Número de dias p/protesto automático com 2 posições - Campo numérico - mínimo 03 (três) dias Quando preenchido com 3 ou 4 dias o sistema comandará protesto em dias úteis após o vencimento. Quando preenchido acima de 4 dias, o sistema comandará protesto em dias corridos após o vencimento.
            BigDecimal valorPercentualJuros = boleto.getBoleto().getValorPercentualJuros();
            if (boleto.getBoleto().getTipoJuros().equals(TipoJurosEnum.PERCENTUAL_MENSAL)
                    && BoletoUtil.isNotNullEMaiorQZero(valorPercentualJuros)) {
                //Se informado percentual de juros mensal, deve dividir por 30 dias para se obter o percentual diário
                valorPercentualJuros = valorPercentualJuros.divide(BigDecimal.valueOf(30), MathContext.DECIMAL32);
            }
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(valorPercentualJuros, 2, 13));//Valor/% de juros por dia de atraso com 13 posições - Preencher com valor (alinhados à direita com zeros à esquerda) ou preencher com zeros.
            linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto() == null ? "000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto(), "ddMMyy"));//Data limite p/concessão de desconto - Informar data no padrão: DDMMAA ou preencher com zeros.
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos(), 2, 13));//Valor/% do desconto com 13 posições - Informar valor do desconto (alinhado à direita e zeros à esquerda) ou preencher com zeros.
            linhaArquivo.append(boleto.getBoleto().isNegativacaoAutomatica() ? "06" : "00");// Instrução de negativação automática - 00 = Não negativar ou 06 Negativar
            linhaArquivo.append(StringUtils.leftPad(String.valueOf(boleto.getBoleto().getNumeroDiasNegativacao()), 2, '0')); //Número de dias para negativação automática
            linhaArquivo.append(StringUtils.repeat("0", 9));//Sempre preencher com zeros neste campo.
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorDeducoes(), 2, 13));//Valor do abatimento com 13 posiçoes - Informar valor do abatimento (alinhado à direita e zeros à esquerda) ou preencher com zeros.
            linhaArquivo.append(boleto.getBoleto().getPagador().isClienteCpf() ? "1" : "2");//Tipo de pessoa do pagador: PF ou PJ com 1 posição - “1” - Pessoa Física “2” - Pessoa Jurídica
            linhaArquivo.append("0");//Sempre preencher com zeros neste campo. 1 posição
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getDocumento(), 14, "0"));//CPF/CNPJ do Pagador com 14 posições - Alinhado à direita e zeros à esquerda; Obs: No momento dos testes para homologação estes dados devem ser enviados com informações válidas.
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getPagador().getNome(), 40), 40, " "));//Nome do pagador com 40 posições - Neste campo informar o nome do pagador sem acentuação ou caracteres especiais.

            StringBuilder enderecoCompletoPagador = new StringBuilder();
            enderecoCompletoPagador.append(Optional.ofNullable(boleto.getBoleto().getPagador().getEndereco().getLogradouro()).orElse(""));
            enderecoCompletoPagador.append(" ");
            enderecoCompletoPagador.append(Optional.ofNullable(boleto.getBoleto().getPagador().getEndereco().getNumero()).orElse(""));
            enderecoCompletoPagador.append(" ");
            enderecoCompletoPagador.append(Optional.ofNullable(boleto.getBoleto().getPagador().getEndereco().getComplemento()).orElse(""));
            enderecoCompletoPagador.append(" - ");
            enderecoCompletoPagador.append(Optional.ofNullable(boleto.getBoleto().getPagador().getEndereco().getCidade()).orElse(""));
            enderecoCompletoPagador.append("/");
            enderecoCompletoPagador.append(Optional.ofNullable(boleto.getBoleto().getPagador().getEndereco().getUf()).orElse(""));
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(enderecoCompletoPagador.toString(), 40), 40, " "));//Endereço do pagador com 40 posições - Neste campo informar o endereço do pagador sem acentuação ou caracteres especiais.
            linhaArquivo.append(StringUtils.leftPad(Optional.ofNullable(boleto.getBoleto().getPagador().getCodigoNoBanco()).orElse(""), 5, "0"));//Código do pagador na cooperativa beneficiário - Se pagador novo, o campo deve conter zeros. Para pagador já cadastrado, enviar o código enviado no primeiro arquivo de retorno ou sempre zeros quando o sistema do beneficiário não utiliza esse campo – campo alfanumérico;
            linhaArquivo.append(StringUtils.repeat("0", 6));//Sempre preencher com zeros neste campo. 6 posições
            linhaArquivo.append(" ");//Deixar em Branco (sem preenchimento) 1 posição
            linhaArquivo.append(BoletoUtil.manterApenasNumeros(boleto.getBoleto().getPagador().getEndereco().getCep()));//CEP do pagador com 8 posições - Obrigatório ser um CEP Válido
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getCodigo(), 5, "0"));//Código do Pagador junto ao cliente com 5 posições - Para homologação deixar com zeros

            if (boleto.getBoleto().getBeneficiarioFinal() != null) {
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getBeneficiarioFinal().getDocumento(), 14, "0"));//CPF/CNPJ do Sacador Avalista com 14 posições - Alinhado à direita e zeros à esquerda. Deixar em branco caso não exista Sacador Avalista. O Sacador Avalista deve ser diferente do beneficiário e pagador.
                linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getBeneficiarioFinal().getNome(), 41), 41, " "));//Nome do Sacador Avalista com 41 posições - Deixar em brancos quando inexistente. Caso utilize usar sem acentuação ou caracteres especiais.
            } else {
                linhaArquivo.append(StringUtils.repeat(" ", 14));//CPF/CNPJ do Sacador Avalista com 14 posições - Alinhado à direita e zeros à esquerda. Deixar em branco caso não exista Sacador Avalista. O Sacador Avalista deve ser diferente do beneficiário e pagador.
                linhaArquivo.append(StringUtils.repeat(" ", 41));//Nome do Sacador Avalista com 41 posições - Deixar em brancos quando inexistente. Caso utilize usar sem acentuação ou caracteres especiais.
            }
            linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));
            linhaArquivo.append((char) 13);
            linhaArquivo.append((char) 10);
            contador++;

            //REGISTRO DE MENSAGENS (Tipo 2)
            if (boleto.getBoleto().getInstrucoes() != null && !boleto.getBoleto().getInstrucoes().isEmpty()) {
                linhaArquivo.append("2");//Identificação do registro detalhe - "2"
                linhaArquivo.append(StringUtils.repeat(" ", 11));//Deixar em Branco (sem preenchimento) com 11 posições
                linhaArquivo.append(boleto.getBoleto().getBeneficiario().getNossoNumero() + boleto.getBoleto().getBeneficiario().getDigitoNossoNumero());//Nosso número com 9 posições

                int contadorInstrucao = 0;
                for (InformacaoModel instrucao : boleto.getBoleto().getInstrucoes()) {
                    contadorInstrucao++;
                    linhaArquivo.append(StringUtils.rightPad(instrucao.getInformacao(), 80, " "));
                    if (contadorInstrucao >= 4) {
                        break;
                    }
                }

                for (int i = contadorInstrucao; i < 4; i++) {
                    linhaArquivo.append(StringUtils.repeat(" ", 80));
                }
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getNumeroDocumento(), 10, '0'));//Seu Número com 10 posições - Este campo nunca pode se repetir (Diferente de branco) normalmente usado neste campo o número da nota fiscal gerada para o pagador.
                linhaArquivo.append(StringUtils.repeat(" ", 43));//Deixar em Branco (sem preenchimento) com 43 posições
                linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));//Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
                linhaArquivo.append((char) 13);
                linhaArquivo.append((char) 10);
                contador++;
            }

            //REGISTRO INFORMATIVO (Tipo 5)
            if (boleto.getBoleto().getDescricoes() != null && !boleto.getBoleto().getDescricoes().isEmpty()) {
                linhaArquivo.append("5");//Identificação do registro detalhe - "5"
                linhaArquivo.append("E");//E – Específico de um título
                linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 5, '0'));//Código do beneficiário/cedente
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getNumeroDocumento(), 10, '0'));//Seu número com 10 posições - 31 - Este campo nunca pode se repetir (Diferente de branco) - normalmente usado neste campo o número da nota fiscal gerada para o sacado.
                linhaArquivo.append(" ");//Deixar em Branco (sem preenchimento) com 1 posições
                linhaArquivo.append("A");//A - Cobrança com registro

                int contadorInformativo = 0;
                for (InformacaoModel informacao : boleto.getBoleto().getDescricoes()) {
                    contadorInformativo++;
                    linhaArquivo.append(StringUtils.leftPad(String.valueOf(contadorInformativo), 2, '0'));//Campo informa o número da linha do informativo - Começando com 1 até 99.
                    linhaArquivo.append(StringUtils.rightPad(informacao.getInformacao(), 80, " "));
                    if (contadorInformativo >= 4) {
                        break;
                    }
                }

                for (int i = contadorInformativo; i < 4; i++) {
                    linhaArquivo.append(StringUtils.leftPad(String.valueOf(i), 2, '0'));//Campo informa o número da linha do informativo - Começando com 1 até 99.
                    linhaArquivo.append(StringUtils.repeat(" ", 80));
                }
                linhaArquivo.append(StringUtils.repeat(" ", 47));//Deixar em Branco (sem preenchimento) com 43 posições
                linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));//Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
                linhaArquivo.append((char) 13);
                linhaArquivo.append((char) 10);
                contador++;
            }

            //REGISTRO DESCONTOS 2 E 3 (Tipo 7)
            if (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos())
                    && (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos2())
                    || BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos3()))) {
                linhaArquivo.append("7");//Identificação do registro detalhe - "7"
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getBeneficiario().getNossoNumero() + boleto.getBoleto().getBeneficiario().getDigitoNossoNumero(), 15, ' ')); //Nosso número Sicredi - com 9 posições
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getNumeroDocumento(), 10, '0'));//Seu número com 10 posições - 31 - Este campo nunca pode se repetir (Diferente de branco) - normalmente usado neste campo o número da nota fiscal gerada para o sacado.
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getDocumento(), 14, "0"));//CPF/CNPJ do Pagador com 14 posições - Alinhado à direita e zeros à esquerda; Obs: No momento dos testes para homologação estes dados devem ser enviados com informações válidas.
                if (boleto.getBoleto().getBeneficiarioFinal() != null) {
                    linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getBeneficiarioFinal().getDocumento(), 14, "0"));//CPF/CNPJ do Sacador Avalista com 14 posições - Alinhado à direita e zeros à esquerda. Deixar em branco caso não exista Sacador Avalista. O Sacador Avalista deve ser diferente do beneficiário e pagador.
                } else {
                    linhaArquivo.append(StringUtils.repeat(" ", 14));//CPF/CNPJ do Sacador Avalista com 14 posições - Alinhado à direita e zeros à esquerda. Deixar em branco caso não exista Sacador Avalista. O Sacador Avalista deve ser diferente do beneficiário e pagador.
                }
                linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto2() == null ? "000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto2(), "ddMMyy")); //Data limite para desconto 2 DDMMYY
                linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos2(), 2, 13));//Valor ou % desconto 2
                linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto3() == null ? "000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto3(), "ddMMyy")); //Data limite para desconto 3 DDMMYY
                linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos3(), 2, 13));//Valor ou % desconto 3
                linhaArquivo.append(StringUtils.repeat(" ", 302));//Deixar em branco 302
                linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));//Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
                linhaArquivo.append((char) 13);
                linhaArquivo.append((char) 10);
                contador++;
            }

            //REGISTRO HÍBRIDO (Tipo 8)
            if (!StringUtils.isBlank(boleto.getBoleto().getPixTxidQrCode())) {
                linhaArquivo.append("8");//Identificação do registro detalhe - "8"
                linhaArquivo.append(StringUtils.rightPad(boleto.getBoleto().getBeneficiario().getNossoNumero() + boleto.getBoleto().getBeneficiario().getDigitoNossoNumero(), 15, ' ')); //Nosso número Sicredi - com 9 posições
                linhaArquivo.append(" ");//Deixar em branco
                linhaArquivo.append("H");//H
                linhaArquivo.append(StringUtils.repeat(" ", 12)); //Deixar em branco 12
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getNumeroDocumento(), 10, '0'));//Seu número com 10 posições - 31 - Este campo nunca pode se repetir (Diferente de branco) - normalmente usado neste campo o número da nota fiscal gerada para o sacado.
                linhaArquivo.append(StringUtils.rightPad(boleto.getBoleto().getPixTxidQrCode(), 35, ' '));//TXID (Código de Identificação do QR Code)
                linhaArquivo.append(StringUtils.repeat(" ", 319)); //Deixar em branco 319
                linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));//Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
                linhaArquivo.append((char) 13);
                linhaArquivo.append((char) 10);
                contador++;
            }
        }

        //REGISTRO TRAILER (Tipo 9)
        linhaArquivo.append("9");//Identificação do registro trailer
        linhaArquivo.append("1");//Identificação do arquivo remessa
        linhaArquivo.append("748");//Número do Sicredi
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 5, '0'));//Código do beneficiário com 5 posições - Conta Corrente sem o DV ou conta beneficiário.
        linhaArquivo.append(StringUtils.repeat(" ", 384));//Deixar em Branco (sem preenchimento) com 384 posições
        linhaArquivo.append(StringUtils.leftPad(contador.toString(), 6, "0"));//Número seqüencial do registro com 6 posições - Alinhado à direita e zeros à esquerda;
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        String arquivoRemessa = linhaArquivo.toString();
        StringBuilder sbRemessa = new StringBuilder();
        for (int i = 0; i < arquivoRemessa.length(); i++) {
            char ch = arquivoRemessa.charAt(i);
            if (SicrediUtil.isASCIISicredi(ch)) {
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
                    if (linha.substring(0, 1).equals("0")) { //Header
                        if (!linha.substring(1, 2).equals("2")) { //2 = RETORNO
                            throw new BoletoException("Formato do aquivo inválido.");
                        }
                        if (!linha.substring(76, 79).equals("748")) { //Número do Sicredi
                            throw new BoletoException("Número do Banco inválido.");
                        }
                        numeroRetorno = linha.substring(110, 117);
                    }
                    if (linha.substring(0, 1).equals("1")) { //Detalhe
                        RemessaRetornoModel remessaRetornoModel = new RemessaRetornoModel();
                        remessaRetornoModel.setBoleto(new BoletoModel());
                        remessaRetornoModel.getBoleto().setPagador(new Pagador());
                        remessaRetornoModel.getBoleto().setBeneficiario(new Beneficiario());

                        remessaRetornoModel.getBoleto().setCodRetorno(numeroRetorno);
                        remessaRetornoModel.getBoleto().getPagador().setCodigoNoBanco(linha.substring(14, 19));
                        remessaRetornoModel.getBoleto().getPagador().setCodigo(linha.substring(19, 24));
                        remessaRetornoModel.setBoletoDda(linha.substring(24, 25)); //1 - Boleto enviado a CIP/DDA ou 2 - Boleto normal
                        remessaRetornoModel.getBoleto().getBeneficiario().setNossoNumero(linha.substring(47, 56));
                        remessaRetornoModel.getBoleto().getBeneficiario().setDigitoNossoNumero(linha.substring(56, 57));
                        remessaRetornoModel.setOcorrencia(linha.substring(108, 110));
                        remessaRetornoModel.setDataOcorrencia(BoletoUtil.formataStringPadraoDDMMYYParaLocalDate(linha.substring(110, 116))); //Formato: DDMMAA
                        remessaRetornoModel.getBoleto().setNumeroDocumento(linha.substring(116, 126));
                        remessaRetornoModel.getBoleto().setDataVencimento(BoletoUtil.formataStringPadraoDDMMYYParaLocalDate(linha.substring(146, 152)));//Formato: DDMMAA
                        remessaRetornoModel.getBoleto().setValorBoleto(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(152, 165)));
                        remessaRetornoModel.setDespesaCobranca(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(175, 188)));
                        remessaRetornoModel.setDespesaCustasProtesto(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(188, 201)));
                        remessaRetornoModel.setAbatimentoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(227, 240)));
                        remessaRetornoModel.setDescontoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(240, 253)));
                        remessaRetornoModel.setValorEfetivamentePago(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(253, 266)));
                        remessaRetornoModel.setJuroDeMora(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(266, 279)));
                        remessaRetornoModel.setMulta(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(279, 292)));
                        remessaRetornoModel.setConfirmacaoProtesto(linha.substring(294, 295));//“A” – aceito “D” – desprezado
                        remessaRetornoModel.setMotivoOcorrencia(linha.substring(318, 328));//XXXXXXXXXX Cada dois dígitos “XX” correspondem a um motivo. Se “00”, não há motivo de ocorrência; Confira tabela no item 6.3.
                        remessaRetornoModel.setDataPrevisaoLancamento(BoletoUtil.formataStringPadraoYYYYMMDDParaLocalDate(linha.substring(328, 336))); //Formato: AAAAMMDD

                        list.add(remessaRetornoModel);
                    }
                    if (linha.substring(0, 1).equals("8")) { //Registro Híbrido
                        String nossoNumero = BoletoUtil.manterApenasNumeros(linha.substring(1, 16)).substring(0, 8);//somente nosso número sem o dígito
                        list.stream().filter(bol -> bol.getBoleto().getBeneficiario().getNossoNumero().equals(nossoNumero))
                                .findFirst().ifPresent(boleto -> {
                                    boleto.getBoleto().setPixTxidQrCode(linha.substring(20, 55));
                                    boleto.getBoleto().setPixUrlQrCode(linha.substring(56, 133));
                                    boleto.getBoleto().setPixCopiaCola(linha.substring(134, 390));
                                });
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
                "BoletoSicredi",
                this.getClass(),
                new HashMap<>());

        return byteRelatorio;
    }

    private void imprimirDesktop(BoletoModel boletoModel, boolean diretoImpressora,
                                 PrintService printService) throws Exception {
        try {
            HashMap<String, Object> parametros = getParametrosRelatorio();

            preparaValidaBoletoImpressao(boletoModel);

            JasperUtil.geraRelatorioDescktop(Arrays.asList(boletoModel), parametros, "BoletoSicredi", this.getClass(),
                    new HashMap<>(), diretoImpressora, printService);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage());
        }
    }

    private HashMap<String, Object> getParametrosRelatorio() throws IOException {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoSicredi.jpg");
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
        codigoBarras.append("7489");
        codigoBarras.append("X"); //Substituir pelo Digito verificador
        codigoBarras.append(SicrediUtil.fatorData(boletoModel.getDataVencimento()));
        codigoBarras.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

        StringBuilder campoLivre = new StringBuilder();
        campoLivre.append("11");
        campoLivre.append(boletoModel.getBeneficiario().getNossoNumero() + boletoModel.getBeneficiario().getDigitoNossoNumero());
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getPostoDaAgencia(), 2, "0"));
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getConta(), 5, '0'));
        campoLivre.append(BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorBoleto()) ? "10" : "00");

        Integer digitoCampoLivre = SicrediUtil.modulo11DvCampoLivre(campoLivre.toString());
        campoLivre.append(digitoCampoLivre);

        codigoBarras.append(campoLivre);

        Integer digitoVerificadorGeral = SicrediUtil.modulo11DvGeralSicredi(codigoBarras.toString().replace("X", ""));
        boletoModel.setCodigoBarras(codigoBarras.toString().replace("X", digitoVerificadorGeral.toString()));

        StringBuilder linhaParte1 = new StringBuilder();
        linhaParte1.append("7489");
        linhaParte1.append(campoLivre.toString().substring(0, 5));
        linhaParte1.append(SicrediUtil.modulo10Sicredi(linhaParte1.toString()));

        StringBuilder linhaParte2 = new StringBuilder();
        linhaParte2.append(campoLivre.toString().substring(5, 15));
        linhaParte2.append(SicrediUtil.modulo10Sicredi(linhaParte2.toString()));

        StringBuilder linhaParte3 = new StringBuilder();
        linhaParte3.append(campoLivre.substring(15, 25));
        linhaParte3.append(SicrediUtil.modulo10Sicredi(linhaParte3.toString()));

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

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualMulta())) {
            String instrucaoMulta = "APOS VENCIMENTO COBRAR MULTA DE "
                    + BoletoUtil.formatarCasasDecimais(boletoModel.getValorPercentualMulta(), 2) + "%";
            boletoModel.getInstrucoes().add(new InformacaoModel(instrucaoMulta));
        }

        if (BoletoUtil.isNotNullEMaiorQZero(boletoModel.getValorPercentualJuros())) {
            BigDecimal valorPercentualJuros = boletoModel.getValorPercentualJuros();
            BigDecimal valorMoraDia = boletoModel.getValorPercentualJuros();
            if (boletoModel.getTipoJuros().equals(TipoJurosEnum.PERCENTUAL_MENSAL)
                    && BoletoUtil.isNotNullEMaiorQZero(valorPercentualJuros)) {
                // Se informado percentual de juros mensal, deve dividir por 30 dias para se
                // obter o percentual diário
                valorPercentualJuros = valorPercentualJuros.divide(BigDecimal.valueOf(30), MathContext.DECIMAL32);
                valorMoraDia = boletoModel.getValorBoleto().multiply(valorPercentualJuros, MathContext.DECIMAL32);
                valorMoraDia = valorMoraDia.divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
            }

            String instrucaoJuros = "APOS VENCIMENTO COBRAR MORA DIARIA DE R$ "
                    + BoletoUtil.formatarCasasDecimais(valorMoraDia, 2);
            boletoModel.getInstrucoes().add(new InformacaoModel(instrucaoJuros));
        }

        validaDadosImpressao(boletoModel);
    }

    private void validaDadosImpressao(BoletoModel boleto) {
        ValidaUtils.validaBoletoModel(boleto,
                Arrays.asList("locaisDePagamento", "dataVencimento", "beneficiario.nomeBeneficiario",
                        "beneficiario.documento", "beneficiario.agencia", "beneficiario.postoDaAgencia",
                        "beneficiario.conta", "dataEmissao", "numeroDocumento", "especieDocumento", "aceite",
                        "beneficiario.nossoNumero", "beneficiario.digitoNossoNumero", "especieMoeda", "valorBoleto",
                        "pagador.nome", "pagador.documento", "pagador.endereco.logradouro", "pagador.endereco.cep",
                        "linhaDigitavel", "codigoBarras", "pagador.endereco.numero", "pagador.endereco.bairro",
                        "pagador.endereco.cidade", "pagador.endereco.uf"));
    }
}
