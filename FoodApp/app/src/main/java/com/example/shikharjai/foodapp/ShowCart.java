package com.example.shikharjai.foodapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shikharjai.foodapp.Common.Checksum;
import com.example.shikharjai.foodapp.Common.Constants;
import com.example.shikharjai.foodapp.Common.Paytm;
import com.example.shikharjai.foodapp.Common.User;
import com.example.shikharjai.foodapp.Model.Request;
import com.example.shikharjai.foodapp.Retrofit.Api;
import com.example.shikharjai.foodapp.SqLite.DatabaseAccess;
import com.example.shikharjai.foodapp.Model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.util.Locale.getAvailableLocales;

public class ShowCart extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Order> orderList;
    private String TAG="ShowCart";
    TextView total_price;
    int total;
    Locale locale;
    private Button placeOrder;
    DatabaseReference requests;
    DatabaseAccess databaseAccess;
    private CartAdapter adapter;

    public ShowCart() {

        total = 0;
        this.orderList = new ArrayList<>();
        locale = new Locale("hi","IN");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cart);

        Log.d(TAG, "onCreate: "+getAvailableLocales().toString() );
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(ShowCart.this);
        databaseAccess.open();
        orderList = databaseAccess.getCarts();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowCart.this);

        recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CartAdapter(orderList, locale);
        recyclerView.setAdapter(adapter);

        total_price = findViewById(R.id.total_price);
        for(Order order: orderList){
            total += Integer.parseInt(order.getProductPrice())*Integer.parseInt(order.getProductQuantity());
        }
        total_price.setText(Currency.getInstance(locale).getSymbol()+"  "+total);

        placeOrder =findViewById(R.id.place_order);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderList.size()==0){
                    Toast.makeText(ShowCart.this, "You have no items in your cart", Toast.LENGTH_SHORT).show();
                    return;
                }
                askAddress();
            }
        });

        requests = FirebaseDatabase.getInstance().getReference("Requests");

    }

    private void askAddress() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShowCart.this);
        alertDialog.setTitle("One More Step !!!");
        alertDialog.setMessage("Enter Your Address");

        final EditText editText = new EditText(ShowCart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(User.currentUser.getName(), ""+User.currentUser.getContact(), editText.getText().toString(), ""+total, orderList);

                requests.child(System.currentTimeMillis()+"").setValue(request);

                databaseAccess=DatabaseAccess.getInstance(ShowCart.this);
                databaseAccess.open();
                databaseAccess.cleanCart(); // clearing the sqlite orderdetails table
                orderList.clear();  // as well as the orders fethched from the sqlite orderdetais table
                adapter.notifyDataSetChanged();
                generateCheckSum(request.getTotal()+"");
                total_price.setText(Currency.getInstance(locale).getSymbol()+"  "+0);

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void generateCheckSum(String total) {
        //getting the tax amount first.
        String txnAmount = total;

        //creating a retrofit object.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //creating the retrofit api service
        Api apiService = retrofit.create(Api.class);

        //creating paytm object
        //containing all the values required
        final Paytm paytm = new Paytm(
                Constants.M_ID,
                Constants.CHANNEL_ID,
                txnAmount,
                Constants.WEBSITE,
                Constants.CALLBACK_URL,
                Constants.INDUSTRY_TYPE_ID
        );

        //creating a call object from the apiService
        Call<Checksum> call = apiService.getChecksum(
                paytm.getmId(),
                paytm.getOrderId(),
                paytm.getCustId(),
                paytm.getChannelId(),
                paytm.getTxnAmount(),
                paytm.getWebsite(),
                paytm.getCallBackUrl(),
                paytm.getIndustryTypeId()
        );

        //making the call to generate checksum
        call.enqueue(new Callback<Checksum>() {
            @Override
            public void onResponse(Call<Checksum> call, Response<Checksum> response) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().getChecksumHash(), paytm);
                Log.d(TAG, "onResponse: Success");
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {
                Toast.makeText(ShowCart.this, "Could not Generate Checksum"+t.toString(), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onFailure: Fail"+t.toString());
            }
        });
    }

    private void initializePaytmPayment(String checksumHash, Paytm paytm) {

            //getting paytm service
        PaytmPGService Service = PaytmPGService.getStagingService();

            //use this when using for production
            //PaytmPGService Service = PaytmPGService.getProductionService();

            //creating a hashmap and adding all the values required

            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("MID", Constants.M_ID);
            paramMap.put("ORDER_ID", paytm.getOrderId());
            paramMap.put("CUST_ID", paytm.getCustId());
            paramMap.put("CHANNEL_ID", paytm.getChannelId());
            paramMap.put("TXN_AMOUNT", paytm.getTxnAmount());
            paramMap.put("WEBSITE", paytm.getWebsite());
            paramMap.put("CALLBACK_URL", paytm.getCallBackUrl());
            paramMap.put("CHECKSUMHASH", checksumHash);
            paramMap.put("INDUSTRY_TYPE_ID", paytm.getIndustryTypeId());


            //creating a paytm order object using the hashmap
            PaytmOrder order = new PaytmOrder(paramMap);

            //intializing the paytm service
            Service.initialize(order, null);

            //finally starting the payment transaction
            Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
                @Override
                public void onTransactionResponse(Bundle inResponse) {
                    Toast.makeText(ShowCart.this, "Thanks " + User.currentUser.getName()+" for placing the order.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(ShowCart.this, "Order Placed" , Toast.LENGTH_SHORT).show();
                }

                @Override
                public void networkNotAvailable() {
                    Toast.makeText(ShowCart.this, "Network Error", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void clientAuthenticationFailed(String inErrorMessage) {
                    Toast.makeText(ShowCart.this, ""+inErrorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void someUIErrorOccurred(String inErrorMessage) {
                    Toast.makeText(ShowCart.this, ""+inErrorMessage, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {
                    Toast.makeText(ShowCart.this, ""+inErrorMessage, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onBackPressedCancelTransaction() {
                    Toast.makeText(ShowCart.this, "Back Pressesd", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                    Toast.makeText(ShowCart.this, ""+inErrorMessage+inResponse.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

