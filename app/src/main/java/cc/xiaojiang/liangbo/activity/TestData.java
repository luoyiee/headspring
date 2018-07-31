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

    private static String mContent = "第一，随着工业及交通运输业的不断发展，大量的有害物质被排放到空气中去，造成了雾霾天气在中国各地的大量出现，PM2.5" +
            "等污染物的迅速增多加大了民众患呼吸系统疾病的风险。虽然国家相关部门正在努力治理空气污染问题，但在未来几年内，雾霾天气仍将比较多见。使用合格的空气净化器可以有效净化室内空气中的PM2.5及其他可吸入颗粒。\n" +
            "其次，装修材料及家具对室内空气的污染也在逐渐得到人们的重视。许多油漆、胶合板、复合地板、塑料贴面等材料均含有甲醛、苯、氨等挥发性有机化合物，这些化合物持续释放出有害气体，长期吸入会危害身体健康，严重的会诱发白血病、癌症等重大疾病。另外，烹调产生的油烟，以及花粉、粉尘、细菌等也在很大程度上影响室内空气质量。" +
            "居民在室内使用空气净化器可以有效降低甲醛、异味等有害气体的含量，进而改善室内空气，提高生活质量。";

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
            dynamicModel.setContent(mContent);
        }
        return dynamicModels;
    }


}
