package com.example.jose.prueba512;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity {

    private EditText inputPelicula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPelicula = (EditText) findViewById(R.id.input_pelicula);
    }
    private void DatosPorDefecto() {
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayList lista = new ArrayList<String>();
        spinner1 = (Spinner) this.findViewById(R.id.spinner1);
        lista.add("Sevilla");
        lista.add("Madrid");
        lista.add("Barcelona");
        lista.add("Paris");
        lista.add("Londres");
        lista.add("");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lista);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adaptador);
    }

    public void buscarPelicula(View view) {
        String titulo = inputPelicula.getText().toString();
        if (!TextUtils.isEmpty(titulo)) {
            String Url= "http://api.worldweatheronline.com/free/v2/tz.ashx?key=f4ce7a0265b39d77a9a8bef1ef0d9060e9377ec4&format=json&query="+titulo;
            new LoadFilmTask().execute(Url);
        }
    }
    private JSONObject getPeliculas(String json) throws JSONException {
        Gson gson = new Gson();
        Type type = new TypeToken<JSONObject>(){}.getType();        //cambiado el type;
        JSONObject jd= new JSONObject(json);                // ESTA ES LA CLAVE!!!!!!!!!!!!!!!!
        return jd; //gson.fromJson(json, type);
    }
    private void mostrarPelicula(JSONObject pelicula) throws JSONException {
        String tagdata= "data";
        String tagrequest= "request";
        TextView textView = (TextView) findViewById(R.id.texto);
        JSONObject json= pelicula.getJSONObject(tagdata);
        JSONArray jarr= json.getJSONArray(tagrequest);
        JSONArray jarr2= json.getJSONArray("time_zone");
        JSONObject AS= jarr2.getJSONObject(0);
        String hora= AS.getString("localtime");
        JSONObject sd= jarr.getJSONObject(0);
        String asj= sd.getString("query");
        String pais= sd.getString("type");
        //String pue2:2blo= jarr.getString(0).toString();
        textView.setText(asj+"   "+"  la hora es  "+hora);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static final String TAG = "com.example.jose.prueba512";
    private class LoadFilmTask extends AsyncTask<String, Long, String> {
        protected String doInBackground(String... urls) {
            try {
                return HttpRequest.get(urls[0]).accept("application/json").body();
            } catch (HttpRequest.HttpRequestException exception) {
                return null;
            }
        }

        protected void onPostExecute(String response) {
            TextView js=(TextView)findViewById(R.id.textView);
            //js.setText(response.toString());
            JSONObject peliculas = null;
            try {
                peliculas = getPeliculas(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            try {
                mostrarPelicula(peliculas);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
