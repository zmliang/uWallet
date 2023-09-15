package com.zml.baseui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BaseBottomSheetFragment extends BottomSheetDialogFragment {
    private List mShareList = new ArrayList();

    private Context context;
    private View view;
    public static BaseBottomSheetFragment newInstance(){
        return new BaseBottomSheetFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new BottomSheetDialog(getContext());
    }

    @Override
    public void onStart() {
        super.onStart();
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        dialog.getWindow().findViewById(com.google.android.material.R.id.design_bottom_sheet).setBackground(new ColorDrawable(
                Color.TRANSPARENT
        ));

        FrameLayout root = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (root!=null){
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)root.getLayoutParams();
            layoutParams.height = getPeekHeight();
            root.setLayoutParams(layoutParams);
            final BottomSheetBehavior behavior = BottomSheetBehavior.from(root);
            behavior.setPeekHeight(getPeekHeight());
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        }


    }

    protected int getPeekHeight(){
        int height = getResources().getDisplayMetrics().heightPixels;
        return height-height/4;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        //view = inflater.inflate(R.layout.layout_bottomsheet,container,false);
        initData();
        initViews(view);
        return view;
    }

    private void initViews(View view){

    }

    private void initData(){

    }

}





















