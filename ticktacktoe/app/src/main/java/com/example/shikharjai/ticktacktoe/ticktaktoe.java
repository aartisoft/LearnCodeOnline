package com.example.shikharjai.ticktacktoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ticktaktoe extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="aaaaaa" ;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
    boolean current_player_x = false;
//    Map<String, Integer> aMap;
    int[][] board=new int[3][3];
    int count = 0;
    Button reset;
    TextView console;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticktaktoe);
        //aMap = new HashMap<String, Integer>();
        console = findViewById(R.id.console);
        reset = (Button)findViewById(R.id.reset);

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        enableAllbuttons(false);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
        resetGame();
    }

    public void onClick(View v){
        Log.d(TAG, "onClick: "+v.getId());

    Button b = findViewById(v.getId());
    b.setEnabled(false);
        if(current_player_x){
            b.setText("X");
//            aMap.put(""+v.getId(), 1);
            console.setText("Player 2 Turn");
        } else{
            b.setText("O");
//            aMap.put(""+v.getId(), 0);
            console.setText("Player 1 Turn");
        }

        count++;
        current_player_x = !current_player_x;
        if(count == 9){
            result("Draw");
        }

        switch (b.getId()){
            case R.id.btn1:
                Log.d(TAG, "checkStatus: sss");
                    if(current_player_x){
                        board[0][0] = 1;
                    } else {
                        board[0][0] = 0;
                    }
                    break;

            case R.id.btn2:
                if(current_player_x){
                    board[0][1] = 1;
                } else {
                    board[0][1] = 0;
                }
                break;

            case R.id.btn3:
                if(current_player_x){
                    board[0][2] = 1;
                } else {
                    board[0][2] = 0;
                }
                break;

            case R.id.btn4:
                if(current_player_x){
                    board[1][0] = 1;
                } else {
                    board[1][0] = 0;
                }
                break;

            case R.id.btn5:
                if(current_player_x){
                    board[1][1] = 1;
                } else {
                    board[1][1] = 0;
                }
                break;

            case R.id.btn6:
                if(current_player_x){
                    board[1][2] = 1;
                } else {
                    board[1][2] = 0;
                }
                break;

            case R.id.btn7:
                if(current_player_x){
                    board[2][0] = 1;
                } else {
                    board[2][0] = 0;
                }
                break;

            case R.id.btn8:
                if(current_player_x){
                    board[2][1] = 1;
                } else {
                    board[2][1] = 0;
                }
                break;

            case R.id.btn9:
                if(current_player_x){
                    board[2][2] = 1;
                } else {
                    board[2][2] = 0;
                }
                break;

            default:
                break;
        }

        checkStatus();
    }

    public void checkStatus(){
        for(int i = 0; i < 3; i++){
            if(board[i][0] == board[i][1] && board[i][0] == board[i][2]){
                if(board[i][0] == 1){
                    result("Player 2 wins. Row is: "+ (i+1));
                }
                else if(board[i][0] == 0){
                    result("Player 1 wins. Row is: "+ (i+1));
                }
            }
        }

        for(int j = 0; j < 3; j++){
            if(board[0][j] == board[1][j] && board[1][j] == board[2][j]){
                if(board[0][j] == 1){
                    result("Player 2 wins. Column is: "+ (j+1));
                }
                else if(board[0][j] == 0){
                    result("Player 1 wins. Column is: "+ (j+1));
                }
            }
        }

        if((board[0][0]==board[1][1] && board[1][1]==board[2][2])){
            if(board[0][0] == 1){
                result("Player 2 wins. First Diagonal");
            }
            else if(board[0][0] == 0){
                result("Player 1 wins. First Diagonal");
            }
        }

        if((board[0][2]==board[1][1] && board[1][1]==board[2][0])){
            Log.d(TAG, "checkStatus: "+board[0][2] +""+ board[1][1] + ""+board[2][0]);
            if(board[0][2] == 1){
                result("Player 2 wins. Second Diagonal");
            }
            else if(board[0][2] == 0){
                result("Player 1 wins. Second Diagonal");
            }
        }
    }

    public void result(String str){
        console.setText(str);
        enableAllbuttons(false);
        reset.setText("RESTART");
        reset.setEnabled(true);
    }

    public void resetGame(){
        enableAllbuttons(true);
        reset.setText("Game Started");
        reset.setEnabled(false);
        btn1.setText("");
        btn2.setText("");
        btn3.setText("");
        btn4.setText("");
        btn5.setText("");
        btn6.setText("");
        btn7.setText("");
        btn8.setText("");
        btn9.setText("");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++ ){
                board[i][j] = -1;
            }
        }
        count = 0;
        current_player_x = true;
        console.setText("Player 1 turn");
    }


    public void enableAllbuttons(boolean val){
        btn1.setEnabled(val);
        btn2.setEnabled(val);
        btn3.setEnabled(val);
        btn4.setEnabled(val);
        btn5.setEnabled(val);
        btn6.setEnabled(val);
        btn7.setEnabled(val);
        btn8.setEnabled(val);
        btn9.setEnabled(val);
    }
}
