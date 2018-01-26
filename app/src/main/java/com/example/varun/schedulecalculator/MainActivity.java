package com.example.varun.schedulecalculator;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements OnClickListener, TextView.OnEditorActionListener {

    private EditText nominalEditText;
    private EditText optimisticEditText;
    private EditText pessimisticEditText;
    private TextView mean_resultTextView;
    private TextView sd_resultTextView;
    private Button meanButton;

    private Button clearButton;

    private String nominalValue = "";
    private String optimisticValue = "";
    private String pessimisticValue = "";

    private float sd_Value,mean_Value;

    float mean , sd, nominal, optimistic, pessimistic;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //getting references
        nominalEditText = (EditText) findViewById(R.id.nominalEditText);
        optimisticEditText = (EditText) findViewById(R.id.optimisticEditText);
        pessimisticEditText = (EditText) findViewById(R.id.pessimisticEditText);
        mean_resultTextView = (TextView) findViewById(R.id.meanresultTextView);
        sd_resultTextView = (TextView) findViewById(R.id.sdresultTextView);
        meanButton = (Button)findViewById(R.id.calculateButton);

        clearButton = (Button) findViewById(R.id.clearButton);

        //Implementing Listeners
        pessimisticEditText.setOnEditorActionListener(this);
        meanButton.setOnClickListener(this);

        clearButton.setOnClickListener(this);

        //Getting SharedPreferences Object
        savedValues = getSharedPreferences("SavedValues",MODE_PRIVATE);

    }


    public void onClick(View v){
        switch(v.getId()){
            case R.id.calculateButton:
                Calculate();
                break;
            case R.id.clearButton:
                nominalEditText.setText("");
                optimisticEditText.setText("");
                pessimisticEditText.setText("");
                mean_resultTextView.setText("");
                sd_resultTextView.setText("");
                break;
        }

    }
    @Override
    public  void onPause(){
        Editor editor = savedValues.edit();
        editor.putFloat("mean_Value",mean);
        editor.putFloat("sd_Value",sd);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        sd_Value = savedValues.getFloat("mean_Value",mean);
        mean_Value = savedValues.getFloat("sd_Value",sd);
        Calculate();
    }

    public void Calculate(){
        nominalValue = nominalEditText.getText().toString();
        optimisticValue = optimisticEditText.getText().toString();
        pessimisticValue = pessimisticEditText.getText().toString();


        if (nominalValue.equals("") && optimisticValue.equals("") && pessimisticValue.equals("")){
            Toast.makeText(MainActivity.this,"Input all Values",Toast.LENGTH_SHORT).show();
        }
        else {
            nominal = Float.parseFloat(nominalValue);
            optimistic = Float.parseFloat(optimisticValue);
            pessimistic = Float.parseFloat(pessimisticValue);

        }

        mean = (optimistic + 4 * nominal + pessimistic) / 6 ;
        sd = (pessimistic - optimistic) / 6;

        sd_resultTextView.setText(String.format("%.1f",sd));
        mean_resultTextView.setText(String.format("%.1f",mean));



    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE){
            Calculate();
        }
        return false;
    }
}
