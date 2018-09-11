package com.example.shikharjai.truthndare;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.file.Path;
import java.util.Random;

public class truthndare extends AppCompatActivity {

    ImageView img;
    boolean rotating = false;
    int initial;
    String truth[] = {"What is your favourite color?",
            "What is your favourite animal?",
            "What is your favourite food?",
            "What is your favourite school subject?",
            "Who is your favourite teacher, and why?",
            "What is your favourite song?",
            "Who is your favourite Disney character?",
            "What is your favourite TV show?",
            "What is your pet’s name?",
            "What is your shoe size?",
            "What is your favourite movie?",
            "Who is your favourite singer?",
            "Do you like bugs?",
            "What is your favourite vegetable?",
            "Would you rather be a chicken or duck?",
            "Would you rather be a pig or snake?",
            "What is your favourite game to play?",
            "Do you like doing chores?",
            "What is your favourite thing to do on the weekends?",
            "Do you know how to dance?",
            "Do you know how to cook?",
            "Can you speak a different language?",
            "What is your favourite thing to do after school?",
            "Are you in any sports?",
            "What is your favourite sport?",
            "What animal are you scared of?",
            "Are you scared of monsters?",
            "Do you have an imaginary friend?",
            "Who is your best friend?",
            "When did you learn how to ride a bike?",
            "Can you use a pogo stick?",
            "What do you want to be when you grow up?",
            "What is your favourite sport in the Olympics?",
            "What is the capital of the state that you live in?",
            "If you could go anywhere in the world, where would you go?",
            "If you a million dollars, what would you do with all of your money?",
            "If you could dye your hair any colour, what colour would you pick?",
            "How many brothers and sisters do you have?",
            "If you could be any animal, which one would you be?",
            "What is your favourite place to go out to eat?",
            "What is the most annoying thing that your mom does?",
            "What is the most annoying thing that your dad does?",
            "Have you ever snuck anyone into to the house?",
            "How many times have you snuck out of the house?",
            "What is the best dish that you mom makes for dinner?",
            "If you could change any of the house rules, what would it be?",
            "Would you rather be a horse or a cow?",
            "What is the funniest thing your grandparents have ever said?",
            "If you could get rid of one household chore, what would it be?",
            "What is your favourite cartoon from your generation?",
            "What is your favourite childhood song?",
            "What is your favourite board game to play with your family?",
            "If you could put one person in your family on mute for a day, who would it be?",
            "At what age did you learn to ride a bike?",
            "Do you know how to swim?",
            "Describe your dream home",
            "What is the funniest dream that you have ever had?",
            "Have you ever had a dream about one of your relatives?",
            "Who is your celebrity husband or wife?",
            "Who taught you how to tie your shoes?",
            "What is the longest that you have ever been without taking a shower?",
            "Have you ever worn the same clothes for more than three days?",
            "What is your favourite holiday and why?",
            "If you could be any dinosaur, which would it be?",
            "How long could you without eating your favourite food?",
            "Who in your family tells the corniest jokes?",
            "Would you rather be a bumblebee or a slug?",
            "Have you ever eaten a worm?",
            "Have you ever picked your nose when you thought no one was looking?",
            "If you could be any super villain, who would you be?"};

