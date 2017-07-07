package com.btz;

import com.btz.contants.QuestionType;

/**
 * Created by User on 2017/7/3.
 */
public class QuestionTypeTest {
    public static void main(String[] args) {
        for (QuestionType questionType :QuestionType.values()) {
            System.out.println(questionType.getCode());
            System.out.println(questionType.getDesc());
        }
    }
}
