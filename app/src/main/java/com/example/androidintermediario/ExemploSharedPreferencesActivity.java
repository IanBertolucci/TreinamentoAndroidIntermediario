package com.example.androidintermediario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExemploSharedPreferencesActivity extends AppCompatActivity {
    private TextView lblStatus;
    private EditText txtNome, txtIdade;
    private RadioButton rbMasculino, rbFeminino;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exemplo_shared_preferences);

        lblStatus = (TextView) findViewById(R.id.textViewStatus);
        txtNome = (EditText) findViewById(R.id.editTextNome);
        txtIdade = (EditText) findViewById(R.id.editTextIdade);
        rbFeminino = (RadioButton) findViewById(R.id.radioFeminino);
        rbMasculino = (RadioButton) findViewById(R.id.radioMasculino);

        findViewById(R.id.btnSalvar).setOnClickListener(clickListenerInterno);

//      readPreferences();
        readFileInterno();
    }

    private View.OnClickListener clickListenerInterno = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nome = txtNome.getText().toString();
            String idade = txtIdade.getText().toString();
            String sexo = "";

            if (rbMasculino.isChecked()){
                sexo = "Masculino";
            } else if (rbFeminino.isChecked()){
                sexo = "Feminino";
            }

            salvarInterno(nome, sexo, idade);
            lblStatus.setText("Status: preferências salvas internamente.");
        }
    };

    private void salvarInterno(String nome, String sexo, String idade){
        String dados = "";
        dados += "nome="+nome;
        dados += "\n";
        dados += "idade="+idade;
        dados += "\n";
        dados += "sexo="+sexo;
        dados += "\n";

        try {
            FileOutputStream fos = openFileOutput("dados.txt", MODE_PRIVATE);
            fos.write(dados.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.e("Erro: ", e.getMessage());
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String nome = txtNome.getText().toString();
            String idade = txtIdade.getText().toString();
            String sexo = "";

            if (rbMasculino.isChecked()){
                sexo = "Masculino";
            } else if (rbFeminino.isChecked()){
                sexo = "Feminino";
            }

            SharedPreferences filePreferences = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor editor = filePreferences.edit();
            editor.putString("nome", nome);
            editor.putString("sexo", sexo);
            editor.putString("idade", idade);
            editor.commit();

            lblStatus.setText("Status: preferências salvas com sucesso.");
        }
    };

    private void readPreferences(){
        SharedPreferences filePreferences = getPreferences(MODE_PRIVATE);

        String nome = filePreferences.getString("nome", "");
        String idade = filePreferences.getString("idade", "");
        String sexo = filePreferences.getString("sexo", "Masculino");

        txtNome.setText(nome);
        txtIdade.setText(idade);

        if (sexo.contains("Masculino")){
            rbMasculino.setChecked(true);
        } else rbFeminino.setChecked(true);
    }

    private void readFileInterno(){
        String nome = "";
        String idade = "";
        String sexo = "Masculino";

        try {
            File dir = getFilesDir();
            File file = new File(dir+"/dados.txt");

            if (file.exists()){
                FileInputStream fis = openFileInput("dados.txt");
                byte[] buffer = new byte[(int) file.length()];

                while (fis.read(buffer) != -1){
                    String texto = new String(buffer);

                    if (texto.contains("nome")){
                        int index = texto.indexOf("=");
                        int indexFinal = texto.indexOf("\n");
                        nome = texto.substring(index+1, indexFinal);
                        texto = texto.substring(indexFinal+1);
                    }

                    if (texto.contains("idade")){
                        int index = texto.indexOf("=");
                        int indexFinal = texto.indexOf("\n");
                        idade = texto.substring(index+1, indexFinal);
                        texto = texto.substring(indexFinal+1);
                    }

                    if (texto.contains("sexo")){
                        int index = texto.indexOf("=");
                        sexo = texto.substring(index+1);
                    }
                }
            }
        } catch (IOException e) {
            Log.e("Erro: ", e.getMessage());
        }

        txtNome.setText(nome);
        txtIdade.setText(idade);

        if (sexo.contains("Masculino")){
            rbMasculino.setChecked(true);
        } else rbFeminino.setChecked(true);
    }
}
