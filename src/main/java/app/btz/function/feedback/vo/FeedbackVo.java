package app.btz.function.feedback.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/7/25.
 */
public class FeedbackVo implements Serializable {

    private Integer id;

    private Integer exerciseId;

    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
