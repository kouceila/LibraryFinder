package com.sorbonne.daar.servlets;


import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sorbonne.daar.indexing.Indexer;



public class Finder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Indexer indexer ;

    public Finder() {
        super();
    }
    
    @Override
    	public void init() throws ServletException {
    		// TODO Auto-generated method stub
    		super.init();
    		indexer= new Indexer();
    		
    		InputStream stBooks = getServletContext().getResourceAsStream("/indexes/books.ser");
    		InputStream stTitles = getServletContext().getResourceAsStream("/indexes/titles.ser");
    		
    		InputStream stAuthors = getServletContext().getResourceAsStream("/indexes/authors.ser");
    		
    		InputStream stKeywords = getServletContext().getResourceAsStream("/indexes/keywords.ser");

    		try {
				indexer.readIndexBooks(stBooks);
				//indexer.readIndexTitle(stTitles);
				//indexer.readIndexAuthor(stAuthors);
				indexer.readIndexkeywords(stKeywords);
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

    	}
    

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
			response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			System.out.println("Finder started ... ");
			
			String service = request.getParameter("service");
			request.setAttribute("indexer", indexer);
			
			switch (service) {
			case "keywords":
				request.getRequestDispatcher("/keywordFinder").forward(request, response);
				break;
			case "regex":
				request.getRequestDispatcher("/regExFinder").forward(request, response);
			default:
				response.getWriter().print("you should specify the service !");
			}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		doGet(request, response);
	}

}
