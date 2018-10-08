package learncode.nj.com.nplay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mtechviral.mplaylib.MusicFinder;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static learncode.nj.com.nplay.MainActivity.mUtil;
import static learncode.nj.com.nplay.MainActivity.musicdata;

public  class mediaUtil {
    MediaPlayer mj;
    int SongIndex=0;
    Context c;
    ImageView malbum,mbackalbum;
    TextView mname;
    SeekBar seekBar;
    SharedPreferences ms;
    Activity activity;
    List<MusicFinder.Song> musicdata;
   mediaUtil(Context contex, List<MusicFinder.Song> m, SharedPreferences sd, Activity activity)  {
       this.c=contex;
       this.activity=activity;
       musicdata=new ArrayList<>();
       this.musicdata=m;
       this.SongIndex=sd.getInt("index",0);
       this.ms=sd;
       mj=new MediaPlayer();

       //first pre prepration for first song
       try {
           mj.setDataSource(activity,musicdata.get(SongIndex).getURI());
           mj.prepare();
       } catch (IOException e) {
           e.printStackTrace();
           Toast.makeText(activity, "error occurred", Toast.LENGTH_SHORT).show();
       }

   }

   public MediaPlayer getMedia(){
       return mj;
   }
   public int curretSongIndex(){
       return SongIndex;
   }

   public void startmedia(int index) throws IOException {
       SongIndex=index;
       updateIndex();
       if(mj.isPlaying()){
           mj.stop();
           mj.release();
       }
       mj=new MediaPlayer();
       //mj.setDataSource(musicdata.get(n).getURI());
       mj.setDataSource(activity,musicdata.get(index).getURI());
       mj.prepare();
       mj.start();
       updateView(index);


   }

   public void playpause(){
       if(mj.isPlaying()){
           mj.pause();
       }else{
           mj.start();
       }
   }

   public void next() throws IOException {
       startmedia(SongIndex=SongIndex+1);
   }

   public void pre() throws IOException {
       startmedia(SongIndex=SongIndex-1);
   }


   public int updateIndex(){
      ms.edit().putInt("index",SongIndex);
      return ms.getInt("index",0);
   }

   public void updateView(int i){
       malbum=activity.findViewById(R.id.balbum);
       mbackalbum=activity.findViewById(R.id.backAlbum);
       mname=activity.findViewById(R.id.bname);
       seekBar=activity.findViewById(R.id.bseek);
       Picasso.with(activity)
               .load(musicdata.get(mUtil.curretSongIndex()).getAlbumArt())
               .fit()
               .centerCrop()
               .placeholder(R.drawable.place)
               .into(malbum);
       Picasso.with(activity)
               .load(musicdata.get(mUtil.curretSongIndex()).getAlbumArt())
               .fit()
               .centerCrop()
               .placeholder(R.drawable.place)
               .into(mbackalbum);
       mname.setText(musicdata.get(i).getTitle());
       seekBar.setProgress(0);
       seekBar.setMax(mj.getDuration()*1000);
   }

}
