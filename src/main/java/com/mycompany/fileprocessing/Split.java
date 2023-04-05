/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.fileprocessing;

/**
 *
 * @author zmcmu
 */
import java.io.*;
import java.util.*;
import java.util.List;

public class Split {
    private List<Entrys> entries;
    private int lines;

    public Split(List<Entrys> entries, int lines) {
        this.entries = entries;
        this.lines = lines;
    }

    public ArrayList<Entrys> split() throws IOException {
        ArrayList<Entrys> result = new ArrayList<>();
        for (Entrys entry : entries) { // goes through list
            if (entry.file.isFile()) {
                result.addAll(splitFile(entry.file));
            }
        }
        return result;
    }

    private List<Entrys> splitFile(File file) throws IOException {
        List<Entrys> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {//reading the input file
            List<String> lines = new ArrayList<>();
            String line;//current line
            while ((line = reader.readLine()) != null) {//sets line to the current line in question, also checks if we are at the end of the file
                lines.add(line);//adds the line to the list
                if (lines.size() == this.lines) {//if this condition is met it creates a new file with the lines then it wipes the current lines
                    result.add(writePart(file, result.size() + 1, lines));//adds subfiles to list of files
                    lines.clear();
                }
            }
            if (!lines.isEmpty()) {//if the lines is not empty and the size is less then lines add it to a new file
                result.add(writePart(file, result.size() + 1, lines)); //adds subfiles to list of files
            }
        }
        return result;
    }

    private Entrys writePart(File file, int partNum, List<String> lines) throws IOException {
        String filename = file.getName(); //gets the name of the parent file
        int lastDot = filename.lastIndexOf(".");//finds the index/location of the ., if there is none then a -1 will be returned
        String baseName = (lastDot == -1) ? filename : filename.substring(0, lastDot);//if false this extracts parent filename up to the dot
        String extension = (lastDot == -1) ? "" : filename.substring(lastDot);//sets up for string addition
        String partFilename = String.format("%s.part%d%s", baseName, partNum, extension); //addition of the string names
        File partFile = new File(file.getParentFile(), partFilename); //sets the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(partFile))) {//writes the lines to the given file
            for (String line : lines) {
                writer.println(line);
            }
        }
        return new LocalEntry(partFile.getAbsolutePath());
    }
}