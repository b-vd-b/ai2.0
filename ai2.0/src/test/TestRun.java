/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import controller.Classifier;
import controller.FileHandler;
import controller.Learner;
import controller.TrainNaiveBayes;
import model.BagOfWords;
import model.Definitions;
import model.ProbabilityFormula;

/**
 * @author bvdb
 *
 */
public class TestRun implements Definitions {

	/**
	 * Test run the classifier. This method does all operations needed to see how the classifier works.
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		 * Let's create a new Bag of Words
		 */
		BagOfWords bow = new BagOfWords();
		
		/*
		 * Fill the initiated Bag of Words with the training files found in the directories defined in Definitions 
		 */
		bow.fillVocabulary(CATEGORIES);
		
		/*
		 * Create an object which has the trainedBagOfWords, trained with the provided BagOfWords.
		 */
		TrainNaiveBayes tnb = new TrainNaiveBayes(bow);
		
		/*
		 * Let's loop through a folder with testing files, and test them against the trainedBagOfWords.
		 */
		File folder = new File(TEST_DIR+"/"+SET+"/M");
		File[] listOfFiles = folder.listFiles();
		if (listOfFiles.length != 0){
			for (int i=0; i < listOfFiles.length; i++){
				File file = listOfFiles[i];
				/*
				 * Create a verdict, i.e. to which category does the testing file belong, according to the classifier 
				 */
				String verdict =  Classifier.verdict(
						/*
						 * Apply Multinomial Bayes probability theory to the testing file, against the trained Bag Of Words
						 */
						Classifier.ApplyMultinomialNaiveBayes(tnb.getTrainedBagOfWords(), file)
						);
				
				System.out.println("The verdict is: " + verdict);
				
				/*
				 * Here we let the user provide feedback on the decision of the classifier.
				 */
				try {
					Learner.processFeedbackOnVerdict(verdict, bow, file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("No more testing files");
		} else {
			System.out.println("There are no testing files.");
		}
		
/*		Classifier.ApplyMultinomialNaiveBayes(TrainNaiveBayes.TrainMultinomialNaiveBayes(CATEGORIES, bow), FileHandler.getRandomTestFile("F"));
		System.out.println(ProbabilityFormula.getMultinomialWordScore(CATEGORIES, "because", bow));
		
		bow1.readAllWordsInCategory(CATEGORY_A, bow1);
		System.out.println(bow1.bagOfWords.toString());
		BagOfWords bow2 = new BagOfWords();
		bow2.readAllWordsInCategory(CATEGORY_B, bow2);
		System.out.println(bow2.bagOfWords.toString());
		ProbabilityFormula.totalDistinctWords = ProbabilityFormula.countDistinctWords(bow1, bow2);
		System.out.println(CATEGORY_A+"WordCount: \t\t\t" + bow1.totalWordCount);
		System.out.println(CATEGORY_B+"WordCount: \t\t\t" + bow2.totalWordCount);
		System.out.println(CATEGORY_A+"WordCount: \t\t\t" + bow1.distinctWordCount);
		System.out.println(CATEGORY_B+"WordCount: \t\t\t" + bow2.distinctWordCount);
		System.out.println("TotalDistinctWordCount: \t" + ProbabilityFormula.totalDistinctWords);*/
		
	}

	

}
