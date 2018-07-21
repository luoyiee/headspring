package cc.xiaojiang.liangbo.view;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import cc.xiaojiang.liangbo.R;


/**
 * @author :jinjiafeng
 * date:  on 18-6-20
 * description:
 */
public class HistoryMarkView extends MarkerView {
    private TextView tvContent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public HistoryMarkView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent =   findViewById(R.id.tv_marker);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText((int)e.getY()+"");
        super.refreshContent(e, highlight);
    }
}
