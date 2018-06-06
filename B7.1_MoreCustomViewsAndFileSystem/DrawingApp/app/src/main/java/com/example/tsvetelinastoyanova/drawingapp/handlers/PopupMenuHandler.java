package com.example.tsvetelinastoyanova.drawingapp.handlers;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.tsvetelinastoyanova.drawingapp.R;
import com.example.tsvetelinastoyanova.drawingapp.RecyclerViewAdapter;
import com.example.tsvetelinastoyanova.drawingapp.drawing.DialogBuilders.Saver;
import com.example.tsvetelinastoyanova.drawingapp.enums.ActionDrawing;
import com.example.tsvetelinastoyanova.drawingapp.handlers.FilesHandler;

import java.io.File;
import java.util.List;

public class PopupMenuHandler {
    Context context;
    PopupMenu popup;
    int position;
    List<File> files;
    RecyclerViewAdapter adapter;

    public PopupMenuHandler(Context context, PopupMenu popup, int position, List<File> files, RecyclerViewAdapter adapter) {
        this.context = context;
        this.popup = popup;
        this.position = position;
        this.files = files;
        this.adapter = adapter;
    }

    public void setClickListenersOnPopupMenu() {
        popup.setOnMenuItemClickListener((item) -> {
            switch (item.getItemId()) {
                case R.id.delete:
                    showDialogToModify(position, ActionDrawing.DELETE);
                    break;
                case R.id.rename:
                    showDialogToModify(position, ActionDrawing.RENAME);
                    Log.d("tag", "rename");
                    break;
                case R.id.share:
                    Log.d("tag", "share");
                    break;
            }
            return false;

        });
    }

    private void showDialogToModify(int position, Enum actionDialog) {
        String title = "";
        boolean isDeleteDialog = true;
        if (actionDialog.equals(ActionDrawing.DELETE)) {
            title = context.getResources().getString(R.string.delete_question);
            isDeleteDialog = true;
        } else if (actionDialog.equals(ActionDrawing.RENAME)) {
            title = context.getResources().getString(R.string.rename_question);
            isDeleteDialog = false;
        }

        Saver saver = new Saver(context);
        saver.setContentView(R.layout.save_drawing_dialog)
                .setTitleToDialog(R.id.title, title)
                .initNameInput(R.id.drawing_name_input, files.get(position).getName()) // done in order to be reused for delete and rename
                .hideTextInputField(R.id.drawing_name_input, isDeleteDialog)
                .initButtonOk(R.id.button_ok)
                .initButtonCancel(R.id.button_cancel)
                .showDialog();
        setOnClickListenerToConfirmAction(actionDialog, position, saver);
    }

    private void setOnClickListenerToConfirmAction(Enum actionDialog, int position, Saver saver) {
        if (actionDialog.equals(ActionDrawing.DELETE)) {
            setOnClickListenerToConfirmDelete(position, saver.getDialog(), saver.getButtonOk());
        } else if (actionDialog.equals(ActionDrawing.RENAME)) {
            setOnClickListenerToConfirmRename(position, saver.getDialog(), saver.getButtonOk());
        }
        setOnClickListenerForCancel(saver.getDialog(), saver.getButtonCancel());
    }

    private void setOnClickListenerToConfirmDelete(int positionToDelete, Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            FilesHandler filesHandler = new FilesHandler();
            String nameOfDirectory = context.getResources().getString(R.string.directory);
            File directoryToTraverse = new File(context.getFilesDir().getAbsolutePath(), nameOfDirectory);
            boolean isDrawingDeletedSuccessfully = filesHandler.deleteDrawing(files.get(positionToDelete), directoryToTraverse);
            if (isDrawingDeletedSuccessfully) {
                files.remove(positionToDelete);
                adapter.notifyItemChanged(positionToDelete);
            }
            dialog.dismiss();
        });
    }

    private void setOnClickListenerToConfirmRename(int positionToRename, Dialog dialog, Button buttonOk) {
        buttonOk.setOnClickListener((v) -> {
            TextInputLayout textInputLayout = dialog.findViewById(R.id.drawing_name_input);
            EditText editText = textInputLayout.getEditText();
            String newName = editText.getText().toString();
           /* if (newName == null) {
                Log.d("tag", "wrong name");
                return;
            }*/
            String nameOfDirectory = context.getResources().getString(R.string.directory);
            File directoryToTraverse = new File(context.getFilesDir().getAbsolutePath(), nameOfDirectory);
            FilesHandler filesHandler = new FilesHandler();
            boolean isDrawingRenamedSuccessfully = filesHandler.renameDrawing(positionToRename, files.get(positionToRename), directoryToTraverse, files, newName);
            if (isDrawingRenamedSuccessfully) {
                adapter.notifyItemChanged(positionToRename);
            }
            dialog.dismiss();
        });
    }

    private void setOnClickListenerForCancel(Dialog dialog, Button buttonCancel) {
        buttonCancel.setOnClickListener((v) -> {
            dialog.dismiss();
        });
    }
}
