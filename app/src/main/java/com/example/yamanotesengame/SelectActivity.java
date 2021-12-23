package com.example.yamanotesengame;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SelectActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "";
    TextView textView2;
    public String odai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setText("お題を選択してください");
    }


    public void SelectOdai(View t) {
        switch (t.getId()) {
            case R.id.odai1:
                odai = "テスト";
                break;
            case R.id.odai2:
                odai = "2";
                break;
            case R.id.odai3:
                odai = "3";
                break;
            case R.id.odai4:
                odai = "4";
                break;
        }

        textView2.setText("選択中のお題:" + odai);
    }

    // お題決定ボタン
    public void decideOdai(View t) {
        if (odai != null){
            // 選択されたお題をPlayActivityへ持っていく
            Intent intent = new Intent(SelectActivity.this, PlayActivity.class);
            intent.putExtra(EXTRA_MESSAGE, odai);
            startActivity(intent);
        }
        else{
            // お題が選択されていない場合
            textView2.setText("お題が選択されていません");
        }


    }


}