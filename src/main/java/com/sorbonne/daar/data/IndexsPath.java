package com.sorbonne.daar.data;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class IndexsPath {
	
	
	public Path getTitlePath() throws IOException {
		String s = new File("titles.ser").getCanonicalPath().toString();
		System.out.println(s);
		Path path = Paths.get("/LibraryFinder/src/main/java/data/titles.ser");
		if(Files.exists(path) && !Files.isDirectory(path)) {
		      return path;
		}
		else {
			System.out.println("titles not found");
			return null;
		}
	}

}
