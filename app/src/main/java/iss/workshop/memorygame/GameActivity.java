package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


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