package br.com.java_brasil.boleto.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

import br.com.java_brasil.boleto.exception.BoletoException;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

@Slf4j
public class JasperUtil {

	public static byte[] geraRelatorio(List<?> dados, HashMap<String, Object> parametros, String arquivoJasper,
			Class<?> classe, HashMap<String, String> arquivoJasperSub) throws Exception {
		try {
			InputStream inputStream = classe.getResourceAsStream("/impressao/" + arquivoJasper + ".jasper");

			arquivoJasperSub.forEach((nome, jasperSub) -> {
				InputStream inputStreamSub = classe.getResourceAsStream("/impressao/" + jasperSub + ".jasper");
				parametros.put(nome, inputStreamSub);
			});

			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
					new JRBeanCollectionDataSource(dados));

			return geraBytesRelatorio(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoletoException(e.getMessage());
		}
	}

	public static byte[] geraRelatorioXml(File fileXml, HashMap<String, Object> parametros, String expression,
			String arquivoJasper, Class<?> classe, HashMap<String, String> arquivoJasperSub) throws Exception {
		try {
			InputStream inputStream = classe.getResourceAsStream("/impressao/" + arquivoJasper + ".jasper");

			arquivoJasperSub.forEach((nome, jasperSub) -> {
				InputStream inputStreamSub = classe.getResourceAsStream("/impressao/" + jasperSub + ".jasper");
				parametros.put(nome, inputStreamSub);
			});

			JRXmlDataSource xmlDataSource = new JRXmlDataSource(fileXml, expression);
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros, xmlDataSource);

			return geraBytesRelatorio(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoletoException(e.getMessage());
		}
	}

	public static byte[] geraBytesRelatorio(JasperPrint jasperPrint) throws Exception {
		try {
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void geraRelatorioDescktop(List<?> dados, HashMap<String, Object> parametros, String arquivoJasper,
			Class<?> classe, HashMap<String, String> arquivoJasperSub, boolean diretoImpressora,
			PrintService printService) throws Exception {

		try {
			InputStream inputStream = classe.getResourceAsStream("/impressao/" + arquivoJasper + ".jasper");

			arquivoJasperSub.forEach((nome, jasperSub) -> {
				InputStream inputStreamSub = classe.getResourceAsStream("/impressao/" + jasperSub + ".jasper");
				parametros.put(nome, inputStreamSub);
			});

			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
					new JRBeanCollectionDataSource(dados));

			if (diretoImpressora) {
				if (printService == null) {//Manda na Padrão
					JasperPrintManager.printReport(jasperPrint, false);
				} else {
					PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
					printRequestAttributeSet.add(MediaSizeName.NA_LETTER);

					PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
					printServiceAttributeSet.add(new PrinterName(printService.getName(), null));
					// printServiceAttributeSet.add(new PrinterName("Epson Stylus 820 ESC/P 2",null));
					// printServiceAttributeSet.add(new PrinterName("hp LaserJet 1320 PCL 6", null));
					// printServiceAttributeSet.add(new PrinterName("PDFCreator", null));

					JRPrintServiceExporter exporter = new JRPrintServiceExporter();
					exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
					SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
					configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
					configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
					configuration.setDisplayPageDialog(false);
					configuration.setDisplayPrintDialog(false);
					exporter.setConfiguration(configuration);
					exporter.exportReport();
				}
			} else {
				JasperViewer.viewReport(jasperPrint, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BoletoException(e.getMessage());
		}
	}

	public static PrintService getImpressora(String impressora) throws Exception {
		PrintService serviceFound = null;
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService service : services) {
			log.debug("Impressora: " + service.getName());
			if (service.getName().trim().equals(impressora.trim())) {
				serviceFound = service;
			}
		}
		return serviceFound;
	}
	
	public static List<String> getImpressorasDisponiveis() throws Exception {
		List<String> list = new ArrayList<>();
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		for (PrintService service : services) {
			list.add(service.getName().trim());
		}
		return list;
	}
}
