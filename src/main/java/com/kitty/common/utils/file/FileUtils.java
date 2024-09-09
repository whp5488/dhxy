package com.kitty.common.utils.file;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;

public class FileUtils
{
    public FileUtils() {
    }
    
    public static String readText(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        FileInputStream in = new FileInputStream(file);
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        Throwable t = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        }
        catch (Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            if (br != null) {
                if (t != null) {
                    try {
                        br.close();
                    }
                    catch (Throwable exception2) {
                        t.addSuppressed(exception2);
                    }
                }
                else {
                    br.close();
                }
            }
        }
        in.close();
        return result.toString();
    }
    
    public static List<String> readLines(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        List<String> result = new ArrayList<>();
        FileInputStream in = new FileInputStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        Throwable t = null;
        try {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        catch (Throwable t2) {
            t = t2;
            throw t2;
        }
        finally {
            if (br != null) {
                if (t != null) {
                    try {
                        br.close();
                    }
                    catch (Throwable exception2) {
                        t.addSuppressed(exception2);
                    }
                }
                else {
                    br.close();
                }
            }
        }
        in.close();
        return result;
    }
    
    public static List<File> listFiles(String path) {
        List<File> result = new ArrayList<>();
        try {
            File file = new File(path);
            if (file.isFile()) {
                result.add(file);
            }
            else {
                File[] files = file.listFiles();
                result.addAll(Arrays.asList(files));
            }
        }
        catch (Exception ex) {}
        return result;
    }
}
