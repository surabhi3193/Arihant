package com.arihanteducationgroup.online.indore.other;

public class Course {

    private String id,courses_name,courses_duration,
            cours_full_name,
            courses_eligibility,courses_seats,admission_process,fees_scholarship;

    public Course() {
    }

    public Course(String id, String courses_name, String courses_duration, String cours_full_name,
                String courses_eligibility, String courses_seats,String admission_process, String fees_scholarship ) {
        this.id = id;
        this.courses_duration = courses_duration;
        this.cours_full_name = cours_full_name;
        this.courses_name = courses_name;
        this.courses_eligibility = courses_eligibility;
        this.courses_seats = courses_seats;
        this.admission_process = admission_process;
        this.fees_scholarship = fees_scholarship;
    }

    public String getFees_scholarship() {
        return fees_scholarship;
    }

    public void setFees_scholarship(String fees_scholarship) {
        this.fees_scholarship = fees_scholarship;
    }

    public String getadmission_process() {
        return admission_process;
    }

    public void setadmission_process(String admission_process) {
        this.admission_process = admission_process;
    }

    public String getcourses_seats() {
        return courses_seats;
    }

    public void setcourses_seats(String courses_seats) {
        this.courses_seats = courses_seats;
    }

    public String getcourses_name() {
        return courses_name;
    }

    public void setcourses_name(String courses_name) {
        this.courses_name = courses_name;
    }


    public String getcourses_duration() {
        return courses_duration;
    }

    public void setcourses_duration(String courses_duration) {
        this.courses_duration = courses_duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getcours_full_name() {
        return cours_full_name;
    }

    public void setcours_full_name(String cours_full_name) {
        this.cours_full_name = cours_full_name;
    }

    public String getcourses_eligibility() {
        return courses_eligibility;
    }

    public void setcourses_eligibility(String courses_eligibility) {
        this.courses_eligibility = courses_eligibility;
    }


}
