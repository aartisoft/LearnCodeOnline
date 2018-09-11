package com.example.shikharjai.calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class calculator extends AppCompatActivity{
    double first, second, result;
    boolean additon=false,
            subtraction=false,
            multiplication=false,
            division=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Button nine = findViewById(R.id.nine);
        Button eight = findViewById(R.id.eight);
        Button seven = findViewById(R.id.seven);
        Button six = findViewById(R.id.six);
        Button five = findViewById(R.id.five);
        Button four = findViewById(R.id.four);
        Button three = findViewById(R.id.three);
        Button two = findViewById(R.id.two);
        Button one = findViewById(R.id.one);
        Button zero = findViewById(R.id.zero);

        Button deci = findViewById(R.id.deci);
        Button del = findViewById(R.id.del);
        Button div = findViewById(R.id.div);
        Button mul = findViewById(R.id.mul);
        Button sub = findViewById(R.id.sub);
        final Button add = findViewById(R.id.add);
        Button equal = findViewById(R.id.equal);

        final TextView tv = findViewById(R.id.tv);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    first=Double.valueOf(tv.getText().toString());
                    tv.setText(null);

                    additon=true;
                    subtraction=false;
                    multiplication=false;
                    division=false;

                } catch (Exception r){

                }
            }
        });

        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    first = Double.valueOf(tv.getText().toString());
                    tv.setText(null);

                    additon = false;
                    subtraction = true;
                    multiplication = false;
                    division = false;
                }
                catch (Exception e){

                }
            }
        });


        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    first=Double.valueOf(tv.getText().toString());
                    tv.setText(null);

                    additon=false;
                    subtraction=false;
                    multiplication=true;
                    division=false;

                } catch (Exception e){

                }
            }
        });

        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    first=Double.valueOf(tv.getText().toString());
                    tv.setText(null);

                    additon=false;
                    subtraction=false;
                    multiplication=false;
                    division=true;
                } catch (Exception e){

                }
            }
        });


        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("9");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append("0");
            }
        });

        deci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.append(".");
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String get = tv.getText().toString();
                    tv.setText(get.substring(0,get.length()-1));
                } catch (Exception e){

                }
            }
        });

        del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                tv.setText(null);
                return false;
            }
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(tv.getText().toString().isEmpty())){
                    second = Double.valueOf(tv.getText().toString());
                    try {
                        if (additon) {
                            result = first + second;
                            tv.setText("" + result);
                        } else if (subtraction) {
                            result = first - second;
                            tv.setText("" + result);
                        } else if (multiplication) {
                            result = first * second;
                            tv.setText("" + result);
                        } else if (division) {
                            result = first / second;
                            tv.setText("" + result);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.one:
//                tv.
//                break;
//            case R.id.two:
//                break;
//            case R.id.three:
//                break;
//            case R.id.four:
//                break;
//            case R.id.five:
//                break;
//            case R.id.six:
//                break;
//            case R.id.seven:
//                break;
//            case R.id.eight:
//                break;
//            case R.id.nine:
//                break;
//            case R.id.zero:
//                break;
//
//        }
//
//    }


}
