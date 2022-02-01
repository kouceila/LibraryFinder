package com.sorbonne.daar.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sorbonne.daar.indexing.Indexer;
import com.sorboone.daar.utils.Stemer;

@WebServlet("/KeywordFinder")
public class KeywordFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public KeywordFinder() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		System.out.println("KeywordFinder started ... ");
		
		
		String keyword = request.getParameter("keyword");
		
		String keywordStem = Stemer.stem(keyword);
		
		Indexer index = (Indexer)request.getAttribute("indexer");
		
		List<Integer> ids = index.getKeywords().getMotCleMap().get(keywordStem);
		
		System.out.println(ids);
		
		response.getWriter().print(ids);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Do Post");
	}

}
