package com.zml.baseui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.zml.baseui.DensityUtil;
import com.zml.baseui.R;

public class ExpendEditView extends LinearLayout implements View.OnClickListener {
    public final static int AREA_RIGHT_ICON = 1;
    public final static int AREA_TITLE = 2;
    public final static int AREA_CONTENT = 3;

    
    private OnAreaClickListener onViewClickListener;

    private TextView mTitleView;


    private Paint mPaint;


    private Bitmap contentRightIcon = null;//右边
    
    private Bitmap contentLeftIcon = null;

    private EditText mContentEdit;

    private boolean inExpendedStatus = false;

    ValueAnimator animator;

    private int titleHeight =-1;

    private int maxHeight = -1;

    private int contentTopMargin = DensityUtil.getPixels(5,getContext());

    private int minHeight = DensityUtil.getPixels(120,getContext());

    private Drawable leftDrawable;

    private String title;

    private String subTitle;

    private String hint;

    private boolean expandedEnabled = true;

    private Drawable contentBg ;

    private int contentIconMargin = DensityUtil.getPixels(20,getContext());

    private RectF rightIconRect = new RectF();

    private boolean contentEditable = true;

    private int contentTextSize;

    private int contentVerticalGravity = 1;

    public ExpendEditView(Context context) {
        this(context,null);
    }

