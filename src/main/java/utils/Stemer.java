package utils;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;

public class Stemer {
	public static String stem(String term) throws IOException {

		  TokenStream tokenStream = null;
		  try {

		    // tokenize
		    tokenStream = new StandardTokenizer(Version.LUCENE_36, new StringReader(term));
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

}
