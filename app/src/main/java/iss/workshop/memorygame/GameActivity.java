package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private int coverImage = R.drawable.cover;
    private Drawable[] chosenImages = new Drawable[6]; // to be used when bitmaps are passed from MainActivity
    private int[] imageViews = {R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4,
            R.id.imageView5, R.id.imageView6, R.id.imageView7, R.id.imageView8,
            R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Set cover image to all image views
        for (int i=0; i<imageViews.length; i++) {
            ImageView imageView = findViewById(imageViews[i]);
            imageView.setImageResource(coverImage);
            // imageView.setImageResource(duplicatedImageList.get(i)); //to test the randomised list
        }

        Intent intent = getIntent();
        ArrayList<String> selectedUrls = intent.getStringArrayListExtra("urlList");
        System.out.println(selectedUrls.get(0));
        System.out.println(selectedUrls.get(1));
        System.out.println(selectedUrls.get(2));
        System.out.println(selectedUrls.get(3));
        System.out.println(selectedUrls.get(4));
        System.out.println(selectedUrls.get(5));

    }
}