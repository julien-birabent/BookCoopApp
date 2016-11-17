package data;

import org.json.JSONObject;

import java.util.ArrayList;

import model.Copy;
import utils.JSONUtils;

/**
 * Created by Julien on 2016-11-16.
 */

public class JSONCopyParser {


    public final static String BOOK_ID = "book_id";
    public final static String COPY_ID = "id";
    public final static String INTEGRITY ="integrity";
    public final static String ID ="$oid";

    public JSONCopyParser() {

    }

    /**
     * Méthode permettant de parse une String représentant un objet JSON en un objet du modèle Copy
     * @return
     */
    public Copy parseCopy(String contents){

        Copy copy = new Copy();
        try {
            // On convertit la chaîne de caractère représentant l'objet JSON en objet JSON
            JSONObject objCopy = new JSONObject(contents);

            // On récupère l'objet JSON contenant l'id du Book associée à la copy.
            JSONObject objBookId = JSONUtils.getObject(BOOK_ID,objCopy);
            // On attribut l'id du Book associé à Copy
            copy.setCopyId(JSONUtils.getInt(ID,objBookId));

            // On récupère l'objet JSON contenant l'id de la Copy
            JSONObject objCopyId = JSONUtils.getObject(COPY_ID,objCopy);
            // On extrait l'id de la Copy contenu dans l'objet précédemment extrait.
            copy.setCopyId(JSONUtils.getInt(ID,objCopyId));

            copy.setPhysicalState(JSONUtils.getString(INTEGRITY,objCopy));

            //copy.setBookId(JSONUtils.getInt(BOOK_ID,objCopy));
           // copy.setCopyId(JSONUtils.getInt(COPY_ID,objCopy));
        }
        catch(Exception e ){

        }

        return copy;
    }

    /**
     * Méthode permettant de parse une String représentant un tableau d'objets JSON contenant les
     * descriptions d'objets du package model Copy.
     * @return
     */
    public ArrayList<Copy> parseManyCopies(String contents){

        ArrayList<Copy> copyArrayList = new ArrayList<Copy>();




        return copyArrayList;
    }

}
