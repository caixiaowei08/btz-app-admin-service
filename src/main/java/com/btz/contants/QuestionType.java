package com.btz.contants;

/**
 * Created by User on 2017/7/3.
 */
public enum QuestionType {

    Q1(1, "单选题", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 1, 1),
    Q2(2, "多选题", "每小题均有多个正确答案，请从每小题的备选答案中选出你认为正确的答案。", 2, 1),
    Q3(3, "判断题", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 3, 1),
    Q4(4, "不定项选择题", "每小题有一个或一个以上正确答案，请从每小题的备选答案中选出你认为正确的答案。", 6, 4),
    Q5(5, "分录题(数字)", "每小题只有唯一答案，请点击“答题”按钮进行答题。", 4, 2),
    Q6(6, "分录题(固定)", "每小题只有唯一答案，请点击“答题”按钮进行答题。", 5, 2),
    Q7(7, "简答题", "请在下方的方框内输入你认为正确的答案。", 5, 2),
    Q8(8, "综合题", "请在下方的方框内输入你认为正确的答案。", 5, 2),
    Q9(9, "计算题", "请在下方的方框内输入你认为正确的答案。", 5, 2),
    Q10(10, "案例分析题", "请在下方的方框内输入你认为正确的答案。", 8, 2),
    Q11(11, "补全短文", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 9, 4),
    Q12(12, "概括大意与完成句子", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 9, 4),
    Q13(13, "完型填空", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 9, 4),
    Q14(14, "阅读理解", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 9, 4),
    Q15(15, "阅读判断", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 9, 4),
    Q16(16, "作文题", "请在下方的方框内输入你认为正确的答案。", 8, 1),
    Q17(17, "英译中", "请在下方的方框内输入你认为正确的答案。", 8, 1),
    Q18(18, "交际英语", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 1, 1),
    Q19(19, "词汇与结构", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 1, 1),
    Q20(20, "组合单选题", "每小题只有一个正确答案，请从每小题的备选答案中选出一个你认为正确的答案。", 1, 1),
    Q21(21, "中级计算题", "请在下方的方框内输入你认为正确的答案。", 8, 2),
    Q22(22, "中级简答题", "请在下方的方框内输入你认为正确的答案。", 8, 2),
    Q23(23, "填空题", "请在方框内输入你认为正确的答案", 5, 1),
    Q24(24, "活动设计题", "活动设计题", 8, 1),
    Q25(25, "论述题", "论述题", 8, 1),
    Q26(26, "教学设计题", "教学设计", 8, 2),
    Q27(27, "材料分析题", "阅读提供的材料，回答问题", 8, 2),
    Q28(28, "辨析题", "阅读提供的材料，回答问题", 8, 1);

    private Integer examType;

    private String examName;

    private String examIntroduce;

    private Integer examShow;

    private Integer parseType;

    QuestionType(Integer examType, String examName, String examIntroduce, Integer examShow, Integer parseType) {
        this.examType = examType;
        this.examName = examName;
        this.examIntroduce = examIntroduce;
        this.examShow = examShow;
        this.parseType = parseType;
    }

    public Integer getExamType() {
        return examType;
    }

    public void setExamType(Integer examType) {
        this.examType = examType;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public String getExamIntroduce() {
        return examIntroduce;
    }

    public void setExamIntroduce(String examIntroduce) {
        this.examIntroduce = examIntroduce;
    }

    public Integer getExamShow() {
        return examShow;
    }

    public void setExamShow(Integer examShow) {
        this.examShow = examShow;
    }

    public Integer getParseType() {
        return parseType;
    }

    public void setParseType(Integer parseType) {
        this.parseType = parseType;
    }

    public static QuestionType getExamTypeByExamName(String examName) {
        for (QuestionType questionType : QuestionType.values()) {
            if (examName.trim().equals(questionType.getExamName().trim())) {
                return questionType;
            }
        }
        return null;
    }

    public static QuestionType getExamTypeByExamType(Integer examType) {
        for (QuestionType questionType : QuestionType.values()) {
            if (examType.equals(questionType.getExamType())) {
                return questionType;
            }
        }
        return null;
    }
}
