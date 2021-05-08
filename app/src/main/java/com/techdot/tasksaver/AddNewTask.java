package com.techdot.tasksaver;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.techdot.tasksaver.model.TodoModel;
import com.techdot.tasksaver.utils.DatabaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {

    private EditText mNewTaskText;
    private Button mSaveButton;
    private DatabaseHandler db;

    public static final String TAG = "ActionBottomDialog";

    public static AddNewTask getInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the view with add_new_task xml file
        View view = inflater.inflate(R.layout.add_new_task, container, false);
        // Set the soft input mode to resize when keyboard is displayed
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // Return the view created
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNewTaskText = getView().findViewById(R.id.newTaskText);
        mSaveButton = getView().findViewById(R.id.newTaskButton);

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        // If task already exists store them in bundle
        final Bundle bundle = getArguments();

        if (bundle != null) {   //Means that we are simply editing an existing task
            isUpdate = true;
            String task = bundle.getString("task");     // Get the String of the original task
            mNewTaskText.setText(task);     // Set the value of edit text to the string of original task

            /* Check if the length of the task is greater than 0 i.e whether a text exists in edit text
            * if true then change the color of the save button from darker gray to white */
            if (task.length() > 0) {
                mSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
        }
        // Add text change listener for the edit text
        mNewTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    mSaveButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mNewTaskText.getText().toString();
                if (finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    TodoModel task = new TodoModel();
                    task.setTask(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        /* Checks if the activity that invoked this function is an instance of (implements the
        * DialogCloseListener interface,
        * if true then the handle dialog close method is invoked which saves the task in the
        * recycler view */
        if (activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
