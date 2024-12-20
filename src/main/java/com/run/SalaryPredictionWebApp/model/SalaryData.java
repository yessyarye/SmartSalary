package com.run.SalaryPredictionWebApp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "salary_datas")
public class SalaryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int age;
    private String gender;
    @Column(name = "Education Level")
    private String educationLevel;
    @Column(name = "Job Title")
    private String jobTitle;
    @Column(name = "Years of Experience")
    private int yearsOfExperience;
    private float salary;
}
