package com.example.androidintermediario;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ThreadActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnExibir;
    private Button btnProgresso;
    private ImageView imageView;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        btnExibir = (Button) findViewById(R.id.btnExibirImagem);
        btnExibir.setOnClickListener(this);

        btnProgresso = (Button) findViewById(R.id.btnProgresso);
        btnProgresso.setOnClickListener(this);

        imageView = (ImageView) findViewById(R.id.imageViewLogo);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnExibirImagem:
//                Runnable processo = new Runnable() {
//                    @Override
//                    public void run() {
//                        String url = "https://pbs.twimg.com/media/ETfDdehWoAAuc4T?format=jpg&name=4096x4096";
//                        try {
//                            final Bitmap imagem = exibirImagem(url);
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    imageView.setImageBitmap(imagem);
//                                }
//                            });
//                        } catch (IOException e) {
//                            Log.i("erro", e.getMessage());
//                        }
//                    }
//                };
//                Thread tarefa = new Thread(processo);
//                tarefa.start();
//
                String url = "https://pbs.twimg.com/media/ETfDdehWoAAuc4T?format=jpg&name=4096x4096";
                new DownloadImagem().execute(url);

                break;
            case R.id.btnProgresso:
                exibirProgressDialog();
                break;
        }
    }

    protected Bitmap exibirImagem(String url) throws IOException {
        Bitmap imagem = null;
        try {
            URL src = new URL(url);
            InputStream inputStream = (InputStream) src.getContent();
            imagem = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return imagem;
    }

    private class DownloadImagem extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null;
            try {
                bitmap = exibirImagem(strings[0]);
            } catch (IOException e) {
                Log.i("erro", e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }

    private void exibirProgressDialog(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Carregando...");
        progressDialog.show();

        new Thread(){
            int total = 0;
            @Override
            public void run() {
                super.run();

                while (total < 100){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.setProgress(total);
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    total++;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Ação concluída!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }
}