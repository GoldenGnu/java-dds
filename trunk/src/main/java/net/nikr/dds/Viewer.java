/*
 * Viewer.java - This file is part of Java DDS ImageIO Plugin
 *
 * Copyright (C) 2011 Niklas Kyster Rasmussen
 *
 * Java DDS ImageIO Plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * Java DDS ImageIO Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Java DDS ImageIO Plugin; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * FILE DESCRIPTION:
 * [TODO] DESCRIPTION
 */

package net.nikr.dds;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;


public class Viewer {
	
	//Frame
	private JFrame jFrame;
	private JPanel jMainPanel;
	private List<BufferedImage> images;
	
	private final JFileChooser jFileChooser = new JFileChooser();
	
	public Viewer() {
		jFileChooser.setAcceptAllFileFilterUsed(false);
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jFileChooser.setFileFilter(new DdsFilter(true));
		
		jMainPanel = new JPanel();
		jMainPanel.setLayout( new BoxLayout(jMainPanel, BoxLayout.Y_AXIS) ); 
		
		JScrollPane scroll = new JScrollPane(jMainPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu = new JMenu("File");
		jMenuBar.add(jMenu);
		JMenuItem jMenuItem = new JMenuItem("Open");
		jMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});
		jMenu.add(jMenuItem);
		
		jMenuItem = new JMenuItem("Clear");
		jMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		jMenu.add(jMenuItem);
		
		jFrame = new JFrame("DDS Viewer");
		jFrame.setSize(850, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.getContentPane().add(scroll);
		jFrame.setJMenuBar(jMenuBar);
		//jFrame.pack();
		jFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
									createAndShowGUI();
							}
					});
	}
	private static void createAndShowGUI() {
		initLookAndFeel();

		//Make sure we have nice window decorations.
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		Viewer runtest = new Viewer();
	}

	private static void initLookAndFeel() {
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName(); //System
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void open(){
		List<File> fileList = new ArrayList<File>();
		int returnVal = jFileChooser.showOpenDialog(jFrame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser.getSelectedFile();
			if (file.isDirectory()){
				File[] files = file.listFiles(new DdsFilter(false));
				if (files.length > 0){
					int value = JOptionPane.showConfirmDialog(jFrame, "Open directory?\n"+files.length+" DDS files", "Open Directory", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (value != JOptionPane.OK_OPTION){
						return;
					}
				} else {
					JOptionPane.showMessageDialog(jFrame, "No dds files in directory...", "Open Directory", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				jMainPanel.removeAll();
				jMainPanel.add( new JLabel("Loading..."));
				jMainPanel.updateUI();
				LoadDir loadDir = new LoadDir(file);
				loadDir.execute();
			} else {
				fileList = loadFile(file);
				build(fileList);
			}
			
        }
	}
	
	private void clear(){
		images = new ArrayList<BufferedImage>();
		jMainPanel.removeAll();
		jMainPanel.updateUI();
	}

	private List<File> loadDir(final File dir){
		File[] files = dir.listFiles(new DdsFilter(false));
		
		List<File> fileList = new ArrayList<File>();
		
		images = new ArrayList<BufferedImage>();
		
		for (File file : files){
			try {
				images.add(ImageIO.read(file));
				fileList.add(file);
			} catch (IOException ex) {
				System.out.println("Failed to load: "+file.getName());
			}
		}
		return fileList;
	}
	private List<File> loadFile(final File file){
		images = new ArrayList<BufferedImage>();
		List<File> fileList = new ArrayList<File>();
		try {
			images.add(ImageIO.read(file));
			fileList.add(file);
		} catch (IOException ex) {
			System.out.println("Failed to load: "+file.getName());
		}
		return fileList;
	}
	
	private void build(List<File> fileList){
		jMainPanel.removeAll();
		int i = 0;
		for (BufferedImage image : images){
			JPanel jPanel = new JPanel();
			jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
			jPanel.setAlignmentX(JScrollPane.CENTER_ALIGNMENT);
			jMainPanel.add(jPanel);
			
			JLabel imageLabel = new JLabel();
			imageLabel.setBackground( Color.magenta);
			imageLabel.setOpaque(true);
			imageLabel.setIcon(new ImageIcon(image));
			
			File file = new File(fileList.get(i).getName());
			JLabel textLabel = new JLabel(file.getName());
			textLabel.setHorizontalAlignment(JLabel.CENTER);
			
			Dimension dimension = new Dimension(Math.max(textLabel.getPreferredSize().width, imageLabel.getPreferredSize().width), textLabel.getPreferredSize().height);
			textLabel.setMaximumSize(dimension);
			textLabel.setPreferredSize(dimension);
			textLabel.setMinimumSize(dimension);
			
			jPanel.add(imageLabel);
			jPanel.add(textLabel);

			i++;
		}
		jMainPanel.updateUI();
	}
	
	public class DdsFilter extends javax.swing.filechooser.FileFilter implements FileFilter {
		
		private boolean acceptDirectories;

		public DdsFilter() {
			this(true);
		}
		
		public DdsFilter(boolean acceptDirectories) {
			this.acceptDirectories = acceptDirectories;
		}
		
		@Override
		public boolean accept(File file) {
			if (file.isDirectory()) return acceptDirectories;
			int index = file.getName().lastIndexOf(".");
			if (index < 0) return false;
			return file.getName().substring(index).toLowerCase().equals(".dds");
		}

		@Override
		public String getDescription() {
			return "DDS files";
		}
	}
	
	class LoadDir extends SwingWorker<Void, Void>{

		private File file;
		private List<File> fileList;

		public LoadDir(File file) {
			this.file = file;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			fileList = loadDir(file);
			return null;
		}

		@Override
		protected void done() {
			try {
				get();
				build(fileList);
			} catch (Exception ex) {
				images = new ArrayList<BufferedImage>();
				JOptionPane.showMessageDialog(jFrame, "Failed to load images....", "Error", JOptionPane.WARNING_MESSAGE);
			}
		}
		
		
		
	}
}
