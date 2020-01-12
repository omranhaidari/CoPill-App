package com.polytech.copill;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnexionActivity extends AppCompatActivity {

    private String login,password;
    private Boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
    }

    public void signIn(View view){
        EditText editTextLogin = (EditText) findViewById(R.id.editText_login);
        EditText editTextPassword = (EditText) findViewById(R.id.editText_password);

        login = editTextLogin.getText().toString();
        password = editTextPassword.getText().toString();

        new DataBaseConnection().execute(view);
    }

    public void signUp(View view){
        Intent intent2 = new Intent(this, RegisterActivity.class);
        startActivity(intent2);
    }

    private class DataBaseConnection extends AsyncTask<View,Void,View> {

        @Override
        protected View doInBackground(View... views) {
            try{
                ConnectionManager connectionManager = new ConnectionManager();
                Connection con = connectionManager.createConnection();
                Statement stmt = con.createStatement();
                String query = "SELECT id FROM user WHERE email='"+login+"' AND password='"+password+"'";
                final ResultSet rs = stmt.executeQuery(query);

                if(rs.next()){
                    isLogged=true;
                }

                connectionManager.closeConnection();

            }catch (Exception e){
                e.printStackTrace();
            }
            return views[0];
        }

        @Override
        protected void onPostExecute(View view) {
            if(isLogged){
                isLogged=false;
                Intent intent1 = new Intent(view.getContext(), HomeActivity.class);
                startActivity(intent1);
            }
            else {
                Toast.makeText(view.getContext(), "Login et/ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        }
    }
}