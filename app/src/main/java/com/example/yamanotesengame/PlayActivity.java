package com.example.yamanotesengame;

import static com.example.yamanotesengame.SelectActivity.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.view.View;
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
    String odai;
    String ans = "";
    public HashMap<String, Integer> odaiList;
    String spokenString;

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
        textView4.setText("回答");

        textView5 = (TextView)findViewById(R.id.textView5);
        textView5.setText("判定");

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

        // 自分が投げたインテントであれば応答する
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            // 全ての結果を配列に受け取る
            ArrayList<String> speechToChar = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (speechToChar.size() > 0) {
                String spokenString = "";

                // 認証結果が複数あった場合一つに結合する
                for (int i = 0; i < speechToChar.size(); i++) {
                    spokenString += speechToChar.get(i);
                }

                textView4.setText("結果: " + speechToChar);

                for(String i : speechToChar){
                    if(odaiList.containsKey(i)){
                        correct_flag = true;

                        if (odaiList.get(i) == 0){
                            textView5.setText("正解");
                            odaiList.put(i, 1);
                        }else{
                            textView5.setText("既に回答された単語です");
                        }
                    }
                }
                if(correct_flag == false){
                    textView5.setText("不正解");
                }

                /*
                // 入力された単語がお題のMapに入っているか
                if(odaiList.containsKey(spokenString)){
                    // 入力された単語が既に言われているか
                    if(odaiList.get(spokenString) == 0){
                        textView5.setText("正解");
                        odaiList.put(spokenString, 1); // 既に言われている判定に設定

                        // CPUの回答を決定
                        List<String> randOdai = new ArrayList<>(odaiList.keySet());
                        Random random = new Random();
                        String cpuAns = randOdai.get(random.nextInt(randOdai.size()));
                        try {
                            Thread.sleep(3000);
                        } catch(InterruptedException e){
                            e.printStackTrace();
                        }

                        textView6.setText("CPUの回答: "+ cpuAns);
                        if (odaiList.get(cpuAns) == 0) {
                            odaiList.put(cpuAns, 1);
                        }else{
                            textView5.setText("CPUの負けです");
                        }

                    }else{
                        textView5.setText("既に回答済みです");
                    }
                }else{
                    textView5.setText("不正解");
                }

                 */

            }


        }
    }

    //終了画面へ移動
    public void moveFinish(View t){
        Intent intent = new Intent(PlayActivity.this, EndActivity.class);
        intent.putExtra(EXTRA_MESSAGE, odai);
        startActivity(intent);

    }

    /*
    // 音声で入力された単語の正誤を判定
    public void compareString(String spokenString){
        boolean correct_flag = false;
        // 入力された単語がお題のMapに入っているか
        if(odaiList.containsKey(spokenString)){
            // 入力された単語が既に言われているか
            if(odaiList.get(spokenString) == 0){
                textView5.setText("正解");
                odaiList.put(spokenString, 1); // 既に言われている判定に設定
                correct_flag = true;

            }else{
                textView5.setText("既に回答済みです");
            }
        }else{
            textView5.setText("不正解");
        }

        if(correct_flag == true){
            // コンピューターの回答へ移行
            decideCpuAns();
        }

    }


    // コンピューターの回答を決定する
    public void decideCpuAns(){
        List<String> randOdai = new ArrayList<>(odaiList.keySet());
        Random random = new Random();
        String cpuAns = randOdai.get(random.nextInt(randOdai.size()));
        try {
            Thread.sleep(3000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

        textView4.setText("CPUの回答: "+ cpuAns);
        if (odaiList.get(cpuAns) == 0) {
            odaiList.put(cpuAns, 1);
        }else{
            textView5.setText("CPUの負けです");
        }

    }

     */

}