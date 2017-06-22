package com.xn.uiframe.layout;

import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xn.uiframe.R;
import com.xn.uiframe.exception.UIFrameLayoutAlreadyExistException;
import com.xn.uiframe.interfaces.IContainerManager;
import com.xn.uiframe.interfaces.IHeaderViewBehavior;

/**
 * <p>
 * 定义一个基本视图布局管理器 HeaderLayoutManager.
 * 负责展示一个页面的头部，该头部可能包括返回图标，文件等信息. 具体内容取决于addLayout传进来的res布局文件.
 * <br>
 * 基本视图是指组成界面的各个基本元素的布局,在这个框架中主要定义了几个如下几个基本视图:
 * 1.HeaderLayoutManager
 * 2.TopLayoutManager
 * 3.CenterLayoutManager
 * 4.BottomLayoutManager
 * 基它全屏类型的视图包括: Dialog,LoadView,ErrorView,ExtraView(备用全屏视图)
 * 这几个全屏视图都通过 FullScreenLayoutManager 来实现，只需要给定它的类型参数指定它属于哪个视图类型;
 * <p>
 * Created by 陈真 on 2017/6/12.
 * Copyright © 2015 深圳市小牛在线互联网信息咨询有限公司 股东公司：深圳市小牛互联网金融服务有限公司 版权所有 备案号：粤ICP备14079927号  ICP证粤B2-20160194
 * </p>
 */

public class HeaderLayoutManager extends AbstractLayoutManager implements IHeaderViewBehavior, View.OnClickListener {

    private OnHeaderViewClickListener mOnHeaderViewClickListener;

    public HeaderLayoutManager(IContainerManager mContainerManager) {
        super(mContainerManager);
        this.mLayer = Layer.LAYER_BASIC_HEADER_PART;
    }

    @Override
    public void onLayout(int left, int top, int right, int bottom) {
        for(View view: mViewCollections) {
            /**如果不可见，则对该布局不进行处理;**/
            if (view.getVisibility() != View.VISIBLE) {
                continue;
            }

            /**获得当前布局的Margin参数**/
            ViewGroup.MarginLayoutParams marginLayoutParams = getMarginLayoutParams();
            int leftMargin = marginLayoutParams.leftMargin;
            int rightMargin = marginLayoutParams.rightMargin;
            int topMargin = marginLayoutParams.topMargin;
            view.layout(left + leftMargin, top + topMargin, right - rightMargin, top + topMargin + view.getMeasuredHeight());
        }
    }

    /**
     * 根据给定的布局文件，在容器中添加一个视图，并返回当前这个视图对象;
     * 如果容器中已经存在该类型的视图，则不充许再次添加.
     *
     * @param containerLayout 当前界面的顶层容器
     * @param layout          需要添加的布局文件
     * @return 布局文件加载后的视图布局Manager对象
     */
    public static HeaderLayoutManager buildLayoutManager(IContainerManager containerLayout, @LayoutRes int layout) {
        HeaderLayoutManager header = new HeaderLayoutManager(containerLayout);
        if (containerLayout.contains(header)) {
            throw new UIFrameLayoutAlreadyExistException("Header视图已经添加到容器当中了，该视图不能重复添加.");
        } else {
            header.addLayout(layout);
            header.setHeaderClickListener();
            containerLayout.addLayoutManager(header);
        }
        return header;
    }


    /**
     * 根据给定的布局文件，在容器中添加一个视图，并返回当前这个视图对象;
     * 如果容器中已经存在该类型的视图，则不充许再次添加.
     *
     * @param containerLayout 当前界面的顶层容器
     * @return 布局文件加载后的视图布局Manager对象
     */
    public static HeaderLayoutManager buildLayoutManager(IContainerManager containerLayout) {
        return buildLayoutManager(containerLayout, R.layout.ui_frame_common_header_layout);
    }


    @Override
    public TextView setHeaderLeftText(int resource) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_left);
        textView.setText(resource);
        return textView;
    }

    @Override
    public TextView setHeaderLeftImage(int resource) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_left);
        Drawable drawable = textView.getContext().getResources().getDrawable(resource);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        return textView;
    }

    @Override
    public TextView setHeaderCenterText(int resource) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_center);
        textView.setText(resource);
        return textView;
    }

    @Override
    public TextView setHeaderRightText(int resource) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_right);
        textView.setText(resource);
        return textView;
    }

    @Override
    public TextView setHeaderRightImage(int resource) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_right);
        Drawable drawable = textView.getContext().getResources().getDrawable(resource);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
        return textView;
    }

    @Override
    public void setOnHeaderClickLister(OnHeaderViewClickListener lister) {
        this.mOnHeaderViewClickListener = lister;
    }

    private void setHeaderClickListener() {
        View view = getContentView();
        TextView right = (TextView) view.findViewById(R.id.ui_frame_header_right);
        TextView left = (TextView) view.findViewById(R.id.ui_frame_header_left);
        if (right != null) {
            right.setOnClickListener(this);
        }
        if (left != null) {
            left.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (this.mOnHeaderViewClickListener == null) {
            return;
        }
        if (v.getId() == R.id.ui_frame_header_left) {
            this.mOnHeaderViewClickListener.onLeftHeaderClicked();
        } else {
            this.mOnHeaderViewClickListener.onRightHeaderClicked();
        }
    }

    public interface OnHeaderViewClickListener {
        void onLeftHeaderClicked();

        void onRightHeaderClicked();
    }

    @Override
    public TextView setHeaderLeftText(String content) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_left);
        textView.setText(content);
        return textView;
    }

    @Override
    public TextView setHeaderRightText(String content) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_right);
        textView.setText(content);
        return textView;
    }

    @Override
    public TextView setHeaderCenterText(String content) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_center);
        textView.setText(content);
        return textView;
    }

    @Override
    public TextView setHeaderLeftDrawable(Drawable drawable) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_left);
        textView.setCompoundDrawables(drawable, null, null, null);
        return textView;
    }

    @Override
    public TextView setHeaderRightDrawable(Drawable drawable) {
        View view = getContentView();
        TextView textView = (TextView) view.findViewById(R.id.ui_frame_header_right);
        textView.setCompoundDrawables(null, null, drawable, null);
        return textView;
    }

}