package com.mdb.wingit.wingit;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CategorySelectorActivity extends AppCompatActivity {
    private TextView title;
    private ImageView food, activity, arrow;
    String category = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_selector);

        String pageTitle = getIntent().getStringExtra("title");

        title = (TextView) findViewById(R.id.title);
        title.setText(pageTitle);
        food = (ImageView) findViewById(R.id.foodImage);
        activity = (ImageView) findViewById(R.id.activityImage);
        arrow = (ImageView) findViewById(R.id.arrow);

        final Intent carousel = new Intent(getApplicationContext(), CarouselActivity.class);

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "food";
                carousel.putExtra("category", category);
                startActivity(carousel);
            }
        });

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "activity";
                carousel.putExtra("category", category);
                startActivity(carousel);
            }
        });

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pastAdventures = new Intent(getApplicationContext(), PastAdventuresActivity.class);
                startActivity(pastAdventures);
            }
        });
    }
}
