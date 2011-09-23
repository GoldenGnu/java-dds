/*
 * Viewer.java - This file is part of Java DDS ImageIO Plugin
 *
 * Copyright (C) 2011 Niklas Kyster Rasmussen
 * 
 * COPYRIGHT NOTICE:
 * Java DDS ImageIO Plugin is based on code from the DDS GIMP plugin.
 * Copyright (C) 2004-2010 Shawn Kirst <skirst@insightbb.com>,
 * Copyright (C) 2003 Arne Reuter <homepage@arnereuter.de>
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
 * TODO Write File Description for Viewer.java
 */

package net.nikr.dds;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


public class Viewer implements KeyListener, ActionListener{
	
	private static final String ACTION_OPEN = "ACTION_OPEN";
	private static final String ACTION_RBG = "ACTION_RBG";
	private static final String ACTION_YCOCG = "ACTION_YCOCG";
	private static final String ACTION_YCOCG_SCALED = "ACTION_YCOCG_SCALED";
	private static final String ACTION_ALPHA_EXPONENT = "ACTION_ALPHA_EXPONENT";
	
	private enum ColorType{
		RBG,
		YCOCG,
		YCOCG_SCALED,
		ALPHA_EXPONENT
	}
	
	//Frame
	private JFrame jFrame;
	private JLabel jImageLabel;
	private JLabel jTextLabel;
	
	private List<File> files;
	private Item item;
	private ColorType type;
	private int fileIndex;
	private int mipMap;
	private boolean updating = false;
	
	private final JFileChooser jFileChooser = new JFileChooser();
	
