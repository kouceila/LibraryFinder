package com.sorboone.daar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.json.JSONObject;

public class ConnexionHandler {
	
	
	public static String getFileContent(String fileURL) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		
		int status = con.getResponseCode();
		BufferedReader reader;
		String line;
		StringBuilder responseContent = new StringBuilder();
				
				
		if (status >= 300) {
			/*reader = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
			}
			reader.close();*/
			return "";
		}
		else {
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while ((line = reader.readLine()) != null) {
				responseContent.append(line);
			}
			reader.close();
		}	
		
		return responseContent.toString();
	}

}
