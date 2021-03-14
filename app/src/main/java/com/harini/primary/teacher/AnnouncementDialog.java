package com.harini.primary.teacher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.harini.primary.R;

public class AnnouncementDialog extends AppCompatDialogFragment {

    private TextInputLayout textInputLayout_announcement;
    private AnnouncementDialogListner announcementDialogListner;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_create_announcement,null);

        builder.setView(view)
                .setTitle("Make an Announcement")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        String announcement = textInputLayout_announcement.getEditText().getText().toString();
                        announcementDialogListner.addText(announcement);



                        announcementDialogListner.addannouncement(announcement);

                    }

                });

        textInputLayout_announcement = view.findViewById(R.id.textInputLayout_announcement);


     //   AlertDialog dialog = builder.create();



     //   dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

//        return  builder.create();

        /*textInputLayout_announcement.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String value = textInputLayout_announcement.getEditText().getText().toString();


                if(value.isEmpty()){
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }
                else{
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });*/

       /*  AlertDialog dialog  = builder.create();

        return dialog;*/

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            announcementDialogListner = (AnnouncementDialogListner) context;
        } catch (ClassCastException exception) {
           throw new ClassCastException(context.toString()+"listner must be implemented");
        }
    }




    public interface AnnouncementDialogListner{

        void addText(String announcement);
        void addannouncement(String announcement);
    }

}

