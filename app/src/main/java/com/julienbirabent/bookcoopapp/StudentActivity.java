package com.julienbirabent.bookcoopapp;

import android.app.Activity;
import android.content.Context;
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

    public final  int SCANNER_REQUEST_CODE=0;
    public final String SCANNER_MODE = "ONE_D_MODE";



    private ListView booksList;
    private ArrayAdapter<String> bookListAdapter ;
    private ArrayList<String> booksString = new ArrayList<String>();
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
    public void initActivity(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.booksList = (ListView) findViewById(R.id.listView_books);

        // on récupère les informations de connection de l'étudiant
        this.sessionStudent = new Student();

        Intent intent = getIntent();
        sessionStudent.setEmail(intent.getStringExtra(LoginActivity.USER_NAME));
        sessionStudent.setPassword(intent.getStringExtra(LoginActivity.TOKEN));


        // On demande la liste de livres de l'étudiant pour pouvoir initliaser la liste de description
        // au démarrage de l'activité
        // GetAllCopiesTask GetAllCopiesTask = new GetAllCopiesTask();
        //GetAllCopiesTask.execute();

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


    }

    /**
     * Méthode permettant de mettre à jour la ListView contenant les description de livres
     * Méthode
     */
    public void fillBookListView(){

        booksString = this.getSessionStudent().booksListToArrayListOfString();
        this.bookListAdapter = new ArrayAdapter<String>(this,R.layout.book_item,booksString);
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
                    // Avec l'isbn récupéré on créé une tâche qui va se charge d'envoyer l'isbn
                    // au serveur pour que celui ci ajoute le livre au compte étudiant.
                  /*  PostBookTask postBookTask = new PostBookTask();
                    postBookTask.execute(isbn);

                    GetLastCopyTask GetLastCopyTask = new GetLastCopyTask();
                    // Les parametres de cet appel sont amenés à changer plus tard
                    GetLastCopyTask.execute(BookHttpClient.BASE_URL + BookHttpClient.GET_LAST_BOOK);*/
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
    private class GetLastCopyTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... params) {

            /*
             * Ici on récupère la dernière copie enregistré dans la BD via BookHttpClient et on le convertit en objet physique
             * avec JSONBookParser.
             */

            return null;
        }

        @Override
        protected void onPostExecute(String data) {
            // A la fin de l'éxécution, le livre que l'on a récupéré est ajouter à la liste
            // de livres de l'étudiant .


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
            String allCopies = bookHttpClient.sendGet(params[0]);
            // On convertit les Copy format JSON en objet Copy
            JSONCopyParser jsonCopyParser = new JSONCopyParser();
            copiesArrayList = jsonCopyParser.parseManyCopies(allCopies);
            // Pour chaque copie, on fait une requête dans la BD pour trouver le Book associé
            // On convertit la description JSON du Book en objet Book et on associe la Copy à ce Book
            for(int i= 0 ;i<copiesArrayList.size();i++){
                String stringBook = bookHttpClient.sendGet(HttpUtils.SERVER_URL + HttpUtils.BOOK
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
            fillBookListView();
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
            // param[1] : paramètres de la requête
            new BookHttpClient().sendPost(params[0], params[1]);

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
