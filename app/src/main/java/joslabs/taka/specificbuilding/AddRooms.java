package joslabs.taka.specificbuilding;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import joslabs.taka.R;

public class AddRooms extends AppCompatActivity {
CardView cardView,cardb;
Bundle extras;
String Key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rooms);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras=getIntent().getExtras();
        Key=extras.getString("Key");
        Toast.makeText(this,Key,Toast.LENGTH_SHORT);
        Log.e("Keyfirea",Key);
      cardView=findViewById(R.id.carda);
      cardb=findViewById(R.id.cardb);
      cardb.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent ints=new Intent(getApplicationContext(),ExpandableFirebase.class);
              ints.putExtra("Key",Key);
              startActivity(ints);
          }
      });
      cardView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent ints=new Intent(getApplicationContext(),AddPlotRoom.class);
              ints.putExtra("Key",Key);
              startActivity(ints);
          }
      });
    }

}
