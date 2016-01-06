/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.*;

import view.GUI;
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

	protected static int confusion_matrix[][] = new int[25][25];
	protected static int a = 0;
	protected static int b = 0;
	protected static int c = 0;
	protected static int d = 0;
	
	/**
	 * Test run the classifier. This method does all operations needed to see how the classifier works.
	 * @param args Please provide "GUI" or "TUI" for the desired interface, or neither to use the TUI.
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
		for(int arg=1; arg<args.length;arg++){
			File folder = new File(TEST_DIR+"/"+SET+"/"+args[arg]);
	
		if (args[0].equalsIgnoreCase("gui")){
			GUI.createAndShowGUI(bow);
		} else {
			/*
			 * Let's loop through a folder with testing files, and test them against the trainedBagOfWords.
			 */
			File folder = new File(TEST_DIR+"/"+SET+"/M");
			File[] listOfFiles = folder.listFiles();
			if (listOfFiles.length != 0){
				for (int i=0; i < listOfFiles.length; i++){
					File file = listOfFiles[i];
					//tnb.TrainMultinomialNaiveBayes(bow);
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
					if(arg==1 && verdict.equals(args[1]) ){
						a++;
					} else if(arg==1 && verdict.equals(args[2])){
						b++;
					} else if(arg==2 && verdict.equals(args[1])){
						c++;
					}else if (arg==2 && verdict.equals(args[2])){
						d++;
					} 
					
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
			System.out.println("");
			System.out.println("Confusion matrix of n="+(a+b+c+d)+" classifications.");
			System.out.println("---------------------------------------------");
			System.out.println("| act / pred   | FIRST_CLASS | SECOND_CLASS |");
			System.out.println("|--------------|-------------|--------------|");
			System.out.println("| FIRST_CLASS  |      "+a+"     |       "+b+"      |");
			System.out.println("|--------------|-------------|--------------|");
			System.out.println("| SECOND_CLASS |      "+c+"      |       "+d+"      |");
			System.out.println("---------------------------------------------");
			System.out.println("Accuracy of classifier = " + 100*((double)(a+d)/(a+b+c+d)) + "%");
			System.out.println("Error rate of classifier = " + 100*((double)(b+c)/(a+b+c+d)) + "%");
		}

	}

	

}
