/**
 * 
 */
package model;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;

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
	public String[] categories;

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
		return categories;
	}

	public void fillVocabulary(String[] categories){
		this.categories = categories;
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
			System.out.println(documentStats);
			//System.out.println(getBag().toString());
			System.out.println(distinctWordStats);
			System.out.println(totalWordStats);
		}
	}
	
	public TrainNaiveBayes trainVocabulary(BagOfWords bow){
		return new TrainNaiveBayes(this);
	}
	
	public void fillAndTrainVocabulary(String[] categories){
		fillVocabulary(categories);
		trainVocabulary(this);
	}
}
