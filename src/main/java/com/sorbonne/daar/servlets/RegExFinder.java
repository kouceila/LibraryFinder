package com.sorbonne.daar.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;

import com.sorbonne.daar.data.Book;
import com.sorbonne.daar.indexing.Indexer;
import com.sorboone.daar.utils.JsonConverter;


public class RegExFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public RegExFinder() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		 response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		System.out.println("RegExFinder started ... ");
		try {
		List<Book> booksResult = new ArrayList<>();
		String regEx = request.getParameter("reg");
		
		Indexer index = (Indexer) request.getAttribute("indexer");
		
		Pattern pattern = Pattern.compile(regEx);
		for (Book book : index.getBooks()) {
			
			String bookContent = book.getContent();
			if (bookContent.equals(""))
				continue;
			Matcher matcher = pattern.matcher(bookContent);
			
			if (matcher.find()) {
				booksResult.add(book);
			}	
		}
		JSONArray arr = new JSONArray();
		for(Book book: booksResult ) {
			arr.put(JsonConverter.bookToJson(book));
		}
		response.getWriter().print(arr);
		response.getWriter().flush();
		}
		catch ( IOException | JSONException e) {
			e.getMessage();
		}
		
		/*response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		System.out.println("RegFinder started ... ");
		
		
		String reg = request.getParameter("reg");
		
		Indexer index = (Indexer)request.getAttribute("indexer");
		
		Pattern pattern = Pattern.compile(reg);
		List<Integer> ids = new ArrayList<>();
		for(String key: index.getKeywords().getMotCleMap().keySet()) {
	
			Matcher m = pattern.matcher(key);
			if(m.matches()) ids.addAll(index.getKeywords().getMotCleMap().get(key));
		} */
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
