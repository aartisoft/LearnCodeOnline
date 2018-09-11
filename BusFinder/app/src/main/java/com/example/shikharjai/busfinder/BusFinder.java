package com.example.shikharjai.busfinder;

import android.provider.DocumentsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BusFinder extends AppCompatActivity {

    ArrayList<String> nameLowFloor = new ArrayList<>();
    ArrayList<String> nameMinibus = new ArrayList<>();
    ArrayList<ArrayList<String>> bus_lowfloor = new ArrayList<>();
    ArrayList<ArrayList<String>> bus_mini = new ArrayList<>();

    AutoCompleteTextView sourcet, dest;
    Button searcht;
    RecyclerView recyclerView;
    String TAG = "Mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_finder);
        sourcet = findViewById(R.id.source);
        dest = findViewById(R.id.dest);
        searcht = findViewById(R.id.search);


        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,getStops());
        dest.setAdapter(adapter);
        sourcet.setAdapter(adapter);
        init();
//        Log.d(TAG, "onCreate:oncreate");

        searcht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView = findViewById(R.id.rec);
                recyclerView.setLayoutManager(new LinearLayoutManager(BusFinder.this));
                List<String> result = new ArrayList();
                result = Search_bus(sourcet.getText().toString(), dest.getText().toString());
                Rcadapter adapter1 = new Rcadapter(BusFinder.this, result);
                recyclerView.setAdapter(adapter1);
                Log.d(TAG+"aa", "onClick: "+result.size());
            }
        });
    }

    public List<String> getStops(){
        Log.d(TAG, "getStops: getstops");
        ArrayList<String> busL = new ArrayList<>();
        try{
            InputStream is = getAssets().open("buses_stops.xml");
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
            doc.getDocumentElement().normalize();
            NodeList newList = doc.getElementsByTagName("stops");
            if(newList != null){
                    newList = ((Element) newList.item(0)).getElementsByTagName("item");
                    for(int k = 0; k < newList.getLength(); k++){
                    busL.add(newList.item(k).getChildNodes().item(0).getNodeValue());
                }
            }
            return busL;
            } catch (Exception e) {
                //Toast.makeText(MainActivity.this, "Error : " + e.toString(), Toast.LENGTH_SHORT;
                return null;
        }
    }

    public String[] getArray(ArrayList<ArrayList<String>> array, int pos) {
        String[] result = new String[((ArrayList) array.get(pos)).size()];
        ((ArrayList) array.get(pos)).toArray(result);
        return result;
    }


    public boolean Search_inArray(String[] obj,String find){

        for(String s : obj){
            if(s.equals(find)){
                return true;
            }
        }  return  false;
    }



    public ArrayList<String> Search_bus(String start, String stop){
        String[] res;
        ArrayList<String> bus_detail = new ArrayList<>();
        bus_detail.clear();

        for(int k = 0; k < bus_lowfloor.size(); k++){
            res = getArray(bus_lowfloor, k);
            Log.d(TAG, "Search_bus: "+res[k]);
            if(Search_inArray(res, start) && Search_inArray(res, stop)){
                Log.d(TAG, "Sssssssssssssssssssssssssss: "+res[k]);
                    bus_detail.add(nameLowFloor.get(k)+":Low Floor");
            }
        }

        for(int l = 0; l < bus_mini.size(); l++){
            res=getArray(bus_mini,l);
            if(Search_inArray(res, start) && Search_inArray(res, stop)){
                Log.d(TAG, "Sssssssssssssssssssssssssss: "+res[l]);
                bus_detail.add(nameMinibus.get(l)+":Mini Bus");
            }
        }
        return bus_detail;
     }


    public void init(){
        ArrayList<String> busL = new ArrayList<>();
        ArrayList<String> minB = new ArrayList<>();
        try{
            Log.d(TAG,"init try");
            InputStream lf = getAssets().open("buses_lowfloor.xml");
            InputStream min = getAssets().open("buses_minibus.xml");
            Document lf_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(lf);
            lf_doc.getDocumentElement().normalize();
            Document min_doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(min);
            min_doc.getDocumentElement().normalize();

            NodeList lf_list = lf_doc.getElementsByTagName("bus");

            if(lf_list != null){

                for(int i=0; i < lf_list.getLength(); i++){

                    Element element = (Element)lf_list.item(i);
                    nameLowFloor.add(element.getAttribute("name"));
                    NodeList lf_list_item = element.getElementsByTagName("item");
                    for(int j = 0; j < lf_list_item.getLength(); j++){
                        busL.add(lf_list_item.item(j).getChildNodes().item(0).getNodeValue());
                    }

                    bus_lowfloor.add(busL);
                }
            }

            NodeList min_list = min_doc.getElementsByTagName("bus");

       if(min_list != null){
                for(int k = 0; k < min_list.getLength(); k++){
                    Element element2 = (Element)min_list.item(k);
                    nameMinibus.add(element2.getAttribute("name"));

                    NodeList min_list_item = element2.getElementsByTagName("item");
                    for(int l = 0; l < min_list_item.getLength(); l++){
                        minB.add(min_list_item.item(l).getChildNodes().item(0).getNodeValue());
                        Log.d(TAG, ""+min_list_item.item(l).getChildNodes().item(0).getNodeValue().toString());
                    }
                    bus_mini.add(minB);
                }
            }
 } catch (Exception e){
            Log.d(TAG,"init catch"+e);
       }
    }
}


