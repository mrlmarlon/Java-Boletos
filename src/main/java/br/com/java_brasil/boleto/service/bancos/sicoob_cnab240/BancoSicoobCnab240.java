package br.com.java_brasil.boleto.service.bancos.sicoob_cnab240;

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
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.*;

public class BancoSicoobCnab240 extends BoletoController {
    @Override
    public byte[] imprimirBoletoJasper(@NonNull BoletoModel boletoModel) {
        try {
            return imprimir(boletoModel);
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    @Override
    public void imprimirBoletoJasperDesktop(@NonNull BoletoModel boletoModel, boolean diretoImpressora, PrintService printService) {
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
        list.forEach(boleto -> ValidaUtils.validaBoletoModel(boleto.getBoleto(),
                this.getConfiguracao().camposObrigatoriosBoleto()));

        Integer contador = 0;
        Integer contadorLinhas = 0;
        BigDecimal valorTotalTitulos = BigDecimal.ZERO;
        StringBuilder linhaArquivo = new StringBuilder();

        //CABEÇALHO
        contadorLinhas++;
        linhaArquivo.append("756"); //Código do Sicoob na Compensação
        linhaArquivo.append("0000"); //Lote de Serviço "0000"
        linhaArquivo.append("0"); //Tipo de Registro "0"
        linhaArquivo.append(StringUtils.repeat(" ", 9)); //Uso Exclusivo FEBRABA/CNAB: Preencher com espaços em branco
        linhaArquivo.append(list.get(0).getBoleto().getBeneficiario().isClienteCpf() ? "1" : "2"); //Tipo de Inscrição da Empresa. 1 = CPF e 2 = CNPJ
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDocumento(), 14, "0")); //Número de inscrição da Empresa
        linhaArquivo.append(StringUtils.rightPad(" ", 20, " "));  //Código do Convênio no Sicoob: Preencher com espaços em branco
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getAgencia(), 5, "0")); //Prefixo da Cooperativa
        linhaArquivo.append(StringUtils.rightPad(list.get(0).getBoleto().getBeneficiario().getDigitoAgencia(), 1, " ")); //Dígito verificador da Cooperativa
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 12, "0"));//Número da Conta
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDigitoConta(), 1, "0"));//Dígito da conta
        linhaArquivo.append("0"); //Dígito verificador ag/conta
        linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(list.get(0).getBoleto().getBeneficiario().getNomeBeneficiario(),30), 30, " "));
        //Nome da Empresa
        linhaArquivo.append(StringUtils.rightPad("SICOOB", 30, " "));//Nome do Banco
        linhaArquivo.append(StringUtils.repeat(" ", 10));//Uso Exclusivo FEBRABA/CNAB: Preencher com espaços em branco
        linhaArquivo.append("1"); //Código Remessa / Retorno: "1"
        linhaArquivo.append(BoletoUtil.getDataFormato(LocalDate.now(), "ddMMyyyy")); //Data de Geração do Arquivo
        linhaArquivo.append(BoletoUtil.getHoraFormato(LocalTime.now(), "HHmmss")); //Hora de Geração do Arquivo
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getNumeroRemessa(), 6, "0")); //Número sequencial do Arquivo
        linhaArquivo.append("081");//Nº da Versão do Layout do Arquivo
        linhaArquivo.append("00000"); //Densidade de Gravação do Arquivo "00000"
        linhaArquivo.append(StringUtils.repeat(" ", 20));//Para uso resevado do Banco: Preencher com espaços em branco
        linhaArquivo.append(StringUtils.repeat(" ", 20));//Para uso resevado da Empresa: Preencher com espaços em branco
        linhaArquivo.append(StringUtils.repeat(" ", 29));//Uso excluisivo FEBRABAN/CNAB: Prencher com espaços em branco
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        //CABEÇALHO LOTE
        contadorLinhas++;
        linhaArquivo.append("756");
        linhaArquivo.append("0001"); //Número sequencial do Lote
        linhaArquivo.append("1"); //Tipo de Registro: "1"
        linhaArquivo.append("R"); //Tipo de Operação: "R"
        linhaArquivo.append("01"); //Tipo de Serviço: "01"
        linhaArquivo.append(StringUtils.repeat(" ", 2));
        linhaArquivo.append("040");
        linhaArquivo.append(" ");
        linhaArquivo.append(list.get(0).getBoleto().getBeneficiario().isClienteCpf() ? "1" : "2"); //Tipo de Inscrição da Empresa. 1 = CPF e 2 = CNPJ
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDocumento(), 15, "0")); //Número de inscrição da Empresa
        linhaArquivo.append(StringUtils.repeat(" ", 20)); //Código do Convênio no Sicoob: Preencher com espaços em branco
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getAgencia(), 5, "0")); //Prefixo da Cooperativa
        linhaArquivo.append(StringUtils.rightPad(list.get(0).getBoleto().getBeneficiario().getDigitoAgencia(), 1, " ")); //Dígito verificador da Cooperativa
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 12, "0"));//Número da Conta
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDigitoConta(), 1, "0"));//Dígito da conta
        linhaArquivo.append(" "); //Dígito verificador ag/conta
        linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(list.get(0).getBoleto().getBeneficiario().getNomeBeneficiario(),30), 30, " "));
        //Nome da Empresa
        linhaArquivo.append(StringUtils.repeat(" ", 40));
        linhaArquivo.append(StringUtils.repeat(" ", 40));
        linhaArquivo.append(StringUtils.leftPad(list.get(0).getNumeroRemessa(), 8, "0"));
        linhaArquivo.append(BoletoUtil.getDataFormato(LocalDate.now(), "ddMMyyyy"));
        linhaArquivo.append(StringUtils.repeat("0", 8));
        linhaArquivo.append(StringUtils.repeat(" ", 33));
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        //DETALHE COM REGISTRO (Tipo 1)
        for (RemessaRetornoModel boleto : list) {
            contador++;
            contadorLinhas++;
            //REGISTRO P
            linhaArquivo.append("756"); //Código do banco
            linhaArquivo.append("0001"); //Lote
            linhaArquivo.append("3"); //Tipo de Registro: "3"
            linhaArquivo.append(StringUtils.leftPad(contador.toString(), 5, "0"));//Nº Registro
            linhaArquivo.append("P"); //Segmento
            linhaArquivo.append(" "); //em branco 1
            linhaArquivo.append(StringUtils.leftPad(boleto.getInstrucao(), 2, "0"));// Código de Movimento Remessa
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getAgencia(), 5, "0")); //Prefixo da Cooperativa
            linhaArquivo.append(StringUtils.rightPad(list.get(0).getBoleto().getBeneficiario().getDigitoAgencia(), 1, " ")); //Dígito verificador da Cooperativa
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getConta(), 12, "0"));//Número da Conta
            linhaArquivo.append(StringUtils.leftPad(list.get(0).getBoleto().getBeneficiario().getDigitoConta(), 1, "0"));//Dígito da conta
            linhaArquivo.append(" "); //Dígito verificador ag/conta

            String nossoNumeroEnvio = StringUtils.leftPad(boleto.getBoleto().getBeneficiario().getNossoNumero(), 9, "0");
            nossoNumeroEnvio = nossoNumeroEnvio + boleto.getBoleto().getBeneficiario().getDigitoNossoNumero();
            nossoNumeroEnvio = nossoNumeroEnvio + StringUtils.leftPad(String.valueOf(boleto.getBoleto().getNumeroDaParcelaCarne()), 2, "0");
            nossoNumeroEnvio += "014";//01 = Simples com Registro - 4 = A4 sem envelopamento

            linhaArquivo.append(StringUtils.rightPad(nossoNumeroEnvio, 20, " ")); //Nosso Número
            linhaArquivo.append(boleto.getBoleto().getBeneficiario().getCarteira()); //Código da Carteira
            linhaArquivo.append("0");//Cadastramento: "0"
            linhaArquivo.append(" ");// Documento
            linhaArquivo.append(boleto.getPostagemTitulo()); //Identificação da Emissão do Boleto: (vide planilha "Contracapa" deste arquivo) '1'  =  Sicoob Emite '2'  =  Beneficiário Emite
            linhaArquivo.append(boleto.getImpressaoTitulo()); //Identificação da Distribuição do Boleto: (vide planilha "Contracapa" deste arquivo) '1'  =  Sicoob Distribui '2'  =  Beneficiário Distribui
            linhaArquivo.append(StringUtils.rightPad(boleto.getBoleto().getNumeroDocumento(), 15, " ")); //Número do Documento
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataVencimento(), "ddMMyyyy")); // Data de Vencimento
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorBoleto(), 2, 15)); //Valor do título com 13 posições - Alinhado à direita e zeros à esquerda;
            valorTotalTitulos = valorTotalTitulos.add(boleto.getBoleto().getValorBoleto());
            linhaArquivo.append("00000"); //Ag. Cobradora
            linhaArquivo.append(" "); //Digito da ag. Cobradora(preencher com espaço em branco)
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getEspecieDocumento(), 2, "0")); //Espécie do título
            linhaArquivo.append(boleto.getBoleto().isAceite() ? "A" : "N"); //Aceite
            linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataEmissao(), "ddMMyyyy"));//Data Emissão

            if (!boleto.getBoleto().getTipoJuros().equals(TipoJurosEnum.ISENTO)
                    && BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualJuros())) {
                linhaArquivo.append(boleto.getBoleto().getTipoJuros().equals(TipoJurosEnum.VALOR_DIA) ? "1" : "2");
                if (boleto.getBoleto().getDiasJuros() > 0) {
                    LocalDate dataParaInicioJuros = BoletoUtil.adicionarDiasData(boleto.getBoleto().getDataVencimento(), boleto.getBoleto().getDiasJuros());
                    linhaArquivo.append(BoletoUtil.getDataFormato(dataParaInicioJuros, "ddMMyyyy"));
                } else {
                    linhaArquivo.append(BoletoUtil.getDataFormato(boleto.getBoleto().getDataVencimento(), "ddMMyyyy"));
                }
                linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualJuros(), 2, 15)); //Valor/% de juros por dia de atraso com 13 posições - Preencher com valor (alinhados à direita com zeros à esquerda) ou preencher com zeros.
            } else {
                linhaArquivo.append("0");
                linhaArquivo.append(StringUtils.repeat("0", 8));
                linhaArquivo.append(StringUtils.repeat("0", 15));
            }

            if (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos())) {
                linhaArquivo.append(boleto.getBoleto().getTipoDesconto().equals(TipoDescontoEnum.VALOR_FIXO) ? "1" : "2");//Desconto
            } else {
                linhaArquivo.append("0");//Desconto
            }
            linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto() == null ? "00000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto(), "ddMMyyyy"));//Data Desconto
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos(), 2, 15));//Valor Desconto
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorIof(), 2, 15));//Valor IOF
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorDeducoes(), 2, 15));//Valor Abatimento
            linhaArquivo.append(StringUtils.repeat(" ", 25));//Identificação do Título
            linhaArquivo.append(boleto.getBoleto().isProtesto() ? "1" : "3");
            linhaArquivo.append(StringUtils.leftPad(String.valueOf(boleto.getBoleto().getDiasProtesto()), 2, '0'));// Número de dias p/protesto automático com 2 posições

            linhaArquivo.append("0"); //Código p/ Baixa/Devolução
            linhaArquivo.append(StringUtils.repeat(" ", 3));//Prazo p/ Baixa/Devolução
            linhaArquivo.append("09");//Código da Moeda
            linhaArquivo.append(StringUtils.repeat("0", 10));//Número do Contrato
            linhaArquivo.append(" ");
            linhaArquivo.append((char) 13);
            linhaArquivo.append((char) 10);
            //FIM REGISTRO P

            //REGISTRO Q
            contadorLinhas++;
            linhaArquivo.append("756"); //Código do banco
            linhaArquivo.append("0001"); //Lote
            linhaArquivo.append("3"); //Tipo de Registro: "3"
            contador++;
            linhaArquivo.append(StringUtils.leftPad(contador.toString(), 5, "0")); //Nº Registro
            linhaArquivo.append("Q"); //Segmento
            linhaArquivo.append(" ");
            linhaArquivo.append(StringUtils.leftPad(boleto.getInstrucao(), 2, "0"));// Código de Movimento Remessa
            linhaArquivo.append(boleto.getBoleto().getPagador().isClienteCpf() ? "1" : "2");//Tipo de inscrição pagador
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getDocumento(), 15, "0"));

            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getPagador().getNome(),40), 40, " ")); //Nome

            String enderecoCompleto = boleto.getBoleto().getPagador().getEndereco().getLogradouro();
            enderecoCompleto = enderecoCompleto + ", " + boleto.getBoleto().getPagador().getEndereco().getNumero();
            if (boleto.getBoleto().getPagador().getEndereco().getComplemento() != null) {
                enderecoCompleto = enderecoCompleto + " " + boleto.getBoleto().getPagador().getEndereco().getComplemento();
            }
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(enderecoCompleto,40), 40, " "));
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getPagador().getEndereco().getBairro(),15), 15, " "));
            linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getPagador().getEndereco().getCep(), 8, "0"));
            linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getPagador().getEndereco().getCidade(),15), 15, " "));
            linhaArquivo.append(boleto.getBoleto().getPagador().getEndereco().getUf());

            if (boleto.getBoleto().getBeneficiarioFinal() != null) {
                linhaArquivo.append(boleto.getBoleto().getBeneficiarioFinal().isClienteCpf() ? "1" : "2");//Sac. / Aval.
                linhaArquivo.append(StringUtils.leftPad(boleto.getBoleto().getBeneficiarioFinal().getDocumento(), 15, "0"));//CNPJ/CPF Avalista
                linhaArquivo.append(StringUtils.rightPad(BoletoUtil.limitarTamanhoString(boleto.getBoleto().getBeneficiarioFinal().getNome(),40), 40, " "));
                //Nome do Sacador/Avalista
            } else {
                linhaArquivo.append("0");//Sac. / Aval.
                linhaArquivo.append(StringUtils.repeat("0", 15));//CNPJ/CPF Avalista
                linhaArquivo.append(StringUtils.repeat(" ", 40));//Nome do Sacador/Avalista
            }
            linhaArquivo.append("000");//Cód. Bco. Corresp. na Compensação
            linhaArquivo.append(StringUtils.repeat(" ", 20));//Nosso Nº no Banco Correspondente
            linhaArquivo.append(StringUtils.repeat(" ", 8)); //Em Branco
            linhaArquivo.append((char) 13);
            linhaArquivo.append((char) 10);
            //FIM REGISTRO Q

            //REGISTRO R
            contadorLinhas++;
            linhaArquivo.append("756"); //Código do banco
            linhaArquivo.append("0001"); //Lote
            linhaArquivo.append("3"); //Tipo de Registro: "3"
            contador++;
            linhaArquivo.append(StringUtils.leftPad(contador.toString(), 5, "0")); //Nº Registro
            linhaArquivo.append("R"); //Segmento
            linhaArquivo.append(StringUtils.repeat(" ", 1));
            linhaArquivo.append(StringUtils.leftPad(boleto.getInstrucao(), 2, "0")); // Código de Movimento Remessa

            if (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos2())) {
                linhaArquivo.append(boleto.getBoleto().getTipoDesconto().equals(TipoDescontoEnum.VALOR_FIXO) ? "1" : "2");//Desconto
            } else {
                linhaArquivo.append("0");//Desconto
            }
            linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto2() == null ? "00000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto2(), "ddMMyyyy"));//Data Desconto
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos2(), 2, 15));//Valor Desconto

            if (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualDescontos3())) {
                linhaArquivo.append(boleto.getBoleto().getTipoDesconto().equals(TipoDescontoEnum.VALOR_FIXO) ? "1" : "2");//Desconto
            } else {
                linhaArquivo.append("0");//Desconto
            }
            linhaArquivo.append(boleto.getBoleto().getDataLimiteParaDesconto3() == null ? "00000000" : BoletoUtil.getDataFormato(boleto.getBoleto().getDataLimiteParaDesconto3(), "ddMMyyyy"));//Data Desconto
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualDescontos3(), 2, 15));//Valor Desconto

            if (BoletoUtil.isNotNullEMaiorQZero(boleto.getBoleto().getValorPercentualMulta())) {
                linhaArquivo.append(boleto.getBoleto().getTipoMulta().equals(TipoMultaEnum.VALOR) ? "1" : "2");
            } else {
                linhaArquivo.append("0");
            }

            if (boleto.getBoleto().getDiasMulta() > 0) {
                LocalDate dataParaInicioMulta = BoletoUtil.adicionarDiasData(boleto.getBoleto().getDataVencimento(), boleto.getBoleto().getDiasMulta());
                linhaArquivo.append(BoletoUtil.getDataFormato(dataParaInicioMulta, "ddMMyyyy"));
            } else {
                linhaArquivo.append(StringUtils.repeat("0", 8));
            }
            linhaArquivo.append(BoletoUtil.formatarValorSemPonto(boleto.getBoleto().getValorPercentualMulta(), 2, 15));//Valor Desconto

            linhaArquivo.append(StringUtils.repeat(" ", 10));
            linhaArquivo.append(StringUtils.repeat(" ", 40));
            linhaArquivo.append(StringUtils.repeat(" ", 40));
            linhaArquivo.append(StringUtils.repeat(" ", 20));

            linhaArquivo.append("00000000");//Data limite de pagamento
            linhaArquivo.append(StringUtils.repeat("0", 3));
            linhaArquivo.append(StringUtils.repeat("0", 5));
            linhaArquivo.append(" ");
            linhaArquivo.append(StringUtils.repeat("0", 12));
            linhaArquivo.append(" ");
            linhaArquivo.append(" ");
            linhaArquivo.append("0");
            linhaArquivo.append(StringUtils.repeat(" ", 9));
            linhaArquivo.append((char) 13);
            linhaArquivo.append((char) 10);
            //FIM REGISTRO R

                /*
                    Por enquando vamos deixar sem o S
                 */
            //REGISTRO S
