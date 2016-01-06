package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.*;

import controller.Classifier;
import controller.TrainNaiveBayes;
import model.BagOfWords;
import model.Definitions;

public class GUI extends JPanel implements Definitions, ActionListener {
	
	JButton openButton;
	JFileChooser fc;
	JComboBox categoryPicker;
	JTextArea log;
	private static TrainNaiveBayes tnb;
	private static BagOfWords bow;
	List<JButton> catButtons = new ArrayList<JButton>();
	File testFile;
	
	public GUI() {
		super(new BorderLayout());
		//BorderLayout bl = new BorderLayout();
		setName("Classifier GUI");
		setLocation(10, 200);
		log = new JTextArea(5,20);
		log.setMargin(new Insets(5,5,5,5));
		log.setEditable(false);
		this.setSize(650, 600);
		JScrollPane logScrollPane = new JScrollPane(log);
		
		fc = new JFileChooser(TEST_DIR+"/"+SET);
		
		openButton = new JButton("Pick test file...");
		openButton.addActionListener(this);
		
		JPanel centerPanel = new JPanel();
		centerPanel.add(openButton);
		
		categoryPicker = new JComboBox<String>(bow.getCategories());
		categoryPicker.addActionListener(this);
		//centerPanel.add(categoryPicker);
		add(centerPanel, BorderLayout.PAGE_START);
		add(logScrollPane, BorderLayout.CENTER);
		
		log.append("Hi, I am a classifier. Pick a file to test against the training set!\n");
		
		JPanel categories = new JPanel();
		
		for (String cat : bow.getCategories()) {
			JButton catButton = new JButton(cat);
			catButton.setEnabled(false);
			catButton.addActionListener(this);
			catButtons.add(catButton);
			categories.add(catButton);
		}
		add(categories, BorderLayout.PAGE_END);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openButton) {
			int returnVal = fc.showOpenDialog(GUI.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				testFile = fc.getSelectedFile();
				log.append("Filling and training Bag Of Words...\n");
				tnb = new TrainNaiveBayes(bow);
				tnb.TrainMultinomialNaiveBayes(bow);
				/*
				 * Apply Multinomial Bayes probability theory to the testing file, against the trained Bag Of Words
				 */
				HashMap<String,Double> scores = Classifier.ApplyMultinomialNaiveBayes(tnb.getTrainedBagOfWords(), testFile);
				String verdict =  Classifier.verdict(scores);
				log.append("The verdict is "+verdict+ "\n" + "The scores were "+scores.toString()+"\n Can you indicate, using the buttons below, to which category the file actually belongs, to improve classification?\n");
				
				for (JButton catBut : catButtons) {
					catBut.setEnabled(true);
				}
			}
		}
		
		for (JButton catBut : catButtons) {
			if (e.getSource() == catBut) {
				testFile.renameTo(new File(TRAINING_DIR+"/"+SET+"/"+catBut.getText()+"/"+testFile.getName()));
				this.log.append("Moved test file to correct training category");
			}
		}
	}
	
	public static void createAndShowGUI(BagOfWords bowArg) {
		//Fill GUI BoW instance
		bow = bowArg;
		
	    //Make sure we have nice window decorations.
	    JFrame.setDefaultLookAndFeelDecorated(false);
	    JDialog.setDefaultLookAndFeelDecorated(false);

	    //Create and set up the window.
	    JFrame frame = new JFrame("Classifier GUI");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    //Create and set up the content pane.
	    JComponent newContentPane = new GUI();
	    newContentPane.setOpaque(true); //content panes must be opaque
	    frame.setContentPane(newContentPane);

	    //Display the window.
	    frame.pack();
	    frame.setVisible(true);
	  }

}
