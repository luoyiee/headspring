package cc.xiaojiang.liangbo.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.List;

import cc.xiaojiang.liangbo.R;


public class OptionPickerHelper {

    @SuppressWarnings("unchecked")
    public static <T> OptionsPickerView<T> createPicker(Context context, String title, List<T> options, int select, final OnOptionsListener listener) {
        OptionsPickerView pickerView = new OptionsPickerView.Builder(context, listener::onOptionsSelect)
                .setTitleText(title)
                .setCancelText("取消")
                .setSubmitColor(ContextCompat.getColor(context, R.color.black))
                .setCancelColor(ContextCompat.getColor(context, R.color.personal_picker_cancel))
                .setSubmitText("确定")
                .setSelectOptions(select)
                .setCyclic(false, false, false)
                .build();
        pickerView.setPicker(options);
        return pickerView;
    }

    public interface OnOptionsListener {
        /**
         * 当picker选中后的回调
         *
         * @param options1 数据集合s1
         * @param options2 数据集合s2
         * @param options3 数据集合s3
         * @param v        view
         */
        void onOptionsSelect(int options1, int options2, int options3, View v);
    }
}
