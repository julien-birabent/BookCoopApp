package utils;

/**
 * Created by Julien on 2016-11-15.
 * classe contenant les constantes String permettant de disposer des URL et des paramètres nécessaire
 * au dialogue avec le serveur de la coopérative.
 */

public class HttpUtils {

    public final static String SERVER_URL = "http://104.236.210.211/";
    public final static String LAST_COPIE = "copies/last.json/";

    // Ex pour récup un book associé à une copie : books/5807184ec8bf97325a533ff7.json
    public final static String BOOK = "book/";

    public final static String STUDENT_PARAM ="?student_email=";
    public final static String MANAGER_PARAM ="?coopmanager_email=";
    public final static String STUDENT_TOKEN_PARAM ="?student_token=";
    public final static String MANAGER_TOKEN_PARAM ="?coopmanager_token=";

    public final static String AND = "&";



}