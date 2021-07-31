package core;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class ProgressBar {
	private static JFrame frame;
	private static JProgressBar progressBar;
	private static JLabel label;
	private static int position;
	
	public static void initializeProgressBar(int maxFiles) {		
		progressBar = new JProgressBar(0, maxFiles);
		progressBar.setBounds(40, 40, 157, 30);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		
		label = new JLabel("", SwingConstants.CENTER);
		label.setBounds(-2, 8, 240, 30);
		
		frame = new JFrame();
		frame.add(progressBar);
		frame.add(label);
		frame.setSize(250, 150);
		frame.setLayout(null);
		frame.setLocationRelativeTo(null); //when set to null it spawns the jFrame at the center of the screen
		frame.setResizable(false);
		frame.setTitle("Sorter");
		frame.setVisible(true);
		
		position = 0;
	}
	
	public static void next() {
		position += 1;
		progressBar.setValue(position);
	}
	
	public static void setString(String string) {
		label.setText(string);
	}
}
