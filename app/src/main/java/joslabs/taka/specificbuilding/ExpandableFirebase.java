package joslabs.taka.specificbuilding;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import joslabs.taka.R;

public class ExpandableFirebase extends AppCompatActivity {
    private LinkedHashMap<String, GroupInfo> subjects = new LinkedHashMap<String, GroupInfo>();
    private ArrayList<GroupInfo> deptList = new ArrayList<GroupInfo>();

    private CustomAdapter listAdapter;
    private ExpandableListView simpleExpandableListView;
    DatabaseReference dbref;
    ArrayList kk;
    List<Locationdesc> locationdescList;
    Bundle extras;
    String rmKey;
    MonthlAdapter monthlAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_firebase);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        extras=getIntent().getExtras();
        rmKey=extras.getString("Key");

        dbref= FirebaseDatabase.getInstance().getReference();
        locationdescList=new ArrayList<>();
        dbref.child("nairobi").child("roomnumber").child(rmKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String datas=dataSnapshot.getValue().toString();
                Log.e("childx",datas);
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    try {
                        Log.e("childs",child.toString());
                        //Log.e("child", child.getValue(Locationdesc.class).toString());
                        locationdescList.add(child.getValue(Locationdesc.class));
                        Log.e("childxx", child.getValue(Locationdesc.class).toString()+"\n"+locationdescList.size()+"\n"+locationdescList.get(0).desc);


                    } catch (Exception e) {
                        Log.e("childerror",e.getMessage());
                    }
                }
                loadData();

                listAdapter = new CustomAdapter(ExpandableFirebase.this, deptList);
                // attach the adapter to the expandable list view
                simpleExpandableListView.setAdapter(listAdapter);

                //expand all the Groups
                expandAll();
                collapseAll();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // loadData();
        simpleExpandableListView = (ExpandableListView) findViewById(R.id.simpleExpandableListView);
        // create the adapter by passing your ArrayList data

        // setOnChildClickListener listener for child row click
        simpleExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);
                //get the child info
                ChildInfo detailInfo =  headerInfo.getProductList().get(childPosition);
                //display it or do something with it
                String monthkey=detailInfo.getRmonthkey();
                Toast.makeText(getBaseContext(), " Clicked on :: " + headerInfo.getName()
                        + "/" + detailInfo.getName(), Toast.LENGTH_LONG).show();
                GetMonths(monthkey);
                Log.e("rmonthkey",monthkey);
                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        simpleExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                GroupInfo headerInfo = deptList.get(groupPosition);
                //display it or do something with it
                Toast.makeText(getBaseContext(), " Header is :: " + headerInfo.getName(),
                        Toast.LENGTH_LONG).show();

                return false;
            }
        });

    }
    private void expandAll() {
        int count = listAdapter.getGroupCount();
       // for (int i = 0; i < count; i++){
            simpleExpandableListView.expandGroup(0);
       // }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 1; i < count; i++){
            simpleExpandableListView.collapseGroup(i);
        }
    }

    //load some initial data into out list
    private void loadData(){
       int kx=locationdescList.size();
       int kxz=0;
        for (int x=0;x<kx-1;x++){
            addProduct(locationdescList.get(x).floorno,locationdescList.get(x).shortdesc,locationdescList.get(x).desc,locationdescList.get(x).title,locationdescList.get(x).lockey);
           /* for (int k=0;k<5;k++){
                String dep="Floor"+x;
                Log.e("expd",dep+"\t"+locationdescList.get(kxz).desc+kxz);
                addProduct(dep,locationdescList.get(kxz).desc+kxz);
                kxz++;
            }*/

        }/*
        addProduct("Android","ListView");
        addProduct("Android","ExpandableListView");
        addProduct("Android","GridView");

        addProduct("Java","PolyMorphism");
        addProduct("Java","Collections");*/

    }



    //here we maintain our products in various departments
    private int addProduct(String department, String product,String papers,String rmno,String key ){

        int groupPosition = 0;

        //check the hash map if the group already exists
        GroupInfo headerInfo = subjects.get(department);
        //add the group if doesn't exists
        if(headerInfo == null){
            headerInfo = new GroupInfo();
            headerInfo.setName(department);
            subjects.put(department, headerInfo);
            deptList.add(headerInfo);
        }

        //get the children for the group
        ArrayList<ChildInfo> productList = headerInfo.getProductList();
        //size of the children list
        int listSize = productList.size();
        //add to the counter
        listSize++;

        //create a new child and add that to the group
        ChildInfo detailInfo = new ChildInfo();
        detailInfo.setSequence(rmno);
        detailInfo.setName(product);
        detailInfo.setPapers(papers);
        detailInfo.setRmonthkey(key);
        productList.add(detailInfo);
        headerInfo.setProductList(productList);

        //find the group position inside the list
        groupPosition = deptList.indexOf(headerInfo);
        return groupPosition;
    }

    private void GetMonths(String monthkey) {

     //  final monthlyrmdetails monthlyrmdetails;
        Log.e("childarraykey",monthkey);
        final ListView listView;

        final ArrayList< monthlyrmdetails>monthlyrmdetailsArrayList=new ArrayList<>();
        final AlertDialog builder = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(this.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.room_update,null);
        builder.setTitle("Monthly payments");
        listView=dialogView.findViewById(R.id.list_view);

        dbref.child("nairobi").child("roomdetails").child(monthkey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // String datas=dataSnapshot.getValue().toString();
                // Log.e("childx",datas);
                for (DataSnapshot child:dataSnapshot.getChildren()) {
                    try {
                        Log.e("childsarray",child.toString());
                        //Log.e("child", child.getValue(Locationdesc.class).toString());
                        // locationdescList.add(child.getValue(Locationdesc.class));
                        // Log.e("childxx", child.getValue(Locationdesc.class).toString()+"\n"+locationdescList.size()+"\n"+locationdescList.get(0).desc);
                        Log.e("childarray",child.getValue(monthlyrmdetails.class).toString());;
                        monthlyrmdetailsArrayList.add(child.getValue(monthlyrmdetails.class));

                    } catch (Exception e) {
                        Log.e("childerror",e.getMessage());
                    }
                }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


      /*  monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));
        monthlyrmdetailsArrayList.add(new  monthlyrmdetails("Jan","yes","R25C","key"));

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

*/




    }
    public class MonthlAdapter extends ArrayAdapter< monthlyrmdetails> {
        private Context context;
        private List < monthlyrmdetails>mdetailslist=new ArrayList<>();

        public MonthlAdapter(@NonNull Context mcontext, ArrayList< monthlyrmdetails> objects) {
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
            month.setText(monthlyrmdetails.getTitle());
            TextView status=listview.findViewById(R.id.txtstatuspay);
            status.setText(monthlyrmdetails.getShortdesc());
            TextView receipt=listview.findViewById(R.id.txtreceipt);
            receipt.setText(monthlyrmdetails.getDesc());

            return listview;
        }
    }

    public static class monthlyrmdetails{
       
        public String title, shortdesc, desc,  lockey;
       // Locationdesc  locationdesce = new Locationdesc(months[m], "not paid", "Rno 1"+m, Keyd);

        public monthlyrmdetails(String title, String shortdesc, String desc, String lockey) {
            this.title = title;
            this.shortdesc = shortdesc;
            this.desc = desc;
            this.lockey = lockey;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getShortdesc() {
            return shortdesc;
        }

        public void setShortdesc(String shortdesc) {
            this.shortdesc = shortdesc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getLockey() {
            return lockey;
        }

        public void setLockey(String lockey) {
            this.lockey = lockey;
        }

        public monthlyrmdetails() {
        }
    }
}
