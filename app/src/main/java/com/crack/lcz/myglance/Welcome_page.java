package com.crack.lcz.myglance;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by lcz on 18-4-27.
 */

public class Welcome_page extends AppCompatActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getHome();
            super.handleMessage(msg);

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome_page);
        handler.sendEmptyMessageDelayed(0,3000);

    }
    public void getHome(){
        Intent intent = new Intent(Welcome_page.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
