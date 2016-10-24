package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.JSONUtils;

/**
 * Created by Julien on 23/10/2016.
 * Classe permettant d'offrir des méthodes d'accès aux tables de données des livres sur
 * le site de notre application.
 */

public class BookHttpClient {

    // A compléter
    public final static String POST_BOOK_URL = "";
    public final static String GET_LIST_BOOK_URL = "";
    public final static String GET_LAST_BOOK = "";

    // Méthode permettant d'envoyer un ISBN au serveur afin que celui-ci cherche le livre correspondant
    // et l'ajoute à la liste de livres d'un étudiant.
    public void postBook(String isbn){


    }

    /**
    méthode permettant de récupérer la liste des livres associée à un étudiant.
     */
    public String getAllBooks(String url){
        return null;
    }

    /**
     * Méthode permettant de fetch le dernier livre ajouté
     * @param url
     * @return
     */
    public String getGetLastBook(String url){


        return null;
    }


}
