/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.nikr.dds;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import net.nikr.dds.Viewer.DdsFilter;

/**
 *
 * @author Niklas
 */
public class ProfileRunner {

	public ProfileRunner(String[] args) {
		List<String> argsList = Arrays.asList(args);
		if (argsList.contains("-24bit")) the24bitX100();
		if (argsList.contains("-all")) all();
	}
	
	
	public static void main(String[] args) {
		ProfileRunner profileRunner = new ProfileRunner(args);
	}
	
	private void the24bitX100(){
		File file = new File("src\\test\\resources\\net\\nikr\\dds\\gimp\\rgb8.dds");
		for (int i = 0; i < 100; i++){
			BufferedImage image = loadFile(file, 0);
				for (int imageIndex = 1; image != null; imageIndex++){
					image = loadFile(file, imageIndex);
				}
		}
	}
	
	private void all(){
		List<File> files = new ArrayList<File>();
		files.addAll(getDirFiles("src\\test\\resources\\net\\nikr\\dds\\"));
		files.addAll(getDirFiles("src\\test\\resources\\net\\nikr\\dds\\gimp\\"));
		for (File file : files){
			System.out.println(file.getName());
			BufferedImage image = loadFile(file, 0);
			for (int imageIndex = 1; image != null; imageIndex++){
				image = loadFile(file, imageIndex);
			}
		}
	}
	
	public BufferedImage loadFile(File file, int imageIndex){
        Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix("dds");
        if (iterator.hasNext()){
			try {
				ImageReader imageReader = iterator.next();
				imageReader.setInput(new FileImageInputStream(file));
				int max = imageReader.getNumImages(true);
				if (imageIndex >= 0 && imageIndex < max){
					return imageReader.read(imageIndex);
				}
			} catch (Exception ex) {
				System.out.println("loadFile fail...");
				ex.printStackTrace();
			}
        }
		return null;
	}
	
	private List<File> getDirFiles(String dir){
		File file = new File(dir);
		return Arrays.asList(file.listFiles(new DdsFilter(false)));
	}
}
