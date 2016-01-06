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
 * Class for training a Bag of Words, i.e. converting word counts to word scores using some probability theory.
 */
public class TrainNaiveBayes implements Definitions {
	
	private HashMap<BagOfWords, HashMap<String, Double>> trainedBagOfWords; 
	
	/**
	 * Constructor
	 */
	public TrainNaiveBayes() {
		trainedBagOfWords = new HashMap<BagOfWords, HashMap<String, Double>>();
	}
	
	/**
	 * Constructor which directly trains the BagOfWords that's provided in the argument
	 */
	public TrainNaiveBayes(BagOfWords bow) {
		trainedBagOfWords = new HashMap<BagOfWords, HashMap<String, Double>>();
		TrainMultinomialNaiveBayes(bow);
	}
	
	/**
	 * Gives the trained bag of words back, with the priors included
	 * @return HashMap of a BagOfWords (a HashMap in itself) and a HashMap with the categories and its priors
	 */
	public HashMap<BagOfWords, HashMap<String, Double>> getTrainedBagOfWords(){
		return trainedBagOfWords;
	}
	
	/**
	 * Train a BagOfWords containing only the amounts of occurences of distinct words, and save the results into the trainedBagOfWords object.
	 * @param bow to train.
	 */
	public void TrainMultinomialNaiveBayes(BagOfWords bow){
		HashMap<BagOfWords, HashMap<String, Double>> resultMap = new HashMap<BagOfWords, HashMap<String, Double>>();
		int totalDocs = 0;
		HashMap<String, Double> priors = new HashMap<String, Double>();
		for (String cat : bow.getCategories()){
			totalDocs+=bow.getDocumentStats(cat);
		}
		for (String cat : bow.getCategories()){
			priors.put(cat, (bow.getDocumentStats(cat)*1.0/totalDocs*1.0));
		}
		for (Entry<String, HashMap<String, Double>> entry : bow.getBag().entrySet()){
			bow.getBag().put(entry.getKey(), ProbabilityFormula.getMultinomialWordScore(bow.getCategories(), entry.getKey(), bow));
		}
		//System.out.println(bow.getBag().toString());
		resultMap.put(bow, priors);
		this.trainedBagOfWords = resultMap;
	}
}
