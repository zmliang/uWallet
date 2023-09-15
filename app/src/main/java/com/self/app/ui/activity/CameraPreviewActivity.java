package com.self.app.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.self.app.R;
import com.self.app.databinding.ActivityScreenBinding;
import com.self.app.databinding.ScanActivityBinding;
import com.self.base.BaseViewModel;
import com.zml.zbar.Result;
import com.zml.zbar.ViewFinderView;
import com.zml.zbar.scan.ScannerView;

public class CameraPreviewActivity extends BaseLoadingActivity<BaseViewModel, ScanActivityBinding> implements ScannerView.ResultHandler {

    private static final int REQUEST_CAMERA_PERMISSION = 0;
    private ScannerView zBarScannerView;
    private Handler handler = new Handler();


    private void initView() {
        ViewGroup container = mViewDataBinding.container;

        //ViewFinderView是根据需求自定义的视图，会被覆盖在相机预览画面之上，通常包含扫码框、扫描线、扫码框周围的阴影遮罩等
        zBarScannerView = new ScannerView(this, new ViewFinderView(this), this);
        zBarScannerView.setShouldAdjustFocusArea(true);//自动调整对焦区域

        container.addView(zBarScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            zBarScannerView.startCamera();//打开系统相机，并进行基本的初始化
        } else {//没有相机权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
        zBarScannerView.stopCamera();//释放相机资源等各种资源
    }

    @Override
    public void handleResult(Result rawResult) {
        //Toast.makeText(this, "Contents = " + rawResult.getContents() + ", Format = " + rawResult.getBarcodeFormat().getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("content", rawResult.getContents());
        setResult(0, intent);
        finish();
    }

    @Override
    public void createDataObserver() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.scan_activity;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        this.initView();
        mViewDataBinding.norBarCamera.backAction.setOnClickListener(v -> finish());
    }
}