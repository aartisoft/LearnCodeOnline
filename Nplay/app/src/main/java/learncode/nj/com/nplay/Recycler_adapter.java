package learncode.nj.com.nplay;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechviral.mplaylib.MusicFinder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nj-nitesh on 5/12/2018.
 */

public class Recycler_adapter extends RecyclerView.Adapter<Recycler_adapter.Recycler_holder> {
        final Context con;
         //final Music_info data[];
       List<MusicFinder.Song> data;
    public Recycler_adapter(Context con, List<MusicFinder.Song> d ) {
        this.con = con;
        data=new ArrayList();
        this.data=d;
    }


    @Override
    public Recycler_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(con);
        View v=inflater.inflate(R.layout.music_cards,parent,false);

        return new Recycler_holder(v);
    }

    @Override
    public void onBindViewHolder(Recycler_holder holder, final int position) {
           final MusicFinder.Song single;

            if (holder!=null){
                single= data.get(position);
                //final int pos=position;
                holder.Sname.setText(single.getTitle());
                holder.Sartist.setText(single.getArtist());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(con, "the song name "+single.getAlbumArt(), Toast.LENGTH_SHORT).show();


                  MainActivity.currentSongIndex=position;

                        try {
                            MainActivity.mUtil.startmedia(position);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Picasso.with(con).load(single.getAlbumArt()).fit().centerCrop()
                        .placeholder(R.drawable.place)
                        .into(holder.cardavtar);

            }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Recycler_holder extends RecyclerView.ViewHolder {
        TextView Sname,Sartist;
        ImageView cardavtar;
        public Recycler_holder(View itemView) {
            super(itemView);
            cardavtar=itemView.findViewById(R.id.avtar);
            Sname=itemView.findViewById(R.id.Sname);
            Sartist=itemView.findViewById(R.id.Sartist);
        }
    }
}
