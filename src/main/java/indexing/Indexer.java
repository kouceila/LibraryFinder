package indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import data.Author;
import data.Book;
import data.Keyword;
import utils.Stemer;

public class Indexer  {
	
	private static List<Book> books = new ArrayList<>() ; 
	private static Map<Author, List<Integer>> authors = new HashMap<>();
	private static Map<String, List<Integer>> titles = new HashMap<>();
	private static Map<Keyword, List<Integer>> keywords = new HashMap<>();
	
	
	
	public static void createBookIndex() throws IOException, JSONException {
		
		for (int index = 1; index < 16; index++) {
			URL url = new URL("https://gutendex.com/books?ids=" + index);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			int status = con.getResponseCode();
			System.out.println("begin" + index);
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
			
			if ( res.getJSONArray("results").length() >0 )
				books.add(new Book(resultsArray.getJSONObject(0)));
			
		}
				System.out.println(books.size());
	}
	
	
	public static void createAuthorIndex() {
		
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
	
	public static void createTitleIndex() {
	
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
	
	public static void createKeywordsIndex() throws IOException {
		for (Book book : books) {
			String link = book.getFileURL();
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
				if ( keywords.containsKey(keyword) ) {
					keywords.get(keyword).add(book.getId());
				}
				else {
					List<Integer> bookIds = new ArrayList<>();
					bookIds.add(book.getId());
					keywords.put(keyword, bookIds);
				}
 			}
			
		}
	}
	
	
	public static void writeIndexKeywords() throws FileNotFoundException, IOException {
		if (keywords.isEmpty()) {
			System.err.println("Index keywords is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("keywords.ser")));
		ois.writeObject(keywords);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Keyword, List<Integer>> readIndexkeywords() throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("keywords.ser"));
		keywords = (Map<Keyword, List<Integer>>) ois.readObject();
		ois.close();
		return keywords;
	}


	public static void writeIndexBooks() throws FileNotFoundException, IOException {
		if (books.isEmpty()) {
			System.err.println("Index Books is empty !");
			return; }
		System.err.println("Writing... !");	
		ObjectOutputStream ois = new ObjectOutputStream(new FileOutputStream(new File("books.ser")));
		ois.writeObject(books);
		ois.flush();
		ois.close();	
	}
	
	@SuppressWarnings("unchecked")
	public static List<Book> readIndexBooks() throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("books.ser"));
		books = (List<Book>) ois.readObject();
		ois.close();
		return books;
	}
	
	
	public static void writeIndexAuthor() throws FileNotFoundException, IOException {
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
	public static Map<Author, List<Integer>> readIndexAuthor() throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("authors.ser"));
		authors = (Map<Author, List<Integer>>) ois.readObject();
		ois.close();
		System.out.println(authors);
		return authors;
	}
	
	
	public static void writeIndexTitle() throws FileNotFoundException, IOException {
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
	public static Map<String, List<Integer>> readIndexTitle() throws FileNotFoundException, IOException, ClassNotFoundException {
		System.err.println("READING... !");	
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("titles.ser"));
		titles = (Map<String, List<Integer>>) ois.readObject();
		ois.close();
		System.out.println(titles);
		return titles;
	}
	
	
	
	
	
	
	
}
