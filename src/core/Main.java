package core;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
	public static void main(String[] args) throws IOException, HeadlessException, URISyntaxException {
		idk(new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath());
		System.exit(1);
	}
	
	public static void idk(String path) throws IOException {
		File folder = new File(path);
		File[] files = folder.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ini");
			}
		});
		
		//non replays available
		if(files.length == 0) {
			JOptionPane.showMessageDialog(new JFrame(),"0 replays found.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		ProgressBar.initializeProgressBar(files.length);
		
		for(File iniFile : files) {
			String[] nomes = getPlayersName(iniFile);
			
			createFolder(iniFile, nomes);
			deleteFiles(iniFile);
			
			ProgressBar.next();
			ProgressBar.setString(nomes[0]);
		}
		JOptionPane.showMessageDialog(new JFrame(), files.length + " replays were sorted!");
	}
	
	protected static void createFolder(File iniFile, String[] playersName) throws IOException {
		//getting data from last modified attribute
		BasicFileAttributes attr = Files.readAttributes(iniFile.toPath(), BasicFileAttributes.class);
		Date fileDate = new Date(attr.lastModifiedTime().toMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		
		String basePath = iniFile.getParent() + "\\";
		String nextPath = "\\" + dateFormat.format(fileDate);
		
		//create nickname/date folder and copy replays into it
		for(String player : playersName) {
			try {
				new File(basePath + player).mkdir();
				new File(basePath + player + nextPath).mkdir();
				copyFiles(iniFile, basePath + player + nextPath);
			} catch (InvalidPathException e) {
				player = "Unknown Player";
				new File(basePath + player).mkdir();
				new File(basePath + player + nextPath).mkdir();
				copyFiles(iniFile, basePath + player + nextPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	protected static void copyFiles(File iniFile, String dir) throws IOException {
		Files.copy(iniFile.toPath(), new File(dir + "\\" + iniFile.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
		Files.copy(new File(iniFile.getPath().replace(".ini",".rnd")).toPath(), new File(dir + "\\" + iniFile.getName().replace(".ini",".rnd")).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	protected static void deleteFiles(File iniFile) throws IOException {
		Files.delete(new File(iniFile.getPath().replace(".ini", ".rnd")).toPath());
		Files.delete(iniFile.toPath());
	}
	
	protected static String[] getPlayersName(File file) throws FileNotFoundException {
		String[] players = new String[2];
		Scanner sc = new Scanner(file,"UTF-8");
		
		for(int i = 0; i < 2; i++) {
			while(sc.hasNextLine()) {
				players[i] = sc.nextLine();
				if(players[i].contains("P" + (i+1) + "Name")) {
					players[i] = players[i].substring(7);
					break;
				}
			}
			if(players[i] == null) {
				players[i] = "Unknown Player";
			}
		}
		
		sc.close();
		return formatPlayersName(players);
	}
	
	protected static String[] formatPlayersName(String[] players) {
		String[] formattedPlayers = new String[2];
		for(int i = 0; i < 2; i++) {			
			formattedPlayers[i] = players[i]
								.replace("/","༼")
								.replace("\\","༽")
								.replace(">", "⊳")
								.replace("<", "⊲")
								.replace("|", "ￜ")
								.replace(":", "╠")
								.replace("?","¿")
								.replace("*", "⋄")
								.replace("\"", "'");
		}
		return formattedPlayers;
	}
}
