package cc.xiaojiang.liangbo.activity;

import java.util.ArrayList;

import cc.xiaojiang.liangbo.model.http.DynamicModel;

/**
 * Created by jinjiafeng
 * Time :18-7-28
 */
public class TestData {
    private static String[] titles = {"空气清新,愉悦家人心", "灵动吹风,清爽一夏", "时尚,简约,多种模式",
            "大能量,小声音,让你安然入睡", "重新发现生活中的美好", "只有风,没有声,舒适睡眠体验"};
    private static String[] covers = {"http://www.0351zhuangxiu.com/uploads/images/212.jpg",
            "http://www.jiajujiazhuang.com/images/120218/0144501296-.jpg",
            "http://blog.pic.xiaokui.io/3a8343fd852e6f88a0a13c1b6953bdc2",
            "http://www.yadea.cn/UploadFiles/FCK/image/2016%E6%96%87%E7%AB%A0/10%E6%9C%88/%E7%8E" +
                    "%B0%E4%BB%A3%E5%AE%B6%E8%A3%85%E8%AE%BE%E8%AE%A1%E6%A1%88%E4%BE%8B1(4).png",
            "http://a2.att.hudong.com/65/36/14300001022490129344365509222_950.jpg",
            "http://pic.tugou.com/jingyan/20160217195430_70770.jpeg"};

    public static ArrayList<DynamicModel> getDynamicData() {
        ArrayList<DynamicModel> dynamicModels = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            DynamicModel dynamicModel = new DynamicModel();
            dynamicModel.setLike(2600);
            dynamicModel.setComment(390);
            dynamicModel.setTitle(titles[i]);
            dynamicModel.setUrl("http://www.ecotechair.com.cn/index.php/archives/3653");
            dynamicModel.setCover(covers[i]);
            dynamicModels.add(dynamicModel);
        }
        return dynamicModels;
    }


}
