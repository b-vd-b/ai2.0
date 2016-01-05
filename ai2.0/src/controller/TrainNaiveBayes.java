/**
 * 
 */
package controller;

import java.util.HashMap;
import java.util.Map.Entry;

import model.BagOfWords;
import model.Definitions;
import model.ProbabilityFormula;

/**
 * @author bvdb
 *
 */
public class TrainNaiveBayes implements Definitions {
	
	public static HashMap<BagOfWords, HashMap<String, Double>> trainedBagOfWords; 
	
	public TrainNaiveBayes() {
		trainedBagOfWords = new HashMap<BagOfWords, HashMap<String, Double>>();
	}
	
	public HashMap<BagOfWords, HashMap<String, Double>> getTrainedBagOfWords(){
		return trainedBagOfWords;
	}

	public static void TrainMultinomialNaiveBayes(String[] categories, BagOfWords bow){
		HashMap<BagOfWords, HashMap<String, Double>> resultMap = new HashMap<BagOfWords, HashMap<String, Double>>();
		int totalDocs = 0;
		HashMap<String, Double> priors = new HashMap<String, Double>();
		for (String cat : categories){
			totalDocs+=bow.getDocumentStats(cat);
		}
		for (String cat : categories){
			priors.put(cat, (bow.getDocumentStats(cat)*1.0/totalDocs*1.0));
		}
		for (Entry<String, HashMap<String, Double>> entry : bow.getBag().entrySet()){
			bow.getBag().put(entry.getKey(), ProbabilityFormula.getMultinomialWordScore(categories, entry.getKey(), bow));
		}
		//System.out.println(bow.getBag().toString());
		resultMap.put(bow, priors);
		trainedBagOfWords = resultMap;
	}
}
