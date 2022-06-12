package br.com.java_brasil.boleto.service.bancos.sicoob_cnab240;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class SicoobUtil {

    /**
     * <p>Retorna o Fator Vencimento com base na data informada.</p>
     *
     * @param dataVencimento  LocalDate contendo a data de vencimento
     * @return String com 4 dígitos contendo o fator para data informada
     */
    public static String fatorData(LocalDate dataVencimento) {
        LocalDate dataBase = LocalDate.of(2000, 7, 3);
        if (dataVencimento.isAfter(LocalDate.of(2025, 2, 21))) {
            dataBase = LocalDate.of(2025, 2, 22);
        }

        long difDia = ChronoUnit.DAYS.between(dataBase, dataVencimento);
        difDia = difDia + 1000;
        return StringUtils.leftPad("" + difDia, 4, '0');

    }

    /**
     * <p>Cálculo Dígito Verificador módulo 11 Sicoob.</p>
     *
     * @param codigo  String contendo o código
     * @return Integer contendo o dígito verificador
     */
    public static Integer modulo11(String codigo) {
        try {
            int total = 0;
            int peso = 2;

            for (int i = 0; i < codigo.length(); i++) {
                total += (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
                peso++;
                if (peso == 10) {
                    peso = 2;
                }
            }
            int resto = total % 11;
            int digito = 11 - resto;
            return (digito == 0 || digito > 9) ? 1 : digito;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * <p>Cálculo Dígito Verificador módulo 10 Sicoob.</p>
     *
     * @param codigo  String contendo o código
     * @return Integer contendo o dígito verificador
     */
    public static Integer modulo10(String codigo) {
        int total = 0;
        int peso = 2;

        for (int i = 0; i < codigo.length(); i++) {
            int valor = (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
            valor = ajustaPeso(valor);
            total += valor;
            if (peso == 2) {
                peso--;
            } else {
                peso++;
            }
        }
        int resto = total % 10;
        int dezena = total + (10 - resto);

        int digito = dezena - total;
        if (digito <= 1 || digito > 9) {
            digito = 1;
        }
        return digito;
    }

    private static int ajustaPeso(int valor) {
        if (valor <= 9) {
            return valor;
        }
        char[] digitos = String.valueOf(valor).toCharArray();
        int novoValor = 0;
        for (char d : digitos) {
            novoValor = novoValor + Character.getNumericValue(d);
        }
        if (novoValor > 9) {
            return ajustaPeso(novoValor);
        }
        return novoValor;
    }

    /**
     * <p>Gera o Dígito do Nosso Número.</p>
     *
     * @param codigoAgencia String com o código da Agência do Beneficiário
     * @param codigoBeneficiario String com o código do Beneficiário
     * @param nossoNumero String contendo o Nosso Número (Sequencial de controle do emitente)
     * @return int com o Dígito do Nosso Número
     */
    public static int geraDigitoNossoNumero(String codigoAgencia,
                                         String codigoBeneficiario,
                                         String nossoNumero)  {
            String digitoVerificador
                    = StringUtils.leftPad(codigoAgencia, 4, "0")
                    + StringUtils.leftPad(codigoBeneficiario, 10,  "0")
                    + StringUtils.leftPad(nossoNumero, 7,"0");

            return digitoVerificadorNossoNumero(digitoVerificador);
    }

    /**
     * <p>Cálculo Dígito Verificador Nosso Número Sicoob.</p>
     *
     * @param codigo  String contendo o código
     * @return Integer contendo o dígito verificador
     */
    public static Integer digitoVerificadorNossoNumero(String codigo)  {
        try {
            int total = 0;
            int peso = 3;

            for (int i = 0; i < codigo.length(); i++) {
                total += (codigo.charAt(i) - '0') * peso;
                switch (peso) {
                    case 3:
                        peso = 1;
                        break;
                    case 1:
                        peso = 9;
                        break;
                    case 9:
                        peso = 7;
                        break;
                    case 7:
                        peso = 3;
                        break;
                    default:
                        break;
                }
            }
            int resto = total % 11;
            int digito = 11 - resto;
            return (digito == 0 || digito > 9) ? 0 : digito;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * <p>Retorna um MAP com as possíveis Ocorrências.</p>
     *
     * @return MAP com as Ocorrências para o banco Sicoob
     */
    public static Map<String, String> getMapOcorrencia() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("02", "Entrada confirmada");
            map.put("03", "Entrada rejeitada");
            map.put("04", "Transferência de Carteira/Entrada");
            map.put("05", "Transferência de Carteira/Baixa");
            map.put("06", "Liquidação");
            map.put("07", "Confirmação do Recebimento da Instrução de Desconto");
            map.put("08", "Confirmação do Recebimento do Cancelamento do Desconto");
            map.put("09", "Baixa");
            map.put("11", "Títulos em Carteira (Em Ser)");
            map.put("12", "Confirmação Recebimento Instrução de Abatimento");
            map.put("13", "Confirmação Recebimento Instrução de Cancelamento Abatimento");
            map.put("14", "Confirmação Recebimento Instrução Alteração de Vencimento");
            map.put("15", "Franco de Pagamento");
            map.put("17", "Liquidação Após Baixa ou Liquidação Título Não Registrado");
            map.put("19", "Confirmação Recebimento Instrução de Protesto");
            map.put("20", "Confirmação Recebimento Instrução de Sustação/Cancelamento de Protesto");
            map.put("23", "Remessa a Cartório (Aponte em Cartório)");
            map.put("24", "Retirada de Cartório e Manutenção em Carteira");
            map.put("25", "Protestado e Baixado (Baixa por Ter Sido Protestado)");
            map.put("26", "Instrução Rejeitada");
            map.put("27", "Confirmação do Pedido de Alteração de Outros Dados");
            map.put("28", "Débito de Tarifas/Custas");
            map.put("29", "Ocorrências do Pagador");
            map.put("30", "Alteração de Dados Rejeitada");
            map.put("33", "Confirmação da Alteração dos Dados do Rateio de Crédito");
            map.put("34", "Confirmação do Cancelamento dos Dados do Rateio de Crédito");
            map.put("35", "Confirmação do Desagendamento do Débito Automático");
            map.put("36", "Confirmação de envio de e-mail/SMS");
            map.put("37", "Envio de e-mail/SMS rejeitado");
            map.put("38", "Confirmação de alteração do Prazo Limite de Recebimento (a data deve ser\"");
            map.put("39", "Confirmação de Dispensa de Prazo Limite de Recebimento");
            map.put("40", "Confirmação da alteração do número do título dado pelo Beneficiário");
            map.put("41", "Confirmação da alteração do número controle do Participante");
            map.put("42", "Confirmação da alteração dos dados do Pagador");
            map.put("43", "Confirmação da alteração dos dados do Pagadorr/Avalista");
            map.put("44", "Título pago com cheque devolvido");
            map.put("45", "Título pago com cheque compensado");
            map.put("46", "Instrução para cancelar protesto confirmada");
            map.put("47", "Instrução para protesto para fins falimentares confirmada");
            map.put("48", "Confirmação de instrução de transferência de carteira/modalidade de cobrança");
            map.put("49", "Alteração de contrato de cobrança");
            map.put("50", "Título pago com cheque pendente de liquidação");
            map.put("51", "Título DDA reconhecido pelo Pagador");
            map.put("52", "Título DDA não reconhecido pelo Pagador");
            map.put("53", "Título DDA recusado pela CIP");
            map.put("54", "Confirmação da Instrução de Baixa/Cancelamento de Título Negativado sem Protesto");
            map.put("55", "Confirmação de Pedido de Dispensa de Multa");
            map.put("56", "Confirmação do Pedido de Cobrança de Multa");
            map.put("57", "Confirmação do Pedido de Alteração de Cobrança de Juros");
            map.put("58", "Confirmação do Pedido de Alteração do Valor/Data de Desconto");
            map.put("59", "Confirmação do Pedido de Alteração do Beneficiário do Título");
            map.put("60", "Confirmação do Pedido de Dispensa de Juros de Mora");
            map.put("80", "Confirmação da instrução de negativação");
            map.put("85", "Confirmação de Desistência de Protesto");
            map.put("86", "Confirmação de cancelamento do Protesto");
            return map;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * <p>Retorna a Descrição da Ocorrência com base no código.</p>
     *
     * @param ocorrencia  String com dois dígitos para ocorrência
     * @return Descrição da Ocorrência com base no código passado,
     *  "Ocorrência não catalogada" caso não localizada
     */
    public static String getOcorrencia(String ocorrencia) {
        try {
            Map<String, String> map = getMapOcorrencia();
            return map.get(ocorrencia) == null ? "Ocorrência não catalogada" : map.get(ocorrencia);
        } catch (Exception e) {
            return "Error - Ocorrência não catalogada";
        }
    }

    /**
     * <p>Retorna um MAP com os possíveis Motivos Ocorrência com base na Ocorrência.</p>
     *
     * @param ocorrencia  String com dois dígitos para ocorrência
     * @return Map<String, String> com os possíveis Motivos Ocorrência
     */
    public static Map<String, String> getMapMotivoOcorrencia(String ocorrencia) {
        try {
            Map<String, String> map = new HashMap<>();
            if (ocorrencia.equals("02")
                    || ocorrencia.equals("03")
                    || ocorrencia.equals("26")
                    || ocorrencia.equals("30")) {
                map.put("39", "Pedido de Protesto/Negativação Não Permitido para o Título");
                map.put("40", "Título com Ordem de Protesto/Negativação Emitida");
                map.put("41", "Pedido de Cancelamento/Sustação para Títulos sem Instrução de Negativação/Protesto");
                map.put("58", "Data da Multa Inválida");
                map.put("79", "Data Juros de Mora Inválido");
            } else if (ocorrencia.equals("28")) {
                map.put("03", "Tarifa de Desistência");
                map.put("04", "Tarifa de protesto");
                map.put("08", "Custas de protesto");
                map.put("11", "Forma de Cadastramento do Título Inválido");
                map.put("21", "Tarifa de Gravação Eletrônica = CRA");
            } else {
                map.put("AA", "Controle inválido");
                map.put("AB", "Tipo de operação inválido");
                map.put("AC", "Tipo de seriço inválido");
                map.put("AD", "Forma de lançamento inválida");
                map.put("AE", "Tipo/Número de inscrição inválido");
                map.put("AF", "Código de convênio inválido");
                map.put("AG", "Agência/conta/dígito inválidos");
                map.put("AH", "Nosso número seguencial do registro no lote inválido");
                map.put("AI", "Código de segmento de detalhe inválido");
                map.put("AJ", "Tipo de movimento inválido");
                map.put("AK", "Código da camâra de compensação do banco favorecido inválido");
                map.put("AL", "Código do banco favorecido, instituição de pagamento ou depositário inválido");
                map.put("AM", "Agência mantenedora da conta corrente do favorecido inválida");
                map.put("AN", "Conta corrente/DV/Conta de pagamento do favorecido inválido");
                map.put("AO", "Nome do favorecido não informado");
                map.put("AP", "Data lançamento inválido");
                map.put("AQ", "Tipo/Quantidade da moeda inválido");
                map.put("AR", "Valor do lançamento inválido");
                map.put("AS", "Aviso ao favorecido - identificação inválida");
                map.put("AT", "Tipo/Número de incrição do favorecido inválido");
                map.put("AU", "Logradouro do favorecido não informado");
                map.put("AV", "Nº do local do favorecido não informado");
                map.put("AW", "Cidade do favorecido não informada");
                map.put("AX", "CEP/Complemento do favorecido inválido");
                map.put("AY", "Sigla do estado do favorecido inválida");
                map.put("AZ", "Código/Nome do banco depositário inválido");
                map.put("BA", "Código/Nome agência depositária não inforamdo");
                map.put("BB", "Seu número inválido");
                map.put("BC", "Nosso número inválido");
                map.put("BD", "Inclusão efetuada com sucesso");
                map.put("BE", "Alteração efetuada com sucesso");
                map.put("BF", "Exclusão efetuada com sucesso");
                map.put("BG", "Agência/Conta impedida legalmente/bloqueada");
                map.put("BH", "Empresa não pagou salário");
                map.put("BI", "Falecimento do mutuário");
                map.put("BJ", "Empresa não envio remessa do mutuário");
                map.put("BK", "Empresa não enviou remessa no vencimento");
                map.put("BL", "Valor da parcela inválida");
                map.put("BM", "Identificação do contrato inválida");
                map.put("BN", "Operação de consignação incluida com sucesso");
                map.put("BO", "Operação de consignação alterada com sucesso");
                map.put("BP", "Operação de consignação excluida com sucesso");
                map.put("BQ", "Operação de consignação liquidada com sucesso");
                map.put("BR", "Reativação efetuada com sucesso");
                map.put("BS", "Suspenção efetuada com sucesso");
                map.put("CA", "Código de barras - Código de banco inválido");
                map.put("CB", "Código de barras - Código da moeda inválido");
                map.put("CC", "Código de barras - Dígito verificador geral inválido");
                map.put("CD", "Código de barras - Valor do título inválido");
                map.put("CE", "Código de barras - Campo livre inválido");
                map.put("CF", "Valor do documento inválido");
                map.put("CG", "Valor do abatimento inválido");
                map.put("CH", "Valor do desconto inválido");
                map.put("CI", "Valor de mora inválido");
                map.put("CJ", "Valor da multa inválido");
                map.put("CK", "Valor do IR inválido");
                map.put("CL", "Valor do ISS inválido");
                map.put("CM", "Valor do IOF inválido");
                map.put("CN", "Valor de outras deduções inválido");
                map.put("CO", "Valor de outros acrescimos inválido");
                map.put("CP", "Valor do INSS inválido");
                map.put("HA", "Lote não aceito");
                map.put("HB", "Inscrição da empresa inválido para o contrato");
                map.put("HC", "Convénio com a empresa inexistente/inválido para o contrato");
                map.put("HD", "Agência/Conta corrente da empresa inexistente/inválido para o contrato");
                map.put("HE", "Tipo de serviço inválido para o contrato");
                map.put("HF", "Conta corrente da empresa com saldo insuficiente");
                map.put("HG", "Lote de serviço fora de seguencia");
                map.put("HH", "Lote de serviço inválido");
                map.put("HI", "Arquivo não aceito");
                map.put("HJ", "Tipo de registo inválido");
                map.put("HK", "Código remessa/retorno inválido");
                map.put("HL", "Versão de Layout inválida");
                map.put("HM", "Mutuário não identificado");
                map.put("HN", "Tipo do benefício não permitido emprestimo");
                map.put("HO", "Benefício cessado/suspenso");
                map.put("HP", "Benefício possui representante legal");
                map.put("HQ", "Benefício é do tipo PA(Pensão alimenticia)");
                map.put("HR", "Quantidade de contratos permitida exedida");
                map.put("HS", "Benefício não pertence ao banco informado");
                map.put("HT", "Inicio do desconto informado já ultrapasado");
                map.put("HU", "Número da parcela inválida");
                map.put("HV", "Quantidade de parcela inválida");
                map.put("HW", "Marggem consignavel excedida para o mutuário dentro do prazo do contrato");
                map.put("HX", "Empréstimo já cadastrado");
                map.put("HY", "Empréstimo inexistente");
                map.put("HZ", "Empréstimo já encerrado");
                map.put("H1", "Arquivo sem Traler");
                map.put("H2", "Mutuário sem crédito na competencia");
                map.put("H3", "Não descontato - Outros motivos");
                map.put("H4", "Retorno de crédito não pago");
                map.put("H5", "Cancelamento de empréstimo retroativo");
                map.put("H6", "Outros motivos de Glosa");
                map.put("H7", "Margem consignavel exedida para o mutuário acima do prazo de contrato");
                map.put("H8", "Mutuário desligado do empregador");
                map.put("H9", "Mutuário afastado por licença");
                map.put("IA", "Primeiro nome do mutuário diferente do primento nome do movimento do censo ou diferente da base de titular do beneficio");
                map.put("IB", "Benefício suspenso/cessado pela APS ou SISOBI");
                map.put("IC", "Benefício suspenso por dependencia de calculo");
                map.put("ID", "Benefício suspenso/cessado pela inspetoria/auditoria");
                map.put("IE", "Benefício bloqueado para emprestimo pelo beneficiario");
                map.put("IF", "Benefício bloqueado para emprestimo por TBM");
                map.put("IG", "Benefício esta em fase de concessão de PA ou desdobramento");
                map.put("IH", "Benefício sessado por óbito");
                map.put("II", "Benefício sessado por fraude");
                map.put("IJ", "Benefício sessado por concessão de outro benefício");
                map.put("IK", "Benefício sessado: estatutário transferido para órgão de origem");
                map.put("IL", "Emprestimo suspenso pela APS");
                map.put("IM", "Empréstimo cancelado pelo banco");
                map.put("IN", "Crédito transformado em PAB");
                map.put("IO", "Término da consignação foi alterado");
                map.put("IP", "Fim do empréstimo ocorreu durante perído de suspensão ou concessão");
                map.put("IQ", "Empréstimo suspenso pelo banco");
                map.put("IR", "Não haverbação de contrato - Quantidade de parcelas/competencias informadas ultrapassou a data limite da extinsão de cota do dependente título de beneficions ");
                map.put("TA", "Lote não aceito - Totais do lote com diferença");
                map.put("YA", "Título não encontrato");
                map.put("YB", "Identificador registro opcional inválido");
                map.put("YC", "Código padrão inválido");
                map.put("YD", "Código de ocorrencia inválido");
                map.put("YE", "Complemento de ocorrencia inválido");
                map.put("YF", "Alegação já informada");
                map.put("ZA", "Agência/Conta do favorecido substituida");
                map.put("ZB", "Divergencia entre o primeiro e último do beneficiario versus primento e último nome na receita federal");
                map.put("ZC", "Confirmação de antecipação de valor");
                map.put("ZD", "Antecipação parcial de valor");
                map.put("ZE", "Título bloqueado na base");
                map.put("ZF", "Sistema em contingência - título valor maior que referencia");
                map.put("ZG", "Sistema em contingência - título vencido");
                map.put("ZH", "Sistema em contigência - título indexado");
                map.put("ZI", "Beneficiario divergente");
                map.put("ZJ", "Limite de pagamento parciais exedido");
                map.put("ZK", "Boleto já liquidado");
                map.put("PA", "PIX não efetivado");
                map.put("PB", "Transação interrompida devido a erro no PSP do recebedor");
                map.put("PC", "Número da conta transacional encerrada no PSP do recebedor");
                map.put("PD", "Tipo incorreto para a conta transacional especificada");
                map.put("PE", "Tipo de transação não é suportado-autorizado na conta transacional especificada");
                map.put("PF", "CPF/CNPJ do usuário recebedor não é consistente com o título da conta transacional especificada");
                map.put("PG", "CPF/CNPJ do usuári recebedor incorreta");
                map.put("PH", "Ordem rejeitada pelo PSP do recebedor");
                map.put("PI", "ISPB do PSP do pagador inválido ou inexistente");
                map.put("PJ", "Chave não cadastrada no DICT");
                map.put("PK", "QR-CODE inválido/vencido");
                map.put("PL", "Forma de iniciação inválida");
                map.put("PM", "Chave de pagamento inválida");
                map.put("PN", "Chave de pagamento não informada");
            }

            return map;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * <p>Retorna a Descrição do Motivo Ocorrência com base no código.</p>
     *
     * @param ocorrencia  String com dois dígitos para Ocorrência
     * @param motivoOcorrencia String com dois dígitos para Motivo Ocorrência
     * @return Descrição da Ocorrência com base no código passado,
     * 	 * "Motivo Ocorrência não catalogada" caso não localizada
     */
    public static String getMotivoOcorrenciaSicredi(String ocorrencia, String motivoOcorrencia) {
        try {
            Map<String, String> map = getMapMotivoOcorrencia(ocorrencia);
            return map.get(motivoOcorrencia) == null ? "Motivo Ocorrência não catalogada" : map.get(motivoOcorrencia);
        } catch (Exception e) {
            return "Error - Motivo Ocorrência não catalogada";
        }
    }

    /**
     * <p>Verifica se o caracter informado é valido para o Banco Sicoob.</p>
     *
     * @param ch  char
     * @return Verdadeiro se o Banco aceita o caracter ou false se não
     */
    public static boolean isASCIISicoob(char ch) {
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
            // Espaço, !, #, $, %, &, (, ), *, +, -, ., /, :, ;, @, [, \, ], {, },
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
        } else return ch == 10 || ch == 13;
    }
}
