package com.example.syama.preferenceex;

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

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by SoushinYamaoka on 2018/02/03.
 */
//ファイルの読み書き
public class FileEx extends Activity implements View.OnClickListener{
        private final static int WC = ViewGroup.LayoutParams.WRAP_CONTENT;//
        private final static int MP = ViewGroup.LayoutParams.MATCH_PARENT;//
        private final static String TAG_WRITE = "write";//TAG_WRITE をやると書き込み
        private final static String TAG_READ= "read";//TAG_WRITE をやると読み込み
        private EditText editText;//テキストの追加

//        アクティビティ起動時に呼ばれる
        @Override
        public void onCreate(Bundle bundle){
            super.onCreate(bundle);
            requestWindowFeature(Window.FEATURE_NO_TITLE);//タイトルバーを非表示にする

//          レイアウトの生成
            LinearLayout layout = new LinearLayout(this);
            layout.setBackgroundColor(Color.WHITE);//背景を白に
            layout.setOrientation(LinearLayout.VERTICAL);//LinearLayoutの特徴としてorientation(方向)を決める　VERTICAL=「垂直」
            setContentView(layout);//setContentViewは、Activity上にView（画面を構成する要素）を表示するメソッド

//            エディットテキストの生成
            editText = new EditText(this);
            editText.setText("これはテストです。");//editTextに「これはテストです。」をぶちこむ
            editText.setLayoutParams(new LinearLayout.LayoutParams(MP, WC));//レイアウトのパラメーターを設定。1番目の引数に幅を、2番目の引数に高さを指定
                                                                            //何か命令を出すとき、その対象を限定するための追加命令が、プログラミングの世界でいうパラメーター
            layout.addView(editText);//レイアウト上にeditTextを生成

//            ボタンの生成
            layout.addView(makeButton("書き込み", TAG_WRITE));//「書き込み」ボタンを生成
            layout.addView(makeButton("読み込み", TAG_READ));//「読み込み」ボタンを生成
        }

//        ボタンの生成
        private Button makeButton(String text, String tag){
            Button button = new Button(this);
            button.setText(text);//ボタンにtextを設定
            button.setTag(tag);//ボタンにtagを設定
            button.setOnClickListener(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(WC, WC));
            return button;
    }

//    ボタンクリック時に呼ばれる
    public void onClick(View v){
        String tag = (String)v.getTag();
//       ファイルへの書き込み
        if(TAG_WRITE.equals(tag)){//読み込みが押された場合
            try{
                String str = editText.getText().toString();//editText.setTextを文字列で返して、strに代入。toSはインスタンスの「文字列表現」を返すメソッド。
                data2file(this, str.getBytes(), "test.txt");//data2fileに・・・
            }catch(Exception e){
                editText.setText("書き込みに失敗しました。");//エラーが起きた場合
            }
        }
//        ファイルからの読み込み
        else if(TAG_READ.equals(tag)){//読み込みの場合
            try{
                String str = new String(file2data(this, "test.txt"));
                editText.setText(str);
            }catch(Exception e){//エラーの場合
                editText.setText("読み込み失敗しました。");
            }
        }
    }
//context:アプリ全体の状態を持っていて、何から起動されたかどういう状態か、何にアクセスしようとしているか、といった情報を受け渡すために使っている。
//    バイト配列→ファイル
    private static void data2file(Context context,byte[] w,//byte型のw[],fileName
                                  String fileName)throws Exception{
        OutputStream out = null;//OutputStreamクラスはバイト単位のデータをファイルに出力する。
        try{
//          ファイル出力ストリームのオープン
            out = context.openFileOutput(fileName,Context.MODE_PRIVATE);//ファイルを作成したアプリケーションからのみ読み出せるファイルを作成する。
//            バイト配列の書き込み
            out.write(w, 0, w.length);//write(byte[] b)→指定されたバイト配列(w)からこの出力ストリームに w.length バイトを書き込みます。

//            ファイル出力ストリームのクローズ
            out.close();
        }catch(Exception e){//エラーの時
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
        byte[] w = new byte[1024];//1024バイト=1KB　※2進法
        InputStream in = null;//InputStreamクラスはバイト単位のデータをファイルに入力する
        ByteArrayOutputStream out = null;//ByteArrayOutputStreamクラスはbyte[]変数、つまりメモリを出力先とする。
        try{                             //※通常、バイト出力ストリームはファイルやソケットを出力先とする
//          ファイル入力ストリームのオープン
            in = context.openFileInput(fileName);//Contextクラスの openFileOutput/openFileInput を使うと
                                                 //ファイルの保存場所を気にすることなくファイルアクセスが可能になります(Androidではファイルの保存場所が厳格に決まっている)
//            バイト配列の読み込み
            out = new ByteArrayOutputStream();
            while(true){//読み込みデータがなくなるまで読み込み
                size = in.read(w);//入力ストリームから、あるバイト数だけ読み取り、バッファ配列にデータを格納する。実際に読み取られたバイト数が整数値で返される。読み取られるバイト数は、最大で bの長さに等しい。
                if(size <= 0)break;
                out.write(w, 0, size);//書き込むデータがなくなるまで書き込み
            }
            out.close();//書き込みストリームを閉じる

//            ファイル入力ストリームのクローズ
            in.close();//読み込みストリームを閉じる

//            ByteArrayOutputStreamオブジェクトのバイト配列
            return out.toByteArray();//戻り値:新しいバイト配列インスタンス
        }catch(Exception e){//エラーの場合
            try{
                if(out != null)in.close();
                if(out != null)out.close();
            }catch(Exception e2){
            }
            throw e;
        }
    }
}
