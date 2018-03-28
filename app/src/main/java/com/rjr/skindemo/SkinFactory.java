package com.rjr.skindemo;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.LayoutInflaterFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class SkinFactory implements LayoutInflaterFactory {

    private static List<SkinView> cacheList = new ArrayList<>(); // 所有符合换肤的控件集合
    private static final String TAG = "rong";
    // 系统报名前缀
    private static final String[] prefixList = {
            "android.webkit.",
            "android.widget.",
            "android.view."
    };
    // 可能需要换肤的attr属性：background，src，textColor
    // background可用到color、drawable、mipmap资源
    private static final String BACKGROUND = "background";
    private static final String SRC = "src";
    private static final String TEXT_COLOR = "textColor";

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        // 根据name判断view的类型
        if (name.contains(".")) {
            // 可能是自定义View，v4、v7包下的view
            // 名字已经是全包名+类名
            view = createView(context, attrs, name);
        } else {
            // 该类型的view，名字不包含包名，要自己把名字拼接完整
            for (String pre : prefixList) {
                view = createView(context, attrs, pre + name);
                if (view != null) {
                    // 找对了包名，实例化成功
                    break;
                }
            }
        }
        if (view != null) {
            parseSkinView(context, attrs, view);
        }
        return view;
    }

    /**
     * 解析需要换肤的控件
     * @param context
     * @param attrs
     */
    private void parseSkinView(Context context, AttributeSet attrs, View view) {
        List<SkinItem> list = new ArrayList<>();
        for (int i = 0, count = attrs.getAttributeCount(); i < count; i++) {
            // 获取attr属性名，像layout_width之类的
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);
//            Log.i(TAG, "parseSkinView: attrName = " + attrName + ", attrValue = " + attrValue);
            // 有以下属性的控件具备换肤的条件
            // android:textColor="@color/skin_text_color"，相当于一个SkinItem
            // android:attrName="resTypeName/resEntryName"
            SkinItem skinItem;
            switch (attrName) {
                case BACKGROUND:
                case SRC:
                case TEXT_COLOR:
                    int resId = Integer.parseInt(attrValue.substring(1));
                    // 拿到资源类型名，color、drawable
                    String resTypeName = context.getResources().getResourceTypeName(resId);
                    // 通过resources拿到属性类型名：skin_text_color
                    String resEntryName = context.getResources().getResourceEntryName(resId);
                    skinItem = new SkinItem(attrName, resTypeName, resEntryName, resId);
                    list.add(skinItem);
                    Log.i(TAG, "parseSkinView: resEntryName = " + resEntryName + ", resTypeName = " + resTypeName);
                    break;
            }
        }
        if (!list.isEmpty()) {
            SkinView skinView = new SkinView(context, view, list);
            cacheList.add(skinView);
        }
    }

    /**
     * 根据包名，实例化对应的view
     * @param context
     * @param attrs
     * @param name
     * @return
     */
    private View createView(Context context, AttributeSet attrs, String name) {
        Log.i(TAG, "onCreateView: " + name);
        try {
            Class<?> viewClass = getClass().getClassLoader().loadClass(name);
            Constructor<?> constructor = viewClass.getConstructor(new Class[]{Context.class, AttributeSet.class});
            View view = (View) constructor.newInstance(context, attrs);


            return view;
        } catch (Exception e) {
            e.printStackTrace();
//            Log.e(TAG, "createView: ", e);
        }
        return null;
    }

    /**
     * 把符合换肤的属性封装成对象
     */
    class SkinItem {
        private String attrName;      // 属性名
        private String resTypeName;   // resources类型 @color、@drawable
        private String resEntryName;  // 属性值类型名 skin_text_color
        private int resId;            // resources资源id

        public SkinItem(String attrName, String resTypeName, String resEntryName, int resId) {
            this.attrName = attrName;
            this.resTypeName = resTypeName;
            this.resEntryName = resEntryName;
            this.resId = resId;
        }
    }

    /**
     * 符合换肤的控件
     */
    class SkinView {
        private Context context;
        private View view;
        private List<SkinItem> list;

        SkinView(Context context, View view, List<SkinItem> list) {
            this.context = context;
            this.view = view;
            this.list = list;
        }

        public void apply() {
            for (SkinItem item : list) {
                switch (item.attrName) {
                    case BACKGROUND:
                        if (TextUtils.equals("color", item.resTypeName)) {
                            // background:@color
                            view.setBackgroundColor(item.resId);
                        } else {
                            // background:@drawable
                            view.setBackgroundDrawable(context.getDrawable(item.resId));
                        }
                        break;
                    case TEXT_COLOR:
                        if (view instanceof TextView) {
                            ((TextView) view).setTextColor(item.resId);
                        }
                        break;
                    case SRC:
                        if (view instanceof ImageView) {
                            ((ImageView) view).setImageBitmap(BitmapFactory.decodeResource(context.getResources(), item.resId));
                        }
                        break;
                }
            }
        }
    }
}
