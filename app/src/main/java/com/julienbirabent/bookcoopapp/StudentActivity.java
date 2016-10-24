package com.julienbirabent.bookcoopapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import data.BookHttpClient;
import model.Book;
import model.Student;

public class StudentActivity extends AppCompatActivity {

    public final  int SCANNER_REQUEST_CODE=0;
    public final String SCANNER_MODE = "ONE_D_MODE";
    private ListView booksList;
    private Student sessionStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        booksList = (ListView) findViewById(R.id.listView_books);
        initializeBookList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", SCANNER_MODE);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);

            }
        });
    }

    /**
     * Méthode appelé au démarrage de l'activité. Sert à aller chercher la liste de livres
     * de l'étudiant et à l'insérer dans l'interface.
     */
    public void initializeBookList(){



    }

    /**
     * Fonction appelée quand le scan est terminée.
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCANNER_REQUEST_CODE) {
            // Handle scan intent
            if (resultCode == Activity.RESULT_OK) {
                // Handle successful scan
                String isbn = intent.getStringExtra("SCAN_RESULT");
                String formatName = intent.getStringExtra("SCAN_RESULT_FORMAT");

                Toast.makeText(getApplicationContext(), " ISBN : " + isbn,Toast.LENGTH_LONG).show();

                if(checkInternetConnection()) {
                    // Avec l'isbn récupéré on créé une tâche qui va se charge d'envoyer l'isbn
                    // au serveur pour que celui ci ajoute le livre au compte étudiant.
                    PostBookTask postBookTask = new PostBookTask();
                    postBookTask.execute(isbn);
                }



            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        } else {
            // Handle other intents
        }

    }

    /**
     * Tâche asynchrone pour s'occuper de récupérer le dernier livre ajouté contenu dans la
     * base de donnée du serveur pour cet étudiant.
     */
    private class GetLastBookTask extends AsyncTask<String,String,Book> {

        @Override
        protected Book doInBackground(String... params) {

            Book book = new Book();
            /**
             * Ici on récupère le livre via BookHttpClient et on le convertit en objet physique
             * avec JSONBookParser.
             */
            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            // A la fin de l'éxécution, le livre que l'on a récupéré est ajouter à la liste
            // de livres de l'étudiant .
            if(book!=null){
                getSessionStudent().getBooksList().add(book);
            }

        }
    }

    /**
     * Tâche asynchrone permettant de récupérer la liste complète de livres d'un étudiant.
     */
    private class GetAllBooksTask extends AsyncTask<String,String,ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... params) {


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            // après avoir récupéré la liste de livre de l'étudiant via le serveur, on définit
            // cette liste comme la liste a afficher.
            if(books!=null) {
                getSessionStudent().setBooksList(books);
            }
        }
    }

    private class PostBookTask extends AsyncTask<String,String,String>{


        /**
         * Ici, faire les requêtes HTTP et parser les JSON reçu pour obtenir une variable de sorti
         * conforme au modèle Book
         * @param params
         *
         */
        @Override
        protected String doInBackground(String... params) {
            // On envoie la requête de dépôt de livre au serveur
            // Params[0] est l'isbn qui est passé
            new BookHttpClient().postBook(params[0]);

            return null;
        }
    }

    /**
     *  Check si la connection internet est présente et active.
     * @return
     */
    private boolean checkInternetConnection() {

        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;

    }

    public ListView getBooksList() {
        return booksList;
    }

    public void setBooksList(ListView booksList) {
        this.booksList = booksList;
    }

    public Student getSessionStudent() {
        return sessionStudent;
    }

    public void setSessionStudent(Student sessionStudent) {
        this.sessionStudent = sessionStudent;
    }
}
