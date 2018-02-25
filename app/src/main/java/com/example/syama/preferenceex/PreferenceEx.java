package com.example.syama.preferenceex;

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

//プリファレンスの読み書き　プリファレンスは、データを、キー名と値の組み合わせで保存する形をとります。
//Androidアプリでは、いくつかのデータ保存方法が用意されていますが、最も簡単に扱える方法が、この「プリファレンス(Preference)」であると思います。
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
        SharedPreferences pref = getSharedPreferences(//
                "PreferencesEx", MODE_PRIVATE);
        //getSharedPreferences()の1つめの引数は生成する設定ファイルのキー（つまりファイル名のようなもの）
          //これはアプリ内で固有のものである必要があります。
        //2つめの引数はこの設定ファイルが自分のアプリ内のみからアクセス可能か、他のアプリからも読み書きが出来るかを設定する定数です。
          //MODE_WORLD_READABLE：他のアプリから読み取り可能 MODE_WORLD_WRITEABLE：他のアプリから書込み可能　MODE_PRIVATE：そのアプリケーションだけで使用可能

//        プリファレンスへの書き込み
        if(TAG_WRITE.equals(tag)){//書き込みが選択された場合
            SharedPreferences.Editor editor = pref.edit();//SharedPreferences.Editorオブジェクトを取得。メソッドを使ってデータの保存をすることができます。
            editor.putString("text", editText.getText().toString());//「text」に「editText.getText().toString()」を保存
            editor.commit();//保存を反映。commit()は同期処理でapply()は非同期処理
        }
//        プリファレンスからの読み込み
        else if(TAG_READ.equals(tag)){//読み込みが選択された場合
            editText.setText(pref.getString("text", ""));
            //1つめの引数はプリファレンスオブジェクトの取得時に指定した設定ファイルのキー
            // 2つめの引数はもしそのキーに値が存在しないときに代入する初期値
        }
    }
}
