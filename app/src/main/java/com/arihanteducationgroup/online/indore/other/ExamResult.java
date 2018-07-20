package com.arihanteducationgroup.online.indore.other;

public class ExamResult {
    private String exam_id,exam_name,totalQuestion,
            score;

    public ExamResult() {
    }

    public ExamResult(String exam_id, String exam_name, String totalQuestion, String score) {
        this.exam_id = exam_id;
        this.totalQuestion = totalQuestion;
        this.score = score;
        this.exam_name = exam_name;
    }

    public String getExam_id() {
        return exam_id;
    }

    public void setExam_id(String exam_id) {
        this.exam_id = exam_id;
    }

    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
