package br.com.java_brasil.boleto.util;

import br.com.java_brasil.boleto.exception.BoletoException;
import org.apache.commons.lang3.StringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Classe Util para fornecer Boletos
 */
public class BoletoUtil {

    public static String converteData(LocalDateTime dataASerFormatada) {

        try {
            GregorianCalendar calendar = GregorianCalendar.from(Optional.ofNullable(dataASerFormatada).orElse(LocalDateTime.now()).atZone(ZoneId.of("Brazil/East")));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            xmlCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

            return (xmlCalendar.toString());

        } catch (DatatypeConfigurationException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    /**
     * Retorna um localDateTima do inicio do dia da data informada
     * Ex: data informada: '2016-11-17' retorno: 2016-11-17T00:00:00
     *
     * @param data
     * @return
     */
    public static LocalDateTime obterInicioDoDia(LocalDate data) {
        LocalTime tempo = LocalTime.of(00, 00, 00);
        LocalDateTime comecoDoDia = LocalDateTime.of(data, tempo);
        return comecoDoDia;
    }

    public static XMLGregorianCalendar converteDataXMLGregorianCalendar(LocalDateTime dataASerFormatada) {

        try {
            GregorianCalendar calendar = GregorianCalendar.from(Optional.ofNullable(dataASerFormatada).orElse(LocalDateTime.now()).atZone(ZoneId.of("Brazil/East")));
            XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            xmlCalendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);

            return xmlCalendar;

        } catch (DatatypeConfigurationException e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    public static LocalDate converteXMLGregorianCalendarData(XMLGregorianCalendar xmlGregorianCalendar) {
        try {
            ZonedDateTime utcZoned = xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime().withZoneSameInstant(ZoneId.of("Brazil/East"));
            return utcZoned.toLocalDate();
        } catch (Exception e) {
            throw new BoletoException(e.getMessage(), e);
        }
    }

    /**
     * Adiciona a quantidade de dias passados na data informada.
     *
     * @param data
     * @param dias
     * @return
     */
    public static LocalDate adicionarDiasData(LocalDate data, long dias) {
        return data.plusDays(dias);
    }

    /**
     * Retorna a diferença em dias entre duas datas
     *
     * @param dataIni
     * @param dataFim
     * @return
     */
    public static long difEntreDatasEmDias(LocalDate dataIni, LocalDate dataFim) {
        long diferencaEmDias = ChronoUnit.DAYS.between(dataIni, dataFim);
        return diferencaEmDias;
    }
    public static String getDataFormatoDDMMYYYY(LocalDate data){
        return data.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    /**
     * Retorna a String da data De Acordo com o pattern
     *
     * @param data
     * @param pattern (Ex: dd/MM/yyyy ou  yyyy-MM-dd)
     * @return
     */
    public static String getDataFormato(LocalDate data, String pattern){
        return data.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Retorna a String da hora de Acordo com o pattern
     *
     * @param hora
     * @param pattern (Ex: HHmmss ou  HH:mm:ss)
     * @return
     */
    public static String getHoraFormato(LocalTime hora, String pattern){
        return hora.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * Este metodo recebe uma string no formato  DDMMAA e converte para local date.
     * Ex: para obter uma instancia de local date da data 01/07/2016 o parametro passado deve ser '010716' sem barras('/')
     *
     * @param data
     * @return
     */
    public static LocalDate formataStringPadraoDDMMYYParaLocalDate(String data) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(data, 0, 2)
                    .append("/")
                    .append(data, 2, 4)
                    .append("/")
                    .append(data, 4, 6);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            Date dataParse = sdf.parse(sb.toString());

            Instant instant = Instant.ofEpochMilli(dataParse.getTime());
            LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

            return localDate;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Este metodo recebe uma string no formato  DDMMAA e converte para local date.
     * Ex: para obter uma instancia de local date da data 01/07/2016 o parametro passado deve ser '01072016' sem barras('/')
     *
     * @param data
     * @return
     */
    public static LocalDate formataStringPadraoDDMMYYYYParaLocalDate(String data) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(data, 0, 2)
                    .append("/")
                    .append(data, 2, 4)
                    .append("/")
                    .append(data, 4, 8);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date dataParse = sdf.parse(sb.toString());

            Instant instant = Instant.ofEpochMilli(dataParse.getTime());
            LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();

            return localDate;

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Receb uma string no formado yyyyMMdd e converte para localDate.
     * Ex: para obter uma instancia de local date da data 18/07/2016 o parametro passado deve ser '20160718' sem barras('/')
     *
     * @param data
     * @return
     */
    public static LocalDate formataStringPadraoYYYYMMDDParaLocalDate(String data) {
        try {
            int year = Integer.parseInt(data.substring(0, 4));
            int month = Integer.parseInt(data.substring(4, 6));
            int dayOfMonth = Integer.parseInt(data.substring(6, 8));

            return LocalDate.of(year, month, dayOfMonth);

        } catch (Exception e) {
            return null;
        }
    }

    public static String valorSemPontos(BigDecimal valor, int casas){
        return valor.setScale(casas, RoundingMode.HALF_UP)
                .toString().replace(".", "");
    }

    /**
     * Retorna true se o bigDecimal informado é != de nulo e > que 0
     *
     * @param valor
     * @return
     */
    public static boolean isNotNullEMaiorQZero(BigDecimal valor) {
        return valor != null && maiorQZero(valor);
    }

    /**
     * Verifica se o valor passado em BigDecimal é maior que zero.
     *
     * @param valor
     * @return
     */
    public static boolean maiorQZero(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }


    /**
     * Retorna uma String do valor bigdecimal sem casas decimais
     *
     * @param valor
     * @return
     */
    public static String bigDecimalSemCasas(BigDecimal valor) {
        return formatarCasasDecimais(valor, 0);
    }

    /**
     * Retorna uma String do valor BigDecimal formatando o numero de casas decimais
     * de acordo com o scale informado.
     *
     * @param valor
     * @param scale
     * @return
     */
    public static String formatarCasasDecimais(BigDecimal valor, int scale) {
        if (valor != null) {
            return valor.setScale(scale, RoundingMode.HALF_UP).toString();
        } else {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP).toString();
        }
    }

    /**
     * Retorna um BigDecimal da String informada (Ex: 10000 = 100.00, 53533 = 535.33)
     * @param valor
     * @return
     */
    public static BigDecimal stringSemPontoParaBigDecimal(String valor) {
        if(StringUtils.isBlank(valor)) return BigDecimal.ZERO;
        return new BigDecimal(valor).divide(BigDecimal.valueOf(100), MathContext.DECIMAL32);
    }

    /**
     * Limita o tamanho da String ate o limite passado.
     *
     * @param valor
     * @param limite
     * @return
     */
    public static String limitarTamanhoString(String valor, int limite) {
        if (StringUtils.isBlank(valor) || valor.length() <= limite) {
            return valor;
        }
        return valor.substring(0, limite);
    }

    /**
     * Remove tudo que não numeros de 0 a 9 da String passada.
     *
     * @param s
     * @return
     */
    public static String manterApenasNumeros(String s) {
        if (StringUtils.isBlank(s)) {
            return s;
        }
        return s.replaceAll("[^0-9]", "");
    }

    /**
     * Ajusta as casas decimais e remove ponto e vírgula.
     * Retorna o valor formatado para o tamanho do campo completando com zeros a esquerda
     *
     * @param valor
     * @param casasDecimais
     * @param tamanho
     * @return
     */
    public static String formatarValorSemPonto(BigDecimal valor, int casasDecimais, int tamanho) {
        if (valor == null) {
            valor = BigDecimal.ZERO;
        }

        return StringUtils.leftPad(manterApenasNumeros(formatarCasasDecimais(valor, casasDecimais)), tamanho, '0');
    }

    public static String formatarCnpjCpf(String documento) {
        if (StringUtils.isBlank(documento)) {
            return "";
        }
        if(documento.length() == 14) {
            documento = documento.substring(0, 2) + "." + documento.substring(2, 5) + "."
                    + documento.substring(5, 8) + "/" + documento.substring(8, 12) + "-" + documento.substring(12, 14);
        } else if(documento.length() == 11) {
            documento = documento.substring(0, 3) + "." + documento.substring(3, 6) + "." + documento.substring(6, 9) + "-" + documento.substring(9, 11);
        }
        return documento;
    }

    public static String formatarParaCodigoBarrasI25(String codigoBarra) {

        Map<String, String> i25Map = new HashMap<>();

        i25Map.put("00", "nnWWn");
        i25Map.put("01", "NnwwN");
        i25Map.put("02", "nNwwN");
        i25Map.put("03", "NNwwn");
        i25Map.put("04", "nnWwN");
        i25Map.put("05", "NnWwn");
        i25Map.put("06", "nNWwn");
        i25Map.put("07", "nnwWN");
        i25Map.put("08", "NnwWn");
        i25Map.put("09", "nNwWn");
        i25Map.put("10", "wnNNw");
        i25Map.put("11", "WnnnW");
        i25Map.put("12", "wNnnW");
        i25Map.put("13", "WNnnw");
        i25Map.put("14", "wnNnW");
        i25Map.put("15", "WnNnw");
        i25Map.put("16", "wNNnw");
        i25Map.put("17", "wnnNW");
        i25Map.put("18", "WnnNw");
        i25Map.put("19", "wNnNw");
        i25Map.put("20", "nwNNw");
        i25Map.put("21", "NwnnW");
        i25Map.put("22", "nWnnW");
        i25Map.put("23", "NWnnw");
        i25Map.put("24", "nwNnW");
        i25Map.put("25", "NwNnw");
        i25Map.put("26", "nWNnw");
        i25Map.put("27", "nwnNW");
        i25Map.put("28", "NwnNw");
        i25Map.put("29", "nWnNw");
        i25Map.put("30", "wwNNn");
        i25Map.put("31", "WwnnN");
        i25Map.put("32", "wWnnN");
        i25Map.put("33", "WWnnn");
        i25Map.put("34", "wwNnN");
        i25Map.put("35", "WwNnn");
        i25Map.put("36", "wWNnn");
        i25Map.put("37", "wwnNN");
        i25Map.put("38", "WwnNn");
        i25Map.put("39", "wWnNn");
        i25Map.put("40", "nnWNw");
        i25Map.put("41", "NnwnW");
        i25Map.put("42", "nNwnW");
        i25Map.put("43", "NNwnw");
        i25Map.put("44", "nnWnW");
        i25Map.put("45", "NnWnw");
        i25Map.put("46", "nNWnw");
        i25Map.put("47", "nnwNW");
        i25Map.put("48", "NnwNw");
        i25Map.put("49", "nNwNw");
        i25Map.put("50", "wnWNn");
        i25Map.put("51", "WnwnN");
        i25Map.put("52", "wNwnN");
        i25Map.put("53", "WNwnn");
        i25Map.put("54", "wnWnN");
        i25Map.put("55", "WnWnn");
        i25Map.put("56", "wNWnn");
        i25Map.put("57", "wnwNN");
        i25Map.put("58", "WnwNn");
        i25Map.put("59", "wNwNn");
        i25Map.put("60", "nwWNn");
        i25Map.put("61", "NwwnN");
        i25Map.put("62", "nWwnN");
        i25Map.put("63", "NWwnn");
        i25Map.put("64", "nwWnN");
        i25Map.put("65", "NwWnn");
        i25Map.put("66", "nWWnn");
        i25Map.put("67", "nwwNN");
        i25Map.put("68", "NwwNn");
        i25Map.put("69", "nWwNn");
        i25Map.put("70", "nnNWw");
        i25Map.put("71", "NnnwW");
        i25Map.put("72", "nNnwW");
        i25Map.put("73", "NNnww");
        i25Map.put("74", "nnNwW");
        i25Map.put("75", "NnNww");
        i25Map.put("76", "nNNww");
        i25Map.put("77", "nnnWW");
        i25Map.put("78", "NnnWw");
        i25Map.put("79", "nNnWw");
        i25Map.put("80", "wnNWn");
        i25Map.put("81", "WnnwN");
        i25Map.put("82", "wNnwN");
        i25Map.put("83", "WNnwn");
        i25Map.put("84", "wnNwN");
        i25Map.put("85", "WnNwn");
        i25Map.put("86", "wNNwn");
        i25Map.put("87", "wnnWN");
        i25Map.put("88", "WnnWn");
        i25Map.put("89", "wNnWn");
        i25Map.put("90", "nwNWn");
        i25Map.put("91", "NwnwN");
        i25Map.put("92", "nWnwN");
        i25Map.put("93", "NWnwn");
        i25Map.put("94", "nwNwN");
        i25Map.put("95", "NwNwn");
        i25Map.put("96", "nWNwn");
        i25Map.put("97", "nwnWN");
        i25Map.put("98", "NwnWn");
        i25Map.put("99", "nWnWn");

        StringBuilder codigoBarraI25 = new StringBuilder();

        for (String valor : codigoBarra.replace("<", "").replace(">", "")
                .split("(?<=\\G.{5})")) {
            codigoBarraI25.append(i25Map.entrySet()
                    .stream()
                    .filter(entry -> Objects.equals(entry.getValue(), valor))
                    .map(Map.Entry::getKey).findFirst().orElse(""));
        }

        return codigoBarraI25.toString();

    }
}
