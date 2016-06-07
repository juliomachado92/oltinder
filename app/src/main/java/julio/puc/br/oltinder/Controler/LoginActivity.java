package julio.puc.br.oltinder.Controler;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import julio.puc.br.oltinder.Model.Constants;
import julio.puc.br.oltinder.R;

/**
 * Created by julio on 16/05/2016.
 */
public class LoginActivity extends AppCompatActivity{



    private Button btnLogin;
    private Button btnCreate;
    private EditText etxtEmail;
    private EditText etxtSenha;
    public String TAG = "OLTINDER";

    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //declarar elementos da tela
        etxtEmail = (EditText)findViewById(R.id.etxtEmail);
        etxtSenha = (EditText)findViewById(R.id.etxtPassword);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnCreate = (Button)findViewById(R.id.btnCreate);

        //criar banco de dados
        Backendless.initApp(this, Constants.APP_ID,Constants.SECRET_KEY, Constants.APP_VERSION);

        sharedPreferences = getSharedPreferences(getString(R.string.prev_title), CONTEXT_RESTRICTED);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    public void login(){

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String email = etxtEmail.getText().toString();
        String senha  = etxtSenha.getText().toString();

        Backendless.UserService.login(email, senha, new AsyncCallback<BackendlessUser>() {
            @Override
            public void handleResponse(BackendlessUser backendlessUser) {

                editor.putString(getString(R.string.prev_user_email),backendlessUser.getEmail());
                editor.putString(getString(R.string.prev_user_name),backendlessUser.getProperty("name").toString());
                editor.putInt(getString(R.string.prev_user_type),Integer.parseInt(backendlessUser.getProperty("TipoDeUsuario").toString()));
                editor.commit();

                Intent it  = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(it);

            }

            @Override
            public void handleFault(BackendlessFault backendlessFault) {
                Log.e(TAG,"ERRO ao logar : "+backendlessFault.toString());
                Toast.makeText(getApplicationContext(),"LOGIN INVALIDO ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createAccount(){
        Intent it  = new Intent(this,CreateAccount.class);
        startActivity(it);
    }
}
