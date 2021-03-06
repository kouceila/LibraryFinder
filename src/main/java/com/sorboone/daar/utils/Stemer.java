package com.sorboone.daar.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import com.sorbonne.daar.data.Keyword;

public class Stemer {
	public static String stem(String term) throws IOException {

		  TokenStream tokenStream = null;
		  try {

		    // tokenize
		    tokenStream = new ClassicTokenizer(Version.LUCENE_40, new StringReader(term));
		    // stem
		    tokenStream = new PorterStemFilter(tokenStream);

		    // add each token in a set, so that duplicates are removed
		    Set<String> stems = new HashSet<String>();
		    CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
		    tokenStream.reset();
		    while (tokenStream.incrementToken()) {
		      stems.add(token.toString());
		    }

		    // if no stem or 2+ stems have been found, return null
		    if (stems.size() != 1) {
		      return null;
		    }
		    String stem = stems.iterator().next();
		    // if the stem has non-alphanumerical chars, return null
		    if (!stem.matches("[a-zA-Z0-9-]+")) {
		      return null;
		    }

		    return stem;

		  } finally {
		    if (tokenStream != null) {
		      tokenStream.close();
		    }
		  }

		}
	
	public static <T> T find(Collection<T> collection, T example) {
		  for (T element : collection) {
		    if (element.equals(example)) {
		      return element;
		    }
		  }
		  collection.add(example);
		  return example;
	}
	
	public static List<Keyword> guessFromString(String input) throws IOException {

		  TokenStream tokenStream = null;
		  try {

		    // hack to keep dashed words (e.g. "non-specific" rather than "non" and "specific")
		    input = input.replaceAll("-+", "-0");
		    // replace any punctuation char but apostrophes and dashes by a space
		    input = input.replaceAll("[\\p{Punct}&&[^'-]]+", " ");
		    // replace most common english contractions
		    input = input.replaceAll("(?:'(?:[tdsm]|[vr]e|ll))+\\b", "");

		    // tokenize input
		    tokenStream = new ClassicTokenizer(Version.LUCENE_40, new StringReader(input));
		    // to lowercase
		    tokenStream = new LowerCaseFilter(Version.LUCENE_40, tokenStream);
		    // remove dots from acronyms (and "'s" but already done manually above)
		    tokenStream = new ClassicFilter(tokenStream);
		    // convert any char to ASCII
		    tokenStream = new ASCIIFoldingFilter(tokenStream);
		    // remove english stop words
		    tokenStream = new StopFilter(Version.LUCENE_40, tokenStream, EnglishAnalyzer.getDefaultStopSet());

		    List<Keyword> keywords = new LinkedList<Keyword>();
		    CharTermAttribute token = tokenStream.getAttribute(CharTermAttribute.class);
		    tokenStream.reset();
		    while (tokenStream.incrementToken()) {
		      String term = token.toString();
		      // stem each term
		      String stem = stem(term);
		      if (stem != null) {
		        // create the keyword or get the existing one if any
		        Keyword keyword = find(keywords, new Keyword(stem.replaceAll("-0", "-")));
		        // add its corresponding initial token
		        keyword.add(term.replaceAll("-0", "-"));
		      }
		    }

		    // reverse sort by frequency
		    Collections.sort(keywords);

		    return keywords;

		  } finally {
		    if (tokenStream != null) {
		      tokenStream.close();
		    }
		  }

		}

}
