package com.codemines.restcontroller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.codemines.request.SearchRequest;
import com.codemines.response.SearchResponse;
import com.codemines.service.EligibiltyService;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	Logger logger=LoggerFactory.getLogger(RestController.class);
	
	@Autowired
	private EligibiltyService eligibiltyService;
	
	@GetMapping("/getplans")
	public ResponseEntity<List<String>> getPlanNames()
	{
		
		List<String> uniquePlanName = eligibiltyService.getUniquePlanName();
		if(uniquePlanName!=null)
		{
			return new ResponseEntity<>(uniquePlanName,HttpStatus.OK);
		}
		logger.debug("rest controller unique name method");
		return new ResponseEntity<>(null,HttpStatus.CREATED);
		
	}
	
	@GetMapping("/getstatus")
	public ResponseEntity<List<String>> getPlanStatus()
	{
		List<String> uniquePlanStatuses = eligibiltyService.getUniquePlanStatuses();
		if(uniquePlanStatuses!=null)
		{
			return new ResponseEntity<>(uniquePlanStatuses,HttpStatus.OK);
		}
		logger.debug("rest controller get plan status method");
		return new ResponseEntity<>(null,HttpStatus.CREATED);
	}
	
	
	//note:-start date and end date k lie method bnaane ki need ni hai becuase 
	//data search button click krne k sath aayega so we will make direct search method
	
	//here we are using postmapping but here we are not saveing soemthing but we are sending some data in body
	//so just we need to send somedata in requestbody thats y we use this
	//apan chahte toh url mapping se bhi data send kr skte the getmapping mai but
	//jada parameter hone mai jada parameter hojaate which is not good approch
	//islie hum object send krdenge in body
	@PostMapping("/search")
	public ResponseEntity<List<SearchResponse>> search(@RequestBody SearchRequest searchRequest)
	{
		List<SearchResponse> searchres = eligibiltyService.search(searchRequest);
		logger.debug("rest controller search method");
		return new ResponseEntity<List<SearchResponse>>(searchres,HttpStatus.OK);
		
	}
	
	
	@GetMapping("/excel")
	public void excelExport(HttpServletResponse response) throws Exception
	{
		//direct download ho jaye so we need to set header and content type
		//The "octet-stream" is used to indicate that a body contains arbitrary binary data.
		response.setContentType("application/octet-stream");
		
		//In a regular HTTP response, the Content-Disposition response header is a header indicating if the content is expected to be displayed inline in the browser, that is, as a Web page or as part of a Web page, or as an attachment, 
		//that is downloaded and saved locally.here we provide it as attachment means downloadable
		String headerkey="Content-Disposition";
		String headervalue="attachment;filename=data.xls";
		response.setHeader(headerkey, headervalue);
		
		eligibiltyService.generateExcel(response);
	}
	
	@GetMapping("/pdf")
	public void pdfExport(HttpServletResponse response) throws Exception
	{
		//Security Considerations. An "application/pdf" resource contains information to be parsed and. processed by the recipient's PDF system. 
		//Because PDF is both a. representation of formatted documents and a container system for the.
		response.setContentType("application/pdf");
		String headerkey="Content-Disposition";
		String headervalue="attachment;filename=data.pdf";
		response.setHeader(headerkey, headervalue);
		
		eligibiltyService.generatePdf(response);
	}
	
	
	
	
	

}//end 
