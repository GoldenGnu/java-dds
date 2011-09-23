/*
 * DDSPixelFormat.java - This file is part of Java DDS ImageIO Plugin
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
 * TODO Write File Description for DDSPixelFormat.java
 */


package net.nikr.dds;


public class DDSPixelFormat {
	//Flags
	public static final int ALPHAPIXELS = 0x1; //0x00000001;
	public static final int ALPHA = 0x2; //0x00000001;
	public static final int FOURCC = 0x4;
	public static final int RGB = 0x40;
	public static final int YUV = 0x200;
	public static final int LUMINANCE = 0x20000;
	
	//Format
	public enum Format {
		NOT_DDS ("NOT DDS FORMAT"),
		UNCOMPRESSED("UNCOMPRESSED"),
		DXT1	("DXT1"),
		DXT2	("DXT2"),
		DXT3	("DXT3"),
		DXT4	("DXT4"),
		DXT5	("DXT5"),
		BC4U	("BC4U"),
		BC4S	("BC4S"),
		ATI1	("ATI1"),
		ATI2	("ATI2"),
		BC5S	("BC5S"),
		RGBG	("RGBG"),
		GRGB	("GRGB"),
		UYVY	("UYVY"),
		YUY2	("YUY2"),
		DX10	("DX10"),
		;

		private String name;

		private Format(String name) {
			this.name = name;
		}

		public int getFourCC() {
			return fourCC(name);
		}

		public String getName() {
			return name;
		}
		
		private int fourCC(String cc){
			int result = 0;
			for ( int i = cc.length()-1; i >= 0; i--) {
				result = ( result << 8 ) + (int) cc.charAt( i );
			}
			return result;
		}
	}
	
	private int size;
    private long flags;
    private long fourCC;
    private long rgbBitCount;
    private long rMask;
	private long gMask;
	private long bMask;
    private long aMask;
    private int rShift;
	private int gShift;
	private int bShift;
    private int aShift;
    private int rBits;
	private int gBits;
	private int bBits;
    private int aBits;
	/**
	 * Creates a new instance of DDSPixelFormat
	 */
	public DDSPixelFormat(int size, long flags, long fourCC, long rgbBitCount, long rBitMask, long gBitMask, long bBitMask, long aBitMask) {
		this.size = size;
		this.flags = flags;
		this.fourCC = fourCC;
		this.rgbBitCount = rgbBitCount;
		this.rMask = rBitMask;
		this.gMask = gBitMask;
		this.bMask = bBitMask;
		this.aMask = aBitMask;
		
		this.rShift = shift(rBitMask);
		this.gShift = shift(gBitMask);
		this.bShift = shift(bBitMask);
		this.aShift = shift(aBitMask);
		
		this.rBits = bits(rBitMask);
		this.gBits = bits(gBitMask);
		this.bBits = bits(bBitMask);
		this.aBits = bits(aBitMask);
	}

	public int getSize() {
		return size;
	}
	public long getFlags() {
		return flags;
	}
	public long getFourCC() {
		return fourCC;
	}
	public long getRgbBitCount() {
		return rgbBitCount;
	}
	public long getRedBitMask() {
		return rMask;
	}
	public long getGreenBitMask() {
		return gMask;
	}
	public long getBlueBitMask() {
		return bMask;
	}
	public long getAlphaBitMask() {
		return aMask;
	}
	public int getRedShift() {
		return rShift;
	}
	public int getGreenShift() {
		return gShift;
	}
	public int getBlueShift() {
		return bShift;
	}
	public int getAlphaShift() {
		return aShift;
	}
	public int getRedBits() {
		return rBits;
	}
	public int getGreenBits() {
		return gBits;
	}
	public int getBlueBits() {
		return bBits;
	}
	public int getAlphaBits() {
		return aBits;
	}
	public Format getFormat(){
		if (isDXT1()) return Format.DXT1;
		if (isDXT2()) return Format.DXT2;
		if (isDXT3()) return Format.DXT3;
		if (isDXT4()) return Format.DXT4;
		if (isDXT5()) return Format.DXT5;
		if (isATI1()) return Format.ATI1;
		if (isATI2()) return Format.ATI2;
		if (fourCC == Format.BC4U.getFourCC()) return Format.BC4U;
		if (fourCC == Format.BC4S.getFourCC()) return Format.BC4S;
		if (fourCC == Format.BC5S.getFourCC()) return Format.BC5S;
		if (fourCC == Format.RGBG.getFourCC()) return Format.RGBG;
		if (fourCC == Format.GRGB.getFourCC()) return Format.GRGB;
		if (fourCC == Format.UYVY.getFourCC()) return Format.UYVY;
		if (fourCC == Format.YUY2.getFourCC()) return Format.YUY2;
		if (fourCC == Format.DX10.getFourCC()) return Format.DX10;
		if (!isCompressed()) return Format.UNCOMPRESSED;
		return Format.NOT_DDS;
	}
	public boolean isCompressed(){
		return ((flags & FOURCC) != 0);
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
	public boolean isATI1(){
		return (fourCC == Format.ATI1.getFourCC());
	}
	public boolean isATI2(){
		return (fourCC == Format.ATI2.getFourCC());
	}
	
	
	public boolean isAlphaPixels(){
		return ((flags & ALPHAPIXELS) != 0);
	}
	public boolean isAlpha(){
		return ((flags & ALPHA) != 0);
	}
	public boolean isFourCC(){
		return ((flags & FOURCC) != 0);
	}
	public boolean isRGB(){
		return ((flags & RGB) != 0);
	}
	public boolean isYUV(){
		return ((flags & YUV) != 0);
	}
	public boolean isLuminance(){
		return ((flags & LUMINANCE) != 0);
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
		if ((flags & ALPHAPIXELS) != 0) System.out.print(" (ALPHAPIXELS)");
		if ((flags & ALPHA) != 0) System.out.print(" (ALPHA)");
		if ((flags & FOURCC) != 0) System.out.print(" (FOURCC)");
		if ((flags & RGB) != 0) System.out.print(" (RGB)");
		if ((flags & YUV) != 0) System.out.print(" (YUV)");
		if ((flags & LUMINANCE) != 0) System.out.print(" (LUMINANCE)");
		System.out.print("\n");
		System.out.println(sSpace+"	fourCC: "+fourCC+" ("+getFormat().getName()+")");
		System.out.println(sSpace+"	rgbBitCount: "+rgbBitCount);
		System.out.println(sSpace+"	rBitMask: "+Long.toHexString(rMask));
		System.out.println(sSpace+"	gBitMask: "+Long.toHexString(gMask));
		System.out.println(sSpace+"	bBitMask: "+Long.toHexString(bMask));
		System.out.println(sSpace+"	rgbAlphaBitMask: "+Long.toHexString(aMask));
		System.out.println(sSpace+"	Format: "+getFormat().getName());
	}
	
	private char shift(long mask){
		char i = 0;
		if(mask <= 0) return 0;
		while(((mask >> i) & 1) <= 0){
			++i;
		}
		return i;
	}
	
	private char bits(long mask){
		char i = 0;

		while(mask > 0) {
			if((mask & 1) != 0) ++i;
			mask >>= 1;
		}
		return i;
	}
}