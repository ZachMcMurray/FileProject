package com.mycompany.fileprocessing;

import java.util.ArrayList;

public class PrintProcessingElement {

    public void printEntries(ArrayList<Entrys> entries) {
        for (Entrys entry : entries) {
            if (entry.location.equals("local")) {
                System.out.println("Name: " + entry.name);
                System.out.println("Length: " + entry.file.length());
                System.out.println("Absolute Path: " + entry.file.getAbsolutePath());
            } else if (entry.location.equals("remte")) {
                System.out.println("Entry Id: " + ((RemteEntry) entry).getEntryId());
                System.out.println("Name: " + entry.name);
                System.out.println("Length: " + entry.file.length());
                System.out.println("Absolute Path: " + ((RemteEntry) entry).entry.getFullPath());
            }
        }

    }
}