    String dare[] = {"Do the silliest dance that you can think of",
            "Act like an old lady or an old man",
            "Spin around 10 times, when you get done try to walk in a straight line",
            "Do 10 jumping jacks, and then do 10 pushups",
            "Say the alphabet backwards in 30 seconds",
            "Go a whole minute without blinking",
            "Hold your breath for 10 seconds",
            "Act like a chicken for 1 minute",
            "Jump up and down until it’s your turn again",
            "Close your eyes until it’s your turn again",
            "Sing “Itsy Bitsy Spider” while standing on your head",
            "Do 5 cartwheels in a row",
            "Do a backbend",
            "Act like your favourite Disney person",
            "Say “banana” after everything you say until it is your turn again",
            "Make up a poem about the colour blue",
            "Act like a monkey until it is your turn again",
            "Do a handstand",
            "Cross your eyes",
            "Lick your nose",
            "Eat a spoonful of peanut butter",
            "Tickle the person to your right",
            "Tie your shoe strings together and try to walk to the door and back",
            "Act like a baby until your next turn",
            "Moo like a cow as loud as you can",
            "Do the sprinkler dance",
            "Act like your favourite superhero",
            "Go 10 minutes without saying the words: but, a, the, or",
            "Act like a statue until it is your turn again (no talking or moving)",
            "Meow like cat",
            "Put your leg behind you head",
            "Sing your favourite song",
            "Make a fish face",
            "Pretend that you are an airplane for 2 minutes",
            "Dance like a ballerina",
            "Say the alphabet in a different language",
            "Do your best hip hop dance",
            "Try to lick your elbow",
            "Paint your fingernails with a crayon",
            "Sit upside down in a chair until your next turn",
            "Put peanut butter on your nose, and try to to lick it off",
            "Do a headstand",
            "Stand on one leg for the next three rounds",
            "Cross your eyes and walk to front yard and back",
            "Do an impression of your favourite relative, do not stop until everyone can guess who you are",
            "Act like the family pet until it is your turn again",
            "For the next 2 rounds, you must do whatever the person to your right says",
            "Quack and walk like a duck",
            "Do your best impression of Mickey Mouse",
            "Spin around for 10 seconds and run out the mailbox and back",
            "Balance a spoon on your nose until the next round",
            "Do your best evil laugh, as loud as you can",
            "Make a funny face, take a selfie, and post it on Facebook",
            "Call a random person and sing happy birthday to them",
            "Blindfolded, go to the kitchen and make sandwich",
            "Give the person to your left a wet willy",
            "Wiggle your ears without using your hands",
            "Talk like a baby for the next 3 rounds",
            "Record yourself doing the silliest dance you can do, and post it to Facebook",
            "Tell a funny knock knock joke, everyone must laugh",
            "Hold your breath for 15 seconds",
            "Do a headstand while you sing your favourite nursery rhyme",
            "Try to spin on your head like a break dancer",
            "Balance the object to your left on your head for the next three rounds",
            "Do your best hula dance",
            "Tapdance on the couch",
            "Stand up and spin around for the next two rounds",
            "Do 150 jumping jacks",
            "Use the person to your right’s stomach as a drum and make up a tribal dance",
            "Stand like a flamingo for the next four rounds"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truthndare);

        img = findViewById(R.id.img);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
//        img.startAnimation(animation);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinObject(v);
            }
        });
    }

    public void spinObject(View view){
        if(!rotating){
            final Random random = new Random();
            int dir[] = {0,90,180,270};
            int end = random.nextInt(dir.length - 1);
            int id = view.getId();

            float x = img.getWidth()/2;
            float y = img.getHeight()/2;
            Animation animation = new RotateAnimation(initial, 2160+dir[end], x, y);
            animation.setDuration(2500);
            animation.setFillAfter(true);//animation does not reset to initial pos after end
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    rotating = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    rotating = false;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(truthndare.this);
                    alertDialogBuilder.setTitle("Choose Your Option");
//                    builder.setTitle("Choose Your Option");
                    alertDialogBuilder.setIcon(R.drawable.aaaaa);

                    alertDialogBuilder.setPositiveButton("Truth", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Dialog dialog1 = new Dialog(truthndare.this);
                            dialog1.setTitle("Here's Your Question");
                            dialog1.setContentView(R.layout.dialog1);
                            TextView tv = (TextView) dialog1.findViewById(R.id.quiz);
                            tv.setText(truth[random.nextInt(truth.length - 1)]);
                            dialog1.show();
                            Button dismiss = dialog1.findViewById(R.id.dismiss);

                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.cancel();
                                }
                            });
                        }
                    });

                    alertDialogBuilder.setNegativeButton("Dare", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final Dialog dialog1 = new Dialog(truthndare.this);
                            dialog1.setTitle("Here's Your Question");
                            dialog1.setContentView(R.layout.dialog1);
                            TextView tv = (TextView) dialog1.findViewById(R.id.quiz);
                            tv.setText(dare[random.nextInt(dare.length - 1)]);
                            dialog1.show();
                            Button dismiss = dialog1.findViewById(R.id.dismiss);

                            dismiss.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog1.cancel();
                                }
                            });
                        }


                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            initial = dir[end];
            img.startAnimation(animation);
        }
    }

}
