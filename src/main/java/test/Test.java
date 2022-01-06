package test;

import java.io.IOException;

import org.json.JSONException;

import indexing.Indexer;

public class Test {
	
	public static void main(String[] args) throws IOException, JSONException {
		Indexer.createBookIndex();
		Indexer.writeIndexBooks();
	}

}
