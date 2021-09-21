package silva.hiago.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import silva.hiago.ceep.R;
import silva.hiago.ceep.model.Nota;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText email = findViewById(R.id.edit_email);
        EditText senha = findViewById(R.id.edit_senha);
        TextView cadastro = findViewById(R.id.text_tela_cadastro);
        Button entrarButton = findViewById(R.id.bt_entrar);

        entrarButton.setOnClickListener(view -> {
                String emailDado = email.getText().toString();
                String senhaDado = senha.getText().toString();

                TelaPrincipal();
        });
    }

    private void TelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,ListaNotasActivity.class);
        startActivity(intent);
        finish();
    }
}
