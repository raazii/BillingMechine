package com.fiume.billingmechine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fiume.billingmechine.R;
import com.fiume.billingmechine.db.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Razi on 12/16/2015.
 */
public class AddItemDialogActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Button btnSave;
    private Button btnCancel;
    private Button btnColor1;
    private Button btnColor2;
    private Button btnColor3;
    private Button btnColor4;
    private Button btnColor5;
    private Button btnColor6;
    private Button btnColor7;
    private Button btnColor8;
    private Button btnCreated;
    private EditText etICode;
    private EditText etIName;
    private EditText etIDispName;
    private EditText etIPrice;
    private EditText etArabicName;
    private String color;
    private String flag;
    private String id;
    private String iName;
    private String iCode;
    private String iDisName;
    private String iPrice;
    private String iColor;
    private String arName;
    ImageButton ibKeyboard;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createitem_dialog_activity);
        setTitle("Add Product");
        dbHelper = new DatabaseHelper(this);
        dateFormatter = new SimpleDateFormat("MMM dd,yyyy  hh:mm:ss a",
                Locale.US);

        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        id = intent.getStringExtra("id");
        iName = intent.getStringExtra("name");
        iCode = intent.getStringExtra("code");
        iDisName = intent.getStringExtra("disname");
        iPrice = intent.getStringExtra("price");
        iColor = intent.getStringExtra("color");
        arName = intent.getStringExtra("arName");

        etICode = (EditText) findViewById(R.id.et_code);
        etIName = (EditText) findViewById(R.id.et_name);
        etIDispName = (EditText) findViewById(R.id.et_display_name);
        etArabicName = (EditText) findViewById(R.id.et_arabic_name);
        etIPrice = (EditText) findViewById(R.id.et_price);
      //  ibKeyboard = (ImageButton) findViewById(R.id.ib_keyboard);


        btnSave = (Button) findViewById(R.id.btn_save);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnColor1 = (Button) findViewById(R.id.btn_c1);
        btnColor2 = (Button) findViewById(R.id.btn_c2);
        btnColor3 = (Button) findViewById(R.id.btn_c3);
        btnColor4 = (Button) findViewById(R.id.btn_c4);
        btnColor5 = (Button) findViewById(R.id.btn_c5);
        btnColor6 = (Button) findViewById(R.id.btn_c6);
        btnColor7 = (Button) findViewById(R.id.btn_c7);
        btnColor8 = (Button) findViewById(R.id.btn_c8);
        btnCreated = (Button) findViewById(R.id.btn_created);

        if (flag.equals("edit")) {
            etICode.setText(iCode);
            etIName.setText(iName);
            etIDispName.setText(iDisName);
            etIPrice.setText(iPrice);
            btnCreated.setText(iDisName);
            etArabicName.setText(arName);
            btnCreated.setBackgroundColor(Color.parseColor(iColor));
            color = iColor;
        }

      /*  ibKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputMethodPicker();
            }
        });
*/
        btnColor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor1.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor2.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor3.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor4.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor5.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor6.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor7.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });
        btnColor8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color = btnColor8.getText().toString();
                btnCreated.setText(etIDispName.getText().toString());
                btnCreated.setBackgroundColor(Color.parseColor(color));
            }
        });


        etIDispName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnCreated.setText(etIDispName.getText().toString());
                // btnCreated.setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar newCalendar = Calendar.getInstance();
                String time = dateFormatter.format(newCalendar.getTime());

                if (flag.equals("new")) {
                    if (etICode.getText().toString().length() > 2 &&
                            etIName.getText().toString().length() > 2 &&
                            etIPrice.getText().toString().length() > 1 && color.length() > 5) {
                        if (!etIDispName.getText().toString().equals(null))// != null
                            dbHelper.insertItemData(etICode.getText().toString(), etIName.getText().toString(),
                                    etIDispName.getText().toString(), etArabicName.getText().toString(), etIPrice.getText().toString(), color, time);
                        else
                            dbHelper.insertItemData(etICode.getText().toString(), etIName.getText().toString(),
                                    etICode.getText().toString(), etArabicName.getText().toString(), etIPrice.getText().toString(), color, time);

                        finish();

                    } else {

                        Toast.makeText(AddItemDialogActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (etICode.getText().toString().length() > 2 &&
                            etIName.getText().toString().length() > 2 &&
                            etIPrice.getText().toString().length() > 0) {
                        if (!etIDispName.getText().toString().equals(null)) // != null
                            dbHelper.updateItemData(id, etICode.getText().toString(), etIName.getText().toString(),
                                    etIDispName.getText().toString(), etArabicName.getText().toString(), etIPrice.getText().toString(), color, time);
                        else
                            dbHelper.updateItemData(id, etICode.getText().toString(), etIName.getText().toString(),
                                    etICode.getText().toString(), etArabicName.getText().toString(), etIPrice.getText().toString(), color, time);

                        finish();

                    } else {

                        Toast.makeText(AddItemDialogActivity.this, "Please Enter Valid Details ", Toast.LENGTH_SHORT).show();
                    }
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

    private void showInputMethodPicker() {
        InputMethodManager imeManager = (InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
        if (imeManager != null) {
            imeManager.showInputMethodPicker();
        } else {
            Toast.makeText(this, "Arabic KeyBoard Not available", Toast.LENGTH_LONG).show();
        }
    }
}
