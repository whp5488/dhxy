package com.kitty.common.utils.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileUtils {
  public static String readText(String fileName) throws IOException {
    File file = new File(fileName);
    if (!file.exists())
      throw new FileNotFoundException(); 
    FileInputStream in = new FileInputStream(file);
    StringBuilder result = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
      String line;
      while ((line = br.readLine()) != null)
        result.append(line).append("\n"); 
    } 
    in.close();
    return result.toString();
  }
  
  public static List<String> readLines(String fileName) throws IOException {
    File file = new File(fileName);
    if (!file.exists())
      throw new FileNotFoundException(); 
    List<String> result = new ArrayList<>();
    FileInputStream in = new FileInputStream(file);
    try (BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
      String line;
      while ((line = br.readLine()) != null)
        result.add(line); 
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
      } else {
        File[] files = file.listFiles();
        result.addAll(Arrays.asList(files));
      } 
    } catch (Exception exception) {}
    return result;
  }
}
