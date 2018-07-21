package cc.xiaojiang.liangbo.adapter;

import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.SpanUtils;

/**
 * @author :jinjiafeng
 * date:  on 18-7-20
 * description:
 */
public class HomeIndoorPmHolder extends Holder<String> {

    private TextView mTextView;

    public HomeIndoorPmHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        mTextView = itemView.findViewById(R.id.tv_indoor_pm);
    }

    @Override
    public void updateUI(String data) {
        mTextView.setText(new SpanUtils()
                .append(data).setFontSize(48, true)
                .append("ug/m").setFontSize(18, true).append("3").setSuperscript().setFontSize
                        (16, true)
                .create());
    }
}
