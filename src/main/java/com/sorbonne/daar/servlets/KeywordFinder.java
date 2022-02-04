package com.sorbonne.daar.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.sorbonne.daar.data.Book;
import com.sorbonne.daar.indexing.Indexer;
import com.sorboone.daar.utils.SortHandler;
import com.sorboone.daar.utils.Stemer;


public class KeywordFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public KeywordFinder() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		System.out.println("KeywordFinder started ... ");
		
		
		try {
			String keyword = request.getParameter("keyword");
			
			String keywordStem = Stemer.stem(keyword);
			
			Indexer index = (Indexer)request.getAttribute("indexer");
			
			List<Book> books = index.getBooks();
			
			
			List<Integer> ids = index.getKeywords().getMotCleMap().get(keyword);
			Map<Integer,Integer> countIds = new HashMap<>();
			int count ;
			if ( ! ( ids == null ) ) {
				for(Book book: books) {
					if(ids.contains(book.getId())) {
						count = StringUtils.countMatches(book.getContent(),keyword );
						countIds.put(book.getId(), count);
					}
				}
			Map<Integer,Integer> res = SortHandler.sortByValues(countIds);
			response.getWriter().print(res);
			}	
		}
		catch(IOException e  ) {
			e.getMessage();
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Do Post");
	}

}
