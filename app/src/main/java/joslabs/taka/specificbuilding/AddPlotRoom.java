package joslabs.taka.specificbuilding;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import joslabs.taka.R;

public class AddPlotRoom extends AppCompatActivity {
    EditText edtroom,edtfloor,edtnpaper,edtpay;
    Button btnsubmit;
    String rmno,flno,ispaid,npapers,Keyd,Keyb,Keye,Key;
    DatabaseReference dbref;
    Locationdesc locationdesc;
    Map<String,Object>postvaluesd,postmonths;;
    Bundle extras;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plot_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbref= FirebaseDatabase.getInstance().getReference();
        edtroom=findViewById(R.id.edtrmno);
        edtfloor=findViewById(R.id.edtfloor);
        edtnpaper=findViewById(R.id.edtnpapers);
        edtpay=findViewById(R.id.edtpay);
        extras=getIntent().getExtras();
         Keyb=extras.getString("Key");
        Log.e("Keyfire",Keyb);
        btnsubmit=findViewById(R.id.btnsubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rmno=edtroom.getText().toString();
                flno=edtfloor.getText().toString();
                ispaid=edtpay.getText().toString();
                npapers=edtnpaper.getText().toString();
                Log.e("Keyb",Keyb);
                locationdesc=new Locationdesc(rmno,ispaid,npapers,Keyb,flno);
                Map<String,Object> postvalues=locationdesc.toMap();
                Map<String,Object>childupdates=new HashMap<>();

                String months[]={"Jan","Feb","March","April","May","June","July","August","Sept","Öct","Nov","Dec"};

                    Keyd=dbref.child("roomnumber").push().getKey();
                    Locationdesc  locationdescd = new Locationdesc(rmno,ispaid,npapers,Keyd,flno);
                    postvaluesd= locationdescd.toMap();
                    for (int m=0;m<months.length;m++){
                        Keye=dbref.child("roomdetails").push().getKey();
                        Locationdesc  locationdesce = new Locationdesc(months[m], "not paid", "Rno 1"+m, Keyd);
                        postmonths=locationdesce.toMap();
                        childupdates.put("/nairobi/roomdetails/"+Keyd+"/"+Keye,postmonths);
                    }
                    childupdates.put("/nairobi/roomnumber/"+Keyb+"/"+Keyd,postvaluesd);



                dbref.updateChildren(childupdates);

            }
        });



    }

}
