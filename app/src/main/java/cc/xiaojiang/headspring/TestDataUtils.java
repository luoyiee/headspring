package cc.xiaojiang.headspring;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author :jinjiafeng
 * date:  on 18-6-12
 * description:
 */
public class TestDataUtils {

    public static List<Entry> getChartData(int count) {
        final Random random = new Random();
        final ArrayList<Entry> entries = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final Entry entry = new Entry(i, random.nextInt(100) + 50);
            entries.add(entry);
        }
        return entries;
    }

    public static List<String> getChartLabel(int count) {
        final ArrayList<String> labels = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            labels.add(i + 1 + "/7");
        }
        return labels;
    }
}
