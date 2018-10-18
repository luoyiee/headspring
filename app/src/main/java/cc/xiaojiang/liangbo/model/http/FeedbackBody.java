package cc.xiaojiang.liangbo.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-27
 * description:
 */
public class FeedbackBody {
    private Long telphone;
    private String view;

    public FeedbackBody(String view,Long telphone) {
        this.telphone = telphone;
        this.view = view;
    }

    public void setTelphone(Long telphone) {
        this.telphone = telphone;
    }

    public void setView(String view) {
        this.view = view;
    }
}
