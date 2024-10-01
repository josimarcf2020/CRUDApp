package com.example.crudapplication;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CadastraUser extends AppCompatActivity {

    public SQLiteDatabase BD_Dados;
    public EditText edtTxtNome;
    public Button btGravarUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastra_user);

        edtTxtNome = (EditText) findViewById(R.id.edtTextNome);
        btGravarUser = (Button) findViewById(R.id.btGravarUser);

        btGravarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gravarUser();
            }
        });
    }

    private void gravarUser() {

        if(!TextUtils.isEmpty(edtTxtNome.getText().toString())) {

            try {

                BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
                String sql = "INSERT INTO user (nome) VALUES (?);";
                SQLiteStatement stmt = BD_Dados.compileStatement(sql);
                stmt.bindString(1, edtTxtNome.getText().toString());
                stmt.executeInsert();
                BD_Dados.close();
                finish();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}