    public ExpendEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ExpendEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this._init(context,attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        Log.i("zml"," mode="+heightMode+"; height="+height);
        if (heightMode == MeasureSpec.AT_MOST){
            height = Math.min(height,minHeight)+contentTopMargin;
            Log.i("zml","取值="+height);
            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),height);
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    height, MeasureSpec.EXACTLY);
            measureChild(mTitleView,widthMeasureSpec,newHeightMeasureSpec);
            measureChild(mContentEdit,widthMeasureSpec,MeasureSpec.makeMeasureSpec(
                    height-mTitleView.getMeasuredHeight()-contentTopMargin, MeasureSpec.EXACTLY));

        }else {

            setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),height);
            int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                    height, MeasureSpec.EXACTLY);
            measureChild(mTitleView,widthMeasureSpec,newHeightMeasureSpec);
            measureChild(mContentEdit,widthMeasureSpec,MeasureSpec.makeMeasureSpec(
                    height-mTitleView.getMeasuredHeight()-contentTopMargin, MeasureSpec.EXACTLY));
        }

        //measureChild(mContentEdit,widthMeasureSpec,heightMeasureSpec);
        if (titleHeight == -1){
            titleHeight = mTitleView.getMeasuredHeight();
        }

        contentRightIcon =this.scaleIcon(contentRightIcon);
        contentLeftIcon = this.scaleIcon(contentLeftIcon);

        boolean hasLeftIcon = contentLeftIcon!=null;
        int left = DensityUtil.getPixels(5,getContext());

        if (hasLeftIcon){
            left+=(contentLeftIcon.getWidth()-contentIconMargin/2+contentLeftIcon.getWidth());
        }

        boolean hasRightIcon = contentRightIcon!=null;
        int right = hasRightIcon ? contentRightIcon.getWidth()+left : left;

        mContentEdit.setPadding(left,0,right,0);

        //Log.i("zml","height="+getMeasuredHeight()+"; title="+titleHeight+"; content="+mContentEdit.getMeasuredHeight());
    }
    
    private Bitmap scaleIcon(Bitmap bmp){
        if (bmp!=null){
            //缩放
            int bmp_height = mContentEdit.getMeasuredHeight()-contentIconMargin;
            return Bitmap.createScaledBitmap(bmp,bmp_height,bmp_height,true);
        }
        return null;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void _init(Context context, @Nullable AttributeSet attrs){

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ExpendEditView);
        title = typedArray.getString(R.styleable.ExpendEditView_title);
        leftDrawable = typedArray.getDrawable(R.styleable.ExpendEditView_title_left_drawable);
        
        minHeight = typedArray.getDimensionPixelSize(R.styleable.ExpendEditView_content_min_height,DensityUtil.getPixels(20,context));
        maxHeight = typedArray.getDimensionPixelSize(R.styleable.ExpendEditView_content_max_height,DensityUtil.getPixels(150,context));
        hint = typedArray.getString(R.styleable.ExpendEditView_content_hint);
        expandedEnabled = typedArray.getBoolean(R.styleable.ExpendEditView_expanded_enabled,true);
        contentEditable = typedArray.getBoolean(R.styleable.ExpendEditView_content_editable,true);
        subTitle = typedArray.getString(R.styleable.ExpendEditView_sub_title);
        contentTextSize = typedArray.getDimensionPixelSize(R.styleable.ExpendEditView_content_text_size,12);

        int inputType = typedArray.getInt(R.styleable.ExpendEditView_content_input_type,2);

        int _hGravity = typedArray.getInt(R.styleable.ExpendEditView_content_vertical_gravity,1);
        if (_hGravity == 1){
            contentVerticalGravity = Gravity.TOP;
        }else if (_hGravity == 2){
            contentVerticalGravity = Gravity.CENTER_VERTICAL;
        }else if (_hGravity == 3){
            contentVerticalGravity = Gravity.BOTTOM;
        }

        BitmapDrawable bd = (BitmapDrawable) typedArray.getDrawable(R.styleable.ExpendEditView_edit_right_drawable);
        if (bd!=null){
            contentRightIcon = bd.getBitmap();
        }

        BitmapDrawable leftD = (BitmapDrawable) typedArray.getDrawable(R.styleable.ExpendEditView_edit_left_drawable);
        if (leftD!=null){
            contentLeftIcon = leftD.getBitmap();
        }

        contentBg = typedArray.getDrawable(R.styleable.ExpendEditView_content_bg);

        typedArray.recycle();

        if (contentBg == null){
            contentBg = context.getDrawable(R.drawable.edit_bg);
        }

        if (!expandedEnabled){
            inExpendedStatus = true;
        }


        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        setOrientation(LinearLayout.VERTICAL);

        //title
        mTitleView = new TextView(context);
        if (title!=null){
            mTitleView.setText(title);
        }
        mTitleView.setGravity(Gravity.CENTER_VERTICAL);

        if (leftDrawable!=null){
            Paint paint = new Paint();
            Paint.FontMetrics fm = paint.getFontMetrics();
            mTitleView.setCompoundDrawablePadding(DensityUtil.getPixels(8,context));//设置图片和text之间的间距
            //int h = fm.descent ;
            //Log.i("zml","height="+leftDrawable.getIntrinsicHeight()/3);
            leftDrawable.setBounds(new Rect(0,0,leftDrawable.getIntrinsicHeight()/3, leftDrawable.getIntrinsicHeight()/3));
            mTitleView.setCompoundDrawables(leftDrawable,null,null,null);
        }
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mTitleView.setLayoutParams(params);

        mTitleView.setGravity(Gravity.LEFT);
        addView(mTitleView);


        //editText
        mContentEdit = new EditText(context);

        if (inputType == 1){
            mContentEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        }


        mContentEdit.setTextSize(contentTextSize);

        mContentEdit.setFocusable(contentEditable);


        //设置hint字体，及大小
        if (hint!=null && hint.length()>0){
            SpannableString ss = new SpannableString(hint);//定义hint的值
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(13,true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mContentEdit.setHint(new SpannedString(ss));
        }


        mContentEdit.setHintTextColor(context.getColor(R.color.gray_white_1));

        mContentEdit.setBackground(contentBg);

        LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT );

        params1.topMargin = contentTopMargin;

        params1.gravity = Gravity.LEFT;

        mContentEdit.setLayoutParams(params1);
        mContentEdit.setGravity(Gravity.LEFT|contentVerticalGravity);

        mContentEdit.setOnClickListener(this);

        addView(mContentEdit);

        mTitleView.setOnClickListener(this);

        setWillNotDraw(false);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!"".equals(subTitle) && subTitle!=null){
            mPaint.setColor(Color.LTGRAY);
            mPaint.setTextSize(25);
            mPaint.setTextAlign(Paint.Align.RIGHT);
            Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
            float baseLine = fontMetrics.descent-fontMetrics.ascent+fontMetrics.bottom;
            canvas.drawText(subTitle, getWidth()-10, baseLine, mPaint);
        }

        if (contentRightIcon!=null){
            int left = mContentEdit.getWidth()-contentRightIcon.getWidth()-contentIconMargin/2;
            int top = mTitleView.getBottom()+contentTopMargin+contentIconMargin/2;
            rightIconRect.set(left,top, left+contentRightIcon.getWidth(),top+contentRightIcon.getHeight());
            canvas.drawBitmap(contentRightIcon,
                    mContentEdit.getWidth()-contentRightIcon.getWidth()-contentIconMargin/2,
                    mTitleView.getBottom()+contentTopMargin+contentIconMargin/2,null);
        }
        
        if (contentLeftIcon!=null){
            canvas.drawBitmap(contentLeftIcon,
                    contentLeftIcon.getWidth()-contentIconMargin/2,
                    mTitleView.getBottom()+contentTopMargin+contentIconMargin/2,null);
        }

    }

    public void setContentLeftIcon(Bitmap contentLeftIcon) {
        this.contentLeftIcon = contentLeftIcon;
        invalidate();
    }

    public void setContentLeftIcon(@DrawableRes int drawableId) {
        BitmapDrawable bd = (BitmapDrawable) getContext().getDrawable(drawableId);
        this.contentLeftIcon = bd.getBitmap();
        invalidate();
    }

    @Override
    public void onClick(View v) {
        if (v==mTitleView){
            if (onViewClickListener!=null){
                onViewClickListener.onClicked(AREA_TITLE);
            }
            if (!expandedEnabled){
                return;
            }
            if (inExpendedStatus){//收起
                animLayoutParam(maxHeight,titleHeight);
            }else {//展开
                animLayoutParam(titleHeight,maxHeight);
            }
        }else if (v == mContentEdit){
            if (onViewClickListener!=null){
                onViewClickListener.onClicked(AREA_CONTENT);
            }
        }
    }

    private void animLayoutParam(int from,int to){
        if (animator!=null && animator.isRunning()){
            animator.cancel();
        }
        animator = ValueAnimator.ofInt(from, to);
        animator.setDuration( 300 );

        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(animation -> {
            ViewGroup.LayoutParams lp = getLayoutParams();
            lp.height = Integer.parseInt(animation.getAnimatedValue().toString());
            setLayoutParams(lp);
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                inExpendedStatus = !inExpendedStatus;
            }
        });

        animator.start();

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (rightIconRect!=null){
                    //Log.i("zml","down x="+ev.getX()+"; y="+ev.getY()+"; rect="+rightIconRect.toString());
                    if (rightIconRect.contains(ev.getX(),ev.getY())){//点击区域落在了右侧icon上
                        if (onViewClickListener!=null){
                            onViewClickListener.onClicked(AREA_RIGHT_ICON);
                        }
                        return true;
                    }
                }
                 break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setHint(String h){
        if (h == null){
            return;
        }
        if (this.hint.equals(h)){
            return;
        }
        this.hint = h;
        if (mContentEdit!=null){
            mContentEdit.setHint(this.hint);
        }
    }

    public String getHint(){
        return this.hint == null ? "" : this.hint;
    }

    public void setOnViewClickListener(OnAreaClickListener listener){
        this.onViewClickListener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       // Log.i("zml","自己的onTouch回调");
        return super.onTouchEvent(event);
    }

    public String getContent(){
        return mContentEdit.getText().toString();
    }

    public void setContent(String content){
        mContentEdit.setText(content);
        //invalidate();
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String value){
        this.subTitle = value;
        invalidate();
    }


    public interface OnAreaClickListener {
        void onClicked(int type);
    }

}



