/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.tools;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.javaxyq.util.Wildcard;

/**
 * @author 龚德伟
 * @history 2008-7-6 龚德伟 新建
 */
public class DefaultFileObject implements FileObject {

    private File file;

    private DefaultFileSystem fileSystem;

	private Comparator<File> fileComparator = new Comparator<File>() {
		@Override
		public int compare(File o1, File o2) {
			int result = (o1.isDirectory()?0:1) - (o2.isDirectory()?0:1);
			if(result==0) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
			return result;
		}
	};

    public DefaultFileObject(DefaultFileSystem filesystem, String pathname) {
        this.fileSystem = filesystem;
        this.file = new File(pathname);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, URI uri) {
        this.fileSystem = filesystem;
        this.file = new File(uri);
    }

    public DefaultFileObject(DefaultFileSystem filesystem, File file) {
        this.fileSystem = filesystem;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public byte[] getData() throws IOException {
    	DataInputStream is = getDataStream();
    	byte[] data = new byte[(int) file.length()];
    	is.readFully(data);
        return data;
    }

    public DataInputStream getDataStream() throws FileNotFoundException {
        return new DataInputStream(new FileInputStream(file));
    }

    public String getName() {
        return file.getName();
    }

    public String getPath() {
        return file.getPath();
    }

    public String getContentType() {
        return FileUtil.getContentType(this);
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public boolean isFile() {
        return file.isFile();
    }

	@Override
	public FileObject[] listFiles(String filter) {
		File[] allfiles = null;
		if(filter!=null && filter.trim().length()!=0 && !filter.trim().equals("*")) {
			if(filter.indexOf('*')==-1) {
				filter = "*"+filter+"*";
			}
			final String pattern = filter.toLowerCase();
			FilenameFilter namefilter = new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return Wildcard.matches(pattern, name.toLowerCase());
				}
			};
			allfiles =  file.listFiles(namefilter);
		}else {
			allfiles = file.listFiles();
		}
		Arrays.sort(allfiles, fileComparator );
		FileObject[] fileObjects = new FileObject[allfiles.length];
		for (int i = 0; i < allfiles.length; i++) {
			DefaultFileObject fileObj = new DefaultFileObject(fileSystem, allfiles[i]);
			fileObjects[i] = fileObj;
		}
		return fileObjects;
	}
	public FileObject[] listFiles() {
		return listFiles(null);
    }

    public int compareTo(FileObject o) {
    	if(this.isDirectory() && !o.isDirectory()) {
    		return 1;
    	}else if(!this.isDirectory()&& o.isDirectory()) {
    		return -1;
    	}
        return this.getPath().compareTo(o.getPath());
    }

    public FileSystem getFileSystem() {
        return this.fileSystem;
    }

    public FileObject getParent() {
        return new DefaultFileObject(this.fileSystem, this.file.getParent());
    }

    @Override
    public String toString() {
        return this.getName();
    }

	@Override
	public long getSize() {
		return this.file.length();
	}


}
