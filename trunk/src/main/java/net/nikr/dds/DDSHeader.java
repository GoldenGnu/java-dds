/*
 * DDSHeader.java - This file is part of Java DDS ImageIO Plugin
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


public class DDSHeader {
	public static final int HEIGHT = 0x00000002;
	public static final int WIDTH = 0x00000004;
	public static final int LINEARSIZE = 0x00080000;
	public static final int DEPTH = 0x00800000;
	public static final int MIPMAPCOUNT = 0x00020000;
	public static final int PIXELFORMAT = 0x00001000;
	public static final int CAPS = 0x00000001;
	public static final int PITCH = 0x00000008;
	
	private int size;
	private int flags;
	private int height;
	private int width;
	private int pitchOrLinearSize;
	private int depth;
	private int mipMapCount;
	private PixelFormat pixelFormat;
	private int caps;
    private int caps2;
    private int caps3;
    private int caps4;
	
	
	public DDSHeader(int size, int flags, int height, int width, int linearSize, int depth, int mipMapCount, PixelFormat pixelFormat, int caps, int caps2, int caps3, int caps4) {
		this.size = size;
		this.flags = flags;
		this.height = height;
		this.width = width;
		this.pitchOrLinearSize = linearSize;
		this.depth = depth;
		this.mipMapCount = mipMapCount;
		this.pixelFormat = pixelFormat;
		this.caps = caps;
		this.caps2 = caps2;
		this.caps3 = caps3;
		this.caps4 = caps4;
	}
	public int getSize() {
		return size;
	}
	public int getFlags() {
		return flags;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public int getPitchOrLinearSize() {
		return pitchOrLinearSize;
	}
	public int getDepth() {
		return depth;
	}
	public int getMipMapCount() {
		return mipMapCount;
	}
	public void printValues(){
		System.out.println("DDSHeader:");
		System.out.println("	size: "+size);
		System.out.print("	flags: "+flags);
		if ((flags & HEIGHT) != 0) System.out.print(" (HEIGHT)");
		if ((flags & WIDTH) != 0) System.out.print(" (WIDTH)");
		if ((flags & LINEARSIZE) != 0) System.out.print(" (LINEARSIZE)");
		if ((flags & DEPTH) != 0) System.out.print(" (DEPTH)");
		if ((flags & MIPMAPCOUNT) != 0) System.out.print(" (MIPMAPCOUNT)");
		if ((flags & PIXELFORMAT) != 0) System.out.print(" (PIXELFORMAT)");
		if ((flags & CAPS) != 0) System.out.print(" (CAPS)");
		if ((flags & PITCH) != 0) System.out.print(" (PITCH)");
		System.out.print("\n");
		System.out.println("	height: "+height);
		System.out.println("	width: "+width);
		System.out.println("	linearSize: "+pitchOrLinearSize);
		System.out.println("	depth: "+depth);
		System.out.println("	mipMapCount: "+mipMapCount);
		pixelFormat.printValues(1);
		System.out.println("	caps: "+caps);
		System.out.println("	caps2: "+caps2);
		System.out.println("	caps3: "+caps3);
		System.out.println("	caps4: "+caps4);
	}
	public PixelFormat getPixelFormat() {
		return pixelFormat;
	}
}
