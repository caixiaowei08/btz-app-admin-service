package com.btz.feedback.vo;

import com.btz.exercise.entity.ExerciseEntity;
import com.btz.feedback.entity.FeedbackEntity;

import java.io.Serializable;

/**
 * Created by User on 2017/9/1.
 */
public class FeedbackVo implements Serializable {

    private FeedbackEntity feedbackEntity;

    private ExerciseEntity exerciseEntity;

    public FeedbackEntity getFeedbackEntity() {
        return feedbackEntity;
    }

    public void setFeedbackEntity(FeedbackEntity feedbackEntity) {
        this.feedbackEntity = feedbackEntity;
    }

    public ExerciseEntity getExerciseEntity() {
        return exerciseEntity;
    }

    public void setExerciseEntity(ExerciseEntity exerciseEntity) {
        this.exerciseEntity = exerciseEntity;
    }
}
