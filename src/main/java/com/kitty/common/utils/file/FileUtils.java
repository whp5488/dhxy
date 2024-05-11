
package com.kitty.common.utils.file;

import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;

public class FileUtils
{
    public static String readText(final String fileName) throws IOException {
        final File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        final FileInputStream in = new FileInputStream(file);
        final StringBuilder result = new StringBuilder();
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        in.close();
        return result.toString();
    }
    
    public static List<String> readLines(final String fileName) throws IOException {
        final File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        final List<String> result = new ArrayList<String>();
        final FileInputStream in = new FileInputStream(file);
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        in.close();
        return result;
    }
    
    public static List<File> listFiles(final String path) {
        final List<File> result = new ArrayList<File>();
        try {
            final File file = new File(path);
            if (file.isFile()) {
                result.add(file);
            }
            else {
                final File[] files = file.listFiles();
                result.addAll(Arrays.asList(files));
            }
        }
        catch (final Exception ex) {}
        return result;
    }
}
