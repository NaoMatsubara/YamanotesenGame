package com.example.yamanotesengame;
import static com.example.yamanotesengame.SelectActivity.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class EndActivity extends AppCompatActivity {
    String odai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Intent intent = getIntent();
        odai = intent.getStringExtra(SelectActivity.EXTRA_MESSAGE);

    }

    // リトライ
    public void reTry(View t){
        Intent intent = new Intent(EndActivity.this, PlayActivity.class);
        intent.putExtra(EXTRA_MESSAGE, odai);
        startActivity(intent);
    }

    // お題選択画面へ移動
    public void reSelect(View t){
        Intent intent = new Intent(EndActivity.this, SelectActivity.class);
        startActivity(intent);

    }
}