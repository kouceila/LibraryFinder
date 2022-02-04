package com.sorboone.daar.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sorbonne.daar.data.Author;
import com.sorbonne.daar.data.Book;

public class JsonConverter {
	
	public static JSONObject bookToJson(Book book) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("id", book.getId());
		json.put("title", book.getTitle());
		JSONArray authors = new JSONArray();
		for (Author author : book.getAuthors()) {
			authors.put(authorToJson(author));
		}
		json.put("authors", authors);
		json.put("image", book.getImageURL());
		json.put("url", book.getFileURL());
		
		return json;
		
	}
	
	public static JSONObject authorToJson(Author author) throws JSONException{
		
		JSONObject json = new JSONObject();
		
		json.put("name", author.getName());
		
		return json;
		
		
	}

}
