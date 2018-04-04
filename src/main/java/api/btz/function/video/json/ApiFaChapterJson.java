package api.btz.function.video.json;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/10/30.
 */
public class ApiFaChapterJson implements Serializable {

    /**
     * 课程主键
     */
    private Integer subCourseId;

    private Integer chapterId;

    private String chapterName;

    private List<ApiChapterJson> subChapterList = new ArrayList<ApiChapterJson>();

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<ApiChapterJson> getSubChapterList() {
        return subChapterList;
    }

    public void setSubChapterList(List<ApiChapterJson> subChapterList) {
        this.subChapterList = subChapterList;
    }
}
