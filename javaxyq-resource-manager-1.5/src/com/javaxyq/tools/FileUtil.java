/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public class FileUtil {

    public static final String TCP_FILE_FLAG = "53 50";//SP

    public static final String MP3_FILE_FLAG = "49 44 33";//ID3

    public static final String MP3_FILE2_FLAG = "FF F2";//?mp3

    public static final String MIDI_FILE_FLAG = "4D 54 68 64";// MThd

    public static final String JPG_FILE_FLAG = "FF D8";

    public static final String PNG_FILE_FLAG = "89 50 4E 47 0D 0A 1A 0A";

    public static final String TGA_FILE_FLAG = "00 00 02 00 00";

    public static final String TGA_FILE_RLE_FLAG = "00 00 10 00 00";

    public static final String GIF_FILE_FLAG = "47 49 46";//GIF

    public static final String BMP_FILE_FLAG = "42 4D";//BM

    public static final String WAV_FILE_FLAG = "52 49 46 46";//RIFF

    public static final String WDFP_FILE_FLAG = "50 46 44 57";//PFDW

    public static final String WDFX_FILE_FLAG = "58 46 44 57";//XFDW

    public static final String WDFH_FILE_FLAG = "48 46 44 57";//HFDW

    public static final String MAP01_FILE_FLAG = "30 2E 31 4D";//0.1M

    public static final Map<String, String> fileTypes = new HashMap<String, String>();

    static {
        fileTypes.put(TCP_FILE_FLAG, FileObject.TCP_FILE);
        fileTypes.put(JPG_FILE_FLAG, FileObject.JPG_FILE);
        fileTypes.put(MP3_FILE_FLAG, FileObject.MP3_FILE);
        fileTypes.put(MP3_FILE2_FLAG, FileObject.MP3_FILE);
        fileTypes.put(PNG_FILE_FLAG, FileObject.PNG_FILE);
        fileTypes.put(TGA_FILE_FLAG, FileObject.TGA_FILE);
        fileTypes.put(TGA_FILE_RLE_FLAG, FileObject.TGA_RLE_FILE);
        fileTypes.put(GIF_FILE_FLAG, FileObject.GIF_FILE);
        fileTypes.put(MIDI_FILE_FLAG, FileObject.MIDI_FILE);
        fileTypes.put(BMP_FILE_FLAG, FileObject.BMP_FILE);
        fileTypes.put(WAV_FILE_FLAG, FileObject.WAV_FILE);
        fileTypes.put(WDFP_FILE_FLAG, FileObject.WDF_FILE);
        fileTypes.put(WDFX_FILE_FLAG, FileObject.WDF_FILE);
        fileTypes.put(WDFH_FILE_FLAG, FileObject.WDF_FILE);
        fileTypes.put(MAP01_FILE_FLAG, FileObject.MAP_FILE);
    }

    public static String getContentType(FileObject fileObject) {
        String type = "unknown";
        byte[] data = new byte[100];
        try {
            if (fileObject != null) {
                if (fileObject.isDirectory()) {
                    return FileObject.DIRECTORY;
                }
                fileObject.getDataStream().read(data);
            }
            Set<String> keys = fileTypes.keySet();
            for (String key : keys) {
                int byteCount = key.split(" ").length;
                if (toHexString(data, 0, byteCount).equals(key)) {
                    return fileTypes.get(key);
                }
            }
            System.out.printf("unknown file:[%s ..]\n", toHexString(data, 0, 10));
        } catch (Exception e) {
            System.out.println("get content type error! node=" + fileObject);
            e.printStackTrace();
        }
        return type;
    }

    public static String toHexString(byte[] buf, int offset, int length) {
        StringBuilder output = new StringBuilder();
        for (int i = offset; i < length; i++) {
            String strHex = Integer.toHexString(buf[offset + i] & 0xFF);
            if (strHex.length() < 2) {
                output.append('0');
            }
            output.append(strHex);
            output.append(' ');
        }
        output.deleteCharAt(output.length() - 1);
        return output.toString().toUpperCase();
    }

}
