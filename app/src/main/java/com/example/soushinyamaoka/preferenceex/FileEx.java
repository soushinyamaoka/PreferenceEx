package com.example.soushinyamaoka.preferenceex;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by SoushinYamaoka on 2018/02/03.
 */
//ファイルの読み書き
public class FileEx extends Activity{
    implements View.OnClickListener{
        private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;
        private final static int MP = ViewGroup.LayoutParams.MATCH_PARENT;
        private final static String TAG_WRITE = "write";
        private final static String TAG_READ= "read";
        private EditText editText;

//        アクティビティ起動時に呼ばれる
        @Override
        public void onCreate(Bundle bundle){
            super.onCreate(bundle);
            requestWindowFeature(Window.FEATURE_NO_TITLE);

//            レイアウトの生成
            LinearLayout layout = new LinearLayout(this);
            layout.setBackgroundColor(Color.WHITE);
            layout.setOrientation(LinearLayout.VERTICAL);
            setContentView(layout);

//            エディットテキストの生成
            editText = new EditText(this);
            editText.setText("これはテストです。");
            editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));
            layout.addView(editText);

//            ボタンの生成
            layout.addView(makeButton("書き込み", TAG_WRITE));
            layout.addView(makeButton("読み込み", TAG_READ));
        }

//        ボタンの生成
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
//       ファイルへの書き込み
        if(TAG_WRITE.equals(tag)){
            try{
                String str = editText.getText().toString();
                data2file(this, str.getBytes(), "test.txt");
            }catch(Exception e){
                editText.setText("書き込みに失敗しました。");
            }
        }
//        ファイルからの読み込み
        else if(TAG_READ.equals(tag)){
            try{
                String str = new String(file2data(this, "test.txt"));
                editText.setText(str);
            }catch(Exception e){
                editText.setText("読み込み失敗しました。");
            }
        }
    }
//    バイト配列→ファイル
    private static void data2file(Context context,byte[] w,
                                  String fileName)throws Exception{
        OutputStream out = null;
        try{
//          ファイル出力ストリームのオープン
            out = context.openFileOutput(fileName,Context.MODE_PRIVATE);
//            バイト配列の書き込み
            out.write(w, 0, w.length);

//            ファイル出力ストリームのクローズ
            out.close();
        }catch(Exception e){
            try{
                if(out != null)out.close();
            }catch(Exception e2){
            }
            throw e;
        }
    }
//    ファイル→バイト配列
    private static byte[] file2data(Context context,
                                    String fileName)throws Exception{
        int size;
        byte[] w = new byte[1024];
        InputStream in = null;
        ByteArrayInputStream out = null;
        try{
//          ファイル入力ストリームのオープン
            in = context.openFileInput(fileName);

//            バイト配列の読み込み
            out = new ByteArrayOutputStream();
            write(true){
                siza = in.read(w);
                if(size <= 0)break;
                out.write(w, 0, size);
            }
            out.close();

//            ファイル入力ストリームのクローズ
            in.close();

//            ByteArrayOutputStreamオブジェクトのバイト配列
            return out.toByteArray();
        }catch(Exception e){
            try{
                if(out != null)in.close();
                if(out != null)out.close();
            }catch(Exception e2){
            }
            throw e;
        }
    }
}
