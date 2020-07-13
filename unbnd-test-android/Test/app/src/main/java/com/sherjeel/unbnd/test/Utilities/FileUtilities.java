package com.sherjeel.unbnd.test.Utilities;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class FileUtilities {

    public File createFile(Context context, String folderName, String fileName) throws IOException {
        final ContextWrapper contextWrapper = new ContextWrapper(context);
        final File directory = contextWrapper.getDir(folderName, Context.MODE_PRIVATE); // path to /data/data/yourapp/folderName
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File file = new File(directory, fileName); // Create image file
        if (file.exists()) {
            if (file.delete()) {
                file.createNewFile();
                return file;
            } else {
                Log.d("FileUtilities", "Unable to create file because file is already exists and in use.");
            }
        } else {
            Log.d("FileUtilities" , fileName + " is created successfully");
            file.createNewFile();
            return file;
        }
        return  null;
    }

    public File loadFile(Context context, String folderName, String fileName) {
        final ContextWrapper contextWrapper = new ContextWrapper(context);
        final File directory = contextWrapper.getDir(folderName, Context.MODE_PRIVATE); // path to /data/data/yourapp/folderName
        final File file = new File(directory, fileName);
        if (file.exists()) {
            return file;
        } else {
            Log.d("FileUtilities", fileName + ": File doesn't exist!");
            return  null;
        }
    }
}
