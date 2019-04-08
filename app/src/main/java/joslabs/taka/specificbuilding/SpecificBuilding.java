package joslabs.taka.specificbuilding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joslabs.taka.R;

public class SpecificBuilding extends AppCompatActivity implements View.OnClickListener {
ImageView imgnexta,imgnextb,imgnextc,imgnextd,imgnexte,imgnextf,imgnextg,imgnnexth;
DatabaseReference dbref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_building);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
dbref= FirebaseDatabase.getInstance().getReference();
    /*    dbref.child("nairobi").child("plotsnumber").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("databx",dataSnapshot.getValue().toString());
                Locationdesc loc=dataSnapshot.getValue(Locationdesc.class);
                Log.e("databxx",loc.desc);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
dbref.child("nairobi").child("plotsnumber").addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.e("datax",dataSnapshot.getValue().toString());

        Locationdesc loc=dataSnapshot.getValue(Locationdesc.class);
        Log.e("dataxx",loc.desc+"\n"+dataSnapshot.getChildrenCount());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.e("datax",dataSnapshot.getValue().toString());
        Locationdesc loc=dataSnapshot.getValue(Locationdesc.class);
        Log.e("dataxx",loc.desc);
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
       imgnexta=findViewById(R.id.imgnext);
       imgnextb=findViewById(R.id.imgnextb);
       imgnextc=findViewById(R.id.imgnextc);
       imgnextd=findViewById(R.id.imgnexta);
       imgnexte=findViewById(R.id.imgnextba);

       imgnexta.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent ints;
        switch (v.getId()){
            case R.id.imgnext:

                GetMonths();

                break;

            case R.id.imgnextb:

                GetMonths();
                break;

            case R.id.imgnextc:

                GetMonths();
                break;

            case R.id.imgnexta:

                GetMonths();
                break;
            case R.id.imgnextba:
                ints=new Intent(getApplicationContext(),SpecificRoom.class);
                startActivity(ints);
                break;
        }
    }

    private void GetMonths() {
         MonthlAdapter monthlAdapter;
         monthlyrmdetails monthlyrmdetails;
        ListView listView;


        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.room_update,null);
        builder.setTitle("Monthly payments");
listView=dialogView.findViewById(R.id.list_view);
ArrayList<monthlyrmdetails>monthlyrmdetailsArrayList=new ArrayList<>();
monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Feb","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("March","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("April","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlyrmdetailsArrayList.add(new monthlyrmdetails("Jan","yes","R25C"));
        monthlAdapter=new MonthlAdapter(dialogView.getContext(),monthlyrmdetailsArrayList);
        listView.setAdapter(monthlAdapter);
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
    public class MonthlAdapter extends ArrayAdapter<monthlyrmdetails> {
        private Context context;
        private List <monthlyrmdetails>mdetailslist=new ArrayList<>();

        public MonthlAdapter(@NonNull Context mcontext,  ArrayList<monthlyrmdetails> objects) {
            super(mcontext, 0, objects);
            context=mcontext;
            mdetailslist=objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listview=convertView;
            if (listview==null)
                listview=LayoutInflater.from(context).inflate(R.layout.monthly_rmlist,parent,false);
                monthlyrmdetails monthlyrmdetails=mdetailslist.get(position);
                TextView month=listview.findViewById(R.id.txtmonthname);
                month.setText(monthlyrmdetails.getMname());
                TextView status=listview.findViewById(R.id.txtstatuspay);
                status.setText(monthlyrmdetails.getMstatus());
                TextView receipt=listview.findViewById(R.id.txtreceipt);
                receipt.setText(monthlyrmdetails.getMreceipt());

            return listview;
        }
    }

    public static class monthlyrmdetails{
        public String mname,mstatus,mreceipt;

        public monthlyrmdetails(String mname, String mstatus, String mreceipt) {
            this.mname = mname;
            this.mstatus = mstatus;
            this.mreceipt = mreceipt;
        }

        public String getMname() {
            return mname;
        }

        public void setMname(String mname) {
            this.mname = mname;
        }

        public String getMstatus() {
            return mstatus;
        }

        public void setMstatus(String mstatus) {
            this.mstatus = mstatus;
        }

        public String getMreceipt() {
            return mreceipt;
        }

        public void setMreceipt(String mreceipt) {
            this.mreceipt = mreceipt;
        }

        public monthlyrmdetails() {
        }
    }
}
