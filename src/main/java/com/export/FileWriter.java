package com.export;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
//import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.detection.DetectionAlgo;
import com.generator.GenerateTradeList;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.pojo.Trade;
import com.sun.xml.bind.v2.runtime.RuntimeUtil.ToStringAdapter;


	public class FileWriter {
		public  void CreateTable(ArrayList<ArrayList<Trade>>arr){
		Document document = new Document();
		XSSFWorkbook workbook = new XSSFWorkbook();
		
		PdfWriter writer;
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream("../FrontRunningScenarios.pdf"));
			document.open();
			document.add(new Paragraph("Front Running "));
			document.add(new Paragraph(""));
			XSSFSheet spreadsheet = workbook.createSheet( " Front Running Scenarios ");
			

//			System.out.println(arr.size());
			//"Trade ID","Execution Time", "Customer ID","Security","Market Price","Order Price","Order type","Quantity","Broker Name"
			
			float sizes[]= {20f,40f,20f,30f,25f,25f,20f,20f,20f};
			int count=0;
			for(int i = 0;i<arr.size();i++)
			{
				
				int num = i+1;
				document.add(new Paragraph("Scenario "+num));
				
				PdfPTable table = new PdfPTable(9);
				
				table.setWidths(sizes);
				table.setSpacingBefore(30);
				addTableHeader(table);
				int j = 0;
				for( j = 0;j<arr.get(i).size();j++)
				{
					XSSFRow row;
					row = spreadsheet.createRow(j+count);  
					
					addExcelRows(row, arr.get(i).get(j));
					addRows(table, arr.get(i).get(j));
				}
				count +=j;
				spreadsheet.createRow(++count);
				
				document.add(table);
				document.newPage();
				
				
			}
			FileOutputStream out = new FileOutputStream(
			         new File("../FrontRunning.xlsx"));
			      
			      try {
					workbook.write(out);
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			      
			document.close();
			writer.close();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



			
		}
		public void addTableHeader(PdfPTable table){
			Stream.of("Trade ID","Execution Time", "Cust ID","Security","Market Price","Order Price","Order type","QTY.","Brok Name")
		      .forEach(columnTitle -> {
		        PdfPCell header = new PdfPCell();
		        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        header.setBorderWidth(2);
		        header.setPhrase(new Phrase(columnTitle));
		        table.addCell(header);
		    });
		}
		public void addExcelRows(XSSFRow row,Trade t) {
			int cellid = 0;
			Cell cell = row.createCell(cellid++);
			cell.setCellValue(Integer.toString(t.getTradeId()));
			cell = row.createCell(cellid++);
			cell.setCellValue(t.getTradeExecutionTime().getTime().toString());
			cell = row.createCell(cellid++);
			cell.setCellValue((Integer.toString(t.getCustomerId())));
			cell = row.createCell(cellid++);
			cell.setCellValue(t.getSecurityName());
			cell = row.createCell(cellid++);
			String MP=String.format("%.3f", t.getMarketPrice());
		    cell.setCellValue(MP);;
		    cell = row.createCell(cellid++);
		    String OP =String.format("%.3f", t.getPrice());
		    cell.setCellValue(OP);
		    cell = row.createCell(cellid++);
		    cell.setCellValue(t.isTradeType()?"buy":"sell");
		    cell = row.createCell(cellid++);
		    cell.setCellValue(Integer.toString(t.getQuantity()));
		    cell = row.createCell(cellid++);
		    cell.setCellValue(t.getBrokerName());
		    
		}
		public void addRows(PdfPTable table,Trade t) {
			table.addCell(Integer.toString(t.getTradeId()));
		    table.addCell(t.getTradeExecutionTime().getTime().toString());
		    table.addCell(Integer.toString(t.getCustomerId()));
		    table.addCell(t.getSecurityName());
		    String MP=String.format("%.3f", t.getMarketPrice());
		    table.addCell(MP);
		    String OP =String.format("%.3f", t.getPrice());
		    
		    table.addCell(OP);
		    table.addCell(t.isChecked()?"buy":"sell");
		    table.addCell(Integer.toString(t.getQuantity()));
		    table.addCell(t.getBrokerName());
		   
		}
	}	

