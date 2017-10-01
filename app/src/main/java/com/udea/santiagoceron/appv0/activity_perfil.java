package com.udea.santiagoceron.appv0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class activity_perfil extends AppCompatActivity {
    private TextView sPass,sMail;
    private String RegMail,RegPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sPass = (TextView) findViewById(R.id.sPass);
        sMail = (TextView) findViewById(R.id.sEmail);

        Bundle extras = getIntent().getExtras();
        RegMail = extras.getString("mail");
        RegPass = extras.getString("pass");

        sPass.setText(RegPass);
        sMail.setText(RegMail);

    }

}
