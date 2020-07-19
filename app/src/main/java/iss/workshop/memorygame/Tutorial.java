package iss.workshop.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Tutorial extends AppCompatActivity implements View.OnClickListener {

    int clickcounter= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        TextView nextbutton= (TextView) findViewById(R.id.Next);
        nextbutton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.Next) {
            if (clickcounter == 0) {
                TextView pagetitle = (TextView) findViewById(R.id.pagetitle);
                pagetitle.setText("Match Images");
                ImageView tutorialimage = (ImageView) findViewById(R.id.Tuorialimage);
                tutorialimage.setImageResource(R.drawable.tutorialpage2);
                TextView nextbutton = (TextView) findViewById(R.id.Next);
                nextbutton.setText("Done");
                clickcounter++;
            } else if (clickcounter == 1) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        }
    }
}