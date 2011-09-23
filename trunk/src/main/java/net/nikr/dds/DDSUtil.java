/*
 * DDSUtil.java - This file is part of Java DDS ImageIO Plugin
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
 * TODO Write File Description for DDSUtil.java
 */

package net.nikr.dds;

import java.awt.image.BufferedImage;


public class DDSUtil {

	public static void decodeYCoCg(BufferedImage image) {
		final float offset = 0.5f * 256.0f / 255.0f;
		for (int y = 0; y < image.getHeight(); ++y) {
			for (int x = 0; x < image.getWidth(); ++x) {
				int rgb = image.getRGB(x, y);
				float Y = (float) ((rgb>>24) & 0xff) / 255.0f; //3 = Alpha
				float Co = (float) ((rgb>>16) & 0xff) / 255.0f; //0 = Blue
				float Cg = (float) ((rgb>>8) & 0xff) / 255.0f; //1 = Green
				int alpha = (rgb & 0xff); //Blue
				/* convert YCoCg to RGB */
				Co -= offset;
				Cg -= offset;
				
				int r = (int)(saturate(Y + Co - Cg) * 255.0f);
				int g = (int)(saturate(Y + Cg) * 255.0f);
				int b = (int)(saturate(Y - Co - Cg) * 255.0f);
				
				rgb = (alpha << 24) | (r << 16) | (g << 8) | (b << 0);
				image.setRGB(x, y, rgb);
			}
		}
	}
	
	
	public static void decodeYCoCgScaled(BufferedImage image) {
		final float offset = 0.5f * 256.0f / 255.0f;
		for (int y = 0; y < image.getHeight(); ++y) {
			for (int x = 0; x < image.getWidth(); ++x) {
				int rgb = image.getRGB(x, y);
				float Y = (float) ((rgb>>24) & 0xff) / 255.0f; // Alpha
				float Co = (float) ((rgb>>16) & 0xff) / 255.0f; //Read
				float Cg = (float) ((rgb>>8) & 0xff) / 255.0f; //Green
				float s = (float)(rgb & 0xff) / 255.0f; //Blue
				
				s = 1.0f / ((255.0f / 8.0f) * s + 1.0f);
				
				/* convert YCoCg to RGB */
				Co = (Co - offset) * s;
				Cg = (Cg - offset) * s;
				
				int r = (int)(saturate(Y + Co - Cg) * 255.0f);
				int g = (int)(saturate(Y + Cg) * 255.0f);
				int b = (int)(saturate(Y - Co - Cg) * 255.0f);
				
				rgb = (255 << 24) | (r << 16) | (g << 8) | (b << 0);
				image.setRGB(x, y, rgb);
			}
		}
	}
	
	public static void decodeAlphaExponent(BufferedImage image) {
		for (int y = 0; y < image.getHeight(); ++y) {
			for (int x = 0; x < image.getWidth(); ++x) {
				int rgb = image.getRGB(x, y);
				float a = ((rgb>>24) & 0xff) / 255.0f;
				float r = ((rgb>>16) & 0xff);
				float g = ((rgb>>8) & 0xff);
				float b = (rgb & 0xff);

				r = r * a;
				g = g * a;
				b = b * a;
				
				r = Math.min(r, 255);
				g = Math.min(g, 255);
				b = Math.min(b, 255);
				
				rgb = (255 << 24) | ((int)r << 16) | ((int)g << 8) | ((int)b << 0);
				image.setRGB(x, y, rgb);
			}
		}
	}
	
	

	public static float saturate(float a) {
		if (a < 0) {
			a = 0;
		}
		if (a > 1) {
			a = 1;
		}
		return a;
	}
}
