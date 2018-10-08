package learncode.nj.com.nplay;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechviral.mplaylib.MusicFinder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
     Toolbar tl;
     public static mediaUtil mUtil;
     public static boolean isFiratPlayed=false;
     private static MediaPlayer mj;
     BottomSheetBehavior bottomSheetBehavior;
     public static List<MusicFinder.Song> musicdata;
     public static int currentSongIndex=0;

     RecyclerView rec;
     View bottoms;
     Button play, next, pre;
     TextView name;
     View.OnClickListener clik;
     FragmentTransaction ft;
     SharedPreferences sh;
     SharedPreferences.Editor ed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tl=findViewById(R.id.toolbar);
        setSupportActionBar(tl);
        sh=getSharedPreferences("currentsong",Context.MODE_PRIVATE);
        mj=new MediaPlayer();
        bottoms=getLayoutInflater().inflate(R.layout.bottomplayer,null,false);
        play=bottoms.findViewById(R.id.bplay);
        next=bottoms.findViewById(R.id.bnext);
        pre=bottoms.findViewById(R.id.bpre);
        name=bottoms.findViewById(R.id.bname);


        if(sh.getInt("index",-1)==-1) {
            ed=sh.edit();
            ed.putInt("index",0);
        }


        musicdata=new ArrayList<>();
        musicdata=getMusics();
        rec=findViewById(R.id.rec);
        View v=findViewById(R.id.bottomsheet);
        ft= getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame,bottomplayer.newInstance());
        ft.commit();





        bottomSheetBehavior=BottomSheetBehavior.from(v);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if(BottomSheetBehavior.STATE_EXPANDED==i){
                    Toast.makeText(MainActivity.this, "expanded", Toast.LENGTH_SHORT).show();
                }
                if(BottomSheetBehavior.PEEK_HEIGHT_AUTO==i){
                    Toast.makeText(MainActivity.this, "peek auto height", Toast.LENGTH_SHORT).show();
                }
                if(BottomSheetBehavior.STATE_COLLAPSED==i){
                   // Toast.makeText(MainActivity.this, "collapsed", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });



        rec.setLayoutManager(new GridLayoutManager(this,2));

        rec.setAdapter(new Recycler_adapter(this,musicdata));



    //initilizing the media util
        mUtil =new mediaUtil(MainActivity.this,musicdata,sh,MainActivity.this);








    }


    private List<MusicFinder.Song> getMusics() {
        MusicFinder songFinder = new MusicFinder(getContentResolver());
        songFinder.prepare();
        List<MusicFinder.Song> songs = songFinder.getAllSongs();
        //System.out.println(songs.size());
       /* for(MusicFinder.Song song:songs) {
            System.out.println(song.getTitle());
            System.out.println(song.getArtist());

        }*/
        return songs;
    }





   public static MediaPlayer getMedia(){
      return mj;
   }



}
