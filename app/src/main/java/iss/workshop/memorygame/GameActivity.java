package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<Drawable> gameimages;
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
/*
        Intent intent = getIntent();
        ArrayList<String> selectedUrls = intent.getStringArrayListExtra("urlList");
        System.out.println(selectedUrls.get(0));
        System.out.println(selectedUrls.get(1));
        System.out.println(selectedUrls.get(2));
        System.out.println(selectedUrls.get(3));
        System.out.println(selectedUrls.get(4));
        System.out.println(selectedUrls.get(5));
        */


    }

    private void loadimg(){
        gameimages= new ArrayList<Drawable>();
        for(int i =0;i<6;i++){
            Intent intent= getIntent();
            byte[] byteArray = intent.getByteArrayExtra("bmp"+i);
            Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            Drawable drawable= new BitmapDrawable(getResources(),bm);
            gameimages.add(drawable);
        }
    }

}