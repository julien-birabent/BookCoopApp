package com.julienbirabent.bookcoopapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import data.BookHttpClient;
import data.JSONBookParser;
import data.JSONCopyParser;
import model.Book;
import model.Copy;
import model.Student;
import utils.HttpUtils;

public class StudentActivity extends AppCompatActivity {

    // Constantes pour l'utilisation du scanner d'isbn
    public final  int SCANNER_REQUEST_CODE=0;
    public final String SCANNER_MODE = "ONE_D_MODE";

    // La liste d'interface contenant les description des copies
    private ListView booksList;
    private Button refreshButton;
    private ArrayAdapter<String> bookListAdapter ;
    private ArrayList<String> copiesDescription = new ArrayList<String>();
    // L'étudiant connecté à l'application
    private Student sessionStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        initActivity();

    }

    /**
     * Méthode instanciant les composants de l'activité.
     */
    private  void initActivity(){
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
     //   setSupportActionBar(toolbar);
        findViewsById();
        defineStudentSession();
        setUIListeners();
    }

    /**
     * Méthode où l'on centralise l'initialisation des listener du UI
     */
    private void setUIListeners(){
        // Définition du bouton flottant permettant de demander l'accès à l'application zxing pour
        // le scan de l'isbn
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", SCANNER_MODE);
                startActivityForResult(intent, SCANNER_REQUEST_CODE);

            }
        });
        // Définition du listener du bouton pour rafrâichir la liste de livre
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On envoie une requête au serveur demandant la liste complète des copies
                // pour l'étudiant connecté.
                getAllCopies();

            }
        });
    }

    /**
     * Méthode ou l'on centralise la récupération des vues
     */
    private void findViewsById(){
        this.booksList = (ListView) findViewById(R.id.listView_books);
        this.refreshButton = (Button) findViewById(R.id.refresh_button);
    }

    /**
     * Méthode où l'on récupère les données de connexion dans un objet Student
     */
    private void defineStudentSession(){
        // on récupère les informations de connection de l'étudiant
        this.sessionStudent = new Student();
        Intent intent = getIntent();
        sessionStudent.setEmail(intent.getStringExtra(LoginActivity.USER_NAME));
        sessionStudent.setPassword(intent.getStringExtra(LoginActivity.TOKEN));

    }

    /**
     * Méthode regroupant les instruction nécessaire pour récupérer la liste de copies de l'étudiant
     * connecté et de l'afficher.
     */
    private void getAllCopies(){
        String url = HttpUtils.SERVER_URL + "copies.json";
        GetAllCopiesTask getAllCopiesTask = new GetAllCopiesTask();
        getAllCopiesTask.execute(url);
    }

    /**
     * Méthode permettant de mettre à jour la ListView contenant les description de livres
     * Elle reset la listView à chaque appel.
     */
    public void fillBookListView(ArrayList<Book> books){

        // On reset notre liste contenant les description d'item à afficher.
        copiesDescription.clear();

        for(int i = 0; i<books.size();i++){

            copiesDescription.add(books.get(i).toString());

        }

        this.bookListAdapter = new ArrayAdapter<String>(this,R.layout.book_item,copiesDescription);
        this.getBooksList().setAdapter(this.bookListAdapter);

    }

    /**
     * méthode permettant d'ajouter une description de livre à la listeView de description de livre
     * de l'activité
     * @param book
     */
    public void addBookToListView(Book book){

        this.getBookListAdapter().add(book.toString());
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

                    // On prépare puis on envoie la requête permettant d'ajouter une copie
                    // dans la liste de l'étudiant connecté via l'isbn du livre scanné.

                    // /books/add?isbn=*ISBN*
                    String urlPostIsbn = HttpUtils.SERVER_URL + HttpUtils.BOOKS + HttpUtils.ADD
                            + HttpUtils.ISBN_PARAM + isbn;
                    PostBookTask postBookTask = new PostBookTask();
                    postBookTask.execute(urlPostIsbn);

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // Handle cancel
            }
        } else {
            // Handle other intents
        }

    }

    /**
     * Tâche asynchrone pour s'occuper de récupérer la dernière copie de livre ajoutée contenu dans la
     * base de données du serveur pour cet étudiant.
     */
    private class GetLastCopyTask extends AsyncTask<String,String,Book> {

        @Override
        protected Book doInBackground(String... params) {

            /*
             * Ici on récupère la dernière copie enregistré dans la BD via BookHttpClient et on le convertit en objet physique
             * avec JSONBookParser.
             */

            // Params[0] = "http://URL_SERVEUR/copies/last.json
            BookHttpClient bookHttpClient = new BookHttpClient();
            String lastCopyString = bookHttpClient.sendGet(params[0]);

            // On convertit le JSON de la Copy récupérée en objet model.Copy
            JSONCopyParser jsonCopyParser = new JSONCopyParser();
            Copy lastCopy = jsonCopyParser.parseCopy(lastCopyString);

            //On récupère le livre associée à cette copie via l'id de livre contenu dans la copie.
            // URL = http://SERVEUR_URL/books/5807184ec8bf97325a533ff7.json
            String url = HttpUtils.SERVER_URL + HttpUtils.BOOKS + lastCopy.getBookId()
                    + HttpUtils.JSON ;

            String lastBookString = bookHttpClient.sendGet(url);
            // On parse l'objet JSON renvoyé en objet model.Book
            JSONBookParser jsonBookParser = new JSONBookParser();
            Book lastBook = jsonBookParser.parseBook(lastBookString);
            // On associe la copie précédemment récupérée à ce livre
            lastBook.setCopy(lastCopy);
            // Debug
            System.out.println("Last book fetched : " + lastBook.toString());

            return lastBook;
        }

        @Override
        protected void onPostExecute(Book book) {
            super.onPostExecute(book);

            // A la fin de l'éxécution, le livre que l'on a récupéré est ajouter à la liste
            // de livres de l'étudiant .

            addBookToListView(book);

        }
    }

    /**
     * Tâche asynchrone permettant de récupérer la liste complète de livres d'un étudiant et
     * de l'afficher à l'endroit prévu dans l'UI.
     */
    private class GetAllCopiesTask extends AsyncTask<String,String,ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(String... params) {

            ArrayList<Copy> copiesArrayList = new ArrayList<Copy>();
            ArrayList<Book> bookArrayList = new ArrayList<Book>();
            JSONBookParser jsonBookParser = new JSONBookParser();
            // On récupère toutes les copies lié à l'étudiant connecté
            BookHttpClient bookHttpClient = new BookHttpClient();
            // http://URL_SERVER:3000/copies.json
            String allCopies = bookHttpClient.sendGet(params[0]);


            // On convertit les Copy format JSON en objet Copy
            JSONCopyParser jsonCopyParser = new JSONCopyParser();
            copiesArrayList = jsonCopyParser.parseManyCopies(allCopies);
            // Pour chaque copie, on fait une requête dans la BD pour trouver le Book associé
            // On convertit la description JSON du Book en objet Book et on associe la Copy à ce Book
            for(int i= 0 ;i<copiesArrayList.size();i++){
                String stringBook = bookHttpClient.sendGet(HttpUtils.SERVER_URL + HttpUtils.BOOKS
                + copiesArrayList.get(i).getBookId() + HttpUtils.JSON);

                Book book = jsonBookParser.parseBook(stringBook);
                book.setCopy(copiesArrayList.get(i));
                bookArrayList.add(book);
            }

            return bookArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            // après avoir récupéré la liste de livre de l'étudiant via le serveur, on définit
            // cette liste comme la liste a afficher.
            if(books!=null) {
                getSessionStudent().setBooksList(books);
            }
            // On remplis et on affiche les descriptions de livres.
            fillBookListView(getSessionStudent().getBooksList());
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
            // Params[0] : url
            BookHttpClient bookHttpClient =  new BookHttpClient();
            String reponse = bookHttpClient.sendGet(params[0]);
            System.out.println(" Response code for posting isbn  :" +  reponse);

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

    public ArrayList<String> getCopiesDescription() {
        return copiesDescription;
    }

    public void setCopiesDescription(ArrayList<String> copiesDescription) {
        this.copiesDescription = copiesDescription;
    }
}
