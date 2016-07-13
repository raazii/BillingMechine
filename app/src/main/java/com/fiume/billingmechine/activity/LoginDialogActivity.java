package com.fiume.billingmechine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fiume.billingmechine.R;

/**
 * Created by Razi on 12/24/2015.
 */
public class LoginDialogActivity extends AppCompatActivity {
    private EditText etPwd;
    private Button btnLogin;
    private Button btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_activity);
        setTitle("Admin Login");
        etPwd = (EditText) findViewById(R.id.editText);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etPwd.getText().toString().trim().equals("9633166009")) {
                    Intent intent = new Intent(
                            LoginDialogActivity.this, SettingsActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
