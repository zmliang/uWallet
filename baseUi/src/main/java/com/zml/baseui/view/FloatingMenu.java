package com.zml.baseui.view;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.zml.baseui.R;

public class FloatingMenu extends FrameLayout {

    private static final String STATIC_VIEW = "StaticView";
    private static final float DEFAULT_ELEVATION = 10f;
    private static final int DEFAULT_TEXT_SIZE = 14;

    private FrameLayout mCoverView;
    private ImageView mCoverImageView;
    private TextView mCoverTextView;
    private int mSubMenuBackgroundResource;
    private Interpolator mAnimationInterpolator;
    private int mAnimationDuration = 400;
    private int mSubMenuBetweenPadding = 300;
    private int mMenuExtraMargin = 0;

    private boolean mIsOpen;
    private boolean mLockedCLick;

    private MenuClickListener mOnMenuClickListener;

    private float mMenuElevation = DEFAULT_ELEVATION;
    private float mTextSize = DEFAULT_TEXT_SIZE;
    private int mTextColor;

    private int mOriginalWidth;
    private int mOriginalHeight;

    public interface MenuClickListener {

        public void onMenuClick(View coverView, boolean isOpen);
    }

    public FloatingMenu(Context context) {
        super(context);
        init(context, null);
    }

    public FloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        setBackground(null);
        mAnimationInterpolator = new AccelerateDecelerateInterpolator();


