package com.core.zander.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.core.zander.constants.FormatConstants;

public class FileUtil {

	public static final SimpleDateFormat format = new SimpleDateFormat(FormatConstants.DATE_TIME_FORMAT);

    /**
     * Get all the files in the folder and its subfolders
     * @param dir	the folder
     * @param list	save all files
     */
    public static void getAllFiles(final File dir, final List<File> list) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            final File[] files = dir.listFiles();
            if (files != null && files.length > 0) {
                for(final File file: files) {
                    if (file.isFile() && !file.isHidden()) {
                        list.add(file);
                    } else if (file.isDirectory() && !file.isHidden()) {
                        getAllFiles(file, list);
                    }
                }
            }
        }
    }

    /**
     * Get the creation time of the file
     * @param filename
     * @return
     */
    public static Date getFileCreationTime(final String filename) {
        final Path path = Paths.get(filename);
        final BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
        try {
            final BasicFileAttributes attr = basicView.readAttributes();
            return new Date(attr.creationTime().toMillis());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(final String[] args) {
        final File dir = new File("F:/file");
        final List<File> list = new ArrayList<File>();
        getAllFiles(dir, list);
        for (final File file : list) {
            System.out.println(file.getAbsolutePath());
            System.out.println("file creation date: " + format.format(getFileCreationTime(file.getAbsolutePath())));
            System.out.println("last modified date: " + format.format(new Date(file.lastModified())));
            System.out.println("-------------------------------------------------------");
        }
    }

}
