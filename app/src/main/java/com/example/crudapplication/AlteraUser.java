package com.example.crudapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AlteraUser extends AppCompatActivity {

    private SQLiteDatabase BD_Dados;
    public Button btAlterarUser;
    public EditText edtTextNome;
    public Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_altera_user);

        btAlterarUser = (Button) findViewById(R.id.btAlterarUser);
        edtTextNome = (EditText) findViewById(R.id.edtTextNome);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);

        carregaDados(id);

        btAlterarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterarUser();
            }
        });

    }

    private void alterarUser() {

        String novoNome = edtTextNome.getText().toString();

        try{
            BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
            String sql = "UPDATE user SET nome=? WHERE id = ?";
            SQLiteStatement stmt = BD_Dados.compileStatement(sql);
            stmt.bindString(1, novoNome);
            stmt.bindLong(2, id);
            stmt.executeUpdateDelete();
            BD_Dados.close();
            Toast.makeText(AlteraUser.this, "Usuario deletado", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }

        finish();
    }

    private void carregaDados(Integer id) {
        try {
            BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
            Cursor cursor = BD_Dados.rawQuery("SELECT id, nome FROM user WHERE id=" + id.toString(), null);
            cursor.moveToFirst();
            edtTextNome.setText(cursor.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}