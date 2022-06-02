package br.com.java_brasil.boleto.service.bancos.banrisul_api;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BanrisulUtil {

    /**
     * <p>Transforma um Objeto em String no formato XML.</p>
     *
     * @param objeto Object a ser transformado
     * @param tipo  String com o tipo do Objeto
     * @return String no formato XML do objeto
     */
    public static String transformarObjetoParaXml(Object objeto, String tipo) throws Exception {
        JAXBContext context;
        switch (tipo){
            case "RegistrarTitulo":
                context = JAXBContext.newInstance(br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.registra.Dados.class);
                break;
            case "AlterarTitulo":
                context = JAXBContext.newInstance(br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.alterar.Dados.class);
                break;
            case "ConsultarTitulo":
                context = JAXBContext.newInstance(br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.consultar.Dados.class);
                break;
            case "BaixarTitulo":
                context = JAXBContext.newInstance(br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.baixar.Dados.class);
                break;
            case "EmitirBoleto":
                context = JAXBContext.newInstance(br.com.java_brasil.boleto.service.bancos.banrisul_api.xsd.emitir.Dados.class);
                break;
            default:
             throw new Exception("Objeto não mapeado!");
        }

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.encoding", "Unicode");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(objeto, sw);
        return sw.toString();
    }

    /**
     * <p>Transforma uma String no formato XML em um Objeto.</p>
     *
     * @param xml String no formato XML a ser transformada
     * @param classe Class
     * @return Objeto Java
     */
    public static <T> T transformarXmlParaObjeto(String xml, Class<T> classe) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classe);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), classe).getValue();
    }

    /**
     * <p>Extrai nó dados do xml de retorno.</p>
     *
     * @param xml String no formato XML
     * @return String xml
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    public static String extrairNoXmlRetorno(String xml) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        ByteArrayInputStream biteArray = new ByteArrayInputStream(xml.getBytes());
        Document doc = dbf.newDocumentBuilder().parse(biteArray);

        NodeList elements = doc.getElementsByTagName("dados");

        StringWriter writer = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer trans = tf.newTransformer();
        trans.transform(new DOMSource(elements.item(0)), new StreamResult(writer));

        return writer.toString();
    }

    /**
     * <p>Comunicação com o WebService.</p>
     *
     * @param envelopeSoap String contendo um XML de envio
     * @param configuracao ConfiguracaoBanrisulAPI contendo as configurações
     * @return String cotendo o xml do retorno
     */
    public static String comunicacaoHttpsURLConnection(String envelopeSoap, ConfiguracaoBanrisulAPI configuracao) throws IOException {
        System.setProperty("javax.net.ssl.keyStore", configuracao.getCaminhoCertificado()); //"D:/Temp/dacar.jks"
        System.setProperty("javax.net.ssl.keyStorePassword", configuracao.getSenhaCertificado()); //"12345678"
        //String address = "https://ww20.banrisul.com.br/boc/link/Bocswsxn_CobrancaOnlineWS.asmx";
        URL url = new URL(configuracao.getURLBase());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-type", "text/xml; charset=\"utf-8\"");
        connection.setDoOutput(true);
        connection.setDoInput(true);

        PrintWriter printWriter = new PrintWriter(connection.getOutputStream(), true);
        printWriter.println(envelopeSoap);
        printWriter.close();
        StringBuilder response = new StringBuilder();
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        for (String line = bufferedReader.readLine(); line != null; line =
                bufferedReader.readLine()) {
            response.append(line);
            response.append(System.getProperty("line.separator"));
        }
        System.out.println("Response: " + response);
        return response.toString();
    }

    /**
     * <p>Retorna o Fator Vencimento com base na data informada.</p>
     *
     * @param dataVencimento  LocalDate contendo a data de vencimento
     * @return String com 4 dígitos contendo o fator para data informada
     */
    public static String fatorData(LocalDate dataVencimento)  {
        LocalDate dataBase = LocalDate.of(1997, 10, 7);
        if(dataVencimento.isAfter(LocalDate.of(2025, 2, 21))){
            dataBase =  dataBase.plusDays(9000);
        }

        long difDia = ChronoUnit.DAYS.between(dataBase, dataVencimento);
        return StringUtils.leftPad("" + difDia, 4, '0');
    }

    /**
     * <p>Retorna o Dígito do nosso número.</p>
     *
     * @param nossoNumero  String nosso número (De controle do emitente)
     * @return String com 2 dígitos contendo o dígito do nosso número
     */
    public static String geraDigitoNossoNumero(String nossoNumero)  {
            nossoNumero = StringUtils.leftPad(nossoNumero, 8, "0");
            Integer digito10 = modulo10Banrisul(nossoNumero);
            nossoNumero = modulo11Banrisul(nossoNumero, digito10);
            return nossoNumero.substring(8); //Somente os últimos 2 dígitos
    }

    public static Integer modulo10Banrisul(String codigo) {
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

    public static String modulo11Banrisul(String codigo, Integer digitoModulo10) {
        int total = 0;
        int peso = 2;
        String codigoModulo10 = codigo + digitoModulo10;

        for (int i = 0; i < codigoModulo10.length(); i++) {
            total += (codigoModulo10.charAt((codigoModulo10.length() - 1) - i) - '0') * peso;
            peso++;
            if (peso == 8) {
                peso = 2;
            }
        }
        int resto;
        if (total < 11) {
            resto = total;
        } else {
            resto = total % 11;
        }
        int digito;
        switch (resto) {
            case 0:
                digito = 0;
                return codigoModulo10 + digito;
            case 1:
                if (digitoModulo10 == 9) {
                    digitoModulo10 = 0;
                    return modulo11Banrisul(codigo, digitoModulo10);
                } else {
                    digitoModulo10++;
                    return modulo11Banrisul(codigo, digitoModulo10);
                }
            default:
                digito = 11 - resto;
                return codigoModulo10 + digito;
        }
    }

    public static String modulo11Dac(String linha) {
        int total = 0;
        int peso = 2;
        for (int i = 0; i < linha.length(); i++) {
            total += (linha.charAt((linha.length() - 1) - i) - '0') * peso;
            peso++;
            if (peso == 10) {
                peso = 2;
            }
        }

        int resto = total % 11;
        if (resto == 0 || resto == 1 || resto == 10 || resto == 11) {
            return "1";
        } else {
            return "" + (11 - resto);
        }
    }
}
