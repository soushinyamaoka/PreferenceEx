package com.example.soushinyamaoka.preferenceex;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

//プリファレンスの読み書き
public class PreferenceEx extends Activity
    implements View.OnClickListener{
    private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final static int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private final static String TAG_WRITE = "write";
    private final static String TAG_READ = "read";
    private EditText editText;

//    アクティビティ起動時に呼ばれる
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

//        レイアウトの生成
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

//        エディットテキストの生成
        editText = new EditText(this);
        editText.setText("これはテストです");
        editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));
        layout.addView(editText);

//        ボタンの生成
        layout.addView(makeButton("書き込み", TAG_WRITE));
        layout.addView(makeButton("読み込み", TAG_READ));
    }
//    ボタンの生成
    private Button makeButton(String text, String tag){
        Button button = new Button(this);
        button.setText(text);
        button.setTag(tag);
        button.setOnClickListener(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        return button;
    }
//    ボタンクリック時に呼ばれる
    public void onClick(View v){
        String tag = (String)v.getTag();
//        プリファレンスオブジェクトの取得
        SharedPreferences pref = getSharedPreferences(
                "PreferencesEx", MODE_PRIVATE);

//        プリファレンスへの書き込み
        if(TAG_WRITE.equals(tag)){
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("text", editText.getText().toString());
            editor.commit();
        }
//        プリファレンスからの読み込み
        else if(TAG_READ.equals(tag)){
            editText.setText(pref.getString("text", ""));
        }
    }
}
