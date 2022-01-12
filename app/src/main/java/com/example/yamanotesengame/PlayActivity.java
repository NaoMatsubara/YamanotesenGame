package com.example.yamanotesengame;

import static com.example.yamanotesengame.SelectActivity.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class PlayActivity extends AppCompatActivity {
    TextView textView3;
    TextView textView4;
    TextView textView5;
    TextView textView6;
    TextView textView7;
    TextView textView8;
    String odai;
    int rarry = 0;
    public HashMap<String, Integer> odaiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Intent intent = getIntent();
        // SubActivityから送られてきたEXTRA_MESSAGEを取得しodaiに格納
        odai = intent.getStringExtra(SelectActivity.EXTRA_MESSAGE);

        // SubActivityで選択されたお題を表示する
        textView3 = (TextView)findViewById(R.id.textView3);
        textView3.setText("お題: " + odai);

        textView4 = (TextView)findViewById(R.id.textView4);
        textView4.setText("認識結果");

        textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setText("判定");

        textView6 = (TextView)findViewById(R.id.textView6);
        textView6.setText("CPUの回答");

        textView7 = (TextView)findViewById(R.id.textView7);
        textView7.setVisibility(View.INVISIBLE);

        textView8 = (TextView)findViewById(R.id.textView8);
        textView8.setText("ラリー数: " + rarry);

        // テスト用のリストを作成
        HashMap<String, HashMap> odaiMap = new HashMap<>();
        HashMap<String, Integer> elementMap = new HashMap<>();

        elementMap.put("1", 0);
        elementMap.put("2",0);
        elementMap.put("3",0);

        odaiMap.put("テスト", elementMap);

        odaiList = odaiMap.get(odai);

    }

    public void record(View t){
        try {
            //インテント作成
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, Boolean.parseBoolean(RecognizerIntent.LANGUAGE_MODEL_FREE_FORM));

            // 表示させる文字列
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "音声を文字で出力します");
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 100);

            //インテント開始
            startActivityForResult(intent, 1234);
        } catch (ActivityNotFoundException e){
            textView4.setText("No Acitvity");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        boolean correct_flag = false;
        boolean match_flag = false;
        Button recordbtn = (Button)PlayActivity.this.findViewById(R.id.recordbtn);

        // 自分が投げたインテントであれば応答する
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            // 全ての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (speechToChar.size() > 0) {
                String spokenString = "";

                // 認識結果の正誤判定
                // 認識結果のリストに正解の単語が含まれていたらmatch_flagをtrueにする
                // その単語が既に使われていなければcorrect_flagをtrueにする
                for(String i : speechToChar){
                    spokenString = i;
                    if(odaiList.containsKey(i)){
                        match_flag = true;
                        rarry += 1;
                        textView8.setText("ラリー数: " + rarry);

                        if (odaiList.get(i) == 0){
                            correct_flag = true;
                            textView5.setText("正解");
                            odaiList.put(i, 1);
                        }else{
                            textView5.setText("既に回答された単語です");
                        }
                        break;
                    }
                }

                // 認識結果のリストの中に正解の単語と一致するものがない場合
                if(match_flag == false){
                    textView5.setText("不正解");
                }

                // 認識した単語を表示
                textView4.setText("認識結果: " + spokenString);

                // 認識結果のリストを表示
                //textView4.setText("結果: " + speechToChar);

                // 回答が正解だったらCPUの回答を決定し、不正解であれば回答ボタンを非表示にする
                if(correct_flag == true) {
                    textView6.setText("考え中・・・");
                    // 3秒ディレイ
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            decideCpuAns();
                        }
                    }, 5000);
                }else{
                    textView6.setText("CPUの回答");
                    // 回答ボタンの非表示
                    recordbtn.setVisibility(View.INVISIBLE);
                    textView7.setText("あなたの負けです");
                    textView7.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //終了画面へ移動
    public void moveFinish(View t){
        Intent intent = new Intent(PlayActivity.this, EndActivity.class);
        intent.putExtra(EXTRA_MESSAGE, odai);
        startActivity(intent);

    }

    // コンピューターの回答を決定する
    public void decideCpuAns(){
        List<String> randOdai = new ArrayList<>(odaiList.keySet());
        Random random = new Random();
        String cpuAns = randOdai.get(random.nextInt(randOdai.size()));
        Button recordbtn = (Button)PlayActivity.this.findViewById(R.id.recordbtn);

        textView6.setText("CPUの回答: " + cpuAns);

        // CPUの回答の正誤判定
        if (odaiList.get(cpuAns) == 0) {
            odaiList.put(cpuAns, 1);
            rarry += 1;
            textView8.setText("ラリー数: " + rarry);
        }else{
            textView7.setText("CPUの負けです");
            textView7.setVisibility(View.VISIBLE);
            // 回答ボタンの非表示
            recordbtn.setVisibility(View.INVISIBLE);

        }
    }
}