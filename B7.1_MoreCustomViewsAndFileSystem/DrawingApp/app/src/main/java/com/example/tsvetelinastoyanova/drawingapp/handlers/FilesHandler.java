package com.example.tsvetelinastoyanova.drawingapp.handlers;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FilesHandler {

    public boolean saveDrawing(Bitmap bitmap, String nameOfDrawing, String fullPathDirectory) {

        FileOutputStream outputStream = null;
        try {
            File subFolder = prepareDirectoryForSavingFile(fullPathDirectory);
            String fullPathToFile = new File(subFolder, nameOfDrawing).toString();
            outputStream = new FileOutputStream(fullPathToFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return true;

        } catch (FileNotFoundException e) {
            Log.e("ERROR", "An exception with the stream while saving the drawing occured." + e.toString());
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                Log.e("ERROR", "An exception while closing the stream occured." + e.toString());
                e.printStackTrace();
            }
        }
        return false;
    }

    private File prepareDirectoryForSavingFile(String fullPathDirectory) {
        File subFolder = new File(fullPathDirectory);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
        return subFolder;
    }

    public boolean deleteDrawing(File fileToDelete, File fullPathDirectory) {
        for (File file : fullPathDirectory.listFiles()) {
            if (file.equals(fileToDelete)) {
                file.delete();
                return true;
            }
        }
        return false;
    }

    public boolean renameDrawing(int position, File fileToRename, File fullPathDirectory, List<File> files, String newName) {
        for (File file : fullPathDirectory.listFiles()) {
            if (file.equals(fileToRename)) {
                File newFile = new File(file.getParent(), newName);
                file.renameTo(newFile);
                files.remove(position);
                files.add(position, newFile);
                return true;
            }
        }
        return false;
    }

}
