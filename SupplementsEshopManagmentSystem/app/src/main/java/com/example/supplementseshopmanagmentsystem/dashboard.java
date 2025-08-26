package com.example.supplementseshopmanagmentsystem;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle; import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList; import java.util.List;
import java.util.Locale;
public class dashboard extends Fragment {
    private TextView totalsalesinsert;
    private TextView totalordersinsert;

    private TextView totalcustomersinsert;
    private double totalearnings;
    private PieChart pieChart;
    private PieDataSet pieDataSet;
    private PieData pieData;
    private float[] valsForPie;
    private int[] colorArray;
    private ProSupDB db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        totalsalesinsert = view.findViewById(R.id.totalsalesinsert);
        totalordersinsert = view.findViewById(R.id.totalordersinsert);
        pieChart = view.findViewById(R.id.pieChart);
        colorArray=new int[]{Color.rgb(109, 215, 253),Color.rgb(0, 147, 203),Color.rgb(0, 90, 205),Color.rgb(0, 95, 143)};
        db = Room.databaseBuilder(getContext(),ProSupDB.class,"pro_sup_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        valsForPie = new float[4];
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = firestore.collection("sales");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(int i=0; i<4; i++){
                    valsForPie[i]=0.0f;
                }
                int salesSize = task.getResult().size();
                List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();
                if(!db.proSupDAO().getAllProducts().isEmpty()) {
                    if(salesSize!=0){
                        int firstDocId=Integer.parseInt(documentSnapshots.get(0).getId());
                        int lastDocId=Integer.parseInt(documentSnapshots.get(salesSize-1).getId());
                        System.out.println(firstDocId);
                        DocumentReference documentReference;
                        for(int i=firstDocId; i<=lastDocId; i++){
                            documentReference = collectionReference.document(Integer.toString(i));
                            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ArrayList<String> productsFromFireStore = (ArrayList<String>) documentSnapshot.get("items");
                                    ArrayList<Long> quantitiesFromFireStore = (ArrayList<Long>) documentSnapshot.get("quantities");
                                    try {
                                        for (int j = 0; j < productsFromFireStore.size(); j++) {
                                            String ptype = db.proSupDAO().showProductByName(productsFromFireStore.get(j)).ptype;
                                            if (ptype.equals("Proteins")) {
                                                valsForPie[0] += Float.parseFloat(quantitiesFromFireStore.get(j).toString());
                                                System.out.println("valsForPie[0]" + valsForPie[0]);
                                            } else if (ptype.equals("Creatine")) {
                                                valsForPie[1] += Float.parseFloat(quantitiesFromFireStore.get(j).toString());
                                                System.out.println("valsForPie[1]" + valsForPie[1]);
                                            } else if (ptype.equals("Amino Acids")) {
                                                valsForPie[2] += Float.parseFloat(quantitiesFromFireStore.get(j).toString());
                                                System.out.println("valsForPie[2]" + valsForPie[2]);
                                            } else if (ptype.equals("Vitamins")) {
                                                valsForPie[3] += Float.parseFloat(quantitiesFromFireStore.get(j).toString());
                                                System.out.println("valsForPie[3]" + valsForPie[3]);
                                            }
                                        }
                                        pieDataSet = new PieDataSet(dataValues(valsForPie), "");
                                        pieDataSet.setColors(colorArray);
                                        pieData = new PieData(pieDataSet);
                                        pieData.setValueTextSize(16f);
                                        Legend legend = pieChart.getLegend();
                                        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
                                        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
                                        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
                                        legend.setTextSize(15f);
                                        legend.setDrawInside(false);
                                        pieChart.setData(pieData);
                                        pieChart.getDescription().setEnabled(false);
                                        pieChart.setDrawEntryLabels(false);
                                        pieChart.setUsePercentValues(true);
                                        pieChart.invalidate();
                                        totalearnings += Double.parseDouble(documentSnapshot.get("totalcost").toString());
                                        totalsalesinsert.setText(totalearnings + "$");
                                    }catch (NullPointerException e){}
                                }
                            });
                        }
                    }
                }else{
                    totalsalesinsert.setText("0$");
                }   totalordersinsert.setText(Integer.toString(salesSize));
            } });
        return view;
    }
    private ArrayList<PieEntry> dataValues(float[] valsForPie){
        ArrayList<PieEntry> dataVals = new ArrayList<>();
        dataVals.add(new PieEntry((valsForPie[0]),"Proteins"));
        dataVals.add(new PieEntry((valsForPie[1]),"Creatine"));
        dataVals.add(new PieEntry((valsForPie[2]),"Amino Acids"));
        dataVals.add(new PieEntry((valsForPie[3]),"Vitamins"));
        return dataVals;
    }
}
