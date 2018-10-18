package cc.xiaojiang.liangbo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.activity.AirActivity;
import cc.xiaojiang.liangbo.utils.SpanUtils;

/**
 * @author :jinjiafeng
 * date:  on 18-7-20
 * description:
 */
public class HomeIndoorPmHolder extends Holder<String> {

    private TextView mTextView;
    private TextView mTvAirQuality;

    public HomeIndoorPmHolder(View itemView) {
        super(itemView);
    }



    @Override
    protected void initView(View itemView) {
        mTextView = itemView.findViewById(R.id.tv_indoor_pm);
        mTvAirQuality = itemView.findViewById(R.id.tv_air_quality);
    }

    @Override
    public void updateUI(String data) {
        mTextView.setText(new SpanUtils()
                .append(data).setFontSize(52, true)
                .append("ug/m").setFontSize(18, true).append("3").setSuperscript().setFontSize
                        (16, true)
                .create());
        if(TextUtils.isEmpty(data) || AirActivity.DEFAULT_DATA.equals(data)){
            mTvAirQuality.setVisibility(View.INVISIBLE);
        }else{
            mTvAirQuality.setVisibility(View.VISIBLE);
            int pm = Integer.parseInt(data);
           if(pm<=35){
               mTvAirQuality.setText("优");
               mTvAirQuality.setBackgroundResource(R.drawable.home_btn_excellent);
           }else if(pm<=75){
               mTvAirQuality.setText("良");
               mTvAirQuality.setBackgroundResource(R.drawable.home_btn_good);
           }else{
               mTvAirQuality.setText("差");
               mTvAirQuality.setBackgroundResource(R.drawable.home_btn_difference);
           }
        }
    }
}
