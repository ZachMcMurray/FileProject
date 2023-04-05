package com.mycompany.fileprocessing;

import java.io.File;
import java.util.ArrayList;

public class Rename {

    public void renamePE(ArrayList<Entrys> entries, String suffix) {

        for (Entrys entry : entries) {
            String name = entry.file.getName();
            String extension = "";
            if (name.contains(".")) {
                int dotIndex = name.lastIndexOf(".");
                extension = name.substring(dotIndex);
                name = name.substring(0, dotIndex);
            }

            String newName = name + suffix + extension;

            File newNameFile = new File(newName);
            entry.file.renameTo(newNameFile);
        }

    }
}
