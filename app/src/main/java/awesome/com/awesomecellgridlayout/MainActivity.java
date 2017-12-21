package awesome.com.awesomecellgridlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import awesome.com.awesomecellgridlayout.Util.UI.AwesomeCellGridLayout;

public class MainActivity extends AppCompatActivity {
    private MainActivity mMainActivity;
    private int screenWidth, screenHeight;
    private int row = 3, col = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainActivity = this;

        /**
         *  获取屏幕宽高
         */
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        /**
         *  事先循环新建需要放在表格里的控件
         */
        ArrayList<View> objs = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Button button = new Button(mMainActivity);
            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            button.setSingleLine();
            button.setText(String.valueOf(i));
            objs.add(button);
        }

        /**
         * 实例化控件
         */
        AwesomeCellGridLayout awesomeCellGridLayout = (AwesomeCellGridLayout) findViewById(R.id.acg);
        float itemCount = objs.size();
        row = (int) Math.ceil(itemCount / (float) col);//向上取整
        awesomeCellGridLayout.setRowCols(row, col); //表格的行列数,可以强制设置行列数，但这样的话多出来的控件就不会显示
        /**
         * 下面这行是设置表格里面每个单元格的大小
         * 如果需要设置控件本身大小在上面新建控件的时候设置
         */
        awesomeCellGridLayout.setMyItemLayoutParams(new LinearLayout.LayoutParams(screenWidth / row, ViewGroup.LayoutParams.WRAP_CONTENT));
        awesomeCellGridLayout.setMyItempadding(10, 10, 10, 10);//设置单元格里面的item的空隙
        awesomeCellGridLayout.setIsPlayAnim(false);//设置弹出菜单
        awesomeCellGridLayout.setViewList(objs, mMainActivity);//设置里面所有控件并且刷新
    }
}