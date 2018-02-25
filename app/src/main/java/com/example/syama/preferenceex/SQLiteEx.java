package com.example.syama.preferenceex;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by syama on 2018/02/03.
 */

public class SQLiteEx extends Activity implements View.OnClickListener {
    private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
    private final static int MP = ViewGroup.LayoutParams.MATCH_PARENT;
    private final static String TAG_WRITE = "write";
    private final static String TAG_READ = "read";
    private final static String DB_NAME = "test.db";//DB名
    private final static String DB_TABLE = "test";//テーブル名
    private final static int    DB_VERSION = 1;   //バージョン

    private EditText editText; //エディットテキスト
    private SQLiteDatabase db;    //データベースオブジェクト

    //アクティビティ起動時に呼ばれる
    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //レイアウトの生成
        LinearLayout layout = new LinearLayout(this);
        layout.setBackgroundColor(Color.WHITE);
        layout.setOrientation(LinearLayout.VERTICAL);
        setContentView(layout);

        //        エディットテキストの生成
        editText = new EditText(this);
        editText.setText("これはテストです");
        editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));
        layout.addView(editText);

        //ボタンの生成
        layout.addView(makeButton("書き込み", TAG_WRITE));
        layout.addView(makeButton("読み込み", TAG_READ));

        //データベースオブジェクトの取得
        DBHelper dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    //ボタンの生成
    private Button makeButton(String text, String tag){
        Button button = new Button(this);
        button.setText(text);
        button.setTag(tag);
        button.setOnClickListener(this);
        button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
        return button;
    }

    //ボタンクリックイベントの処理
    public void onClick(View v){
        String tag = (String)v.getTag();
        //DBへの書き込み
        if (TAG_WRITE.equals(tag)){//書き込みが選択された場合
            try{
                String str = editText.getText().toString();//書き込まれた内容(getText)をstrに格納
                writeDB(str);//writeDBメソッドを呼び出し、strを引数として渡す
            }catch(Exception e){//エラーの場合
                editText.setText("書き込み失敗しました");
            }
        }
        //DBからの読み込み
        else if(TAG_READ.equals(tag)){//読み込みが選択された場合
            try{
                String str = readDB();//readDBメソッドをstrに格納
                editText.setText(str);//strをeditTextで表示
            }catch(Exception e){
                editText.setText("書き込み失敗しました");
            }
        }
    }

    //データベースへの書き込み
    private void writeDB(String info) throws Exception{
        ContentValues values = new ContentValues();//値を格納するためのvaluesを宣言
        values.put("id", "0");
        values.put("info", info);
        int colNum = db.update(DB_TABLE, values, null, null);
        if(colNum == 0)db.insert(DB_TABLE, "", values);
        //上記はINSERT INTO DB_TABLE (id, 0) VALUES("info", info);と同じ処理をしている
    }

    //データベースからの読み込み
    private String readDB() throws Exception{
        Cursor c = db.query(DB_TABLE, new String[]{"id", "info"},//query()メソッドを使用してデータ検索をする
                "id='0'", null, null, null,null);
        //query()の第一引数にはテーブル名、第二引数には取得対象のカラム名を配列の形で指定する。
        //第三引数以降は取得条件(like演算子など)やgroup by句、order by句を指定する。今回はorder by句のみ指定して、あとはnull
        //検索した結果はcに格納される
        if(c.getCount() == 0)throw new Exception();//全件取得する場合は、moveToFirst()メソッドを使用
        c.moveToFirst();                           //確実にcursor内のデータ参照先を先頭に持ってくる。
        String str = c.getString(1);
        c.close();
        return str;
    }

    //データベースヘルパーの定義
    private static class DBHelper extends SQLiteOpenHelper {
        //データベースヘルパーのコンストラクタ
        public DBHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION); //DB名、テーブル名、DBバージョンを定数として保持している
        }

        //データベースの生成
        //@Override
        public void onCreate(SQLiteDatabase db){
            db.execSQL("create table if not exists " +
                    DB_TABLE + "(id text primary key,info text)");
        }

        //データベースのアップグレード
        @Override
        public void onUpgrade(SQLiteDatabase db,int oldVersion,
                              int newVersion){
            db.execSQL("drop table if exists "+DB_TABLE);
            onCreate(db);
        }
    }
}
