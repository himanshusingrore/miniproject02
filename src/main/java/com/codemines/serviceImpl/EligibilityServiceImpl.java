package com.codemines.serviceImpl;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.codemines.entites.EligibilityDetails;
import com.codemines.repo.EligibiltyRepo;
import com.codemines.request.SearchRequest;
import com.codemines.response.SearchResponse;
import com.codemines.service.EligibiltyService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;



@Service
public class EligibilityServiceImpl implements EligibiltyService {
	
	@Autowired
	private EligibiltyRepo eligibiltyRepo;
	
	Logger logger=LoggerFactory.getLogger(EligibilityServiceImpl.class);

	//ye method plane name wale drop down mai unique plane name display kraane k lie 
	//plan name laayega
	@Override
	public List<String> getUniquePlanName() {
		// TODO Auto-generated method stub
		logger.debug("gett unique plan name ");
		return eligibiltyRepo.findPlanNames();
	}

	//ye method unique plan status wale dropdown mai unique plan status display
	//lraane k lie status laayega
	@Override
	public List<String> getUniquePlanStatuses() {
		// TODO Auto-generated method stub
		logger.debug("gett unique plan status ");
		return eligibiltyRepo.findPlanStatuses();
	}

	//user jb dropdowns mai value select kre/ya fir nahi kre a/c to that we are searching
	@Override
	public List<SearchResponse> search(SearchRequest request) {
  List<SearchResponse> response=new ArrayList<>();
  
  //user can select multiple dropdown based on that we need to fetch records here we don't know that 
  //ki user kon -kon se drop down ki value select krega islie apan query  by example ka concept use krenge
  //pehle apan user ne jo jo drop down ko select kiya hai uska data elegibilitydetail object mai set krdenge fir
  //is object ko findAll() mai daal denge..
  EligibilityDetails queryBuilder=new EligibilityDetails();
  if(request.getPlanName()!=null && !request.getPlanName().equals(""))
  {
	  logger.debug("plan name drop down selected ");
	  queryBuilder.setPlanName(request.getPlanName());
  }
  
  if(request.getPlanStatus()!=null && !request.getPlanStatus().equals(""))
  {
	  logger.debug("plan status drop down selected  ");
	  queryBuilder.setPlanStatus(request.getPlanStatus());
  }
  
  if(request.getPlanStartDate()!=null)
  {
	  logger.debug("plan start date drop down selected ");
	  queryBuilder.setPlanStartDate(request.getPlanStartDate());
  }
  
  if(request.getPlanEndDate()!=null)
  {
	  logger.debug("plan end date drop down selected ");
	  queryBuilder.setPlanEndDate(request.getPlanEndDate());
  }

  //here we prepare example object isko findall ko dedenge so is example mai jo jo object ki value hongi uske 
  //a/c result dega fetch kr k
Example<EligibilityDetails> example=Example.of(queryBuilder);
  
  List<EligibilityDetails> entities = eligibiltyRepo.findAll(example);
  for(EligibilityDetails entity:entities)
  {
	  SearchResponse sr=new SearchResponse();
	  BeanUtils.copyProperties(entity, sr);
	  response.add(sr);
  }
		
		return response;
	}
	
	
	

	@Override
	public void generateExcel(HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		List<EligibilityDetails> entites = eligibiltyRepo.findAll();
		HSSFWorkbook workbook=new HSSFWorkbook();//apan ne workbook bnaaye
		HSSFSheet sheet=workbook.createSheet();//workbook k andr sheet
		HSSFRow headerRow = sheet.createRow(0);//sheet k andr row and fir row mai index wise column ko point kr k usme value add kr rhe hai
		headerRow.createCell(0).setCellValue("S.No");//
		headerRow.createCell(1).setCellValue("Name");
		headerRow.createCell(2).setCellValue("Mobile");
		headerRow.createCell(3).setCellValue("Gender");
		headerRow.createCell(4).setCellValue("SSN");
		
		int rowcount=1;
		for(EligibilityDetails entity:entites)
		{
			HSSFRow dataRow = sheet.createRow(rowcount);
			dataRow.createCell(0).setCellValue(rowcount);
			dataRow.createCell(1).setCellValue(entity.getName());
			dataRow.createCell(2).setCellValue(entity.getMobileNo());
			dataRow.createCell(3).setCellValue(entity.getGender());
			dataRow.createCell(4).setCellValue(entity.getSsn());
			rowcount++;
		}
		ServletOutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		logger.debug("excel export method  ");
		workbook.close();
		outputStream.close();
		
	}
	
	private void pdfTableHeader(PdfPTable table)
	{
		 PdfPCell cell = new PdfPCell();
	        cell.setBackgroundColor(Color.BLUE);
	        cell.setPadding(5);
	        
	        Font font = FontFactory.getFont(FontFactory.HELVETICA);
	        font.setColor(Color.WHITE);
	        
	        cell.setPhrase(new Phrase("S.NO", font));
	        table.addCell(cell);//first cell in header
	        
	        cell.setPhrase(new Phrase("NAME", font));
	        table.addCell(cell);//2nd cell in header
	        
	        cell.setPhrase(new Phrase("MOBILE", font));
	        table.addCell(cell);//3rd cell in header
	        
	        cell.setPhrase(new Phrase("GENDER", font));
	        table.addCell(cell);//4th cell in header
	        
	        cell.setPhrase(new Phrase("SSN", font));
	        table.addCell(cell);//5th cell in header
	        
	        
	        
	        
	}
	
	private void pdfTableBody(PdfPTable table,List<EligibilityDetails> entites)
	{
		int sno=0;
		   for (EligibilityDetails entity : entites) {
	            table.addCell(String.valueOf(sno));
	            table.addCell(entity.getName());
	            table.addCell(entity.getMobileNo().toString());
	            table.addCell(entity.getGender().toString());
	            table.addCell(entity.getSsn().toString());
	            sno++;
	        }
		
	}

	@Override
	public void generatePdf(HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		List<EligibilityDetails> entites = eligibiltyRepo.findAll();
		
		 Document document = new Document(PageSize.A4);
		 
		PdfWriter.getInstance(document, response.getOutputStream());
		
		  document.open();
	        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
	        font.setSize(18);
	        font.setColor(Color.BLUE);
	        
	        Paragraph p = new Paragraph("List of Users", font);
	        p.setAlignment(Paragraph.ALIGN_CENTER);
	        document.add(p);
	        
	        PdfPTable table=new PdfPTable(5);
	        table.setWidthPercentage(100f);
	        table.setWidths(new float[] {1.5f,1.5f, 3.5f, 3.0f, 3.0f});
	        table.setSpacingBefore(10);
	        
	        //note:-pdf mai rows ka koi index nah hota hai in table ..jese no of column jo apan ne
	        //diya hai ex:-6 usse exceed hoha automaticlly it comes to 2nd row
	        
	        pdfTableHeader(table);//calling header method for display header in tble
	        pdfTableBody(table,entites);//calling this for displaying data in table
	        
	        document.add(table);
	        
		
		document.close();
		
		
		logger.debug("export PDf method ");
	}

}
