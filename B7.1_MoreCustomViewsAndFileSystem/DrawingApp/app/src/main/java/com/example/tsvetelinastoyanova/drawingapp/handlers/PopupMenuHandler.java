package com.example.tsvetelinastoyanova.drawingapp.handlers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.FileProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.drawingapp.BuildConfig;
import com.example.tsvetelinastoyanova.drawingapp.R;
import com.example.tsvetelinastoyanova.drawingapp.RecyclerViewAdapter;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.DeleteDialog;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.SaveDialog;

import java.io.File;
import java.util.List;

public class PopupMenuHandler {
    private Context context;
    private PopupMenu popup;
    private int position;
    private List<File> files;
    private RecyclerViewAdapter adapter;

    public PopupMenuHandler(Context context, PopupMenu popup, int position, List<File> files, RecyclerViewAdapter adapter) {
        this.context = context;
        this.popup = popup;
        this.position = position;
        this.files = files;
        this.adapter = adapter;
    }

    public void setClickListenersOnPopupMenu(Activity activity) {
        popup.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()) {
                case R.id.delete:
                    showDialogToDelete(position, activity);
                    break;
                case R.id.rename:
                    showDialogToRename(position, activity);
                    break;
                case R.id.share:
                    showDialogToSendEmail(position);
                    break;
            }
            return false;

        });
    }

    private void showDialogToDelete(int position, Activity activity) {
        String title = context.getResources().getString(R.string.delete_question);
        DeleteDialog deleteDialog = new DeleteDialog(context);
        deleteDialog.setContentView(R.layout.delete_drawing_dialog)
                .setTitleToDialog(R.id.title, title)
                .initButtonOk(R.id.button_ok)
                .initButtonCancel(R.id.button_cancel)
                .showDialog();
        setOnClickListenerToConfirmDelete(activity, position, deleteDialog.getDialog(), deleteDialog.getButtonOk());
        setOnClickListenerForCancel(deleteDialog.getDialog(), deleteDialog.getButtonCancel());
    }

    private void setOnClickListenerToConfirmDelete(Activity activity, int positionToDelete, Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            FilesHandler filesHandler = new FilesHandler();
            String nameOfDirectory = context.getResources().getString(R.string.directory);
            File directoryToTraverse = new File(context.getFilesDir().getAbsolutePath(), nameOfDirectory);
            boolean isDrawingDeletedSuccessfully = filesHandler.deleteDrawing(files.get(positionToDelete), directoryToTraverse, activity,
                    () -> Toast.makeText(context, context.getResources().getString(R.string.success_delete_drawing), Toast.LENGTH_SHORT).show(),
                    () -> Toast.makeText(context, context.getResources().getString(R.string.not_success_delete_drawing), Toast.LENGTH_SHORT).show());
            if (isDrawingDeletedSuccessfully) {
                files.remove(positionToDelete);
                adapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        });
    }

    private void showDialogToRename(int position, Activity activity) {
        String title = context.getResources().getString(R.string.rename_question);
        SaveDialog saveDialog = new SaveDialog(context);
        saveDialog.setContentView(R.layout.save_drawing_dialog)
                .setTitleToDialog(R.id.title, title)
                .initNameInput(R.id.drawing_name_input, files.get(position).getName())
                .initButtonOk(R.id.button_ok)
                .initButtonCancel(R.id.button_cancel)
                .showDialog();
        setOnClickListenerToConfirmRename(activity, position, saveDialog.getDialog(), saveDialog.getButtonOk());
        setOnClickListenerForCancel(saveDialog.getDialog(), saveDialog.getButtonCancel());
    }

    private void setOnClickListenerToConfirmRename(Activity activity, int positionToRename, Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            TextInputLayout textInputLayout = dialog.findViewById(R.id.drawing_name_input);
            EditText editText = textInputLayout.getEditText();
            String newName = context.getResources().getString(R.string.jpg_format, editText.getText().toString());
            String nameOfDirectory = context.getResources().getString(R.string.directory);
            File directoryToTraverse = new File(context.getFilesDir().getAbsolutePath(), nameOfDirectory);
            FilesHandler filesHandler = new FilesHandler();
            // todo: too many arguments
            boolean isDrawingRenamedSuccessfully = filesHandler.renameDrawing(positionToRename, directoryToTraverse, files, newName, activity,
                    () -> Toast.makeText(context, context.getResources().getString(R.string.success_rename_drawing), Toast.LENGTH_SHORT).show(),
                    () -> Toast.makeText(context, context.getResources().getString(R.string.not_success_rename_drawing), Toast.LENGTH_SHORT).show());
            if (isDrawingRenamedSuccessfully) {
                adapter.notifyItemChanged(positionToRename);
            }
            dialog.dismiss();
        });
    }

    private void showDialogToSendEmail(int position) {
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = files.get(position);
        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("image/jpg");
            Uri uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", fileWithinMyDir);
            intentShareFile.putExtra(Intent.EXTRA_STREAM, uri);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "Sharing File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File " + fileWithinMyDir.getName());
            context.startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    private void setOnClickListenerForCancel(Dialog dialog, Button buttonCancel) {
        buttonCancel.setOnClickListener((v) -> dialog.dismiss());
    }
}
