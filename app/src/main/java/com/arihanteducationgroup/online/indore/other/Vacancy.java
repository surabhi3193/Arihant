package com.arihanteducationgroup.online.indore.other;

public class Vacancy {
    private String id,vacancies_title,vacancies_icon_img,
            vacancies_desc,
            vacancies_noti_pdf,post_date,type;

    public Vacancy() {
    }

    public Vacancy(String id,String vacancies_title,String vacancies_icon_img, String vacancies_desc,String vacancies_noti_pdf,String post_date,String type) {
        this.id = id;
        this.vacancies_icon_img = vacancies_icon_img;
        this.vacancies_desc = vacancies_desc;
        this.vacancies_title = vacancies_title;
        this.vacancies_noti_pdf = vacancies_noti_pdf;
        this.post_date = post_date;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getvacancies_title() {
        return vacancies_title;
    }

    public void setvacancies_title(String vacancies_title) {
        this.vacancies_title = vacancies_title;
    }


    public String getvacancies_icon_img() {
        return vacancies_icon_img;
    }

    public void setvacancies_icon_img(String vacancies_icon_img) {
        this.vacancies_icon_img = vacancies_icon_img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getvacancies_desc() {
        return vacancies_desc;
    }

    public void setvacancies_desc(String vacancies_desc) {
        this.vacancies_desc = vacancies_desc;
    }

    public String getvacancies_noti_pdf() {
        return vacancies_noti_pdf;
    }

    public void setvacancies_noti_pdf(String vacancies_noti_pdf) {
        this.vacancies_noti_pdf = vacancies_noti_pdf;
    }

    
    
}
