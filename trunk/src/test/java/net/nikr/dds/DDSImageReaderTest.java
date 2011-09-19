/*
 * DDSImageReaderTest.java - This file is part of Java DDS ImageIO Plugin
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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class DDSImageReaderTest {
	
	private final TestParams[] params = {
		new TestParams("acids.dds", 8, 128, 128),			//DXT1 (With 8 mipMaps)
		new TestParams("ATI1N.dds", 1, 256, 256),			//ATI1N - OK?
		new TestParams("ATI2N.dds", 1, 256, 256),			//ATI2N - OK?
		new TestParams("ATI2N_alt.dds", 1, 256, 256),		//ATI2N - OK?
		new TestParams("dxt1.dds", 1, 256, 256),			//DXT1 - OK
		new TestParams("dxt3.dds", 1, 256, 256),			//DXT3 - OK
		new TestParams("dxt5.dds", 1, 256, 256),			//DXT5 - OK
		new TestParams("dxt5_ati2n.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_rbxg.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_rgxb.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_rxbg.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_xgbr.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_xgxr.dds", 1, 256, 256),		//DXT5 - OK
		new TestParams("dxt5_xrbg.dds", 1, 256, 256),		///DXT5 - OK
		new TestParams("test_image.dds", 1, 256, 256),		//A8R8G8B8 - OK
		new TestParams("test_image-bc1c.dds", 9, 256, 256),	//DXT1 (no Alpha) - OK
		new TestParams("test_image-bc1a.dds", 9, 256, 256),	//DXT1 (1bit Alpha) - OK
		new TestParams("test_image-bc2.dds", 9, 256, 256),	//DXT3 - OK
		new TestParams("test_image-bc3.dds", 9, 256, 256),	//DXT5 - OK
		new TestParams("test_image-bc4.dds", 1, 256, 256),	//ATI1N - OK?
		new TestParams("test_image-bc4.dds", 1, 256, 256),	//ATI2N - OK?
	};
    
	
	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void tearDown() {
	}


	/**
	 * Test of setInput method, of class DDSImageReader.
	 */
	@Test
	public void testSetInput() {
		System.out.println("setInput");
		for (int i = 0; i < params.length; i++){
			Object input = getInput(i);
			boolean seekForwardOnly = false;
			boolean ignoreMetadata = false;
			DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
			instance.setInput(input, seekForwardOnly, ignoreMetadata);
			assertEquals(input, instance.getInput());
		}
	}

	/**
	 * Test of getNumImages method, of class DDSImageReader.
	 */
	@Test
	public void testGetNumImages() throws Exception {
		System.out.println("getNumImages");
		for (int i = 0; i < params.length; i++){
			boolean allowSearch = false;
			DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
			instance.setInput( getInput(i) );
			int result = instance.getNumImages(allowSearch);
			assertEquals(params[i].getMipMaps(), result);
		}
	}

	/**
	 * Test of getWidth method, of class DDSImageReader.
	 */
	@Test
	public void testGetWidth() throws Exception {
		System.out.println("getWidth");
		for (int i = 0; i < params.length; i++){
			for (int imageIndex = 0; imageIndex < params[i].getMipMaps(); imageIndex++){
				DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
				instance.setInput( getInput(i) );
				int result = instance.getWidth(imageIndex);
				assertEquals(result, params[i].getWidth() / (imageIndex+1));
			}
		}
	}

	/**
	 * Test of getHeight method, of class DDSImageReader.
	 */
	@Test
	public void testGetHeight() throws Exception {
		System.out.println("getHeight");
		for (int i = 0; i < params.length; i++){
			for (int imageIndex = 0; imageIndex < params[i].getMipMaps(); imageIndex++){
				DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
				instance.setInput( getInput(i) );
				int result = instance.getHeight(imageIndex);
				assertEquals(result, params[i].getHeight() / (imageIndex+1));
			}
		}
	}

	/**
	 * Test of getImageTypes method, of class DDSImageReader.
	 */
	@Test
	public void testGetImageTypes() throws Exception {
		System.out.println("getImageTypes");
		for (int i = 0; i < params.length; i++){
			for (int imageIndex = 0; imageIndex < params[i].getMipMaps(); imageIndex++){
				DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
				instance.setInput( getInput(i) );
				ImageTypeSpecifier expResult = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_4BYTE_ABGR);
				ImageTypeSpecifier result = instance.getImageTypes(imageIndex).next();
				assertEquals(expResult, result);
			}
		}
	}

	/**
	 * Test of getStreamMetadata method, of class DDSImageReader.
	 */
	@Test
	public void testGetStreamMetadata() {
		System.out.println("getStreamMetadata");
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		IIOMetadata result = instance.getStreamMetadata();
		assertNull(result);
	}

	/**
	 * Test of getImageMetadata method, of class DDSImageReader.
	 */
	@Test
	public void testGetImageMetadata() {
		System.out.println("getImageMetadata");
		int imageIndex = 0;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		IIOMetadata result = instance.getImageMetadata(imageIndex);
		assertNull(result);
	}

	/**
	 * Test of read method, of class DDSImageReader.
	 */
	@Test
	public void testRead() throws Exception {
		System.out.println("read");
		for (int i = 0; i < params.length; i++){
			for (int imageIndex = 0; imageIndex < params[i].getMipMaps(); imageIndex++){
				ImageReadParam param = null;
				DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
				instance.setInput( getInput(i) );
				BufferedImage result = instance.read(imageIndex, param);
				assertNotNull(result);
			}
		}
	}
	
	/**
	 * Test of ImageIO
	 */
	@Test
	public void testImageIO() throws Exception {
		System.out.println("ImageIO");
		try {
			for (TestParams param : params){
				ImageIO.read(new File(param.getFilename()));
			}
		} catch (IOException ex) {
			fail("testImageIO fail");
		}
	}
	
	private Object getInput(int i){
		Object input = null;
		try {
			input = new FileImageInputStream(new File(params[i].getFilename()));
		} catch (FileNotFoundException ex) {
			fail("File not found.");
		} catch (IOException ex) {
			fail("IO Failed.");
		}
		return input;
	}
	
	private BufferedImage getImage(int i){
		BufferedImage image = null;
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(new File(params[i].getFilename()));
			Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix ("dds");
			ImageReader reader = (ImageReader) iter.next();
			reader.setInput (iis, true);
			ImageReadParam irp = reader.getDefaultReadParam ();
			//image = reader.read (0, irp);
			image = reader.read (0);
			// Cleanup.
			reader.dispose ();
		} catch (IOException ex) {
			
		}
		
		/*
		try {
			image = ImageIO.read(new File(testdir+testfile));
		} catch (IOException ex) {
			
		}
		 */
		return image;
	}
	
	private class TestParams{
		private final String dir = "src\\test\\resources\\net\\nikr\\dds\\";
		private String filename;
		private int mipMaps;
		private int width;
		private int height;

		public TestParams(String filename, int mipMaps, int width, int height) {
			this.filename = filename;
			this.mipMaps = mipMaps;
			this.width = width;
			this.height = height;
		}

		public String getFilename() {
			return dir+filename;
		}

		public int getHeight() {
			return height;
		}

		public int getMipMaps() {
			return mipMaps;
		}

		public int getWidth() {
			return width;
		}
	}
}
