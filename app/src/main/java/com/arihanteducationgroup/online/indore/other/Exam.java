package com.arihanteducationgroup.online.indore.other;

public class Exam {

    private String id,Exam_title,Exam_duration,
            Exam_desc,
            payment_type,exam_fees,create_date;

    public Exam() {
    }

    public Exam(String id, String Exam_title, String Exam_duration, String Exam_desc, String payment_type, String exam_fees,String create_date ) {
        this.id = id;
        this.Exam_duration = Exam_duration;
        this.Exam_desc = Exam_desc;
        this.Exam_title = Exam_title;
        this.payment_type = payment_type;
        this.exam_fees = exam_fees;
        this.create_date = create_date;
    }

    public String getcreate_date() {
        return create_date;
    }

    public void setcreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getexam_fees() {
        return exam_fees;
    }

    public void setexam_fees(String exam_fees) {
        this.exam_fees = exam_fees;
    }

    public String getExam_title() {
        return Exam_title;
    }

    public void setExam_title(String Exam_title) {
        this.Exam_title = Exam_title;
    }


    public String getExam_duration() {
        return Exam_duration;
    }

    public void setExam_duration(String Exam_duration) {
        this.Exam_duration = Exam_duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExam_desc() {
        return Exam_desc;
    }

    public void setExam_desc(String Exam_desc) {
        this.Exam_desc = Exam_desc;
    }

    public String getpayment_type() {
        return payment_type;
    }

    public void setpayment_type(String payment_type) {
        this.payment_type = payment_type;
    }


}