        Drawable menuBackground = null;
        int transparentColor = getResources().getColor(android.R.color.transparent);
        mTextColor = transparentColor;

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FloatingMenu, 0, 0);
            try {
                String menuTitle = a.getString(R.styleable.FloatingMenu_fmText);
                mTextColor = a.getColor(R.styleable.FloatingMenu_fmTextColor, transparentColor);
                int menuIcon = a.getResourceId(R.styleable.FloatingMenu_fmIcon, transparentColor);
                int backgroundResource = a.getResourceId(R.styleable.FloatingMenu_fmBackground, 0);
                menuBackground = backgroundResource != 0 ? ContextCompat.getDrawable(context, backgroundResource) : null;
                mMenuElevation = a.getDimension(R.styleable.FloatingMenu_fmElevation, DEFAULT_ELEVATION);
                mTextSize = a.getDimensionPixelSize(R.styleable.FloatingMenu_fmTextSize, DEFAULT_TEXT_SIZE);

                createCoverView(menuIcon, menuTitle, menuBackground);

            } finally {
                a.recycle();
            }
        } else {
            createCoverView(transparentColor, null, null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();


        ((ViewGroup) getParent()).setClipChildren(false);
        ((ViewGroup) getParent()).setClipToPadding(false);


        if (mOriginalWidth == 0 && mOriginalHeight == 0) {
            mOriginalWidth = getLayoutParams().width;
            mOriginalHeight = getLayoutParams().height;
        }

        for (int i = 0; i < getChildCount(); i++) {
            centralizeSubmenu(getChildAt(i));
        }


        setCoverViewLayoutParams();

        getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
    }


    private void centralizeSubmenu(View view) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 0, 0, mCoverView.getHeight() / 2 - view.getHeight() / 2);
        view.setLayoutParams(params);
    }


    public FloatingMenu addSubMenu(int iconId, OnClickListener onClickListener) {
        ImageButton subMenu = new ImageButton(getContext());
        addSubMenu(subMenu, onClickListener);
        subMenu.setImageResource(iconId);

        return this;
    }


    public FloatingMenu addSubMenu(CharSequence text, int textColor, OnClickListener onClickListener) {
        TextView subMenu = new TextView(getContext());
        subMenu.setGravity(Gravity.CENTER);
        addSubMenu(subMenu, onClickListener);
        subMenu.setText(text);
        subMenu.setTextColor(textColor);

        return this;
    }


    private void addSubMenu(View view, OnClickListener onClickListener) {
        view.setBackground(ContextCompat.getDrawable(getContext(), mSubMenuBackgroundResource));
        view.setOnClickListener(onClickListener);
        centralizeSubmenu(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(mMenuElevation - 2);
        }
        addView(view);
    }


    OnClickListener mExpandCollapseClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /**
             * Prevents the user from opening and closing frenetically the menu. The next action will only happen when the previous one is finished
             */
            if (mLockedCLick) {
                return;
            }
            mLockedCLick = true;

            if (mOnMenuClickListener != null) {
                mOnMenuClickListener.onMenuClick(mCoverView, mIsOpen);
            }

            /**
             * Performs a translation animation for each submenu
             */
            int distance = mMenuExtraMargin;
            if(getChildCount() == 1) { //No animation
                mLockedCLick = false;
            }
            for (int i = getChildCount() - 1; i >= 0; i--) {
                Object tag = getChildAt(i).getTag();
                if (tag != null && tag.toString().equals(STATIC_VIEW)) {
                    continue;
                }
                distance += mSubMenuBetweenPadding;
                ObjectAnimator anim = null;
                if (mIsOpen) {
                    anim = ObjectAnimator.ofFloat(getChildAt(i), "translationY", -distance, 0);
                } else {
                    anim = ObjectAnimator.ofFloat(getChildAt(i), "translationY", 0, -distance);
                }
                anim.setInterpolator(mAnimationInterpolator);
                anim.setDuration(mAnimationDuration);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLockedCLick = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                anim.start();
            }
            mIsOpen = !mIsOpen;
        }
    };


    private void createCoverView(int menuIcon, String menuTitle, Drawable menuBackground) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mCoverView = (FrameLayout) inflater.inflate(R.layout.cover_view, null);

        setCoverViewLayoutParams();

        mCoverImageView = (ImageView) mCoverView.findViewById(R.id.floating_menu_image);

        if (menuIcon != 0) {
            mCoverImageView.setImageResource(menuIcon);
        }

        mCoverTextView = (TextView) mCoverView.findViewById(R.id.floating_menu_text);

        if (menuTitle != null) {
            mCoverTextView.setText(menuTitle);
        }
        mCoverTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mCoverTextView.setTextColor(mTextColor);

        mCoverView.setOnClickListener(mExpandCollapseClickListener);
        mCoverView.setBackground(menuBackground);
        mCoverView.setTag(STATIC_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCoverView.setElevation(mMenuElevation);
        }
        addView(mCoverView);
    }


    private void setCoverViewLayoutParams() {
        FrameLayout.LayoutParams params = null;
        if (mOriginalWidth != 0 && mOriginalHeight != 0) {
            params = new FrameLayout.LayoutParams(mOriginalWidth, mOriginalHeight);
        } else {
            params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
        params.gravity = Gravity.CENTER | Gravity.BOTTOM;

        mCoverView.setLayoutParams(params);
    }


    public FloatingMenu setSubMenuBackgroundResource(int subMenuBackgroundResource) {
        mSubMenuBackgroundResource = subMenuBackgroundResource;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getTag() == null || !child.getTag().toString().equals(STATIC_VIEW)) {
                child.setBackground(ContextCompat.getDrawable(getContext(), subMenuBackgroundResource));
            }
        }

        return this;
    }


    public void removeSubmenu(int position) {
        removeViewAt(position + 1);
    }


    public void removeAllSubmenus() {
        int childCount = getChildCount();
        for(int i = childCount - 1; i > 0; i--) {
            removeViewAt(i);
        }
    }


    public int getSubMenuBackground() {
        return mSubMenuBackgroundResource;
    }


    public FloatingMenu setMenuBackground(Drawable menuBackground) {
        mCoverView.setBackground(menuBackground);
        return this;
    }


    public Drawable getMenuBackground() {
        return mCoverView.getBackground();
    }


    public FloatingMenu setMenuIcon(int menuIcon) {
        mCoverImageView.setImageResource(menuIcon);
        return this;
    }



    public FloatingMenu setMenuText(CharSequence menuText) {
        mCoverTextView.setText(menuText);
        return this;
    }


    public FloatingMenu setMenuTextColor(int menuTextColor) {
        mCoverTextView.setTextColor(menuTextColor);
        return this;
    }


    public FloatingMenu setMenuTextSize(int menuTextSize) {
        mCoverTextView.setTextSize(menuTextSize);
        return this;
    }


    public FloatingMenu setAnimationInterpolator(Interpolator animationInterpolator) {
        mAnimationInterpolator = animationInterpolator;
        return this;
    }


    public FloatingMenu setAnimationDuration(int animationDuration) {
        mAnimationDuration = animationDuration;
        return this;
    }


    public FloatingMenu setSubMenuBetweenPadding(int subMenuBetweenPadding) {
        mSubMenuBetweenPadding = subMenuBetweenPadding;
        return this;
    }


    public FloatingMenu setMenuExtraMargin(int menuExtraMargin) {
        mMenuExtraMargin = menuExtraMargin;
        return this;
    }


    public FloatingMenu setMenuElevation(float menuElevation) {
        mMenuElevation = menuElevation;
        return this;
    }


    public FloatingMenu setOnMenuClickListener(MenuClickListener onMenuClickListener) {
        this.mOnMenuClickListener = onMenuClickListener;
        return this;
    }


}
