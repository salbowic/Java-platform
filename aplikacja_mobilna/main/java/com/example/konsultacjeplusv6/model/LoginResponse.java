package com.example.konsultacjeplusv6.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("student")
    private Student student;
    @SerializedName("prowadzacy")
    private Prowadzacy prowadzacy;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Prowadzacy getProwadzacy() {
        return prowadzacy;
    }

    public void setProwadzacy(Prowadzacy prowadzacy) {
        this.prowadzacy = prowadzacy;
    }
}
