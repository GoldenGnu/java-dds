/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author NKR
 */
public class DDSImageReaderTest {
	
	//A8R8G8B8 - OK
	private static final String testfile = "src\\test\\resources\\net\\nikr\\dds\\test_image.dds";
	private static final int mipMaps = 1;
	private static final int nWidth = 256;
	private static final int nHeight = 256;
	
	//DXT1 (no Alpha) - OK
	/*
	private static final String testfile = "test_image-bc1c.dds";
	private static final int mipMaps = 1;
	private static final int nWidth = 256;
	private static final int nHeight = 256;
	
	 */
	
	//DXT1 (1bit Alpha) - OK
	/*
	private static final String testfile = "test_image-bc1a.dds";
	private static final int mipMaps = 1;
	private static final int nWidth = 256;
	private static final int nHeight = 256;
	 */
	
	//DXT3 - OK
	/*
	private static final String testfile = "test_image-bc2.dds";
	private static final int mipMaps = 1;
	private static final int nWidth = 256;
	private static final int nHeight = 256;
	 */
	
	//DXT5 - OK
	/*
	private static final String testfile = "test_image-bc3.dds";
	private static final int mipMaps = 1;
	private static final int nWidth = 256;
	private static final int nHeight = 256;
	 */
	
	//DXT1 (With 8 mipMaps)
	/*
	private static final String testfile = "acids.dds";
	private static final int mipMaps = 8;
	private static final int nWidth = 128;
	private static final int nHeight = 128;
	 */
    
	
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
		Object input = getInput();
		boolean seekForwardOnly = false;
		boolean ignoreMetadata = false;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput(input, seekForwardOnly, ignoreMetadata);
		assertEquals(input, instance.getInput());
	}

	/**
	 * Test of getNumImages method, of class DDSImageReader.
	 */
	@Test
	public void testGetNumImages() throws Exception {
		System.out.println("getNumImages");
		boolean allowSearch = false;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput( getInput() );
		int result = instance.getNumImages(allowSearch);
		assertEquals(mipMaps, result);
	}

	/**
	 * Test of getWidth method, of class DDSImageReader.
	 */
	@Test
	public void testGetWidth() throws Exception {
		System.out.println("getWidth");
		int imageIndex = 0;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput( getInput() );
		int result = instance.getWidth(imageIndex);
		System.out.println(result);
		assertEquals(result, nWidth / (imageIndex+1));
	}

	/**
	 * Test of getHeight method, of class DDSImageReader.
	 */
	@Test
	public void testGetHeight() throws Exception {
		System.out.println("getHeight");
		int imageIndex = 0;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput( getInput() );
		int result = instance.getHeight(imageIndex);
		assertEquals(result, nHeight / (imageIndex+1));
	}

	/**
	 * Test of getImageTypes method, of class DDSImageReader.
	 */
	/*
	@Test
	public void testGetImageTypes() throws Exception {
		System.out.println("getImageTypes");
		int imageIndex = 0;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput( getInput() );
		Iterator<ImageTypeSpecifier> expResult = null;
		Iterator<ImageTypeSpecifier> result = instance.getImageTypes(imageIndex);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
	 * 
	 */

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
		assertNotNull( getImage(testfile) );
		
		/*
		int imageIndex = 0;
		ImageReadParam param = null;
		DDSImageReader instance = new DDSImageReader( new DDSImageReaderSpi() );
		instance.setInput( getInput() );
		BufferedImage expResult = null;
		BufferedImage result = instance.read(imageIndex, param);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
		 */
	}
	
	private Object getInput(){
		Object input = null;
		try {
			input = new FileImageInputStream(new File(testfile));
		} catch (FileNotFoundException ex) {
			fail("File not found.");
		} catch (IOException ex) {
			fail("IO Failed.");
		}
		return input;
	}
	private BufferedImage getImage(String filename){
		BufferedImage image = null;
		try {
			ImageInputStream iis = ImageIO.createImageInputStream(new File(testfile));
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
}
