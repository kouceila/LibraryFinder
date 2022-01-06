package data;

import java.io.Serializable;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Book implements Serializable {
	
	private int id;
	private String title;
	
	private List<Author> authors;
	

	private String fileURL;
	private String imageURL;
	private int download_count;
	
	public Book(JSONObject book) throws JSONException {
		this.id = book.getInt("id");
		this.title = book.getString("title");
		JSONArray aut = book.getJSONArray("authors");
		
		for (int i=0; i<aut.length(); i++ ) {
			this.authors.add(new Author(aut.getJSONObject(i)));
		}
		
		JSONObject format = book.getJSONObject("formats");
		this.fileURL = format.getString("text/plain");
		this.imageURL = format.getString("image/jpeg");
		
		this.download_count = book.getInt("download_count");
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Author> getAuthors() {
		return authors;
	}
	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	public String getFileURL() {
		return fileURL;
	}
	public void setFileURL(String fileURL) {
		this.fileURL = fileURL;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public int getDownload_count() {
		return download_count;
	}
	public void setDownload_count(int download_count) {
		this.download_count = download_count;
	}
	
	

}
