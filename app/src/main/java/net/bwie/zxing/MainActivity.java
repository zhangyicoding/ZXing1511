package net.bwie.zxing;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.encoding.EncodingHandler;

/**
 * 结论：二维码 == 字符串
 * 1、识别二维码（扫码）：Decode解码，图片转文字
 * 2、生成二维码：Encode编码，文字转图片
 * <p>
 * 1、导包
 * 2、添加权限
 * 3、导入源码，原则：缺啥补啥
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText mContentEt;
    protected Button mEncodingQrCodeBtn;
    protected Button mDecodingQrCodeBtn;
    protected ImageView mQrCodeIv;
    protected TextView mResultTv;

    // 扫码界面请求码
    public static final int SCAN_QRCODE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mContentEt = (EditText) findViewById(R.id.content_et);
        mEncodingQrCodeBtn = (Button) findViewById(R.id.encoding_qr_code_btn);
        mEncodingQrCodeBtn.setOnClickListener(MainActivity.this);
        mDecodingQrCodeBtn = (Button) findViewById(R.id.decoding_qr_code_btn);
        mDecodingQrCodeBtn.setOnClickListener(MainActivity.this);
        mQrCodeIv = (ImageView) findViewById(R.id.qr_code_iv);
        mResultTv = (TextView) findViewById(R.id.result_tv);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.encoding_qr_code_btn) {// 生成二维码
            String content = mContentEt.getText().toString();
            generateQRCode(content);
        } else if (view.getId() == R.id.decoding_qr_code_btn) {// 扫码
            scanQRCode();
        }
    }

    // 生成二维码
    private void generateQRCode(String content) {
        // 将文字转换为图片，即Bitmap
        try {
            String qrCode = new String(content.getBytes("UTF-8"), "ISO-8859-1");
            Bitmap qrCodeBitmap = EncodingHandler.createQRCode(qrCode, 512);
            mQrCodeIv.setImageBitmap(qrCodeBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 扫码
    private void scanQRCode() {
        // 跳转二维码扫描界面
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, SCAN_QRCODE_REQUEST_CODE);
    }

    // 接收扫码界面回传的文本内容
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SCAN_QRCODE_REQUEST_CODE:
                Bundle bundle = data.getExtras();
                String result = bundle.getString("qr_scan_result");// ZXing提供的key不可改变
                mResultTv.setText(result);
                break;
        }
    }
}
