/*
 * DDSPixelFormat.java - This file is part of Java DDS ImageIO Plugin
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


public class DDSPixelFormat {
	//Flags
	public static final int RGB = 0x00000040;
	public static final int ALPHAPIXELS = 0x1; //0x00000001;
	public static final int FOURCC = 0x00000004;
	
	//Format
	public enum Format {
		NOT_DDS (0, "NOT DDS FORMAT"),
		DXT1	(0x31545844, "DXT1"),
		DXT2	(0x32545844, "DXT2"),
		DXT3	(0x33545844, "DXT3"),
		DXT4	(0x34545844, "DXT4"),
		DXT5	(0x35545844, "DXT5"),
		ATI1N	(0x31495441, "ATI1N"),
		ATI2N	(0x32495441, "ATI2N"),
		A8R8G8B8(0,"RGB (A8R8G8B8)"), //FIXME no fourCC flag
		R8G8B8	(0,"RGB (R8G8B8)"), //FIXME no fourCC flag
		X8R8G8B8(0,"RGB (X8R8G8B8)"); //FIXME no fourCC flag

		private int fourCC = 0;
		private String name;

		private Format(int fourCC, String name) {
			this.fourCC = fourCC;
			this.name = name;
		}

		public int getFourCC() {
			return fourCC;
		}

		public String getName() {
			return name;
		}
	}
	
	private int size;
    private int flags;
    private int fourCC;
    private int rgbBitCount;
    private int rBitMask;
	private int gBitMask;
	private int bBitMask;
    private int rgbAlphaBitMask;
	/**
	 * Creates a new instance of DDSPixelFormat
	 */
	public DDSPixelFormat(int size, int flags, int fourCC, int rgbBitCount, int rBitMask, int gBitMask, int bBitMask, int rgbAlphaBitMask) {
		this.size = size;
		this.flags = flags;
		this.fourCC = fourCC;
		this.rgbBitCount = rgbBitCount;
		this.rBitMask = rBitMask;
		this.gBitMask = gBitMask;
		this.bBitMask = bBitMask;
		this.rgbAlphaBitMask = rgbAlphaBitMask;
	}

	public int getSize() {
		return size;
	}
	public int getFlags() {
		return flags;
	}
	public int getFourCC() {
		return fourCC;
	}
	public int getRgbBitCount() {
		return rgbBitCount;
	}
	public int getRedBitMask() {
		return rBitMask;
	}
	public int getGreenBitMask() {
		return gBitMask;
	}
	public int getBlueBitMask() {
		return bBitMask;
	}
	public int getAlphaBitMask() {
		return rgbAlphaBitMask;
	}
	public Format getFormat(){
		if (isA8R8G8B8()) return Format.A8R8G8B8;
		if (isR8G8B8()) return Format.R8G8B8;
		if (isX8R8G8B8()) return Format.X8R8G8B8;
		if (isDXT1()) return Format.DXT1;
		if (isDXT2()) return Format.DXT2;
		if (isDXT3()) return Format.DXT3;
		if (isDXT4()) return Format.DXT4;
		if (isDXT5()) return Format.DXT5;
		if (isATI1N()) return Format.ATI1N;
		if (isATI2N()) return Format.ATI2N;
		
		return Format.NOT_DDS;
	}
	public boolean isCompressed(){
		return ((flags & FOURCC) != 0);
	}
	public boolean isA8R8G8B8(){
		return (getRgbBitCount() == 32 && getRedBitMask() == 0x00FF0000 && getGreenBitMask() == 0x0000FF00 && getBlueBitMask() == 0x000000FF && getAlphaBitMask() == 0xFF000000);
	}
	public boolean isR8G8B8(){
		return (getRgbBitCount() == 24 && getRedBitMask() == 0x00FF0000 && getGreenBitMask() == 0x0000FF00 && getBlueBitMask() == 0x000000FF && getAlphaBitMask() == 0xFF000000);
	}
	public boolean isX8R8G8B8(){
		return (getRgbBitCount() == 24 && getRedBitMask() == 0x00FF0000 && getGreenBitMask() == 0x0000FF00 && getBlueBitMask() == 0x000000FF && getAlphaBitMask() != 0xFF000000);
	}
	public boolean isDXT1(){
		return (fourCC == Format.DXT1.getFourCC());
	}
	public boolean isDXT2(){
		return (fourCC == Format.DXT2.getFourCC());
	}
	public boolean isDXT3(){
		return (fourCC == Format.DXT3.getFourCC());
	}
	public boolean isDXT4(){
		return (fourCC == Format.DXT4.getFourCC());
	}
	public boolean isDXT5(){
		return (fourCC == Format.DXT5.getFourCC());
	}
	public boolean isATI1N(){
		return (fourCC == Format.ATI1N.getFourCC());
	}
	public boolean isATI2N(){
		return (fourCC == Format.ATI2N.getFourCC());
	}
	public void printValues(){
		printValues(0);
	}
	
	public void printValues(int nSpace){
		String sSpace = "";
		for (int i = 0; i < nSpace; i++){
			sSpace = sSpace + "	";
		}
		System.out.println(sSpace+"PixelFormat: ");
		
		System.out.println(sSpace+"	size: "+size);
		System.out.print(sSpace+"	flags: "+flags);
		if ((flags & RGB) != 0) System.out.print(" (RGB)");
		if ((flags & FOURCC) != 0) System.out.print(" (FOURCC)");
		if ((flags & ALPHAPIXELS) != 0) System.out.print(" (ALPHAPIXELS)");
		System.out.print("\n");
		System.out.println(sSpace+"	fourCC: "+fourCC+" ("+getFormat().getName()+")");
		System.out.println(sSpace+"	rgbBitCount: "+rgbBitCount);
		System.out.println(sSpace+"	rBitMask: "+Integer.toHexString(rBitMask));
		System.out.println(sSpace+"	gBitMask: "+Integer.toHexString(gBitMask));
		System.out.println(sSpace+"	bBitMask: "+Integer.toHexString(bBitMask));
		System.out.println(sSpace+"	rgbAlphaBitMask: "+Integer.toHexString(rgbAlphaBitMask));
		System.out.println(sSpace+"	Format: "+getFormat().getName());
	}
}
