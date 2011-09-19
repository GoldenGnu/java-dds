/*
 * Runtest.java - This file is part of Java DDS ImageIO Plugin
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
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


public class Runtest {
	
	private final int COLUMNS = 3;
	
	private static final String[] TESTFILES = {
		"src\\test\\resources\\net\\nikr\\dds\\test_image.dds",		//A8R8G8B8 - OK
		"src\\test\\resources\\net\\nikr\\dds\\test_image-bc1c.dds",//DXT1 (no Alpha) - OK
		"src\\test\\resources\\net\\nikr\\dds\\test_image-bc1a.dds",//DXT1 (1bit Alpha) - OK
		"src\\test\\resources\\net\\nikr\\dds\\test_image-bc2.dds",	//DXT3 - OK
		"src\\test\\resources\\net\\nikr\\dds\\test_image-bc3.dds",	//DXT5 - OK
		//"src\\test\\resources\\net\\nikr\\dds\\ATI1N.dds",		//FAIL
		//"src\\test\\resources\\net\\nikr\\dds\\ATI2N.dds",		//??
		//"src\\test\\resources\\net\\nikr\\dds\\ATI2N_alt.dds",		//??
		"src\\test\\resources\\net\\nikr\\dds\\dxt1.dds",		//DXT1
		"src\\test\\resources\\net\\nikr\\dds\\dxt3.dds",		//DXT3
		"src\\test\\resources\\net\\nikr\\dds\\dxt5.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_ati2n.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_rbxg.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_rgxb.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_rxbg.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_xgbr.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_xgxr.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\dxt5_xrbg.dds",		//DXT5
		"src\\test\\resources\\net\\nikr\\dds\\acids.dds",			//DXT1 (With 8 mipMaps)
		"src\\test\\resources\\net\\nikr\\dds\\acids.jpg",			//JPG - not by us
	};
	
	//Frame
	private JFrame jFrame;
	private JPanel jMainPanel;
	private BufferedImage[] images = new BufferedImage[TESTFILES.length];
	
	public Runtest() {
		try {
			int i = 0;
			for (String testfile : TESTFILES){
				images[i] = ImageIO.read(new File(testfile));
				i++;
			}
		} catch (IOException ex) {
			
		}
		
		jMainPanel = new JPanel();
		//jMainPanel.setLayout( new BoxLayout(jMainPanel, BoxLayout.LINE_AXIS) ); 
		//jMainPanel.setLayout( new FlowLayout(FlowLayout.LEFT, 10, 10) ); 
		jMainPanel.setLayout( new GridLayout((int)Math.ceil(TESTFILES.length/(double)COLUMNS),COLUMNS, 5, 5) ); 
		//jMainPanel.setMinimumSize(new Dimension(800, 600));
		//jMainPanel.setPreferredSize(new Dimension(800, 600));
		//jMainPanel.setMaximumSize(new Dimension(800, Integer.MAX_VALUE));
		
		for (BufferedImage image : images){
			JPanel jPanel = new JPanel();
			JLabel label = new JLabel();
			label.setBackground( Color.magenta);
			label.setOpaque(true);
			label.setIcon(new ImageIcon(image));
			jPanel.add(label);
			jMainPanel.add(jPanel);
		}
		JScrollPane scroll = new JScrollPane(jMainPanel);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		jFrame = new JFrame("NiKR DDS Lib - the Runtest");
		jFrame.setSize(850, 600);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.getContentPane().add(scroll);
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

		Runtest runtest = new Runtest();
	}

	private static void initLookAndFeel() {
		String lookAndFeel = null;
			
		lookAndFeel = UIManager.getSystemLookAndFeelClassName(); //System
		//lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName(); //Java (Any Platform)
		//lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel"; //CDE/Motif (Any Platform)
		//lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel"; //Windows (Windows Only)
		//lookAndFeel = "javax.swing.plaf.mac.MacLookAndFeel"; //Mac (Mac Only)
		//lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel"; //Java (Any Platform)
		//lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel"; //GTK (Don't work on windows)
		try {
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