	public Viewer() {
		jFileChooser.setAcceptAllFileFilterUsed(false);
		jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jFileChooser.setFileFilter(new DdsFilter(true));
		
		JPanel jPanel = new JPanel();
		GroupLayout groupLayout = new GroupLayout(jPanel);
		jPanel.setLayout(groupLayout);
		
		JScrollPane scroll = new JScrollPane(jPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JPanel jImagePanel = new JPanel();
		jImagePanel.setLayout(new BoxLayout(jImagePanel, BoxLayout.Y_AXIS));
		
		groupLayout.setHorizontalGroup(
			groupLayout.createSequentialGroup()
				.addGap(10, 10, Integer.MAX_VALUE)
				.addComponent(jImagePanel)
				.addGap(10, 10, Integer.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createSequentialGroup()
				.addGap(10, 10, Integer.MAX_VALUE)
				.addComponent(jImagePanel)
				.addGap(10, 10, Integer.MAX_VALUE)
		);

		jImageLabel = new JLabel();
		jImageLabel.setHorizontalAlignment(JLabel.CENTER);
		jImageLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		jImageLabel.setBackground( Color.magenta);
		jImageLabel.setOpaque(true);

		jTextLabel = new JLabel("Nothing loaded...");
		jTextLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		jTextLabel.setHorizontalAlignment(JLabel.CENTER);

		jImagePanel.add(jImageLabel);
		jImagePanel.add(jTextLabel);
		
		
		JMenuBar jMenuBar = new JMenuBar();
		JMenu jMenu = new JMenu("File");
		jMenuBar.add(jMenu);
		JMenuItem jMenuItem = new JMenuItem("Open");
		jMenuItem.setActionCommand(ACTION_OPEN);
		jMenuItem.addActionListener(this);
		jMenu.add(jMenuItem);
		
				
		jMenu = new JMenu("Colors");
		jMenuBar.add(jMenu);
		
		ButtonGroup group = new ButtonGroup();
		
		JRadioButtonMenuItem rbMenuItem = new JRadioButtonMenuItem("RBG");
		rbMenuItem.setSelected(true);
		rbMenuItem.setActionCommand(ACTION_RBG);
		rbMenuItem.addActionListener(this);
		jMenu.add(rbMenuItem);
		group.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("YCoCg");
		rbMenuItem.setActionCommand(ACTION_YCOCG);
		rbMenuItem.addActionListener(this);
		jMenu.add(rbMenuItem);
		group.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("YCoCg Scaled");
		rbMenuItem.setActionCommand(ACTION_YCOCG_SCALED);
		rbMenuItem.addActionListener(this);
		jMenu.add(rbMenuItem);
		group.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Alpha Exponent");
		rbMenuItem.setActionCommand(ACTION_ALPHA_EXPONENT);
		rbMenuItem.addActionListener(this);
		jMenu.add(rbMenuItem);
		group.add(rbMenuItem);
		
		jFrame = new JFrame("DDS Viewer");
		jFrame.setSize(850, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.getContentPane().add(scroll);
		jFrame.setJMenuBar(jMenuBar);
		jFrame.addKeyListener(this);
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
	
	private void openFile(final File file){
		files = Arrays.asList(file.getParentFile().listFiles(new DdsFilter(false)));
		Collections.sort(files);
		fileIndex = files.indexOf(file);
		loadFile(file);
	}
	
	public void loadFile(){
		loadFile(files.get(fileIndex), mipMap);
	}
	
	public void loadFile(File file){
		loadFile(file, 0);
	}
	
	public void loadFile(File file, int imageIndex){
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix("dds");
        if (iterator.hasNext()){
			try {
				ImageReader imageReader = iterator.next();
				imageReader.setInput(new FileImageInputStream(file));
				int max = imageReader.getNumImages(true);
				if (imageIndex > max || imageIndex < 0) throw new IOException("imageIndex ("+imageIndex+") not found");
				BufferedImage image = imageReader.read(imageIndex);
				if (type == ColorType.YCOCG) DDSUtil.decodeYCoCg(image);
				if (type == ColorType.YCOCG_SCALED) DDSUtil.decodeYCoCgScaled(image);
				if (type == ColorType.ALPHA_EXPONENT) DDSUtil.decodeAlphaExponent(image);
				item = new Item(image, file);
				update();
			} catch (Exception ex) {
				System.out.println("loadFile fail...");
				ex.printStackTrace();
			}
        }
	}
	
	public int getMipMaps(File file){
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix("dds");
        if (iterator.hasNext()){
			try {
				ImageReader imageReader = iterator.next();
				imageReader.setInput(new FileImageInputStream(file));
				return imageReader.getNumImages(true);
			} catch (Exception ex) {
				System.out.println("getMipMaps fail...");
			}
        }
		return 1;
	}
	
	private void update(){
		jImageLabel.setIcon(new ImageIcon(item.getImage()));
		jTextLabel.setText(item.getFile().getName());
		
		/*
		jTextLabel.setPreferredSize(null);
		Dimension dimension = new Dimension(Math.max(jTextLabel.getPreferredSize().width, jImageLabel.getPreferredSize().width), jTextLabel.getPreferredSize().height);
		jTextLabel.setMaximumSize(dimension);
		jTextLabel.setPreferredSize(dimension);
		jTextLabel.setMinimumSize(dimension);
		 * 
		 */
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (ACTION_OPEN.equals(e.getActionCommand())){
			int returnVal = jFileChooser.showOpenDialog(jFrame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = jFileChooser.getSelectedFile();
				openFile(file);
			}
		}
		if (ACTION_RBG.equals(e.getActionCommand())){
			type = ColorType.RBG;
			loadFile();
		}
		if (ACTION_YCOCG.equals(e.getActionCommand())){
			type = ColorType.YCOCG;
			loadFile();
		}
		if (ACTION_YCOCG_SCALED.equals(e.getActionCommand())){
			type = ColorType.YCOCG_SCALED;
			loadFile();
		}
		if (ACTION_ALPHA_EXPONENT.equals(e.getActionCommand())){
			type = ColorType.ALPHA_EXPONENT;
			loadFile();
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()){
			case KeyEvent.VK_RIGHT:
				if (!updating){
					updating = true;
					fileIndex++;
					if (fileIndex >= files.size()) fileIndex = 0;
					mipMap = 0;
					loadFile();
					updating = false;
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case KeyEvent.VK_LEFT:
				if (!updating){
					updating = true;
					fileIndex--;
					if (fileIndex < 0) fileIndex = files.size()-1;
					mipMap = 0;
					loadFile();
					updating = false;
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case KeyEvent.VK_UP:
				if (!updating){
					updating = true;
					mipMap--;
					if (mipMap >= 0 && mipMap < getMipMaps(files.get(fileIndex))){
						loadFile();
					} else {
						mipMap++;
					}
					updating = false;
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
			case KeyEvent.VK_DOWN:
				if (!updating){
					updating = true;
					mipMap++;
					if (mipMap >= 0 && mipMap < getMipMaps(files.get(fileIndex))){
						loadFile();
					} else {
						mipMap--;
					}
					updating = false;
				} else {
					Toolkit.getDefaultToolkit().beep();
				}
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}
	
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
	
	private static class Item{
		private BufferedImage image;
		private File file;

		public Item(BufferedImage image, File file) {
			this.image = image;
			this.file = file;
		}

		public File getFile() {
			return file;
		}

		public BufferedImage getImage() {
			return image;
		}
	}
}
