package com.example.tsvetelinastoyanova.drawingapp.handlers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class FilesHandler {

    public boolean saveDrawing(Bitmap bitmap, String nameOfDrawing, String fullPathDirectory,
                               Activity activity, Runnable beforeSaveAction, Runnable afterSaveAction) {
        activity.runOnUiThread(beforeSaveAction);

        new Thread(() -> {
            FileOutputStream outputStream = null;
            try {
                File subFolder = prepareDirectory(fullPathDirectory);
                String fullPathToFile = new File(subFolder, nameOfDrawing).toString();
                outputStream = new FileOutputStream(fullPathToFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            } catch (FileNotFoundException e) {
                Log.e("ERROR", "An exception with the stream while saving the drawing occured." + e.toString());
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e("ERROR", "An exception while closing the stream occured." + e.toString());
                    e.printStackTrace();
                }
                activity.runOnUiThread(afterSaveAction);
            }
        }).start();
        return true;
    }

    public File prepareDirectory(String fullPathDirectory) {
        File subFolder = new File(fullPathDirectory);
        if (!subFolder.exists()) {
            subFolder.mkdirs();
        }
        return subFolder;
    }

    public boolean deleteDrawing(File fileToDelete, File fullPathDirectory, Activity activity, Runnable afterDeleteAction, Runnable notSuccessfulDeleteAction) {
        new Thread(() -> {
            for (File file : fullPathDirectory.listFiles()) {
                if (file.equals(fileToDelete)) {
                    file.delete();
                    activity.runOnUiThread(afterDeleteAction);
                }
            }
            activity.runOnUiThread(notSuccessfulDeleteAction);
        }).start();
        return true;
    }

    public boolean renameDrawing(int position, File fullPathDirectory, List<File> files, String newName, Activity activity, Runnable afterRenameAction, Runnable notSuccessfulRenameAction) {
        File fileToRename = files.get(position);
        new Thread(() -> {
            for (File file : fullPathDirectory.listFiles()) {
                if (file.equals(fileToRename)) {
                    File newFile = new File(file.getParent(), newName);
                    file.renameTo(newFile);
                    files.remove(position);
                    files.add(position, newFile);
                    activity.runOnUiThread(afterRenameAction);
                }
            }
            activity.runOnUiThread(notSuccessfulRenameAction);
        }).start();
        return true;
    }

}
