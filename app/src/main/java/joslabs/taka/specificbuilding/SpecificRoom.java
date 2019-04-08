package joslabs.taka.specificbuilding;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

import joslabs.taka.R;

public class SpecificRoom extends AppCompatActivity implements View.OnClickListener {
int numbers[]={2,6,7,8,9};
int namba[]=new int [numbers.length];
RelativeLayout layerjan;
CardView carda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Arrays.sort(numbers);
        layerjan=findViewById(R.id.layerjan);

        carda=findViewById(R.id.carda);
       // carda.setOnClickListener(this);
        carda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditRoom();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.carda:
                EditRoom();
                break;
        }

    }

    private void EditRoom() {
        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.room_update,null);
        builder.setTitle("Request quick loan");

        builder.setView(dialogView);
        builder.setButton(DialogInterface.BUTTON_NEUTRAL, "ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });



        builder.show();

    }
}
