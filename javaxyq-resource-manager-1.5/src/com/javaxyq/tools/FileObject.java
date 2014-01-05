/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public interface FileObject extends Comparable<FileObject>,Serializable{
	public static final String BMP_FILE = "bmp";
	
	public static final String MIDI_FILE = "midi";
	
	public static final String GIF_FILE = "gif";
	
	public static final String TGA_RLE_FILE = "tga(rle)";
	
	public static final String TGA_FILE = "tga";
	
	public static final String PNG_FILE = "png";
	
	public static final String MP3_FILE = "mp3";
	
	public static final String WAV_FILE = "wav";
	
	public static final String JPG_FILE = "jpg";
	
	public static final String TCP_FILE = "tca/tcp";
	
	public static final String WDF_FILE = "wdf";
	
	public static final String UNKNOWN_FILE = "unknown";
	
	public static final String DIRECTORY = "directory";
	
	public static final String MAP_FILE = "map";

    boolean isDirectory();

    boolean isFile();

    FileObject[] listFiles(String filter);

    FileObject[] listFiles();
    
    String getName();

    String getPath();
    
    FileObject getParent();

    byte[] getData() throws IOException;

    InputStream getDataStream() throws IOException;
    
    String getContentType();
    
    FileSystem getFileSystem();

	long getSize();
	
}
