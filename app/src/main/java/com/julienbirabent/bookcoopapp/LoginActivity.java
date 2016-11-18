package com.julienbirabent.bookcoopapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import data.BookHttpClient;
import model.Student;
import utils.HttpUtils;

public class LoginActivity extends AppCompatActivity {


    public final static String USER_NAME = "user_name";
    public final static String TOKEN = "token";
    public final static String CODE_VALID ="200";

    private Button signInButton;
    private EditText userName;
    private EditText token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getViewsById();
        setOnClickListener();
        userName.setText("jul.birabent@gmail.com");
        token.setText("GsUQuFBRter9sYbhg6yY ");

    }
    private void setOnClickListener(){
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // On créé la chaîne de caractère qui va contenir les paramètres de la requête serveur
                String params = buildAthentificationUrl();
                // On lance une tâche asynchrone pour effectuer la requête serveur
                LoginTask loginTask = new LoginTask();
                loginTask.execute(new String[]{HttpUtils.SERVER_URL+params});

            }
        });
    }

    /**
     * Méthode ou l'on créé l'url permettant d'envoyer la requpete d'authenfication du client.
     * @return
     */
    private String buildAthentificationUrl(){

        // On récupère les données fournis par l'utilisateur
        String userName = getUserName().getText().toString();
        String token = getToken().getText().toString();

        // On créé la chaîne de caractère qui va contenir les paramètres de la requête serveur
        String params ="?"+ HttpUtils.STUDENT_PARAM + userName +
                HttpUtils.AND + HttpUtils.STUDENT_TOKEN_PARAM + token;
        return params;
    }

    private void testGoToStudentActivity(){
        Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
        startActivity(intent);
    }

    private void getViewsById(){
        signInButton = (Button)findViewById(R.id.signinButton);
        userName = (EditText) findViewById(R.id.usernameId);
        token = (EditText) findViewById(R.id.passwordId);

    }

    private class LoginTask extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            // On désactive les champs le temps que la tâche s'éxécute
            userName.setEnabled(false);
            token.setEnabled(false);
            signInButton.setEnabled(false);

        }

        @Override
        protected String doInBackground(String... params) {



            BookHttpClient hhtpClient = new BookHttpClient();
            // On envoie la requête HTTP avec l'url données en paramètres
            String response = hhtpClient.getResponseCodeFor(params[0]);


            return response;
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            // Si la réponse du serveur nous autorise, on passe à l'activité suivante
           if(s.equals(CODE_VALID)){
                Intent intent = new Intent(LoginActivity.this, StudentActivity.class);
                intent.putExtra(USER_NAME, userName.getText().toString());
                intent.putExtra(TOKEN,token.getText().toString());
                startActivity(intent);
            }
            // On réactive les composants du login
            userName.setEnabled(true);
            token.setEnabled(true);
            signInButton.setEnabled(true);

        }


    }

    public Button getSignInButton() {
        return signInButton;
    }

    public void setSignInButton(Button signInButton) {
        this.signInButton = signInButton;
    }

    public EditText getUserName() {
        return userName;
    }

    public void setUserName(EditText userName) {
        this.userName = userName;
    }

    public EditText getToken() {
        return token;
    }

    public void setToken(EditText token) {
        this.token = token;
    }
}
