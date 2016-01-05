package model;

import java.util.HashMap;

public class ProbabilityFormula implements Definitions {

	public static HashMap<String, Double> getMultinomialWordScore(String[] categories, String input, BagOfWords bow) {
		HashMap<String,Double> resultMap = new HashMap<String,Double>();		
		for (String cat : categories)	{
			if (bow.getBag().get(input).get(cat) != null){
				//System.out.print(cat + ": \t" + bow.getTotalWordStats(cat) + " ");
				//System.out.println("totaldistinctwordcount \t" + bow.getTotalDistinctWordsStats());
				resultMap.put(cat, (bow.getBag().get(input).get(cat)+SMOOTHING)/(bow.getTotalWordStats(cat)+(SMOOTHING*bow.getTotalDistinctWordsStats())));
			} else {
				resultMap.put(cat, SMOOTHING/(bow.getTotalWordStats(cat)+(SMOOTHING*bow.getTotalDistinctWordsStats())));
			}
		}
		return resultMap;
		 
	}
}
