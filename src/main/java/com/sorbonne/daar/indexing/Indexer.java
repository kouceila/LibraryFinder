package com.sorbonne.daar.indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sorbonne.daar.data.Author;
import com.sorbonne.daar.data.Book;
import com.sorbonne.daar.data.Keyword;
import com.sorbonne.daar.utils.keywords.MotCleMap;
import com.sorboone.daar.utils.Stemer;



public class Indexer  {
	
	private List<Book> books = new ArrayList<>() ; 
	private  Map<Author, List<Integer>> authors = new HashMap<>();
	private   Map<String, List<Integer>> titles = new HashMap<>();
	private MotCleMap keywords = new MotCleMap();
	
	
	public   void createBookIndex() throws IOException, JSONException {
		int cpt=1;
		int i = 1;
		try {
			while ( cpt < 1665) {
				URL url = new URL("https://gutendex.com/books?ids=" + i);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				
				int status = con.getResponseCode();
				System.out.println("begin" + i);
				BufferedReader reader;
				String line;
				StringBuilder responseContent = new StringBuilder();
						
						
				if (status >= 300) {
					reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
					while ((line = reader.readLine()) != null) {
						responseContent.append(line);
					}
					reader.close();
				}
				else {
					reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
					while ((line = reader.readLine()) != null) {
						responseContent.append(line);
					}
					reader.close();
				}	
				
				JSONObject res = new JSONObject(responseContent.toString());
				JSONArray resultsArray = new JSONArray(res.getString("results"));
				
				if ( res.getJSONArray("results").length() >0 ) {
					Book book = new Book(resultsArray.getJSONObject(0));
					if ( (book.getFileURL()!=null) && ( !book.getFileURL().contains(".zip"))) {
						books.add(book);
						cpt ++;
					}
					
				}
				i++;
			}	
			
		}catch(ConnectException e) {
			System.out.print("Connection timed out");
			i++;
		}
	}
	
	
	public List<Book> getBooks() {
		return books;
	}


	public void setBooks(List<Book> books) {
		this.books = books;
	}


	public Map<Author, List<Integer>> getAuthors() {
		return authors;
	}


	public void setAuthors(Map<Author, List<Integer>> authors) {
		this.authors = authors;
	}


	public Map<String, List<Integer>> getTitles() {
		return titles;
	}


	public void setTitles(Map<String, List<Integer>> titles) {
		this.titles = titles;
	}


	

	public MotCleMap getKeywords() {
		return keywords;
	}


	public void setKeywords(MotCleMap keywords) {
		this.keywords = keywords;
	}


	public void createAuthorIndex() {
		
		for(Book book: books) {
			List<Author> bookAuthors = book.getAuthors();
			for(Author author: bookAuthors) {
				if(authors.containsKey(author)) {
					authors.get(author).add(book.getId());
					
				}else {
					List<Integer> arr = new ArrayList<>();
					arr.add(book.getId());
					authors.put(author, arr);
				}
			}
		}
		
	}
	
	public   void createTitleIndex() {
	
		for(Book book: books) {
			String title = book.getTitle();
			
			if (titles.containsKey(title))
				titles.get(title).add(book.getId());
			else {
				List<Integer> ids = new ArrayList<>();
				ids.add(book.getId());
				titles.put(title, ids);
			}
		}	
	}
	
	public   void createKeywordsIndex() throws IOException {
		for (Book book : books) {
			String link = book.getFileURL();
			if (link == null) continue;
			URL url = new URL(link);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader reader;
			String line;
			StringBuilder responseContent = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
			}
			reader.close();
			
			List<Keyword> res = Stemer.guessFromString(responseContent.toString());
			
			for( Keyword keyword : res) {
				if ( keywords.getMotCleMap().containsKey(keyword.getStem()) ) {
					keywords.getMotCleMap().get(keyword.getStem()).add(book.getId());
				}
				else {
					List<Integer> bookIds = new ArrayList<>();
					bookIds.add(book.getId());
					keywords.getMotCleMap().put(keyword.getStem(), bookIds);
				}
 			}
			
		}
	}
	
	
	public   void writeIndexKeywords() throws FileNotFoundException, IOException {
		if (keywords.getMotCleMap().isEmpty()) {
			System.err.println("Index keywords is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("keywords.ser")));
		ois.writeObject(keywords);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public   MotCleMap readIndexkeywords(InputStream st) throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		ObjectInputStream ois = new ObjectInputStream(st);
		keywords = (MotCleMap) ois.readObject();
		ois.close();
		return keywords;
	}


	public   void writeIndexBooks() throws FileNotFoundException, IOException {
		if (books.isEmpty()) {
			System.err.println("Index Books is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("LibraryFinder/src/main/java/data/books.ser")));
		ois.writeObject(books);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public   List<Book> readIndexBooks(InputStream st) throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		//FileInputStream fis = new FileInputStream("LibraryFinder/src/main/java/data/books.ser");
		ObjectInputStream ois = new ObjectInputStream(st);
		books = (List<Book>) ois.readObject();
		ois.close();
		return books;
	}
	
	
	public   void writeIndexAuthor() throws FileNotFoundException, IOException {
		if (authors.isEmpty()) {
			System.err.println("Index Author is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("authors.ser")));
		ois.writeObject(authors);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public   Map<Author, List<Integer>> readIndexAuthor(InputStream st) throws FileNotFoundException, IOException, ClassNotFoundException {

		ObjectInputStream ois = new ObjectInputStream(st);
		authors = (Map<Author, List<Integer>>) ois.readObject();
		ois.close();
		return authors;
	}
	
	
	public void writeIndexTitle() throws FileNotFoundException, IOException {
		if (titles.isEmpty()) {
			System.err.println("Index Title is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("titles.ser")));
		ois.writeObject(titles);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public  Map<String, List<Integer>> readIndexTitle(InputStream st) throws FileNotFoundException, IOException, ClassNotFoundException {
	
		      ObjectInputStream ois = new ObjectInputStream(st);
				titles = (Map<String, List<Integer>>) ois.readObject();
				ois.close();
				return titles;
	}	
}