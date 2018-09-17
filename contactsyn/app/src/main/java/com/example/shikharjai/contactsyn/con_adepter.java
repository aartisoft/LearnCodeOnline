package com.example.shikharjai.contactsyn;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class con_adepter extends RecyclerView.Adapter<con_adepter.viewHolder> {
    Context context;
    List<Contact> contactList;

    public con_adepter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_list, viewGroup, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, final int i) {
        viewHolder.name.setText(contactList.get(i).getContact_name());
        viewHolder.email.setText(contactList.get(i).contact_email);

        viewHolder.rel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog d = new Dialog(context);
                d.setContentView(R.layout.contact_dialog);
                TextView name = d.findViewById(R.id.name);
                TextView email = d.findViewById(R.id.email);
                TextView number = d.findViewById(R.id.number);
                TextView address = d.findViewById(R.id.address);

                name.setText(contactList.get(i).getContact_name());
                email.setText(contactList.get(i).getContact_email());
                email.setSelectAllOnFocus(true);
                number.setText(contactList.get(i).getContact_number());
                address.setText(contactList.get(i).getContact_add());

                number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Uri number = Uri.parse("tel:" + contactList.get(i).contact_number);
                        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            Toast.makeText(context, "Call Permission Not Granted", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        context.startActivity(callIntent);
                    }
                });

                d.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView email;
        TextView address;
        TextView number;
        View rel_layout;
        LinearLayout ll;


        public viewHolder(@NonNull View itemView) {
            super(itemView);
            rel_layout= itemView.findViewById(R.id.rel_layout);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            address = itemView.findViewById(R.id.address);
            number = itemView.findViewById(R.id.number);
        }
    }
}
