package com.example.tasky;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangePasswordDialog extends AppCompatDialogFragment {
    private TextView prev_text_view;
    private TextView new_pass_1_text_view;
    private TextView new_pass_2_text_view;

    private EditText edit_text_old_password;
    private EditText edit_text_new_password1;
    private EditText edit_text_new_password2;
    private ChangePasswordDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.change_password_dialog,null);

        prev_text_view = view.findViewById(R.id.text_view_old_password);
        new_pass_1_text_view = view.findViewById(R.id.text_view_new_password1);
        new_pass_2_text_view = view.findViewById(R.id.text_view_new_password2);

        builder.setView(view)
                .setTitle("Change password")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String old_password = edit_text_old_password.getText().toString();
                        String new_password1 = edit_text_new_password1.getText().toString();
                        String new_password2 = edit_text_new_password2.getText().toString();

                        if(!old_password.isEmpty() && !new_password1.isEmpty() && !new_password2.isEmpty()){
                            listener.applyPasswordChange(old_password, new_password1, new_password2);

                        }
                        else{
                            prev_text_view.setText("*Previous password:");
                            new_pass_1_text_view.setText("New password:");
                            new_pass_2_text_view.setText("*Repeat new password:");

                            Toast.makeText(getContext(), "You must fill all fields!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        edit_text_old_password = view.findViewById(R.id.edit_text_old_password);
        edit_text_new_password1 = view.findViewById(R.id.edit_text_new_password1);
        edit_text_new_password2 = view.findViewById(R.id.edit_text_new_password2);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ChangePasswordDialogListener) context;
        } catch(ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement ChangePasswordDialogListener");
        }
    }

    public interface ChangePasswordDialogListener{
        void applyPasswordChange(String old_pass, String new_pass1, String new_pass2);
    }
}
