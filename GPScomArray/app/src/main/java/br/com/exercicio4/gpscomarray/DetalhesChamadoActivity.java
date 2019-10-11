package br.com.exercicio4.gpscomarray;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetalhesChamadoActivity extends AppCompatActivity {

    private TextView nomeFilaTextView;
    private TextView descricaoChamadoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_chamado);
        nomeFilaTextView = findViewById(R.id.nomeFilaTextView);
        descricaoChamadoTextView =
                findViewById(R.id.descricaoChamadoTextView);
        Intent origemIntent = getIntent();
        String chamadoEscolhido =
                origemIntent.getStringExtra("chamado_escolhido");
        String [] partes = chamadoEscolhido.split(":");
        String nomeFila = partes[0];
        String descricaoChamado = partes[1];
        nomeFilaTextView.setText(nomeFila);
        descricaoChamadoTextView.setText(descricaoChamado);
    }
}
