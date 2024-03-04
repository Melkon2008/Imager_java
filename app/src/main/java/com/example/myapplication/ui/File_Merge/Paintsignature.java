package com.example.myapplication.ui.File_Merge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Paintsignature extends AppCompatActivity {

    TextInputLayout Signaturename;

    TextView Signaturetext;

    Button btnSignature1;
    Button btnSignature2;
    Button btnSignature3;
    Button btnSignature4;


    Button btnnormal;
    Button btnitalic;
    Button btnbold;


    SeekBar SeekbarSignature;

    int erkarutundp;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paintsignature);
        Signaturename = findViewById(R.id.glav_signature_name);
        Signaturetext = findViewById(R.id.textforsignature);

        btnSignature1 = findViewById(R.id.btnsignature1);
        btnSignature2 = findViewById(R.id.btnsignature2);
        btnSignature3 = findViewById(R.id.btnSignature3);
        btnSignature4 = findViewById(R.id.btnsignature4);

        btnnormal = findViewById(R.id.btnnormal);
        btnbold = findViewById(R.id.btnbold);
        btnitalic = findViewById(R.id.btnitalic);

        SeekbarSignature = findViewById(R.id.SeekbarSignatures);
        SeekbarSignature.setMax(80);
        SeekbarSignature.setProgress(40);


        SeekbarSignature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                int Seekbarprogress = seekBar1.getProgress();
                Signaturetext.setTextSize(TypedValue.COMPLEX_UNIT_SP, Seekbarprogress);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {


            }
        });

        erkarutundp = erkarutunDP(this);
        btnSignature1.setMinWidth(erkarutundp / 4);
        btnSignature2.setMinWidth(erkarutundp / 4);
        btnSignature3.setMinWidth(erkarutundp / 4);
        btnSignature4.setMinWidth(erkarutundp / 4);

        btnitalic.setMinWidth(erkarutundp / 3);
        btnbold.setMinWidth(erkarutundp / 3);
        btnnormal.setMinWidth(erkarutundp / 3);


        btnSignature1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignature1();
            }
        });
        btnSignature2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignature2();
            }
        });
        btnSignature3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changeSignatur3();
            }
        });
        btnSignature4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {changeSignatur4();
            }
        });









        TextInputEditText editText = (TextInputEditText) Signaturename.getEditText();
        if (editText != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    Signaturetext.setText(charSequence);
                    btnSignature1.setText(charSequence);
                    btnSignature2.setText(charSequence);
                    btnSignature3.setText(charSequence);
                    btnSignature4.setText(charSequence);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    public void changeSignature1(){
        Signaturetext.setTypeface(ResourcesCompat.getFont(this, R.font.singature1));
    }
    public void changeSignature2(){
        Signaturetext.setTypeface(ResourcesCompat.getFont(this, R.font.signature2));
    }
    public void changeSignatur3(){
        Signaturetext.setTypeface(ResourcesCompat.getFont(this, R.font.signature3));
    }
    public void changeSignatur4(){
        Signaturetext.setTypeface(ResourcesCompat.getFont(this, R.font.signature4));

    }

    private int erkarutunDP(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();

        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            display.getMetrics(metrics);
            return metrics.widthPixels;
        }

        return 0;
    }
}




