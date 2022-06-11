package br.com.java_brasil.boleto.service.bancos.sicredi_cnab400;

import br.com.java_brasil.boleto.util.BoletoUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class SicrediUtil {

	/**
	 * <p>Gera o Nosso Número.</p>
	 *
	 * @param serie String contendo a série
	 * @param nossoNumero String contendo o Nosso Número (Sequencial de controle do emitente)
	 * @param dataBoleto LocalDate data de emissão do boleto
	 * @return String com o Nosso Número
	 */
	public static String gerarNossoNumero(String serie,
            String nossoNumero,
            LocalDate dataBoleto) {
        try {
        	StringBuilder sb = new StringBuilder();
        	sb.append(BoletoUtil.getDataFormato(dataBoleto, "yy"));
            sb.append(StringUtils.leftPad(serie, 1, "0"));
            sb.append(StringUtils.leftPad(nossoNumero, 5, "0"));
        	
            return sb.toString();
        } catch (Exception e) {
            throw e;
        }
    }

	/**
	 * <p>Gera o Digito do Nosso Número.</p>
	 *
	 * @param serie String contendo a série
	 * @param nossoNumero String contendo o Nosso Número (Sequencial de controle do emitente)
	 * @param dataBoleto LocalDate data de emissão do boleto
	 * @param codigoAgencia String com o código da Agência do Beneficiário
	 * @param codigoConta String com o código da Conta do Beneficiário
	 * @param postoDaAgencia String com o Número do Posto da Agência do Beneficiário
	 * @return int com o Dígito do Nosso Número
	 */
	public static int gerarDigitoNossoNumero(String serie,
										  String nossoNumero,
										  LocalDate dataBoleto,
										  String codigoAgencia,
										  String codigoConta,
										  String postoDaAgencia) {

			StringBuilder digitoVerificador = new StringBuilder();
			digitoVerificador.append(StringUtils.leftPad(codigoAgencia, 4, "0"));
			digitoVerificador.append(StringUtils.leftPad(postoDaAgencia, 2, "0"));
			digitoVerificador.append(StringUtils.leftPad(codigoConta, 5, "0"));
			digitoVerificador.append(BoletoUtil.getDataFormato(dataBoleto, "yy"));
			digitoVerificador.append(StringUtils.leftPad(serie, 1, "0"));
			digitoVerificador.append(StringUtils.leftPad(nossoNumero, 5, "0"));

			return modulo11Sicredi(digitoVerificador.toString());
	}

	/**
	 * <p>Retorna o Nome do Arquivo de Remessa.</p>
	 *
	 * @param codigoConta String contendo o código da conta do Beneficiário
	 * @param dataRemessa LocalDate contendo a data da Remessa
	 * @param numeroRemessaNoDia Integer contendo o número da Remessa no dia
	 * @return String com o nome do arquivo. Se numeroRemessaNoDia == 0 extensão do arquivo será ".CRM" se não ".RM"
	 */
	public static String retornaNomeArquivoRemessa(String codigoConta, LocalDate dataRemessa, Integer numeroRemessaNoDia) {
		String mesGeracaoArquivo = retornaMes(dataRemessa.getMonth());

		String extensao = numeroRemessaNoDia.compareTo(0) == 0 ? ".CRM" : ".RM" + (numeroRemessaNoDia + 1);;
		
		return StringUtils.leftPad(codigoConta, 5, '0') 
				+ mesGeracaoArquivo 
				+ StringUtils.leftPad(String.valueOf(dataRemessa.getDayOfMonth()), 2, "0") 
				+ extensao;
	}

	/**
	 * <p>Retorna o Mês.</p>
	 *
	 * @param month Month com o Mês
	 * @return String com o número do mês, "O" se mês Outubro,
	 * "N" se mês Novembro ou "D" se mês Dezembro
	 */
	private static String retornaMes(Month month) {
		switch (month) {
		case OCTOBER:
			return "O";
		case NOVEMBER:
			return "N";
		case DECEMBER:
			return "D";
		default:
			return String.valueOf(month.getValue());
		}
	}

	/**
	 * <p>Retorna o Fator Vencimento com base na data informada.</p>
	 *
	 * @param dataVencimento  LocalDate contendo a data de vencimento
	 * @return String com 4 dígitos contendo o fator para data informada
	 */
	public static String fatorData(LocalDate dataVencimento) {
        LocalDate dataBase = LocalDate.of(1997, 10, 7);
        if(dataVencimento.isAfter(LocalDate.of(2025, 2, 21))){
            dataBase =  dataBase.plusDays(9000);
        }
        
        long difDia = ChronoUnit.DAYS.between(dataBase, dataVencimento);
        return StringUtils.leftPad("" + difDia, 4, '0');

    }

	/**
	 * <p>Cálculo Dígito Verificador Campo Livre módulo 11 Sicredi.</p>
	 *
	 * @param codigo  String contendo o código
	 * @return Integer contendo o dígito verificador
	 */
	public static Integer modulo11DvCampoLivre(String codigo) {
		int total = 0;
		int peso = 2;

		for (int i = 0; i < codigo.length(); i++) {
			total += (codigo.charAt((codigo.length() - 1) - i) - '0') * peso;
			peso++;
			if (peso == 10) {
				peso = 2;
			}
		}
		int parteInteira = total / 11;
		int multiplicacao = parteInteira * 11;
		int resultado = total - multiplicacao;
		if (resultado < 2) {
			return 0;
		}
		return 11 - resultado;
	}

	/**
	 * <p>Cálculo Dígito Verificador Geral módulo 11 Sicredi.</p>
	 *
	 * @param codigo  String contendo o código
	 * @return Integer contendo o dígito verificador
	 */
	public static Integer modulo11DvGeralSicredi(String codigo) {
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
		return (digito == 0 || digito == 1 || digito > 9) ? 1 : digito;
	}

	/**
	 * <p>Cálculo Dígito Verificador módulo 10 Sicredi.</p>
	 *
	 * @param codigo  String contendo o código
	 * @return Integer contendo o dígito verificador
	 */
	public static Integer modulo10Sicredi(String codigo) {
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


	/**
	 * <p>Cálculo Dígito Verificador módulo 11 Sicredi.</p>
	 *
	 * @param codigo  String contendo o código
	 * @return Integer contendo o dígito verificador
	 */
	public static Integer modulo11Sicredi(String codigo) {
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
            return (digito == 10 || digito == 11) ? 0 : digito;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

	/**
	 * <p>Retorna um MAP com as possíveis Ocorrências.</p>
	 *
	 * @return MAP com as Ocorrências para o banco Sicredi
	 */
	public static Map<String, String> getMapOcorrencia() {
		try {
			Map<String, String> map = new HashMap<>();
			map.put("02", "Entrada confirmada");
			map.put("03", "Entrada rejeitada");
			map.put("06", "Liquidação normal");
			map.put("09", "Baixado automaticamente via arquivo");
			map.put("10", "Baixado conforme instruções da cooperativa de crédito");
			map.put("12", "Abatimento concedido");
			map.put("13", "Abatimento cancelado");
			map.put("14", "Vencimento alterado");
			map.put("15", "Liquidação em cartório");
			map.put("17", "Liquidação após baixa");
			map.put("19", "Confirmação de recebimento de instrução de protesto");
			map.put("20", "Confirmação de recebimento de instrução de sustação de protesto");
			map.put("23", "Entrada de título em cartório");
			map.put("24", "Entrada rejeitada por CEP irregular");
			map.put("27", "Baixa rejeitada");
			map.put("28", "Tarifa");
			map.put("29", "Rejeição do pagador");
			map.put("30", "Alteração rejeitada");
			map.put("32", "Instrução rejeitada");
			map.put("33", "Confirmação de pedido de alteração de outros dados");
			map.put("34", "Retirado de cartório e manutenção em carteira");
			map.put("35", "Aceite do pagador");
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
        	if(ocorrencia.equals("28")) {
        		map.put("03", "Tarifa de sustação");
            	map.put("04", "Tarifa de protesto");
            	map.put("08", "Tarifa de custas de protesto");
            	map.put("A9", "Tarifa de manutenção de título vencido");
            	map.put("B1", "Tarifa de baixa da carteira");
            	map.put("B3", "Tarifa de registro de entrada do título");
            	map.put("F5", "Tarifa de entrada na rede Sicredi"); 
        	} else {
        		map.put("00", "Ocorrência confirmada(OK)");
        		map.put("01", "Código do banco inválido");
        		map.put("02", "Código do registro detalhe inválido");
        		map.put("03", "Código da ocorrência inválido");
        		map.put("04", "Código de ocorrência não permitida para a carteira");
        		map.put("05", "Código de ocorrência não numérico");
        		map.put("07", "Cooperativa/agência/conta/dígito inválidos");
        		map.put("08", "Nosso número inválido");
        		map.put("09", "Nosso número duplicado");
        		map.put("10", "Carteira inválida");
        		map.put("14", "Título protestado");
        		map.put("15", "Cooperativa/carteira/agência/conta/nosso número inválidos");
        		map.put("16", "Data de vencimento inválida");
        		map.put("17", "Data de vencimento anterior à data de emissão");
        		map.put("18", "Vencimento fora do prazo de operação");
        		map.put("20", "Valor do título inválido");
        		map.put("21", "Espécie do título inválida");
        		map.put("22", "Espécie não permitida para a carteira");
        		map.put("24", "Data de emissão inválida");
        		map.put("29", "Valor do desconto maior/igual ao valor do título");
        		map.put("31", "Concessão de desconto - existe desconto anterior");
        		map.put("33", "Valor do abatimento inválido");
        		map.put("34", "Valor do abatimento maior/igual ao valor do título");
        		map.put("36", "Concessão de abatimento - existe abatimento anterior");
        		map.put("38", "Prazo para protesto inválido");
        		map.put("39", "Pedido para protesto não permitido para o título");
        		map.put("40", "Título com ordem de protesto emitida");
        		map.put("41", "Pedido cancelamento/sustação sem instrução de protesto");
        		map.put("44", "Cooperativa de crédito/agência beneficiária não prevista");
        		map.put("45", "Nome do pagador inválido");
        		map.put("46", "Tipo/número de inscrição do pagador inválidos");
        		map.put("47", "Endereço do pagador não informado");
        		map.put("48", "CEP irregular");
        		map.put("49", "Número de Inscrição do pagador/avalista inválido");
        		map.put("50", "Pagador/avalista não informado");
        		map.put("60", "Movimento para título não cadastrado");
        		map.put("63", "Entrada para título já cadastrado");
        		map.put("A", "Aceito");
        		map.put("D", "Desprezado");
        		map.put("A1", "Praça do pagador não cadastrada.");
        		map.put("A2", "Tipo de cobrança do título divergente com a praça do pagador.");
        		map.put("A3", "Cooperativa/agência depositária divergente: atualiza o cadastro de praças da Coop./agência beneficiária");
        		map.put("A4", "Beneficiário não cadastrado ou possui CGC/CIC inválido");
        		map.put("A5", "Pagador não cadastrado");
        		map.put("A6", "Data da instrução/ocorrência inválida");
        		map.put("A7", "Ocorrência não pode ser comandada");
        		map.put("A8", "Recebimento da liquidação fora da rede Sicredi - via compensação eletrônica");
        		map.put("B4", "Tipo de moeda inválido");
        		map.put("B5", "Tipo de desconto/juros inválido");
        		map.put("B6", "Mensagem padrão não cadastrada");
        		map.put("B7", "Seu número inválido");
        		map.put("B8", "Percentual de multa inválido");
        		map.put("B9", "Valor ou percentual de juros inválido");
        		map.put("C1", "Data limite para concessão de desconto inválida");
        		map.put("C2", "Aceite do título inválido");
        		map.put("C3", "Campo alterado na instrução “31 – alteração de outros dados” inválido");
        		map.put("C4", "Título ainda não foi confirmado pela centralizadora");
        		map.put("C5", "Título rejeitado pela centralizadora");
        		map.put("C6", "Título já liquidado");
        		map.put("C7", "Título já baixado");
        		map.put("C8", "Existe mesma instrução pendente de confirmação para este título");
        		map.put("C9", "Instrução prévia de concessão de abatimento não existe ou não confirmada");
        		map.put("D1", "Título dentro do prazo de vencimento (em dia)");
        		map.put("D2", "Espécie de documento não permite protesto de título");
        		map.put("D3", "Título possui instrução de baixa pendente de confirmação");
        		map.put("D4", "Quantidade de mensagens padrão excede o limite permitido");
        		map.put("D5", "Quantidade inválida no pedido de bloquetos pré-impressos da cobrança sem registro");
        		map.put("D6", "Tipo de impressão inválida para cobrança sem registro");
        		map.put("D7", "Cidade ou Estado do pagador não informado");
        		map.put("D8", "Seqüência para composição do nosso número do ano atual esgotada");
        		map.put("D9", "Registro mensagem para título não cadastrado");
        		map.put("E2", "Registro complementar ao cadastro do título da cobrança com e sem registro não cadastrado");
        		map.put("E3", "Tipo de postagem inválido, diferente de S, N e branco");
        		map.put("E4", "Pedido de bloquetos pré-impressos");
        		map.put("E5", "Confirmação/rejeição para pedidos de bloquetos não cadastrado");
        		map.put("E6", "Pagador/avalista não cadastrado");
        		map.put("E7", "Informação para atualização do valor do título para protesto inválido");
        		map.put("E8", "Tipo de impressão inválido, diferente de A, B e branco");
        		map.put("E9", "Código do pagador do título divergente com o código da cooperativa de crédito");
        		map.put("F1", "Liquidado no sistema do cliente");
        		map.put("F2", "Baixado no sistema do cliente");
        		map.put("F3", "Instrução inválida, este título está caucionado/descontado");
        		map.put("F4", "Instrução fixa com caracteres inválidos");
        		map.put("F6", "Nosso número / número da parcela fora de seqüência – total de parcelas inválido");
        		map.put("F7", "Falta de comprovante de prestação de serviço");
        		map.put("F8", "Nome do beneficiário incompleto / incorreto.");
        		map.put("F9", "CNPJ / CPF incompatível com o nome do pagador / Sacador Avalista");
        		map.put("G1", "CNPJ / CPF do pagador Incompatível com a espécie");
        		map.put("G2", "Título aceito: sem a assinatura do pagador");
        		map.put("G3", "Título aceito: rasurado ou rasgado");
        		map.put("G4", "Título aceito: falta título (cooperativa/ag. beneficiária deverá enviá-lo)");
        		map.put("G5", "Praça de pagamento incompatível com o endereço");
        		map.put("G6", "Título aceito: sem endosso ou beneficiário irregular");
        		map.put("G7", "Título aceito: valor por extenso diferente do valor numérico");
        		map.put("G8", "Saldo maior que o valor do título");
        		map.put("G9", "Tipo de endosso inválido");
        		map.put("H1", "Nome do pagador incompleto / Incorreto");
        		map.put("H2", "Sustação judicial");
        		map.put("H3", "Pagador não encontrado");
        		map.put("H4", "Alteração de carteira");
        		map.put("H5", "Recebimento de liquidação fora da rede Sicredi – VLB Inferior – Via Compensação");
        		map.put("H6", "Recebimento de liquidação fora da rede Sicredi – VLB Superior – Via Compensação");
        		map.put("H7", "Espécie de documento necessita beneficiário ou avalista PJ");
        		map.put("H8", "Recebimento de liquidação fora da rede Sicredi – Contingência Via Compe");
        		map.put("H9", "Dados do título não conferem com disquete");
        		map.put("I1", "Pagador e Sacador Avalista são a mesma pessoa");
        		map.put("I2", "Aguardar um dia útil após o vencimento para protestar");
        		map.put("I3", "Data do vencimento rasurada"); 
        		map.put("I4", "Vencimento – extenso não confere com número"); 
        		map.put("I5", "Falta data de vencimento no título"); 
        		map.put("I6", "DM/DMI sem comprovante autenticado ou declaração"); 
        		map.put("I7", "Comprovante ilegível para conferência e microfilmagem"); 
        		map.put("I8", "Nome solicitado não confere com emitente ou pagador"); 
        		map.put("I9", "Confirmar se são 2 emitentes. Se sim, indicar os dados dos 2"); 
        		map.put("J1", "Endereço do pagador igual ao do pagador ou do portador"); 
        		map.put("J2", "Endereço do apresentante incompleto ou não informado"); 
        		map.put("J3", "Rua/número inexistente no endereço"); 
        		map.put("J4", "Falta endosso do favorecido para o apresentante"); 
        		map.put("J5", "Data da emissão rasurada"); 
        		map.put("J6", "Falta assinatura do pagador no título"); 
        		map.put("J7", "Nome do apresentante não informado/incompleto/incorreto"); 
        		map.put("J8", "Erro de preenchimento do titulo"); 
        		map.put("J9", "Titulo com direito de regresso vencido"); 
        		map.put("K1", "Titulo apresentado em duplicidade"); 
        		map.put("K2", "Titulo já protestado"); 
        		map.put("K3", "Letra de cambio vencida – falta aceite do pagador"); 
        		map.put("K4", "Falta declaração de saldo assinada no título"); 
        		map.put("K5", "Contrato de cambio – Falta conta gráfica"); 
        		map.put("K6", "Ausência do documento físico"); 
        		map.put("K7", "Pagador falecido"); 
        		map.put("K8", "Pagador apresentou quitação do título"); 
        		map.put("K9", "Título de outra jurisdição territorial"); 
        		map.put("L1", "Título com emissão anterior a concordata do pagador"); 
        		map.put("L2", "Pagador consta na lista de falência"); 
        		map.put("L3", "Apresentante não aceita publicação de edital"); 
        		map.put("L4", "Dados do Pagador em Branco ou inválido");
        		map.put("L5", "Código do Pagador na agência beneficiária está duplicado");
        		map.put("M1", "Reconhecimento da dívida pelo pagador");
        		map.put("M2", "Não reconhecimento da dívida pelo pagador");
        		map.put("X1", "Regularização centralizadora – Rede Sicredi");
        		map.put("X2", "Regularização centralizadora – Compensação");
        		map.put("X3", "Regularização centralizadora – Banco correspondente");
        		map.put("X4", "Regularização centralizadora - VLB Inferior - via compensação");
        		map.put("X5", "Regularização centralizadora - VLB Superior - via compensação");
        		map.put("X0", "Pago com cheque");
        		map.put("X6", "Pago com cheque – bloqueado 24 horas");
        		map.put("X7", "Pago com cheque – bloqueado 48 horas");
        		map.put("X8", "Pago com cheque – bloqueado 72 horas");
        		map.put("X9", "Pago com cheque – bloqueado 96 horas");
        		map.put("XA", "Pago com cheque – bloqueado 120 horas");
        		map.put("XB", "Pago com cheque – bloqueado 144 horas");
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
	 * <p>Verifica se o caracter informado é valido para o Banco Sicredi.</p>
	 *
	 * @param ch  char
	 * @return Verdadeiro se o Banco aceita o caracter ou false se não
	 */
    public static boolean isASCIISicredi(char ch) {
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
		} else return ch == 10 || ch == 13;
	}
}
