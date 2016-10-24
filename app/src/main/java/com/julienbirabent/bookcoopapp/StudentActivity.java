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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import data.BookHttpClient;
import data.JSONBookParser;
import model.Book;
import model.Student;

public class StudentActivity extends AppCompatActivity {

    public final  int SCANNER_REQUEST_CODE=0;
    public final String SCANNER_MODE = "ONE_D_MODE";
    private ListView booksList;
    private ArrayAdapter<String> bookListAdapter ;
    private Student sessionStudent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        booksList = (ListView) findViewById(R.id.listView_books);


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


        // On récupère la liste de Book de l'étudiant sous forme de tableau 1D
        Book[] bookList = (Book[]) getSessionStudent().getBooksList().toArray();
        // On définit un autre tableau 1D qui contiendra les String décrivant chaque Book
        String[] bookStringList = new String[bookList.length];
        // On initialise l'ArrayAdapter responsable de faire le lien entre nos données et la listView
        bookListAdapter= new ArrayAdapter<String>(this, R.layout.book_item, bookStringList);
        // chaque string décrivant un livre est ajouter à l'adapter.
        for(int i =0 ; i < bookStringList.length;i++){
            bookStringList[i] = bookList[i].toString();
            bookListAdapter.add(bookStringList[i]);
        }

        // On donne le modèle de donnée à afficher à notre listview
        booksList.setAdapter(bookListAdapter);




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

                    GetLastBookTask getLastBookTask = new GetLastBookTask();
                    // Les parametres de cet appel sont amenés à changer plus tard
                    getLastBookTask.execute(BookHttpClient.BASE_URL + BookHttpClient.GET_LAST_BOOK);
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
            BookHttpClient bookHttpClient = new BookHttpClient();
            String lastBook = bookHttpClient.getBookRessources(params[0]);

            book = JSONBookParser.parseBook(lastBook);

            return book;
        }

        @Override
        protected void onPostExecute(Book book) {
            // A la fin de l'éxécution, le livre que l'on a récupéré est ajouter à la liste
            // de livres de l'étudiant .
            if(book!=null){
                getSessionStudent().getBooksList().add(book);
                // Ajouter le livre dans la liste de l'interface aussi
                // *Code*
            }

        }
    }

    /**
     * Tâche asynchrone permettant de récupérer la liste complète de livres d'un étudiant.
     */
    private class GetAllBooksTask extends AsyncTask<String,String,ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... params) {

            ArrayList<Book> bookArrayList = new ArrayList<Book>();


            BookHttpClient bookHttpClient = new BookHttpClient();
            String allBooks = bookHttpClient.getBookRessources(params[0]);

            bookArrayList = JSONBookParser.parseManyBooks(allBooks);

            return bookArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            // après avoir récupéré la liste de livre de l'étudiant via le serveur, on définit
            // cette liste comme la liste a afficher.
            if(books!=null) {
                getSessionStudent().setBooksList(books);
            }
            // On affiche la liste
            initializeBookList();
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

    public ArrayAdapter<String> getBookListAdapter() {
        return bookListAdapter;
    }

    public void setBookListAdapter(ArrayAdapter<String> bookListAdapter) {
        this.bookListAdapter = bookListAdapter;
    }
}
