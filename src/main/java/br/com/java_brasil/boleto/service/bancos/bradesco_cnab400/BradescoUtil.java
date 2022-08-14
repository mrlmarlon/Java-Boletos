package br.com.java_brasil.boleto.service.bancos.bradesco_cnab400;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class BradescoUtil {

    public static String fatorData(LocalDate dataVencimento) {
        LocalDate dataBase = LocalDate.of(1997, 10, 7);
        if (dataVencimento.isAfter(LocalDate.of(2025, 2, 21))) {
            dataBase = dataBase.plusDays(9000);
        }

        long difDia = ChronoUnit.DAYS.between(dataBase, dataVencimento);
        return StringUtils.leftPad("" + difDia, 4, '0');

    }

    public static String geraDigitoNossoNumero(String carteiraNossoNumero) {
        int s = 0, p = 2, cont = 13;

        for (int i = carteiraNossoNumero.length() - 1; i >= 0; i--) {
            s += Integer.parseInt(carteiraNossoNumero.substring(i, cont)) * p;
            if (p == 7) {
                p = 2;
            } else {
                p++;
            }
            cont--;
        }
        int r = (s % 11);
        switch (r) {
            case 0:
                return "0";
            case 1:
                return "P";
            default:
                return String.valueOf(11 - r);
        }
    }

    public static Integer modulo11(String codigo) {
        try {
            int total = 0;
            int peso = 2;

            for (int i = 0; i < codigo.length(); i++) {
                total += (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
                peso++;
                if (peso == 9) {
                    peso = 2;
                }
            }
            int resto = total % 11;
            int digito = 11 - resto;
            return (digito == 0 || digito == 1 || digito > 9) ? 1 : digito;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Integer modulo10(String codigo) {
        int total = 0;
        int peso = 2;

        for (int i = 0; i < codigo.length(); i++) {
            int valor = (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
            if (valor > 9) {
                valor = valor - 9;
            }
            total += valor;
            if (peso == 2) {
                peso--;
            } else {
                peso++;
            }
        }
        int resto;
        if (total < 10) {
            resto = total;
        } else {
            resto = total % 10;
        }
        int digito = resto == 0 ? 0 : (10 - resto);
        return digito;
    }

    public static Map<String, String> getMapOcorrencia() {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("02", "Entrada Confirmada");
            map.put("03", "Entrada Rejeitada");
            map.put("06", "Liquidação Normal");
            map.put("07", "Conf. Exc. Cadastro Pagador Débito");
            map.put("08", "Rej. Ped. Exc. Cadastro de Pagador Débito");
            map.put("09", "Baixado Automat. via Arquivo");
            map.put("10", "Baixado conforme instruções da Agência");
            map.put("11", "Em Ser - Arquivo de Títulos Pendentes");
            map.put("12", "Abatimento Concedido");
            map.put("13", "Abatimento Cancelado");
            map.put("14", "Vencimento Alterado");
            map.put("15", "Liquidação em Cartório");
            map.put("16", "Título Pago em Cheque - Vinculado");
            map.put("17", "Liquidação após Baixa ou Título não Registrado");
            map.put("18", "Acerto de Depositária");
            map.put("19", "Confirmação Receb. Inst. de Protesto");
            map.put("20", "Confirmação Recebimento Instrução Sustação de Protesto");
            map.put("21", "Acerto do Controle do Participante");
            map.put("22", "Título com Pagamento Cancelado");
            map.put("23", "Entrada do Título em Cartório");
            map.put("24", "Entrada Rejeitada por CEP Irregular");
            map.put("25", "Confirmação Receb.Inst.de Protesto Falimentar");
            map.put("27", "Baixa Rejeitada");
            map.put("28", "Débito de Tarifas/Custas");
            map.put("29", "Ocorrências do Pagador");
            map.put("30", "Alteração de Outros Dados Rejeitados");
            map.put("31", "Confirmado Inclusão Cadastro Pagador");
            map.put("32", "Instrução Rejeitada");
            map.put("33", "Confirmação Pedido Alteração Outros Dados");
            map.put("34", "Retirado de Cartório e Manutenção Carteira");
            map.put("35", "Desagendamento do Débito Automático");
            map.put("37", "Rejeitado Inclusão Cadastro Pagador");
            map.put("38", "Confirmado Alteração Pagador");
            map.put("39", "Rejeitado Alteração Cadastro Pagador");
            map.put("40", "Estorno de Pagamento");
            map.put("55", "Sustado Judicial");
            map.put("68", "Acerto dos Dados do Rateio de Crédito");
            map.put("69", "Cancelamento de Rateio");
            map.put("73", "Confirmação Receb. Pedido de Negativação");
            map.put("74", "Confir Pedido de Excl de Negat");

            return map;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static Map<String, String> getMapMotivoOcorrencia(String ocorrencia) {
        try {
            Map<String, String> map = new HashMap<>();
            switch (ocorrencia) {
                case "02":
                    map.put("00", "Ocorrência Aceita");
                    map.put("01", "Código do Banco Inválido");
                    map.put("02", "Pendente de Autorização (Autorização Débito Automático)");
                    map.put("03", "Pendente de Ação do Pagador (Autorização Débito Automático - Data Vencimento)");
                    map.put("04", "Código do Movimento não Permitido para a Carteira");
                    map.put("15", "Características da Cobrança Incompatíveis");
                    map.put("17", "Data de Vencimento Anterior à Data de Emissão");
                    map.put("21", "Espécie do Título Inválido");
                    map.put("24", "Data da Emissão Inválida");
                    map.put("27", "Valor/Taxa de Juros Mora Inválido");
                    map.put("38", "Prazo para Protesto/Negativação Inválido");
                    map.put("39", "Pedido para Protesto/Negativação não Permitido para o Título");
                    map.put("43", "Prazo para Baixa e Devolução Inválido");
                    map.put("45", "Nome do Pagador Inválido");
                    map.put("46", "Tipo/Num. de Inscrição do Pagador Inválidos");
                    map.put("47", "Endereço do Pagador não Informado");
                    map.put("48", "CEP Inválido");
                    map.put("50", "CEP referente a Banco Correspondente");
                    map.put("53", "Nº de Inscrição do Pagador/Avalista Inválidos (CPF/CNPJ)");
                    map.put("54", "Beneficiário Final não Informado");
                    map.put("67", "Débito Automático Agendado");
                    map.put("68", "Débito não Agendado - Erro nos Dados de Remessa");
                    map.put("69", "Débito não Agendado - Pagador não Consta no Cadastro de Autorizante");
                    map.put("70", "Débito não Agendado - Beneficiário não Autorizado pelo Pagador");
                    map.put("71", "Débito não Agendado - Beneficiário não Participa da Modalidade de Déb.Automático");
                    map.put("72", "Débito não Agendado - Código de Moeda Diferente de R$");
                    map.put("73", "Débito não Agendado - Data de Vencimento Inválida/Vencida");
                    map.put("75", "Débito não Agendado - Tipo do Número de Inscrição do Pagador Debitado Inválido");
                    map.put("76", "Pagador Eletrônico DDA - Esse motivo somente será disponibilizado no arquivo-retorno para as empresas cadastradas nessa condição");
                    map.put("86", "Seu Número do Documento Inválido");
                    map.put("87", "Título Baixado por Coobrigação e Devolvido para Carteira");
                    map.put("89", "Email Pagador não Enviado - Título com Débito Automático");
                    map.put("90", "Email Pagador não Enviado - Título de Cobrança sem Registro");
                    break;
                case "03":
                    map.put("00", "Ocorrência Aceita");
                    map.put("02", "Código do Registro Detalhe Inválido");
                    map.put("03", "Código da Ocorrência Inválida");
                    map.put("04", "Código de Ocorrência não Permitida para a Carteira");
                    map.put("05", "Código de Ocorrência não Numérico");
                    map.put("07", "Agência/Conta/Dígito Inválido");
                    map.put("08", "Nosso Número Inválido");
                    map.put("09", "Nosso Número Duplicado");
                    map.put("10", "Carteira Inválida");
                    map.put("13", "Identificação da Emissão do Bloqueto Inválida");
                    map.put("16", "Data de Vencimento Inválida");
                    map.put("18", "Vencimento fora do Prazo de Operação");
                    map.put("20", "Valor do Título Inválido");
                    map.put("21", "Espécie do Título Inválida");
                    map.put("22", "Espécie não Permitida para a Carteira");
                    map.put("23", "Tipo Pagamento não Contratado");
                    map.put("24", "Data de Emissão Inválida");
                    map.put("27", "Valor/Taxa de Juros Mora Inválido");
                    map.put("28", "Código do Desconto Inválido");
                    map.put("29", "Valor Desconto > ou = Valor Título");
                    map.put("32", "Valor do IOF Inválido");
                    map.put("34", "Valor do Abatimento Maior ou Igual ao Valor do Título");
                    map.put("38", "Prazo para Protesto/Negativação Inválido");
                    map.put("39", "Pedido de Protesto/Negativação não Permitida para o Título");
                    map.put("44", "Código da Moeda Inválido");
                    map.put("45", "Nome do Pagador não Informado");
                    map.put("46", "Tipo/Número de Inscrição do Pagador Inválidos");
                    map.put("47", "Endereço do Pagador não Informado");
                    map.put("48", "CEP Inválido");
                    map.put("49", "CEP sem Praça de Cobrança");
                    map.put("50", "CEP Irregular - Banco Correspondente");
                    map.put("53", "Tipo/Número de Inscrição do Beneficiário Final Inválido");
                    map.put("54", "Sacador/Avalista (Beneficiário Final) não Informado");
                    map.put("59", "Valor/Percentual da Multa Inválido");
                    map.put("63", "Entrada para Título já Cadastrado");
                    map.put("65", "Limite Excedido");
                    map.put("66", "Número Autorização Inexistente");
                    map.put("68", "Débito não Agendado - Erro nos Dados de Remessa");
                    map.put("69", "Débito não Agendado - Pagador não Consta no Cadastro de Autorizante");
                    map.put("70", "Débito não Agendado - Beneficiário não Autorizado pelo Pagador");
                    map.put("71", "Débito não Agendado - Beneficiário não Participa do Débito Automático");
                    map.put("72", "Débito não Agendado - Código de Moeda Diferente de R$");
                    map.put("73", "Débito não Agendado - Data de Vencimento Inválida/Cadastro Vencido");
                    map.put("74", "Débito não Agendado - Conforme seu Pedido, Título não Registrado");
                    map.put("75", "Débito não Agendado - Tipo de Número de Inscrição do Debitado Inválido");
                    map.put("79", "Data de Juros de Mora Inválida");
                    map.put("80", "Data do Desconto Inválida");
                    map.put("86", "Seu Número Inválido");
                    map.put("A3", "Benef. Final/ Sacador/Pagador Devem ser Iguais");
                    map.put("A6", "Esp. BDP/Depósito e Aporte, não Aceita Pgto Parcial");
                    break;
                case "06":
                    map.put("00", "Crédito Disponível");
                    map.put("15", "Crédito Indisponível");
                    map.put("18", "Pagamento Parcial");
                    map.put("42", "Rateio não Efetuado, Cód. Cálculo 2 (VLR. Registro)");
                    break;
                case "07":
                    map.put("A0", "Cadastro Excluído pelo Beneficiário");
                    map.put("A1", "Cadastro Excluído pelo Pagador");
                    break;
                case "08":
                    map.put("C0", "Informações do Tipo 6 Inválidas");
                    map.put("B9", "Cadastro Pagador não Localizado");
                    break;
                case "09":
                    map.put("00", "Ocorrência Aceita");
                    map.put("10", "Baixa Comandada pelo Cliente");
                    map.put("18", "Pagador não Aceitou o Débito (Autorização Débito Automático)");
                    map.put("19", "Pendente de Ação do Pagador (Autorização Débito Automático)");
                    break;
                case "10":
                    map.put("00", "Baixado Conforme Instruções da Agência");
                    map.put("14", "Título Protestado");
                    map.put("16", "Título Baixado pelo Banco por Decurso Prazo");
                    map.put("20", "Titulo Baixado e Transferido para Desconto");
                    break;
                case "15":
                    map.put("00", "Crédito Disponível");
                    map.put("15", "Crédito Indisponível");
                    break;
                case "17":
                    map.put("00", "Crédito Disponível");
                    map.put("15", "Crédito Indisponível");
                    break;
                case "24":
                    map.put("00", "Ocorrência Aceita");
                    map.put("48", "CEP Inválido");
                    map.put("49", "CEP sem Praça de Cobrança");
                    break;
                case "27":
                    map.put("00", "Ocorrência Aceita");
                    map.put("02", "Código do Registro Detalhe Inválido");
                    map.put("04", "Código de Ocorrência não Permitido para a Carteira");
                    map.put("07", "Agência/Conta/Dígito Inválidos");
                    map.put("08", "Nosso Número Inválido");
                    map.put("09", "Nosso Número Duplicado");
                    map.put("10", "Carteira Inválida");
                    map.put("15", "Carteira/Agência/Conta/Nosso Número Inválidos");
                    map.put("16", "Data Vencimento Inválida");
                    map.put("18", "Vencimento Fora do Prazo de Operação");
                    map.put("20", "Valor Título Inválido");
                    map.put("40", "Título com Ordem de Protesto Emitido");
                    map.put("42", "Código para Baixa/Devolução Inválido");
                    map.put("45", "Nome do Sacado não Informado ou Inválido");
                    map.put("46", "Tipo/Número de Inscrição do Sacado Inválido");
                    map.put("47", "Endereço do Sacado não Informado");
                    map.put("48", "CEP Inválido");
                    map.put("60", "Movimento para Título não Cadastrado");
                    map.put("77", "Transferência para Desconto não Permitido para a Carteira");
                    map.put("85", "Título com Pagamento Vinculado");
                    map.put("86", "Seu Número Inválido");
                    break;
                case "28":
                    map.put("02", "Tarifa de Permanência Título Cadastrado");
                    map.put("03", "Tarifa de Sustação/Excl Negativação");
                    map.put("04", "Tarifa de Protesto/Incl Negativação");
                    map.put("08", "Custas de Protesto");
                    map.put("12", "Tarifa de Registro");
                    map.put("13", "Tarifa Título Pago no Bradesco");
                    map.put("14", "Tarifa Título Pago Compensação");
                    map.put("15", "Tarifa Título Baixado não Pago");
                    map.put("16", "Tarifa Alteração de Vencimento");
                    map.put("17", "Tarifa Concessão Abatimento");
                    map.put("18", "Tarifa Cancelamento de Abatimento");
                    map.put("19", "Tarifa Concessão Desconto");
                    map.put("20", "Tarifa Cancelamento Desconto");
                    map.put("21", "Tarifa Título Pago CICS");
                    map.put("22", "Tarifa Título Pago Internet");
                    map.put("23", "Tarifa Título Pago Term. Gerencial Serviços");
                    map.put("24", "Tarifa Título Pago Pag-Contas");
                    map.put("25", "Tarifa Título Pago Fone Fácil");
                    map.put("26", "Tarifa Título Déb. Postagem");
                    map.put("28", "Tarifa Título Pago BDN");
                    map.put("29", "Tarifa Título Pago Term. Multi Função");
                    map.put("32", "Tarifa Título Pago PagFor");
                    map.put("33", "Tarifa Reg/Pgto - Guichê Caixa");
                    map.put("34", "Tarifa Título Pago Retaguarda");
                    map.put("35", "Tarifa Título Pago Subcentro");
                    map.put("36", "Tarifa Título Pago Cartão de Crédito");
                    map.put("37", "Tarifa Título Pago Comp Eletrônica");
                    map.put("38", "Tarifa Título Baix. Pg. Cartório");
                    map.put("39", "Tarifa Título Baixado Acerto Bco");
                    map.put("40", "Baixa Registro em Duplicidade");
                    map.put("41", "Tarifa Título Baixado Decurso Prazo");
                    map.put("42", "Tarifa Título Baixado Judicialmente");
                    map.put("43", "Tarifa Título Baixado via Remessa");
                    map.put("44", "Tarifa Título Baixado Rastreamento");
                    map.put("45", "Tarifa Título Baixado Conf. Pedido");
                    map.put("46", "Tarifa Título Baixado Protestado");
                    map.put("47", "Tarifa Título Baixado p/ Devolução");
                    map.put("48", "Tarifa Título Baixado Franco Pagto");
                    map.put("49", "Tarifa Título Baixado Sust/Ret/Cartório");
                    map.put("50", "Tarifa Título Baixado Sus/Sem/Rem/Cartório");
                    map.put("51", "Tarifa Título Transferido Desconto");
                    map.put("54", "Tarifa Baixa por Contabilidade");
                    map.put("55", "Tr. Tentativa Cons Déb Aut");
                    map.put("56", "Tr. Crédito On-Line");
                    map.put("57", "Tarifa Reg/Pagto Bradesco Expresso");
                    map.put("58", "Tarifa Emissão Papeleta");
                    map.put("78", "Tarifa Cadastro Cartela Instrução Permanente");
                    map.put("80", "Tarifa Parcial Pagamento Compensação");
                    map.put("81", "Tarifa Reapresentação Automática Título");
                    map.put("82", "Tarifa Registro Título Déb. Automático");
                    map.put("83", "Tarifa Rateio de Crédito");
                    map.put("89", "Tarifa Parcial Pagamento Bradesco");
                    map.put("96", "Tarifa Reg. Pagto Outras Mídias");
                    map.put("97", "Tarifa Reg/Pagto - Net Empresa");
                    map.put("98", "Tarifa Título Pago Vencido");
                    map.put("99", "Tr.Tít. Baixado por Decurso Prazo");
                    break;
                case "29":
                    map.put("78", "Pagador Alega que Faturamento é Indevido");
                    map.put("95", "Pagador Aceita/Reconhece Faturamento");
                    break;
                case "30":
                    map.put("00", "Ocorrência Aceita");
                    map.put("01", "Código do Banco Inválido");
                    map.put("04", "Código de Ocorrência não Permitido para a Carteira");
                    map.put("05", "Código da Ocorrência não Numérico");
                    map.put("08", "Nosso Número Inválido");
                    map.put("15", "Característica da Cobrança Incompatível");
                    map.put("16", "Data de Vencimento Inválido");
                    map.put("17", "Data de Vencimento Anterior à Data de Emissão");
                    map.put("18", "Vencimento Fora do Prazo de Operação");
                    map.put("20", "Valor Título Inválido");
                    map.put("21", "Espécie Título Inválida");
                    map.put("22", "Espécie não Permitida para a Carteira");
                    map.put("23", "Tipo Pagamento não Contratado");
                    map.put("24", "Data de Emissão Inválida");
                    map.put("26", "Código de Juros de Mora Inválido");
                    map.put("27", "Valor/Taxa de Juros de Mora Inválido");
                    map.put("28", "Código de Desconto Inválido");
                    map.put("29", "Valor do Desconto Maior/Igual ao Valor do Título");
                    map.put("30", "Desconto a Conceder não Confere");
                    map.put("31", "Concessão de Desconto já Existente (Desconto Anterior)");
                    map.put("32", "Valor do IOF Inválido");
                    map.put("33", "Valor do Abatimento Inválido");
                    map.put("34", "Valor do Abatimento Maior/Igual ao Valor do Título");
                    map.put("36", "Concessão Abatimento");
                    map.put("38", "Prazo para Protesto/ Negativação Inválido");
                    map.put("39", "Pedido para Protesto/ Negativação não Permitido para o Título");
                    map.put("40", "Título com Ordem/Pedido de Protesto/Negativação Emitido");
                    map.put("42", "Código para Baixa/Devolução Inválido");
                    map.put("43", "Prazo para Baixa/Devolução Inválido");
                    map.put("46", "Tipo/Número de Inscrição do Pagador Inválidos");
                    map.put("48", "CEP Inválido");
                    map.put("53", "Tipo/Número de Inscrição do Pagador/Avalista Inválidos");
                    map.put("54", "Pagador/Avalista não Informado");
                    map.put("57", "Código da Multa Inválido");
                    map.put("58", "Data da Multa Inválida");
                    map.put("60", "Movimento para Título não Cadastrado");
                    map.put("79", "Data de Juros de Mora Inválida");
                    map.put("80", "Data do Desconto Inválida");
                    map.put("85", "Título com Pagamento Vinculado");
                    map.put("88", "E-mail Pagador não Lido no Prazo 5 Dias");
                    map.put("91", "E-mail Pagador não Recebido");
                    map.put("C0", "Informações do Tipo 6 Inválidas");
                    map.put("C1", "Informações do Tipo 6 Divergentes do Cadastro");
                    break;
                case "32":
                    map.put("00", "Ocorrência Aceita");
                    map.put("01", "Código do Banco Inválido");
                    map.put("02", "Código Registro Detalhe Inválido");
                    map.put("04", "Código de Ocorrência não Permitido para a Carteira");
                    map.put("05", "Código de Ocorrência não Numérico");
                    map.put("06", "Espécie BDP, não Aceita Pagamento Parcial");
                    map.put("07", "Agência/Conta/Dígito Inválidos");
                    map.put("08", "Nosso Número Inválido");
                    map.put("10", "Carteira Inválida");
                    map.put("15", "Características da Cobrança Incompatíveis");
                    map.put("16", "Data de Vencimento Inválida");
                    map.put("17", "Data de Vencimento Anterior à Data de Emissão");
                    map.put("18", "Vencimento Fora do Prazo de Operação");
                    map.put("20", "Valor do Título Inválido");
                    map.put("21", "Espécie do Título Inválida");
                    map.put("22", "Espécie não Permitida para a Carteira");
                    map.put("23", "Tipo Pagamento não Contratado");
                    map.put("24", "Data de Emissão Inválida");
                    map.put("26", "Código Juros Mora Inválido");
                    map.put("27", "Valor/Taxa Juros Mira Inválido");
                    map.put("28", "Código de Desconto Inválido");
                    map.put("29", "Valor do Desconto Maior/Igual ao Valor do Título");
                    map.put("30", "Desconto a Conceder não Confere");
                    map.put("31", "Concessão de Desconto - Já Existe Desconto Anterior");
                    map.put("33", "Valor do Abatimento Inválido");
                    map.put("34", "Valor do Abatimento Maior/Igual ao Valor do Título");
                    map.put("36", "Concessão Abatimento - Já Existe Abatimento Anterior");
                    map.put("38", "Prazo para Protesto/Negativação Inválido");
                    map.put("39", "Pedido para Protesto/Negativação não Permitido para o Título");
                    map.put("40", "Título com Ordem/Pedido de Protesto/Negativação Emitido");
                    map.put("41", "Pedido de Sustação/Excl p/ Título sem Instrução de Protesto/Negativação");
                    map.put("45", "Nome do Pagador não Informado");
                    map.put("46", "Tipo/Número de Inscrição do Pagador Inválidos");
                    map.put("47", "Endereço do Pagador não Informado");
                    map.put("48", "CEP Inválido");
                    map.put("50", "CEP referente a um Banco Correspondente");
                    map.put("52", "Unidade da Federação Inválida");
                    map.put("53", "Tipo de Inscrição do Pagador Avalista Inválidos");
                    map.put("60", "Movimento para Título não Cadastrado");
                    map.put("65", "Limite Excedido");
                    map.put("66", "Número Autorização Inexistente");
                    map.put("85", "Título com Pagamento Vinculado");
                    map.put("86", "Seu Número Inválido");
                    map.put("94", "Título Cessão Fiduciária - Instrução Não Liberada pela Agência");
                    map.put("97", "Instrução não Permitida Título Negativado");
                    map.put("98", "Inclusão Bloqueada face à Determinação Judicial");
                    map.put("99", "Telefone Beneficiário não Informado / Inconsistente");
                    break;
                case "35":
                    map.put("81", "Tentativas Esgotadas, Baixado");
                    map.put("82", "Tentativas Esgotadas, Pendente");
                    map.put("83", "Cancelado pelo Pagador e Mantido Pendente, Conforme Negociação");
                    map.put("84", "Cancelado pelo Pagador e Baixado, Conforme Negociação");
                    break;
                case "37":
                case "39":
                    map.put("C0", "Informações do Tipo 6 Inválidas");
                    map.put("C1", "Informações do Tipo 6 Divergentes do Cadastro");
                    break;
            }
            return map;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static boolean isASCIIBradesco(char ch) {
        //Número de 0 a 9
        if (ch == 48
                || ch == 49
                || ch == 50
                || ch == 51
                || ch == 52
                || ch == 53
                || ch == 54
                || ch == 55
                || ch == 56
                || ch == 57) {
            return true;
            // Espaço, !, #, $, %, &, (, ), *, +, -, ., /, :, ;, @, [, \, ], {, }
        } else if (ch == 32
                || ch == 33
                || ch == 35
                || ch == 36
                || ch == 37
                || ch == 38
                || ch == 40
                || ch == 41
                || ch == 42
                || ch == 43
                || ch == 45
                || ch == 46
                || ch == 47
                || ch == 58
                || ch == 59
                || ch == 64
                || ch == 91
                || ch == 92
                || ch == 93
                || ch == 123
                || ch == 125) {
            return true;
            //A a Z
        } else if (ch >= 65
                && ch <= 90) {
            return true;
            //a a z
        } else if (ch >= 97
                && ch <= 122) {
            return true;
        } else {
            return ch == 10 || ch == 13;
        }
    }
}
