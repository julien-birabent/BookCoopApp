package com.julienbirabent.bookcoopapp;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Classer permettant d'ouvrir un dialogue après le scan d'un livre afin de compélter les informations
 * liées à la copie du livre à ajouter.
 * Created by Julien on 2016-11-20.
 */

public class AddCopyDialog extends Dialog {

    private EditText priceEditText;
    private Spinner integrityDropDown;
    private Button confirmButton;
    private boolean isComplete;

    public AddCopyDialog(Context context) {
        super(context);
        this.setContentView(R.layout.add_copy_dialog);
        setTitle("Add a copy to your list");

        priceEditText = (EditText) findViewById(R.id.copy_price);
        integrityDropDown = (Spinner) findViewById(R.id.integrity_dropdown);
        confirmButton = (Button) findViewById(R.id.confirm_dialog_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.integrity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        integrityDropDown.setAdapter(adapter);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isDouble(priceEditText.getText().toString()) &&  !priceEditText.getText().toString().isEmpty()) {
                    setComplete(true);
                    dismiss();
                }
            }
        });
    }

    public String getPrice(){
        return priceEditText.getText().toString();
    }

    public String getIntegrity(){
        return integrityDropDown.getSelectedItem().toString();
    }

    private  boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}

