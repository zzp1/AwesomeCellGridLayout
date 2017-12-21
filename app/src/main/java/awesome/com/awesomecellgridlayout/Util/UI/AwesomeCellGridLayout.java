package awesome.com.awesomecellgridlayout.Util.UI;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import java.util.ArrayList;


public class AwesomeCellGridLayout extends LinearLayout {
    /**
     * 默认行数
     */
    private int PARENT_ROW_NUM = 1;
    /**
     * 默认列数
     */
    private int PARENT_COLUMNS_NUM = 4;
    /**
     * 每个单元格的大小
     */
    private LayoutParams layoutParams = null;
    /**
     * 背景颜色
     */
    private String BackgroundColor = null;
    /**
     * 背景图片
     */
    private int res = -1;
    /**
     * 从list的第几个开始遍历
     */
    private int page = 0;
    /**
     * 每个单元格是否居中
     * 默认居左
     */
    private int gravity = Gravity.LEFT;
    /**
     * 每个单元的间距
     */
    private int[] myItemPadding = {0, 0, 0, 0};
    /**
     * 是否播放动画
     */
    private boolean isPlayAnim = true;
    /**
     * 动画速度
     */
    private int myItemAnimDuration = 400;
    /**
     * 每一个单元格的动画
     */
    private Animation myItemAnim = null;

    private Context context;
    private LinearLayout layoutY;
    private ArrayList<View> objs = new ArrayList<View>();
    private int row = 0, col = 0;

    public AwesomeCellGridLayout(Context context) {
//        super(context);
        this(context, null);
    }

