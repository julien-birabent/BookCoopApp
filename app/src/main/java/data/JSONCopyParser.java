package data;

import java.util.ArrayList;

import model.Copy;

/**
 * Created by Julien on 2016-11-16.
 */

public class JSONCopyParser {


    public JSONCopyParser() {

    }

    /**
     * Méthode permettant de parse une String représentant un objet JSON en un objet du modèle Copy
     * @return
     */
    public Copy parseCopy(String contents){

        Copy copy = new Copy();

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
