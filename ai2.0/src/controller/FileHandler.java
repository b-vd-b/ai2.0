package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import model.Definitions;

public class FileHandler implements Definitions {

	public static String[] ReadFile(File file){

		StringBuilder allwords = new StringBuilder();
		
		//System.out.println("TOTALDOCUMENTCOUNT: " + totalDocumentCount);
		if (file.isFile() && file.getName().endsWith(".txt")){
			try {
				//totalDocumentCount++;
				allwords.append(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Tokenizer.tokenize(allwords.toString());
	}
	
	public static String[] ReadFiles(File[] listOfFiles){
		
		StringBuilder allwords = new StringBuilder();

		for (File file : listOfFiles){
			if (file.isFile() && file.getName().endsWith(".txt")){
				try {
					//totalDocumentCount++;
					allwords.append(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return Tokenizer.tokenize(allwords.toString());
		
	}
	
	public static String[] readRandomTestFile(String category){
		File folder = new File(TEST_DIR+"/"+SET+"/"+category);
		File[] listOfFiles = folder.listFiles();
		
		System.out.println(folder.getAbsolutePath());
		
		StringBuilder allwords = new StringBuilder();
		
		int randomFile = (int) (Math.random()*listOfFiles.length);
		File file = listOfFiles[randomFile];
		System.out.println(file.getName());
		if (file.isFile() && file.getName().endsWith(".txt")){
			try {
				allwords.append(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Went through a random file");
		
		String[] result = Tokenizer.tokenize(allwords.toString());
		
		return result;
	}
	
	public static File getRandomTestFile(String category){
		File folder = new File(TEST_DIR+"/"+SET+"/"+category);
		File[] listOfFiles = folder.listFiles();
		
		int randomFile = (int) (Math.random()*listOfFiles.length);
		return listOfFiles[randomFile];
	}
	
}
