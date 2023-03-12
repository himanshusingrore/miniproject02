package com.codemines.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.codemines.request.SearchRequest;
import com.codemines.response.SearchResponse;

public interface EligibiltyService {
	
	public List<String> getUniquePlanName();
	
	public List<String> getUniquePlanStatuses();
	
	public List<SearchResponse> search(SearchRequest request);
	
	public void generateExcel(HttpServletResponse response) throws Exception;
	
	public void generatePdf(HttpServletResponse response) throws Exception;
	
	

}