    public AwesomeCellGridLayout(Context context, AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public AwesomeCellGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Awesome);
//        PARENT_ROW_NUM = a.getInt(R.styleable.Awesome_row, 1);
//        PARENT_COLUMNS_NUM = a.getInt(R.styleable.Awesome_col, 1);
//        BackgroundColor = a.getString(R.styleable.Awesome_MyItemBackgroundColor);
//        res = a.getInteger(R.styleable.Awesome_MyItemBackgroundResource, -1);
//        isPlayAnim = a.getBoolean(R.styleable.Awesome_IsPlayAnim, true);
//        myItemAnimDuration = a.getInteger(R.styleable.Awesome_MyItemAnimDuration, 400);
    }

    private void initView(Context context) {

        Activity m = (Activity) context;
        this.context = context;

        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int num = page;

        //行
        for (int x = 0; x < PARENT_ROW_NUM; x++) {
            LinearLayout layoutX = new LinearLayout(context);
            //列
            for (int y = 0; y < PARENT_COLUMNS_NUM; y++) {
                /**
                 * 如果没有View集合设置进来就去寻找他的孩子
                 */
                if (objs == null) {
                    objs = new ArrayList<View>();
                    for (int i = 0; i < getChildCount(); i++) {
                        objs.add(getChildAt(i));
                    }
                }
                /**
                 * 删除所有view开始重绘界面
                 */
                removeAllViews();
                if (num < objs.size()) {
                    layoutY = new LinearLayout(context);

                    /**
                     * 默认的大小参数
                     * 屏幕宽度除以列数
                     */
                    if (layoutParams == null) {
                        int w = m.getWindowManager().getDefaultDisplay().getWidth() / PARENT_COLUMNS_NUM;
                        layoutParams = new LayoutParams(w, ViewGroup.LayoutParams.WRAP_CONTENT);
                    }
                    /**
                     * 默认背景透明颜色
                     */
                    if (BackgroundColor == null) {
                        BackgroundColor = "#00ffffff";
                    }
                    layoutY.setBackgroundColor(Color.parseColor(BackgroundColor));
                    /**
                     * 因为BackgroundResource和BackgroundColor只能设置一个
                     * 所以默认-1的时候不设置，有设置的时候BackgroundResource会覆盖BackgroundColor
                     */
                    if (res != -1) {
                        layoutY.setBackgroundResource(res);
                    }
                    layoutY.setPadding(myItemPadding[0], myItemPadding[1], myItemPadding[2], myItemPadding[3]);
                    layoutY.setId(num);
                    layoutY.setTag("overview" + num);
                    layoutY.setGravity(gravity);
                    layoutY.setVisibility(View.INVISIBLE);

                    View t = (View) objs.get(num);
//                    t.setTag(R.id.x, x);
//                    t.setTag(R.id.y, y);

                    ViewGroup parent = (ViewGroup) t.getParent();
                    if (parent != null) {
                        parent.removeAllViews();
                        parent.removeAllViewsInLayout();
                    }

                    layoutY.setLayoutParams(layoutParams);
                    layoutY.addView(t);
                    num++;
                    layoutX.addView(layoutY);

                    /**
                     * 是否播放动画
                     */
                    if (isPlayAnim) {
                        /**
                         * 动画集合
                         * 淡入动画固定，其他动画可设置
                         * 如果没有设置就缩放动画
                         */
                        AnimationSet set = new AnimationSet(context, null);
                        set.setDuration(myItemAnimDuration);
                        set.setFillAfter(true);
                        set.setInterpolator(new OvershootInterpolator(1f));

                        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                        alphaAnimation.setDuration(myItemAnimDuration);

                        set.addAnimation(alphaAnimation);

                        /**
                         * 如果没有设置动画就用默认缩放动画
                         */
                        if (myItemAnim == null) {
                            ScaleAnimation scaleAnimation = new ScaleAnimation(0.3f, 1, 0.3f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            scaleAnimation.setDuration(myItemAnimDuration);

                            set.addAnimation(scaleAnimation);
                        } else {
                            set.addAnimation(myItemAnim);
                        }
                        /**
                         * 如果列数大于1就单个延时执行
                         * 如果是表格状就单排延时执行
                         */
                        if (PARENT_COLUMNS_NUM > 1) {
                            set.setStartOffset(y * 50);
                        } else {
                            set.setStartOffset(num * 50);
                        }
                        layoutY.startAnimation(set);

                    } else {
                        layoutY.setVisibility(View.VISIBLE);
                    }
                }
            }
            layout.addView(layoutX);
        }
        addView(layout);
    }

    public void addChlidForOrder(View view) {

    }

    /**
     * 设置行数列数
     *
     * @param row
     * @param col
     */
    public void setRowCols(int row, int col) {
        PARENT_ROW_NUM = row;
        PARENT_COLUMNS_NUM = col;
    }

    /**
     * 设置每个单元格的大小
     *
     * @param layoutParams
     */
    public void setMyItemLayoutParams(LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }

    /**
     * 设置每个单元格的背景颜色
     *
     * @param BackgroundColor
     */
    public void setMyItemBackgroundColor(String BackgroundColor) {
        this.BackgroundColor = BackgroundColor;
    }

    /**
     * 设置每个单元格的背景图片
     */
    public void setMyItemBackgroundResource(int res) {
        this.res = res;
    }

    /**
     * 设置从第几个开始遍历
     */
    public void setPageNum(int page) {
        this.page = page;
    }

    /**
     * 设置这个控件里包含的view
     *
     * @param objs
     */
    public void setViewList(ArrayList<View> objs, Context context) {
        this.objs = objs;
        initView(context);
    }

    public void setMyItemGravity(int gravity) {
        this.gravity = gravity;
    }


    public void setMyItempadding(int l, int t, int r, int b) {
        myItemPadding[0] = l;
        myItemPadding[1] = t;
        myItemPadding[2] = r;
        myItemPadding[3] = b;
    }

    public void setIsPlayAnim(boolean isPlayAnim) {
        this.isPlayAnim = isPlayAnim;
    }

    /**
     * 设置动画
     *
     * @param animation
     */
    public void setMyItemAnim(Animation animation) {
        this.myItemAnim = animation;
    }

    public void setMyItemAnimDuration(int Duration) {
        this.myItemAnimDuration = Duration;
    }

    /**
     * 更新界面
     *
     * @param context
     */
    public void updateView(Context context) {
        initView(context);
    }


    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);

    }
}
