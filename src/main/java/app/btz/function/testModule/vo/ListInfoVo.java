package app.btz.function.testModule.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/17.
 */
public class ListInfoVo implements Serializable{
    /**
     * 章节ID
     */
    private Integer chapterId;
    /**
     * 章节层级
     */
    private String level;
    /**
     * 章节名称
     */
    private String tit;
    /**
     * 叶子节点
     */
    private boolean leaf;

    /**
     * 题目起点序号
     */
    private Integer beg = 0;
    /**
     * 该节题目总数
     */
    private Integer all = 0;
    /**
     *
     */
    private List<ListInfoVo> sub;

    public String getTit() {
        return tit;
    }

    public void setTit(String tit) {
        this.tit = tit;
    }

    public Integer getBeg() {
        return beg;
    }

    public void setBeg(Integer beg) {
        this.beg = beg;
    }

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public List<ListInfoVo> getSub() {
        return sub;
    }

    public void setSub(List<ListInfoVo> sub) {
        this.sub = sub;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
