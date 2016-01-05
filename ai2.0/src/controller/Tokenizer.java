package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Definitions;

public class Tokenizer implements Definitions {

	public static String[] tokenize(String in){
		String[] out = in.toLowerCase().replaceAll("[^a-z'0-9]"," ").split("\\s+");
		final List<String> tempList = new ArrayList<String>();
		Collections.addAll(tempList, out);
		final List<String> stopWords = new ArrayList<String>();
		Collections.addAll(stopWords, STOPWORDS);
		tempList.removeAll(stopWords);
		final List<String> alphabet = new ArrayList<String>();
		Collections.addAll(alphabet, ALPHABET);
		tempList.removeAll(alphabet);
		out = tempList.toArray(new String[tempList.size()]);
		return out;
	}
	
}
