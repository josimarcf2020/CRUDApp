package com.example.crudapplication;
import static androidx.appcompat.app.AlertDialog.*;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.io.File;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase BD_Dados;
    public ListView listViewDados;
    public Button btnCadastrarUser;
    public ArrayList<Integer> arrayIds;
    public Integer idSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        criarBanco();
        listViewDados = (ListView) findViewById(R.id.listaDeDados);
        btnCadastrarUser = (Button) findViewById(R.id.btnCadastrar);

        btnCadastrarUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                telaCadastrarUser();
            }
        });

        listarDados();

        listViewDados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                idSelecionado = arrayIds.get(position);
                excluir(idSelecionado);
                return true;
            }
        });

        listViewDados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idSelecionado = arrayIds.get(position);
                alterar(idSelecionado);
            }
        });

    }

    private void alterar(int position) {
        Intent intent = new Intent(this, AlteraUser.class);
        intent.putExtra("id", position);
        startActivity(intent);
    }

    public void excluir(Integer position) {

        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo

        builder.setTitle("Atenção!!!");
        //define a mensagem
        builder.setMessage("Deletar USUÁRIO?");
        //define a image icon
        builder.setIcon(android.R.drawable.ic_menu_delete);

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                deleteUser();
            }

            private void deleteUser() {
                try{
                    BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
                    String sql = "DELETE FROM user WHERE id = ?";
                    SQLiteStatement stmt = BD_Dados.compileStatement(sql);
                    stmt.bindLong(1, position);
                    stmt.executeUpdateDelete();
                    listarDados();
                    BD_Dados.close();
                    Toast.makeText(MainActivity.this, "Usuario deletado", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "Deleção CANCELADA!", Toast.LENGTH_SHORT).show();
            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    @Override
    protected void onResume(){
        super.onResume();
        listarDados();
    }

    public void telaCadastrarUser() {
        Intent intent = new Intent(this, CadastraUser.class);
        startActivity(intent);
    }

    private void listarDados() {

        try {
            arrayIds = new ArrayList<Integer>();
            BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
            Cursor meuCursor = BD_Dados.rawQuery("SELECT id, nome FROM user;", null);

            ArrayList<String> linhas = new ArrayList<String>();
            ArrayAdapter meuAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    linhas
            );
            listViewDados.setAdapter(meuAdapter);
            meuCursor.moveToFirst();

            while (meuCursor != null) {
                int i;
                linhas.add(meuCursor.getString(1));
                arrayIds.add(meuCursor.getInt(0));
                meuCursor.moveToNext();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criarBanco() {

        try{
            BD_Dados = openOrCreateDatabase("CRUDapp", MODE_PRIVATE, null);
            BD_Dados.execSQL("CREATE TABLE IF NOT EXISTS user(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nome VARCHAR)");
            BD_Dados.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //atributo da classe.
    public AlertDialog alerta;

    public static boolean bancoexiste(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

}