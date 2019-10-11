package br.com.exercicio4.array50localizacoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListaLocalizacoes extends AppCompatActivity {

    private ListView localizacoesListView;
    private LocationManager locationManager;            // classe que permite o uso do Hardware GPS
    private LocationListener locationListener;
    private double latitudeAtual;
    private double longitudeAtual;
    private List<String> listaLoc = new ArrayList<>();
    private static final int REQUEST_CODE_GPS = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_localizacoes);
        localizacoesListView = findViewById(R.id.localizacoesListView);
        Intent origemIntent = getIntent();
        String cordenada = origemIntent.getStringExtra("cordenada");
        final List<String> localizacoes = buscaCoordenadas(cordenada);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, localizacoes);
        localizacoesListView.setAdapter(adapter);
        localizacoesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Uri gmmIntentUri = Uri.parse(String.format("geo: %f, %f?q=", latitudeAtual, longitudeAtual));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.putExtra("localizacao_escolhido", localizacoes.get(position));
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lon = location.getLongitude();
                latitudeAtual = lat;
                longitudeAtual = lon;
                if (listaLoc.size() > 50) {
                    listaLoc.remove(0);
                } else {
                    listaLoc.add("Localização: latitude " + latitudeAtual + " longitude " + longitudeAtual);

                }

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }


    public List<String> buscaCoordenadas(String chave) {
        List<String> lista = listaLoc;
        if (chave == null || chave.length() == 0) {
            return lista;
        } else {
            List<String> subLista = new ArrayList<>();
            for (String nome : lista) {
                if (nome.toUpperCase().contains(chave.toUpperCase())) {
                    subLista.add(nome);
                }
            }
            return subLista;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //A permissão já foi dada?
        //somente ativa
        //a localização é obtida via hardware, intervalo de 0 segundos e 0 metros entre as atualizações
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            //Se a permissão ainda não foi dada, solicita ao usuário
            //quando o usuário responder, o método onRequestPermissionResult vai ser chamado
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GPS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Permissão concedida ativamos o GPS
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } else {
                // usuário negou não ativamos
                Toast.makeText(this, getString(R.string.no_gps_no_app), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }


}
