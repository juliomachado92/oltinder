package julio.puc.br.oltinder.Controler;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.BackendlessCallback;
import com.backendless.exceptions.BackendlessFault;

import julio.puc.br.oltinder.R;

/**
 * Created by julio on 16/05/2016.
 */
public class CreateAccount extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{

    private Button btnLogin;
    private EditText etxtName;
    private EditText etxtCreateEmail;
    private EditText etxtCreateSenha;
    private Spinner tipo;

    public String TAG = "OLTINDER";
    private String item = "";
    private int itemPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //setar elementos
        etxtName = (EditText) findViewById(R.id.etxtName);
        etxtCreateEmail = (EditText)findViewById(R.id.etxtCreateEmail);
        etxtCreateSenha = (EditText)findViewById(R.id.etxtCreatePassword);
        btnLogin = (Button)findViewById(R.id.btnEntrar);
        tipo = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.tipos_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(adapter);
        tipo.setOnItemSelectedListener(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BackendlessUser user = new BackendlessUser();
                user.setEmail(etxtCreateEmail.getText().toString());
                user.setPassword(etxtCreateSenha.getText().toString());
                user.setProperty("name",etxtName.getText().toString());

                if(itemPosition == 1 || item.equals("COMPRADOR")){
                    user.setProperty("TipoDeUsuario",1);
                }else if(itemPosition ==2 || item.equals("VENDEDOR")){
                    user.setProperty("TipoDeUsuario",2);
                }else{
                    user.setProperty("TipoDeUsuario",0);
                }


                Backendless.UserService.register(user, new BackendlessCallback<BackendlessUser>() {
                    @Override
                    public void handleResponse(BackendlessUser backendlessUser) {
                        //resposta positiva criaou aconta
                        Log.i( TAG, backendlessUser.getEmail() + " successfully registered" );
                        Intent it = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(it);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        //Aconteceu algum erro e não pode criar a conta
                        Toast.makeText(getApplicationContext(),"ERRO AO CRIAR CONTA " ,Toast.LENGTH_SHORT).show();
                        if(fault.getCode().equals("3033")){
                            Log.e(TAG, "Usuario ja existe :  " + fault.toString());
                        }
                        switch (fault.getCode()){
                            case "3033":
                                Log.e(TAG, "Usuario ja existe :  " + fault.toString());
                                Toast.makeText(getApplicationContext(),"ERRO AO CRIAR CONTA : Usuario ja existe" ,Toast.LENGTH_SHORT).show();
                                break;
                            case "8023":
                                Log.e(TAG, "Senha Invalida :  " + fault.toString());
                                Toast.makeText(getApplicationContext(),"ERRO AO CRIAR CONTA : Senha Invalida" ,Toast.LENGTH_SHORT).show();
                                break;
                            case "3040":
                                Log.e(TAG, "Formato email invalido :  " + fault.toString());
                                Toast.makeText(getApplicationContext(),"ERRO AO CRIAR CONTA : Formato email invalido" ,Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Log.e(TAG, "ERRO :  " + fault.toString());
                        }
                    }
                });
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(),"Conteudo " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
        Log.e(TAG,"Select : "+ parent.getItemAtPosition(position).toString());
        item =parent.getItemAtPosition(position).toString();
        itemPosition = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(getApplicationContext(),"NADAAAAAAAA",Toast.LENGTH_SHORT).show();
        Log.e(TAG,"NADA select");
    }
}
