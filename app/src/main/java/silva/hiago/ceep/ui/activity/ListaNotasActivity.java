package silva.hiago.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.nfc.Tag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import silva.hiago.ceep.R;
import silva.hiago.ceep.dao.NotaDAO;
import silva.hiago.ceep.model.Nota;
import silva.hiago.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private final String BASE_URL = "https://notasfe-backend.herokuapp.com/notas/all";
    private ListaNotasAdapter adapter;
    private List<Nota> todasNotas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(view -> {
            Intent iniciaFormularioNota =
                    new Intent(ListaNotasActivity.this,
                            FormularioNotaActivity.class);
            startActivityForResult(iniciaFormularioNota, 1);
        });

        buscarDados();
    }

    private void buscarDados() {

        StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
//                    JSONObject object = new JSONObject(response);
                    String newStr = URLDecoder.decode(URLEncoder.encode(response, "iso8859-1"),"UTF-8");
                    Log.d("check", "onResponse: "+response  );
                    JSONArray array = new JSONArray(newStr);

                    for(int i = 0; i < array.length(); i ++){
                        JSONObject object1 = array.getJSONObject(i);

                        todasNotas.add(
                                new Nota(
                                        object1.getString("titulo"),
                                        object1.getString("descricao")
                                )
                        );

                    }

                    adapter.notifyDataSetChanged();

                }catch (JSONException | UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == 2 && data.hasExtra("nota")){
            Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
    }
}
