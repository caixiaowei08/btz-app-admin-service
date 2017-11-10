package api.btz.function.video.json;

import java.io.Serializable;

/**
 * Created by User on 2017/10/27.
 */
public class ApiChapterJson implements Serializable{

    private Integer subCourseId;

    private Integer faChapterId;

    private Integer chapterId;

    private String chapterName;

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

    public Integer getFaChapterId() {
        return faChapterId;
    }

    public void setFaChapterId(Integer faChapterId) {
        this.faChapterId = faChapterId;
    }
}
