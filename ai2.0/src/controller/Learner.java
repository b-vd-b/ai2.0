package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import model.BagOfWords;
import model.Definitions;

public class Learner implements Definitions {

	/**
	 * Method that processes the user input in the console, and acts coherently. A testing-file is moved to the
	 * correct training test after the user has given his input. Mind the permissions on your system!
	 * @param verdict 	The category as which the testing file was classified by Classifier.verdict 
	 * @param bow 		The bag of words against which the testing file was tested
	 * @param file		The testing file
	 * @throws IOException
	 */
	public static void processFeedbackOnVerdict(String verdict, BagOfWords bow, File file) throws IOException {
		
		/*
		 * Create a reader to read the input from the console
		 */
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Is the verdict " + verdict + " correct? (y/n)");
		String s = br.readLine();
		
		/*
		 * Look at what the user submitted (either y or n)
		 */
		if (s.equalsIgnoreCase("y")){
			/*
			 * Is the verdict correct, then move the testing file to the training directory of the correct category.
			 * After that worked without errors, retrain the entire vocabulary with the training set.
			 */
			file.renameTo(new File(TRAINING_DIR+"/"+SET+"/"+verdict+"/"+file.getName()));
			System.out.println("Great, file is moved to training set. Retraining now...");
			/*
			 * Refill and retrain the BagOfWords with the newly added data
			 */
			bow.fillAndTrainVocabulary(bow);
		} else if (s.equalsIgnoreCase("n")) {
			/*
			 * Is the verdict not correct, then show all the possible categories and ask the user to which category the
			 *  test file should belong.
			 */
			System.out.println("Then, which category was it?");
			int count = 1;
			for (String cat : bow.getCategories()) {
				System.out.println(count + ". " + cat);
				count++;
			}
			System.out.println(count + ". -Test file belongs to new category-");
			System.out.println("Input the integer of the correct category, please.");
			/*
			 * Parse the user's input number, and move the file to the correct category in the training set.
			 */
			try{
				int i = Integer.parseInt(br.readLine());
				String cat = "";
				/*
				 * Look if user has chosen to make a new category
				 */
				if (i == count){
					/*
					 * Let user create a name for the new category
					 */
					System.out.println("Please provide a name for the new category");
					String newCat = br.readLine();
					File dir = new File(TRAINING_DIR+"/"+SET+"/"+newCat);
					boolean success = dir.mkdirs();
					if (!success) {
						System.err.println("Failed to create a new category directory");
					} else {
						/*
						 * If directory creation for the new category has succeeded, create a new category in the BagOfWords 
						 *  AND move the testfile to the new directory
						 */
						bow.addCategory(newCat);
						cat = newCat;
						file.renameTo(new File(TRAINING_DIR+"/"+SET+"/"+newCat+"/"+file.getName()));
						System.out.println("Succesfully added "+newCat+" to the database of categories.");
					}
				} else {
					file.renameTo(new File(TRAINING_DIR+"/"+SET+"/"+bow.getCategories()[i-1]+"/"+file.getName()));
					cat = bow.getCategories()[i-1];
				}
				System.out.println("Allright, file is moved to correct training set, "+cat+". Retraining now...");
				/*
				 * Refill and retrain the BagOfWords with the newly added data
				 */
				bow.fillAndTrainVocabulary(bow);
			} catch(NumberFormatException nfe){
				System.err.println("That's no, or an incorrect number");
				processFeedbackOnVerdict(verdict, bow, file);
			} catch(ArrayIndexOutOfBoundsException aioobe){
				System.err.println("That was NOT one of the options. Shame on you.");
				aioobe.printStackTrace();
				processFeedbackOnVerdict(verdict, bow, file);
			}
		} else {
			System.err.println("That's not accepted as an input");
			processFeedbackOnVerdict(verdict, bow, file);
		}

	}
	
}
