package learncode.nj.com.nplay;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import static learncode.nj.com.nplay.MainActivity.*;

public class bottomplayer extends Fragment {
    Button play, next, pre;
    TextView lname;
    ImageView lalbumArt, backAlbum;
    SeekBar seek;
    Handler handler;
    Runnable SeekTask;

    public bottomplayer() {
    }
    public static bottomplayer newInstance() {

        bottomplayer fragment = new bottomplayer();

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottomplayer,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        play=getActivity().findViewById(R.id.bplay);
        next=getActivity().findViewById(R.id.bnext);
        pre=getActivity().findViewById(R.id.bpre);
        lalbumArt=getActivity().findViewById(R.id.balbum);
        backAlbum=getActivity().findViewById(R.id.backAlbum);
        lname=getActivity().findViewById(R.id.bname);
        seek=getActivity().findViewById(R.id.bseek);
        handler=new Handler();
        updatePlayer();
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    mUtil.getMedia().seekTo(i);
                    mUtil.getMedia().start();


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                    mUtil.getMedia().pause();
                    handler.removeCallbacks(SeekTask);


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mUtil.getMedia().start();
                    updateSeekStart();



            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "next", Toast.LENGTH_SHORT).show();

                if(currentSongIndex!=musicdata.size()){
                    try {
                        mUtil.next();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFiratPlayed){
                    isFiratPlayed=true;
                    try {
                        mUtil.startmedia(currentSongIndex);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    mUtil.playpause();
                }


            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "pre", Toast.LENGTH_SHORT).show();


                if(currentSongIndex!=0){
                    try {
                        mUtil.pre();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }
        });
        mUtil.getMedia().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    mUtil.next();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });




        //setup the runnable code for the seekbar
        SeekTask=new Runnable() {
            int progress=0;
            @Override
            public void run() {
                progress=(mUtil.getMedia().getCurrentPosition()
                        /mUtil.getMedia().getDuration())*1000;
              seek.setProgress(progress);
            }
        };




    }


    public void updatePlayer(){
        Toast.makeText(getActivity(), ""+mUtil.curretSongIndex(), Toast.LENGTH_SHORT).show();

        Picasso.with(getActivity())
                .load(musicdata.get(mUtil.curretSongIndex()).getAlbumArt())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.place)
                .into(lalbumArt);
        Picasso.with(getActivity())
                .load(musicdata.get(mUtil.curretSongIndex()).getAlbumArt())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.place)
                .into(backAlbum);
      lname.setText(musicdata.get(mUtil.curretSongIndex()).getTitle());
    }

   public void updateSeekStart(){
        handler.postDelayed(SeekTask,1000);
   }
}
