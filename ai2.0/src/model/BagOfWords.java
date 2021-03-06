/**
 * 
 */
package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import controller.FileHandler;
import controller.Tokenizer;
import controller.TrainNaiveBayes;


/**
 * @author bvdb
 *
 */
public class BagOfWords implements Definitions {

	public HashMap<String, HashMap<String,Double>> bagOfWords;
	public HashMap<String, Integer> documentStats;
	public HashMap<String, Integer> distinctWordStats;
	public HashMap<String, Integer> totalWordStats;
	private String[] categories;

	public BagOfWords(){
		bagOfWords = new HashMap<String, HashMap<String, Double>>();
		documentStats = new HashMap<String, Integer>();
		distinctWordStats = new HashMap<String, Integer>();
		totalWordStats = new HashMap<String, Integer>();
		
	}

	public HashMap<String, HashMap<String,Double>> getBag() {
		return bagOfWords;
	}

	public HashMap<String, Integer> getDocumentStats() {
		return documentStats;
	}
	
	public int getDocumentStats(String cat){
		return getDocumentStats().get(cat);
	}
	
	public HashMap<String, Integer> getDistinctWordStats() {
		return distinctWordStats;
	}
	
	public int getDistinctWordStats (String cat){
		return getDistinctWordStats().get(cat);
	}
	
	public int getTotalDistinctWordsStats(){
		return this.getBag().size();
	}
	
	public HashMap<String, Integer> getTotalWordStats() {
		return totalWordStats;
	}
	
	public int getTotalWordStats(String cat){
		return getTotalWordStats().get(cat);
	}
	
	public String[] getCategories(){
		return this.categories;
	}
	
	public void addCategory(String cat){
		final List<String> categories = new ArrayList<String>();
		Collections.addAll(categories, this.getCategories());
		Collections.addAll(categories, cat);
		this.categories = categories.toArray(new String[categories.size()]);
		System.out.println(this.categories.toString());
	}

	
	public void fillVocabulary(String[] categories){
		this.categories = categories;
		this.bagOfWords.clear();
		//int totalDocumentCount = 0;
		for (String cat : categories){
			File folder = new File(TRAINING_DIR+"/"+SET+"/"+cat);
			File[] listOfFiles = folder.listFiles();

			System.out.println(folder.getAbsolutePath());
			
			int totalWordsInClass = 0;
			int distinctWordsInClass = 0;
			int docsInClass = listOfFiles.length;
			
			String[] result = FileHandler.ReadFiles(listOfFiles);
			
			for (String word : result){
				totalWordsInClass++;
				if(getBag().containsKey(word)){
					HashMap<String, Double> tempMap = getBag().get(word);
					if (tempMap.get(cat)==null){
						distinctWordsInClass++;
						tempMap.put(cat, 1.0);
					} else{
						tempMap.put(cat, tempMap.get(cat)+1);
					}
					getBag().put(word, tempMap);
				}				
				else {
					distinctWordsInClass++;
					HashMap<String, Double> tempMap = new HashMap<String, Double>();
					tempMap.put(cat, 1.0);
					getBag().put(word, tempMap);
				}
				
			}
			
			documentStats.put(cat, docsInClass);
			distinctWordStats.put(cat, distinctWordsInClass);
			totalWordStats.put(cat, totalWordsInClass);
			
		}
		/*
		 * This removes all words that occur only once for a category
		 */
		HashMap<String, HashMap<String,Double>> deepBow = new HashMap<String, HashMap<String,Double>>(bagOfWords);
		bagOfWords.clear();
		for (Entry<String, HashMap<String, Double>> entry : deepBow.entrySet()){
			String word = entry.getKey();
			HashMap<String, Double> tempMap = entry.getValue();
			HashMap<String, Double> tempMap2 = new HashMap<String, Double>(tempMap);

			for (String tmp : tempMap.keySet()) {
				if (tempMap.get(tmp) == 1.0) {
					tempMap2.remove(tmp);
				}
			}
			for (String tmp2 : tempMap2.keySet()){
			if(tempMap2.containsKey(tmp2)){
				bagOfWords.put(word,tempMap2);
				}
			}
		}
		//System.out.println(bagOfWords.toString());
		System.out.println(documentStats);
		//System.out.println(getBag().toString());
		System.out.println(distinctWordStats);
		System.out.println(totalWordStats);
	}
	
	public TrainNaiveBayes trainVocabulary(BagOfWords bow){
		return new TrainNaiveBayes(bow);
	}
	
	public void fillAndTrainVocabulary(BagOfWords bow){
		fillVocabulary(bow.getCategories());
		trainVocabulary(bow);
	}
}
