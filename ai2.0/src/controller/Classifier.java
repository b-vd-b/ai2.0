/**
 * 
 */
package controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import model.BagOfWords;
import model.Definitions;

/**
 * @author bvdb
 *
 */
public class Classifier implements Definitions {

	public static HashMap<String, Double> ApplyMultinomialNaiveBayes(HashMap<BagOfWords, HashMap<String, Double>> trainData, File file){
		String[] testFile = FileHandler.ReadFile(file);
		HashMap<String, Double> scoreMap = new HashMap<String, Double>();
		for (Entry<BagOfWords, HashMap<String, Double>> entry : trainData.entrySet()){
			
			for (String cat : entry.getKey().getCategories()){
				
				double score = entry.getValue().get(cat);
				
				for (String word : testFile) {
					if (entry.getKey().getBag().containsKey(word)){
						score += Math.log10(entry.getKey().getBag().get(word).get(cat))/Math.log10(2);
						//System.out.println(Math.log(entry.getKey().getBag().get(word).get(cat)));
						//System.out.println(score);
					} else {
						score += Math.log10((SMOOTHING/(entry.getKey().getTotalWordStats(cat)+(SMOOTHING*entry.getKey().getTotalDistinctWordsStats()))))/Math.log10(2);
					}
				}
				
				scoreMap.put(cat, score);
			}
		}
		System.out.print(scoreMap.toString() + " \t-> ");
		return scoreMap;
	}
	
	public static String verdict(HashMap<String,Double> scores){
		String result ="";
		double max = Double.NEGATIVE_INFINITY;
		for (Entry<String, Double> entry : scores.entrySet()) {
			double elem = entry.getValue();
			if (elem > max){
				max = elem;
				result = entry.getKey();
			}
		}
		return result;
	}
}
