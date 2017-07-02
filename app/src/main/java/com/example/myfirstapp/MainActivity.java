package com.example.myfirstapp;
import java.util.*;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera.Parameters;


public class MainActivity extends AppCompatActivity {

    protected String expression = new String();
    protected boolean isFlashing = false;
    protected static Camera cam = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(MainActivity.this, "Application has been resumed!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, "Application has been minimized!", Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }


    public void vibrate(View view){
        Vibrator vibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(1000);
    }

    public void update(View view){
        TextView mytextview = (TextView) findViewById(R.id.textView);
        mytextview.setText(expression);
    }

    public void clear(View view){
        expression = "";
    }

    public void dial0(View view){
        expression += "0";
        update(view);
    }

    public void dial1(View view){
        expression += "1";
        update(view);
    }

    public void dial2(View view){
        expression += "2";
        update(view);
    }

    public void dial3(View view){
        expression += "3";
        update(view);
    }

    public void dial4(View view){
        expression += "4";
        update(view);
    }


    public void dial5(View view){
        expression += "5";
        update(view);
    }

    public void dial6(View view){
        expression += "6";
        update(view);
    }

    public void dial7(View view){
        expression += "7";
        update(view);
    }


    public void dial8(View view){
        expression += "8";
        update(view);
    }

    public void dial9(View view){
        expression += "9";
        update(view);
    }

    public void dialplus(View view){
        expression += "+";
        update(view);
    }

    public void dialminus(View view){
        expression += "-";
        update(view);
    }

    public void dialdevision(View view){
        expression += "/";
        update(view);
    }

    public void dialmultiplication(View view){
        expression += "*";
        update(view);
    }

    public void dialto(View view){
        expression += "^";
        update(view);
    }

    public void dialC(View view){
        clear(view);
        update(view);
    }

    public void dialleft(View view){
        expression += "(";
        update(view);
    }

    public void dialright(View view){
        expression += ")";
        update(view);
    }

    public void dialdecimal(View view){
        expression += ".";
        update(view);
    }

    public void evaluate(View view){
        try {
            Double res = calculate_expr(expression);
            expression = res.toString();
            update(view);
        }
        catch(Exception e) {
            Toast.makeText(MainActivity.this, "Invalid expression!", Toast.LENGTH_SHORT).show();
            vibrate(view);
        }
    }

    public void backspace(View view){
        if (expression != null && expression.length() > 0) {
            expression = expression.substring(0, expression.length() - 1);
            update(view);
        }
    }


    public static void apply(Character op,Stack<Double> st){
        Double rarg = st.pop();
        Double larg = st.pop();

        switch (op) {
            case '+': st.push(larg + rarg);break;
            case '-': st.push(larg - rarg);break;
            case '*': st.push(larg * rarg);break;
            case '/': st.push(larg / rarg);break;
            case '^': st.push(Math.pow(larg,rarg));break;
            default : st.push(0D);
        }
    }

    public static boolean isop(Character c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static int priority(Character op) {
        switch (op) {
            case '+':return 1;
            case '-':return 1;
            case '*':return 2;
            case '/':return 2;
            case '^':return 3;
            default: return 0;
        }
    }


    public static Double calculate_expr(String expr)throws Exception {
        int index = 0;
        int length = expr.length();
        Stack<Character> ops = new Stack<Character> ();
        Stack<Double> results = new Stack<Double> ();
        if (expr.charAt(0) == '-')
        {
            results.push(0d);
        }

        Character c = expr.charAt(0);
        if (c == '+' || c == '.' || c == '*' || c == '/' || c == '^' || c == ')')
        {
            Exception e = new Exception();
            throw(e);
        }

        while (	index < length) {
            if (expr.charAt(index) == '('){
                ops.push(expr.charAt(index));
                if (expr.charAt(index+1) == '-')
                {
                    results.push(0d);
                }
            }
            else if (isop(expr.charAt(index))) {
                while (!ops.empty() && priority(ops.peek()) >= priority(expr.charAt(index)))
                    apply(ops.pop(), results);
                ops.push(expr.charAt(index));
            }
            else if (isdigit(expr.charAt(index))){
                String number = "";
                while (index<length && !isop(expr.charAt(index)) && expr.charAt(index) != '(' && expr.charAt(index) != ')' ){
                    number += expr.charAt(index++);
                }
                results.push(Double.valueOf(number));
                index--;
            }
            else if (expr.charAt(index) == ')') {
                while(ops.peek() != '(')
                    apply(ops.pop(), results);
                ops.pop();
            }
            else if (expr.charAt(index) == '.'){
                Exception e = new Exception();
                throw(e);
            }
            index++;
        }
        while (!ops.empty())
            apply(ops.pop(), results);
        return results.pop();
    }

    public static boolean isdigit(char item){
        if ( item >= '0' && item <= '9'){
            return true;
        }
        return false;
    }

    public void flash(View view) {
        boolean FlashCapable = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (FlashCapable) {
            if (isFlashing) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    cam.stopPreview();
                    cam.release();
                    isFlashing = false;
                }
            } else if (!isFlashing) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    cam = Camera.open();
                    Parameters p = cam.getParameters();
                    p.setFlashMode(Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    isFlashing = true;
                }
            }
        }
        else{
            Toast.makeText(MainActivity.this, "Flash not available on phone!", Toast.LENGTH_SHORT).show();
        }
    }

}
