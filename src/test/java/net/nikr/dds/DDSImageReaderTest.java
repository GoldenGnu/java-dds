/*
 * DDSImageReaderTest.java - This file is part of Java DDS ImageIO Plugin
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
 * TODO Write File Description for DDSImageReaderTest.java
 */

package net.nikr.dds;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.FileImageInputStream;
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
		new TestParams("test_mipmap1.dds", 1, 256, 256),	//
		new TestParams("test_mipmap2.dds", 9, 256, 256),	//

		new TestParams("25x25_ati1.dds", 5, 25, 25),	//"bad size" - ATI1
		new TestParams("25x25_ati2.dds", 5, 25, 25),	//"bad size" - ATI2
		new TestParams("25x25_dxt1.dds", 5, 25, 25),	//"bad size" - DXT1
		new TestParams("25x25_dxt3.dds", 5, 25, 25),	//"bad size" - DXT2
		new TestParams("25x25_dxt5.dds", 5, 25, 25),	//"bad size" - DXT5
		new TestParams("25x25_plain.dds", 5, 25, 25),	//"bad size" - Uncompressed

		//DX10
		new TestParams("dx10" + File.separator + "25x25_dxt1.dds", 5, 2, 25, 25),	//DX10 - DXT1
		new TestParams("dx10" + File.separator + "25x25_dxt3.dds", 5, 2, 25, 25),	//DX10 - DXT2
		new TestParams("dx10" + File.separator + "25x25_dxt5.dds", 5, 2, 25, 25),	//DX10 - DXT5
		new TestParams("dx10" + File.separator + "25x25_rgb8.dds", 5, 2, 25, 25),	//DX10 - RGB8
		new TestParams("dx10" + File.separator + "25x25_rgba8.dds", 5, 2, 25, 25),	//DX10 - RGBA8
		new TestParams("dx10" + File.separator + "25x25_bgr8.dds", 5, 2, 25, 25),	//DX10 - BGR8
		new TestParams("dx10" + File.separator + "25x25_abgr8.dds", 5, 2, 25, 25),	//DX10 - ABGR8
		new TestParams("dx10" + File.separator + "25x25_r5g6b5.dds", 5, 2, 25, 25), //DX10 - R5G6B5
		new TestParams("dx10" + File.separator + "25x25_rgba4.dds", 5, 2, 25, 25),	//DX10 - RGBA4
		new TestParams("dx10" + File.separator + "25x25_rgb5a1.dds", 5, 2, 25, 25), //DX10 - RGB5A1
		new TestParams("dx10" + File.separator + "25x25_rgb10a2.dds", 5, 2, 25, 25),//DX10 - RGB10A2
		new TestParams("dx10" + File.separator + "25x25_r2g3b2.dds", 5, 2, 25, 25), //DX10 - R2G3B2
		new TestParams("dx10" + File.separator + "25x25_a8.dds", 5, 2, 25, 25),		//DX10 - A8
		new TestParams("dx10" + File.separator + "25x25_l8.dds", 5, 2, 25, 25),		//DX10 - L8
		new TestParams("dx10" + File.separator + "25x25_l8a8.dds", 5, 2, 25, 25),	//DX10 - L8A8
		new TestParams("dx10" + File.separator + "25x25_aexp.dds", 5, 2, 25, 25),	//DX10 - AEXP
		new TestParams("dx10" + File.separator + "25x25_ycocg.dds", 5, 2, 25, 25),	//DX10 - YCOCG
		new TestParams("dx10" + File.separator + "dds_dxt1.dds", 1, 4, 256, 256),	//DX10 - DXT1
		new TestParams("dx10" + File.separator + "dds_dxt3.dds", 1, 4, 256, 256),	//DX10 - DXT2
		new TestParams("dx10" + File.separator + "dds_dxt5.dds", 1, 4, 256, 256),	//DX10 - DXT5
		new TestParams("dx10" + File.separator + "dds_plain.dds", 1, 4, 256, 256),	//DX10 - Uncompressed

		//GIMP
		new TestParams("gimp" + File.separator + "a8.dds", 9, 256, 256),				//NONE A8 - OK
		new TestParams("gimp" + File.separator + "abgr8.dds", 9, 256, 256),				//NONE ABGR8 - OK
		new TestParams("gimp" + File.separator + "aexp.dds", 9, 256, 256),				//??? - OK
		new TestParams("gimp" + File.separator + "alpha_exponent_dxt5.dds", 9, 256, 256),//bc3/dxt5 (Alpha Exponent)
		new TestParams("gimp" + File.separator + "bc1.dds", 9, 256, 256),				//bc1/dxt1 - OK
		new TestParams("gimp" + File.separator + "bc2.dds", 9, 256, 256),				//bc2/dxt3 - OK
		new TestParams("gimp" + File.separator + "bc3.dds", 9, 256, 256),				//bc3/dxt5 - OK
		new TestParams("gimp" + File.separator + "bc3n.dds", 9, 256, 256),				//bc3n/dxt5 - OK
		new TestParams("gimp" + File.separator + "bc4.dds", 9, 256, 256),				//bc4/ati1 - OK
		new TestParams("gimp" + File.separator + "bc5.dds", 9, 256, 256),				//bc5/ati2 - OK
		new TestParams("gimp" + File.separator + "bgr8.dds", 9, 256, 256),				//NONE BGR8
		new TestParams("gimp" + File.separator + "l8.dds", 9, 256, 256),				//NONE L8 - OK
		new TestParams("gimp" + File.separator + "l8a8.dds", 9, 256, 256),				//NONE L8A8 - OK
		new TestParams("gimp" + File.separator + "r3g3b2.dds", 9, 256, 256),			//NONE R3G3B2 - OK
		new TestParams("gimp" + File.separator + "r5g6b5.dds", 9, 256, 256),			//NONE R5G6B5 - OK
		new TestParams("gimp" + File.separator + "rgb5a1.dds", 9, 256, 256),			//NONE RGB5A1 - FAIL
		new TestParams("gimp" + File.separator + "rgb8.dds", 9, 256, 256),				//NONE RGB8 - FAIL
		new TestParams("gimp" + File.separator + "rgb10a2.dds", 9, 256, 256),			//NONE RGB10A2 - FAIL
		new TestParams("gimp" + File.separator + "rgba4.dds", 9, 256, 256),				//NONE RGBA4 - OK
		new TestParams("gimp" + File.separator + "rgba8.dds", 9, 256, 256),				//NONE RGBA8 - OK
		new TestParams("gimp" + File.separator + "ycocg.dds", 9, 256, 256),				//NONE YCoCg
		new TestParams("gimp" + File.separator + "ycocg_dxt5.dds", 9, 256, 256),		//DXT5 (YCoCg)
		new TestParams("gimp" + File.separator + "ycocg_scaled_dxt5.dds", 9, 256, 256),	//DXT5 (YCoCg scaled)

		//DxTex
		new TestParams("dxtex" + File.separator + "16bit_a1r5g5b5.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_a4r4g4b4.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_a8l8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_a8r3g3b2.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_r5g6b5.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_x1r5g5b5.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "16bit_x4r4g4b4.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "24bit_r8g8b8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_a2b10g10r10.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_a2r10g10b10.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_a8b8g8r8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_a8r8g8b8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_g16r16.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_x8b8g8r8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "32bit_x8r8g8b8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "8bit_a4l4.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "8bit_a8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "8bit_l8.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "8bit_r3g3b2.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_dxt1.dds", 1, 256, 256),
		//new TestParams("dxtex" + File.separator + "fourcc_dxt2.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_dxt3.dds", 1, 256, 256),
		//new TestParams("dxtex" + File.separator + "fourcc_dxt4.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_dxt5.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_grgb.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_rgbg.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_uyvy.dds", 1, 256, 256),
		new TestParams("dxtex" + File.separator + "fourcc_yuy2.dds", 1, 256, 256),
		/*
		16bit_a8p8.txt
		8bit_p8.txt
		_128bit_float_a32b32g32r32f.dds
		_16bit_l16.dds
		_16bit_mixed_l6v5u5.dds
		_16bit_signed_CxV8U8.dds
		_16bit_signed_v8u8
		_16b_float_r16f.dds
		_32bit_float_g16r16f.dds
		_32bit_float_r32f.dds
		_32bit_mixed_a2w10v10u10.dds
		_32bit_mixed_x8l8v8u8.dds
		_32bit_signed_q8w8v8u8.dds
		_32bit_signed_v16u16.dds
		_64bit_a16b16g16r16.dds
		_64bit_float_a16b16g16r16f.dds
		_64bit_float_g32r32f.dds
		_64bit_signed_q16w16v16u16.dds
		*/

		//GIMP DX10
		new TestParams("dx10_gimp" + File.separator + "a8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "abgr8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "aexp.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "ati1.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "ati2.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "bgr8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt1.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt3.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5nm.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5_aexp.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5_rxgb.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5_ycocg.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "dxt5_ycocg_scaled.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "l8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "l8a8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "r3g3b2.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "r5g6b5.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "rgb10a2.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "rgb5a1.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "rgb8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "rgba4.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "rgba8.dds", 9 , 2, 256, 256),
		new TestParams("dx10_gimp" + File.separator + "ycocg.dds", 9 , 2, 256, 256),
	};

	@BeforeClass
	public static void setUpClass() { }

	@AfterClass
	public static void tearDownClass() { }

	@Before
	public void setUp() { }

	@After
	public void tearDown() { }

	/**
	 * Test of setInput method, of class DDSImageReader.
	 */
	@Test
	public void testSetInput() {
		System.out.println("setInput");
		for (TestParams param : params){
			Object input = getInput(param);
			boolean seekForwardOnly = false;
			boolean ignoreMetadata = false;
			DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
			instance.setInput(input, seekForwardOnly, ignoreMetadata);
			assertEquals(param.getName(), input, instance.getInput());
		}
	}

	/**
	 * Test of getNumImages method, of class DDSImageReader.
	 * @throws javax.imageio.IIOException
	 */
	@Test
	public void testGetNumImages() throws IIOException {
		System.out.println("getNumImages");
		for (TestParams param : params){
			boolean allowSearch = false;
			DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
			instance.setInput( getInput(param) );
			int result = instance.getNumImages(allowSearch);
			assertEquals(param.getName(), param.getMipMaps() * param.getArraySize(), result);
		}
	}

	/**
	 * Test of getWidth method, of class DDSImageReader.
	 * @throws javax.imageio.IIOException
	 */
	@Test
	public void testGetWidth() throws IIOException {
		System.out.println("getWidth");
		for (TestParams param : params){
			for (int arraySize = 0; arraySize < param.getArraySize(); arraySize++){
				int width = param.getWidth();
				for (int imageIndex = 0; imageIndex < param.getMipMaps(); imageIndex++){
					DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
					instance.setInput( getInput(param) );
					int result = instance.getWidth(imageIndex + (param.getMipMaps() * arraySize));
					assertEquals(param.getName(), result, width);
					width = Math.max(width / 2, 1);
				}
			}
		}
	}

	/**
	 * Test of getHeight method, of class DDSImageReader.
	 * @throws javax.imageio.IIOException
	 */
	@Test
	public void testGetHeight() throws IIOException {
		System.out.println("getHeight");
		for (TestParams param : params){
			for (int arraySize = 0; arraySize < param.getArraySize(); arraySize++){
				int height = param.getHeight();
				for (int imageIndex = 0; imageIndex < param.getMipMaps(); imageIndex++){
					DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
					instance.setInput( getInput(param) );
					int result = instance.getHeight(imageIndex + (param.getMipMaps() * arraySize));
					assertEquals(param.getName(), result, height);
					height = height / 2;
				}
			}
		}
	}

	/**
	 * Test of getImageTypes method, of class DDSImageReader.
	 * @throws javax.imageio.IIOException
	 */
	@Test
	public void testGetImageTypes() throws IIOException {
		System.out.println("getImageTypes");
		for (TestParams param : params){
			for (int arraySize = 0; arraySize < param.getArraySize(); arraySize++){
				for (int imageIndex = 0; imageIndex < param.getMipMaps(); imageIndex++){
					DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
					instance.setInput( getInput(param) );
					ImageTypeSpecifier expResult = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_4BYTE_ABGR);
					ImageTypeSpecifier result = instance.getImageTypes(imageIndex + (param.getMipMaps() * arraySize)).next();
					assertEquals(param.getName(), expResult, result);
				}
			}
		}
	}

	/**
	 * Test of getStreamMetadata method, of class DDSImageReader.
	 */
	@Test
	public void testGetStreamMetadata() {
		System.out.println("getStreamMetadata");
		for (TestParams param : params){
			DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
			instance.setInput( getInput(param) );
			IIOMetadata result = instance.getStreamMetadata();
			assertNull(param.getName(), result); //TODO no meta data returned...
		}
	}

	/**
	 * Test of getImageMetadata method, of class DDSImageReader.
	 */
	@Test
	public void testGetImageMetadata() {
		System.out.println("getImageMetadata");
		for (TestParams param : params){
			for (int arraySize = 0; arraySize < param.getArraySize(); arraySize++){
				for (int imageIndex = 0; imageIndex < param.getMipMaps(); imageIndex++){
					DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
					instance.setInput( getInput(param) );
					IIOMetadata result = instance.getImageMetadata(imageIndex + (param.getMipMaps() * arraySize));
					assertNull(param.getName(), result); //TODO no meta data returned...
				}
			}
		}
	}

	/**
	 * Test of read method, of class DDSImageReader.
	 * @throws java.io.IOException
	 */
	@Test
	public void testRead() throws IOException {
		System.out.println("read");
		for (TestParams param : params){
			for (int arraySize = 0; arraySize < param.getArraySize(); arraySize++){
				for (int imageIndex = 0; imageIndex < param.getMipMaps(); imageIndex++){
					DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
					instance.setInput( getInput(param) );
					BufferedImage result;
					result = instance.read(imageIndex + (param.getMipMaps() * arraySize));
					assertNotNull(param.getName(), result);
				}
			}
		}
	}
	
	/**
	 * Test of ImageIO
	 * @throws java.io.IOException
	 */
	@Test
	public void testImageIO() throws IOException {
		System.out.println("ImageIO");
		for (TestParams param : params){
			BufferedImage result;
			result = ImageIO.read(new File(param.getFilename()));
			assertNotNull(param.getName(), result);
		}
	}

	/**
	 * Test pixel by pixel comparing with GiMP output
	 * @throws java.io.IOException
	 */
	@Test
	public void testPixels() throws IOException {
		System.out.println("Pixels");
		for (TestParams param : params){
			File file = new File(param.getCompareFilename());
			if (!file.exists()) {
				continue;
			}
			BufferedImage png = ImageIO.read(file);
			BufferedImage dds = ImageIO.read(new File(param.getFilename()));
			for (int x = 0; x < dds.getWidth(); x++) {
				for (int y = 0; y < dds.getHeight(); y++) {
					int rgbDDS = dds.getRGB(x, y);
					int rgbPNG = png.getRGB(x, y);
					int a = (rgbDDS >> 24 & 0xff);
					int r = (rgbDDS >> 16 & 0xff);
					int g = (rgbDDS >> 8 & 0xff);
					int b = (rgbDDS & 0xff);
					assertEquals(param.getName() + " XY: " + x + ", " + y + " Alpha)", a, (rgbPNG >> 24 & 0xff));
					if (a != 0) {
						assertEquals(param.getName() + " XY: " + x + ", " + y + " Blue)", b, (rgbPNG & 0xff));
						assertEquals(param.getName() + " XY: " + x + ", " + y + " Green)", g, ((rgbPNG >> 8) & 0xff));
						assertEquals(param.getName() + " XY: " + x + ", " + y + " Red)", r, ((rgbPNG >> 16) & 0xff));
					}
				}
			}
		}
	}

	@Test
	public void testThreads() throws Exception {
		System.out.println("Threads");
		List<ThreadTester> threads = new ArrayList<ThreadTester>();
		for (int i = 0; i < 8; i++) {
			ThreadTester thread = new ThreadTester();
			threads.add(thread);
			thread.start();
		}
		for (ThreadTester thread : threads) {
			thread.join();
			assertFalse("Thread test failed", thread.isFailed());
		}	
	}

	private Object getInput(TestParams param){
		Object input = null;
		File file = new File(param.getFilename());
		try {
			input = new FileImageInputStream(file);
		} catch (FileNotFoundException ex) {
			fail("getInput -> File "+file.getName()+" not found -> "+ex.getMessage());
		} catch (IOException ex) {
			fail("getInput -> IO Failed for "+file.getName()+" -> "+ex.getMessage());
		}
		return input;
	}

	private class ThreadTester extends Thread{

		private boolean failed = false;

		@Override
		public void run() {
			for (TestParams param : params){
				try {
					BufferedImage read = ImageIO.read(new File(param.getFilename()));
					if (read == null) {
						failed = true;
					}
				} catch (IOException ex) {
					failed = true;
				}
			}
		}

		public boolean isFailed() {
			return failed;
		}
	}

	private class TestParams{
		private final String dir = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "net" + File.separator + "nikr" + File.separator + "dds" + File.separator;
		private final String filename;
		private final String compareFilename;
		private final int mipMaps;
		private final int arraySize;
		private final int width;
		private final int height;

		public TestParams(String filename, int mipMaps, int width, int height) {
			this(filename, mipMaps, 1, width, height);
		}

		public TestParams(String filename, int mipMaps, int arraySize, int width, int height) {
			this.filename = filename;
			this.mipMaps = mipMaps;
			this.arraySize = arraySize;
			this.width = width;
			this.height = height;
			this.compareFilename = filename.substring(0, filename.lastIndexOf('.')) + ".png";
		}

		public int getArraySize() {
			return arraySize;
		}

		public String getName(){
			return filename;
		}

		public String getFilename() {
			return dir+filename;
		}

		public String getCompareFilename() {
			return dir+compareFilename;
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