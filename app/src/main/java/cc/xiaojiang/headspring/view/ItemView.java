package cc.xiaojiang.headspring.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.xiaojiang.headspring.R;


/**
 * @author :jinjiafeng
 * date:  on 18-6-6
 * description:
 */
public class ItemView extends RelativeLayout{
    private boolean mShowArrow = true;
    private CharSequence mValue;
    private CharSequence mKey;

    private TextView mTvItemValue;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_item_view, this);
        initAttr(context, attrs);
        initView();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        this.mShowArrow = array.getBoolean(R.styleable.ItemView_showArrow, mShowArrow);
        this.mValue = array.getText(R.styleable.ItemView_itemValue);
        this.mKey = array.getText(R.styleable.ItemView_itemKey);
        array.recycle();
    }

    private void initView() {
        TextView tvItemKey = findViewById(R.id.tv__item_key);
        mTvItemValue = findViewById(R.id.tv__item_value);
        tvItemKey.setText(mKey);

        mTvItemValue.setText(mValue);
        if(!mShowArrow){
            mTvItemValue.setCompoundDrawables(null,null,null,null);
        }
    }

    public void setItemValue(String value) {
        this.mValue = value;
        mTvItemValue.setText(mValue);
    }

    public void setItemValue(@StringRes  int value) {
        this.mValue = getContext().getString(value);
        mTvItemValue.setText(mValue);
    }
}