//                sb.append("756"); //Código do banco
//                sb.append("0001"); //Lote
//                sb.append("3"); //Tipo de Registro: "3"
//                contador++;
//                sb.append(Util.putCaracter(contador.toString(), 5, "H", "0")); //Nº Registro
//                sb.append("R"); //Segmento
//                sb.append(Util.repete(" ", 1));
//                sb.append(Util.putCaracter(bm.getInstrucao(), 2, "H", "0")); // Código de Movimento Remessa
//                sb.append((char) 13);
//                sb.append((char) 10);
            //FIM REGISTRO S
        }

        //REGISTRO TRAILER LOTE
        linhaArquivo.append("756"); //Código do banco
        linhaArquivo.append("0001"); //Lote
        linhaArquivo.append("5"); //Tipo de Registro: "5"
        linhaArquivo.append(StringUtils.repeat(" ", 9));
        linhaArquivo.append(StringUtils.leftPad(contadorLinhas.toString(), 6, "0"));
        linhaArquivo.append(StringUtils.leftPad(String.valueOf(list.size()), 6, "0"));
        linhaArquivo.append(BoletoUtil.formatarValorSemPonto(valorTotalTitulos, 2, 17));
        linhaArquivo.append(StringUtils.repeat("0", 6));
        linhaArquivo.append(StringUtils.repeat("0", 17));
        linhaArquivo.append(StringUtils.repeat("0", 6));
        linhaArquivo.append(StringUtils.repeat("0", 17));
        linhaArquivo.append(StringUtils.repeat("0", 6));
        linhaArquivo.append(StringUtils.repeat("0", 17));
        linhaArquivo.append(StringUtils.repeat(" ", 8));
        linhaArquivo.append(StringUtils.repeat(" ", 117));
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        //REGISTRO TRAILER ARQUIVO
        contadorLinhas++;
        linhaArquivo.append("756");
        linhaArquivo.append("9999");
        linhaArquivo.append("9");
        linhaArquivo.append(StringUtils.repeat(" ", 9));
        linhaArquivo.append("000001");
        linhaArquivo.append(StringUtils.leftPad(contadorLinhas.toString(), 6, "0"));
        linhaArquivo.append("000000");
        linhaArquivo.append(StringUtils.repeat(" ", 205));
        linhaArquivo.append((char) 13);
        linhaArquivo.append((char) 10);

        String arquivoRemessa = linhaArquivo.toString();
        StringBuilder sbRemessa = new StringBuilder();
        for (int i = 0; i < arquivoRemessa.length(); i++) {
            char ch = arquivoRemessa.charAt(i);
            if (SicoobUtil.isASCIISicoob(ch)) {
                sbRemessa.append(ch);
            } else {
                sbRemessa.append((char) 32);
            }
        }

        return sbRemessa.toString();
    }

    private List<RemessaRetornoModel> importarArquivo(String arquivo) {
        List<RemessaRetornoModel> list = new ArrayList<>();
        try (Scanner scanner = new Scanner(arquivo)) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.length() > 1) {
                    String numeroRetorno = null;
                    if (linha.charAt(7) == '0') { //Header
                        if (linha.charAt(142) != '2') { //2 = RETORNO
                            throw new BoletoException("Formato do aquivo inválido.");
                        }
                        if (!linha.substring(0, 3).equals("756")) { //Número do Sicoob
                            throw new BoletoException("Número do Banco inválido.");
                        }
                        numeroRetorno = linha.substring(157, 163);
                    }
                    if (linha.charAt(7) == '3') { //Detalhe
                        String tipoRegistro = linha.substring(13, 14);
                        if (tipoRegistro.equals("T")) { //REGISTRO DETALHE SEGMENTO T
                            RemessaRetornoModel remessaRetornoModel = new RemessaRetornoModel();
                            remessaRetornoModel.setBoleto(new BoletoModel());
                            remessaRetornoModel.getBoleto().setPagador(new Pagador());
                            remessaRetornoModel.getBoleto().setBeneficiario(new Beneficiario());

                            remessaRetornoModel.getBoleto().setCodRetorno(numeroRetorno);
                            remessaRetornoModel.getBoleto().getPagador().setDocumento(linha.substring(133, 148));
                            remessaRetornoModel.getBoleto().getPagador().setNome(linha.substring(148, 188));
                            remessaRetornoModel.getBoleto().getBeneficiario().setNossoNumero(linha.substring(37, 46));
                            remessaRetornoModel.getBoleto().getBeneficiario().setDigitoNossoNumero(linha.substring(46, 47));
                            remessaRetornoModel.setOcorrencia(linha.substring(15, 17));
                            remessaRetornoModel.getBoleto().setNumeroDocumento(linha.substring(58, 73));
                            remessaRetornoModel.getBoleto().setDataVencimento(BoletoUtil.formataStringPadraoDDMMYYYYParaLocalDate(linha.substring(73, 81)));//Formato: DDMMAAAA
                            remessaRetornoModel.getBoleto().setValorBoleto(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(81, 96)));
                            remessaRetornoModel.setDespesaCobranca(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(198, 213)));
                            remessaRetornoModel.setMotivoOcorrencia(linha.substring(213, 223));
                            list.add(remessaRetornoModel);
                        } else if (tipoRegistro.equals("U")) { //REGISTRO DETALHE SEGMENTO U
                            //é referente ao registro anterior lançado na list (Ultimo registro)
                            list.get(list.size() - 1).setAbatimentoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(47, 62)));
                            String previsaoConta = linha.substring(145, 153);
                            if (!previsaoConta.replaceAll("\\D", "").equals("00000000")) {
                                list.get(list.size() - 1).setDataPrevisaoLancamento(BoletoUtil.formataStringPadraoDDMMYYYYParaLocalDate(previsaoConta));
                            }

                            list.get(list.size() - 1).setDataOcorrencia(BoletoUtil.formataStringPadraoDDMMYYYYParaLocalDate(linha.substring(137, 145)));//Formato: DDMMAAAA
                            list.get(list.size() - 1).setDescontoConcedido(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(32, 47)));
                            list.get(list.size() - 1).setDespesaCobranca(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(107, 122)));
                            list.get(list.size() - 1).setJuroDeMora(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(17, 32)));
                            list.get(list.size() - 1).setOcorrencia(linha.substring(15, 17));
                            list.get(list.size() - 1).setValorEfetivamentePago(BoletoUtil.stringSemPontoParaBigDecimal(linha.substring(92, 107)));
                        }
                    }
                }
            }
        }
        return list;
    }

    private byte[] imprimir(BoletoModel boletoModel) throws Exception {
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

        InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoSicoob.png");
        Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
        parametros.put("LogoBanco", image);

        preparaValidaBoletoImpressao(boletoModel);

        return JasperUtil.geraRelatorio(Arrays.asList(boletoModel),
                parametros,
                "BoletoSicoob",
                this.getClass(),
                new HashMap<>());
    }

    private void imprimirDesktop(BoletoModel boletoModel, boolean diretoImpressora,
                                 PrintService printService) throws Exception {
        try {
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));

            InputStream inputStream = this.getClass().getResourceAsStream("/logo/LogoSicoob.png");
            Image image = new ImageIcon(IOUtils.toByteArray(inputStream)).getImage();
            parametros.put("LogoBanco", image);

            preparaValidaBoletoImpressao(boletoModel);

            JasperUtil.geraRelatorioDescktop(Collections.singletonList(boletoModel), parametros, "BoletoSicoob", this.getClass(),
                    new HashMap<>(), diretoImpressora, printService);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BoletoException(e.getMessage());
        }
    }

    private void preparaValidaBoletoImpressao(BoletoModel boletoModel) {
        boletoModel.getBeneficiario().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiario().getDocumento()));
        boletoModel.getPagador().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getPagador().getDocumento()));
        if (boletoModel.getBeneficiarioFinal() != null) {
            boletoModel.getBeneficiarioFinal().setDocumento(BoletoUtil.formatarCnpjCpf(boletoModel.getBeneficiarioFinal().getDocumento()));
        }

        StringBuilder codigoBarras = new StringBuilder();
        codigoBarras.append("7569");
        codigoBarras.append("X"); //Substituir pelo Digito verificador
        codigoBarras.append(SicoobUtil.fatorData(boletoModel.getDataVencimento()));
        codigoBarras.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

        StringBuilder campoLivre = new StringBuilder();
        campoLivre.append("1");
        campoLivre.append(StringUtils.leftPad(boletoModel.getBeneficiario().getAgencia(), 4, "0"));
        campoLivre.append("01");
        campoLivre.append(boletoModel.getBeneficiario().getNumeroConvenio());
        campoLivre.append(boletoModel.getBeneficiario().getNossoNumero().substring(2));
        campoLivre.append(boletoModel.getBeneficiario().getDigitoNossoNumero());
        campoLivre.append("001");
        codigoBarras.append(campoLivre);

        Integer digitoVerificadorGeral = SicoobUtil.modulo11(codigoBarras.toString().replace("X", ""));
        boletoModel.setCodigoBarras(codigoBarras.toString().replace("X", digitoVerificadorGeral.toString()));

        StringBuilder linhaParte1 = new StringBuilder();
        linhaParte1.append("75691");
        linhaParte1.append(boletoModel.getBeneficiario().getAgencia());
        linhaParte1.append(SicoobUtil.modulo10(linhaParte1.toString()));

        StringBuilder linhaParte2 = new StringBuilder();
        linhaParte2.append("01");
        linhaParte2.append(boletoModel.getBeneficiario().getNumeroConvenio());
        linhaParte2.append(boletoModel.getBeneficiario().getNossoNumero().substring(2, 3));
        linhaParte2.append(SicoobUtil.modulo10(linhaParte2.toString()));

        StringBuilder linhaParte3 = new StringBuilder();
        linhaParte3.append(boletoModel.getBeneficiario().getNossoNumero().substring(3));
        linhaParte3.append(boletoModel.getBeneficiario().getDigitoNossoNumero());
        linhaParte3.append("001");
        linhaParte3.append(SicoobUtil.modulo10(linhaParte3.toString()));

        StringBuilder linhaParte4 = new StringBuilder();
        linhaParte4.append(digitoVerificadorGeral);

        StringBuilder linhaParte5 = new StringBuilder();
        linhaParte5.append(SicoobUtil.fatorData(boletoModel.getDataVencimento()));
        linhaParte5.append(BoletoUtil.formatarValorSemPonto(boletoModel.getValorBoleto(), 2, 10));

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
                Arrays.asList("locaisDePagamento",
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
                        "pagador.endereco.uf"));
    }
}
