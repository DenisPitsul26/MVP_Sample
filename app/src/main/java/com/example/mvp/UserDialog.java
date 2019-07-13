package com.example.mvp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class UserDialog {

    UsersActivity parentActivity;

    public UserDialog(UsersActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(parentActivity);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.user_dialog);

        final EditText name = dialog.findViewById(R.id.name_dialog);
        final EditText email = dialog.findViewById(R.id.email_dialog);
        name.setText(parentActivity.getCurrentUser().getName());
        email.setText(parentActivity.getCurrentUser().getEmail());

        Button update = dialog.findViewById(R.id.update_dialog);
        Button delete = dialog.findViewById(R.id.delete_dialog);
        Button cancel = dialog.findViewById(R.id.cancel_dialog);

        dialog.show();

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();
                user.setId(parentActivity.getCurrentUser().getId());
                user.setName(name.getText().toString());
                user.setEmail(email.getText().toString());
                parentActivity.update(user);
                dialog.cancel();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentActivity.delete();
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }
}
