package com.gohool.firstlook.unitconvertapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String number = null;

    // Declare buttons
    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
            btn_8, btn_9, btn_0, btn_dot, btn_delete, btn_convert;

    // Declare text views
    private TextView inputView, outputView;

    // Declare spinners
    private Spinner from;
    private Spinner to;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize button
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_0 = findViewById(R.id.btn_0);
        btn_dot = findViewById(R.id.btn_dot);
        btn_delete = findViewById(R.id.btn_delete);
        btn_convert = findViewById(R.id.btn_convert);

        // Initialize text views
        inputView = findViewById(R.id.inputView);
        outputView = findViewById(R.id.outputView);

        // Initialize spinners
        from = (Spinner) findViewById(R.id.spinnerFrom);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        from.setAdapter(adapter);

        to = (Spinner) findViewById(R.id.spinnerTo);
        adapter = ArrayAdapter.createFromResource(this, R.array.spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        to.setAdapter(adapter);

        // OnClick button
        btn_1.setOnClickListener(v -> numberClick("1"));
        btn_2.setOnClickListener(v -> numberClick("2"));
        btn_3.setOnClickListener(v -> numberClick("3"));
        btn_4.setOnClickListener(v -> numberClick("4"));
        btn_5.setOnClickListener(v -> numberClick("5"));
        btn_6.setOnClickListener(v -> numberClick("6"));
        btn_7.setOnClickListener(v -> numberClick("7"));
        btn_8.setOnClickListener(v -> numberClick("8"));
        btn_9.setOnClickListener(v -> numberClick("9"));
        btn_0.setOnClickListener(v -> numberClick("0"));
        btn_dot.setOnClickListener(v -> numberClick("."));

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = number.substring(0, number.length() - 1);
                inputView.setText(number);
            }
        });



        from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectItemFrom = parent.getItemAtPosition(position).toString();

                to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selectItemTo = parent.getItemAtPosition(position).toString();

                        btn_convert.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (selectItemFrom.equals("None")) outputView.setText("0");

                                if (selectItemFrom.equals("Meter"))
                                {
                                    if (selectItemTo.equals("None")) outputView.setText("0");

                                    if (selectItemTo.equals("Meter")) outputView.setText(inputView.getText());

                                    if (selectItemTo.equals("Millimetre")) {
                                        double input = Double.parseDouble(inputView.getText().toString());
                                        double output = input * 1000;
                                        outputView.setText(String.valueOf(output));
                                    }

                                    if (selectItemTo.equals("Mile")) {
                                        double input = Double.parseDouble(inputView.getText().toString());
                                        double output = input * 0.000621371;
                                        outputView.setText(String.valueOf(output));
                                    }

                                    if (selectItemTo.equals("Foot")) {
                                        double input = Double.parseDouble(inputView.getText().toString());
                                        double output = input * 3.28084;
                                        outputView.setText(String.valueOf(output));

                                    }

                                }
                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void numberClick(String view)
    {
        if (number == null)
        {
            number = view;
        }
        else
        {
            number += view;
        }
        inputView.setText(number);
    }
}