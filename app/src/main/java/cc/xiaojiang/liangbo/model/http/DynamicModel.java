package cc.xiaojiang.liangbo.model.http;

/**
 * @author :jinjiafeng
 * date:  on 18-7-13
 * description:
 */
public class DynamicModel {

    /**
     * id : 1
     * cover : url
     * title : title
     * author : author
     * comment : 100
     * like : 100
     * url : url
     */

    private String id;
    private String cover;
    private String title;
    private String author;
    private int comment;
    private int like;
    private String url;

    public void setId(String id) {
        this.id = id;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover == null ? "" : cover;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public String getAuthor() {
        return author == null ? "" : author;
    }

    public int getComment() {
        return comment;
    }

    public int getLike() {
        return like;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }
}
