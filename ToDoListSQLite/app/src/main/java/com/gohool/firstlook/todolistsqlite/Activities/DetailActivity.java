package com.gohool.firstlook.todolistsqlite.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.gohool.firstlook.todolistsqlite.R;

public class DetailActivity extends AppCompatActivity {

    // Declare variables
    private TextView titleDet;
    private TextView descriptionDet;
    private TextView dateAddedDet;
    private TextView dateStartedDet;
    private TextView dateFinishedDet;
    private TextView durationDet;
    private int taskID;

    // Override methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);

        titleDet = (TextView) findViewById(R.id.titleDet);
        descriptionDet = (TextView) findViewById(R.id.descriptionDet);
        dateAddedDet = (TextView) findViewById(R.id.dateAddedDet);
        dateStartedDet = (TextView) findViewById(R.id.dateStartedDet);
        dateFinishedDet = (TextView) findViewById(R.id.dateFinishedDet);
        durationDet = (TextView) findViewById(R.id.durationDet);

        // Get data from intent
        Bundle bundle = getIntent().getExtras();

        if (bundle != null)
        {
            titleDet.setText(bundle.getString("title"));
            descriptionDet.setText(bundle.getString("description"));
            dateAddedDet.setText(bundle.getString("dateAdded"));
            dateStartedDet.setText(bundle.getString("dateStarted"));
            dateFinishedDet.setText(bundle.getString("dateFinished"));
            durationDet.setText(bundle.getString("duration"));
            taskID = bundle.getInt("id");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}