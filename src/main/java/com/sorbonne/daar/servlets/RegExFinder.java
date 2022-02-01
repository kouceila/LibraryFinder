package com.sorbonne.daar.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import com.sorbonne.daar.data.Book;
import com.sorbonne.daar.indexing.Indexer;
import com.sorboone.daar.utils.ConnexionHandler;
import com.sorboone.daar.utils.Stemer;


@WebServlet("/RegExFinder")
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
		
		for (Book book : index.getBooks()) {
			
			String bookContent = book.getContent();
			if (bookContent.equals(""))
				continue;
			if (bookContent.matches(regEx)) {
				booksResult.add(book);
			}	
		}
		System.out.println("izannn");
		response.getWriter().print(booksResult);
		response.getWriter().flush();
		}
		catch ( IOException e) {
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
