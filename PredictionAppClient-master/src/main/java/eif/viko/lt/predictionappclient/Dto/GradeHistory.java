package eif.viko.lt.predictionappclient.Dto;

import java.time.LocalDateTime;

public class GradeHistory {
    private Long id;
    private double attendance;
    private double assignments;
    private double midterm;
    private double finalExam;
    private String predictedGrade;
    private String actualGrade;
    private LocalDateTime predictionDate;

    public GradeHistory() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getAssignments() {
        return assignments;
    }

    public void setAssignments(double assignments) {
        this.assignments = assignments;
    }

    public double getMidterm() {
        return midterm;
    }

    public void setMidterm(double midterm) {
        this.midterm = midterm;
    }

    public double getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(double finalExam) {
        this.finalExam = finalExam;
    }

    public String getPredictedGrade() {
        return predictedGrade;
    }

    public void setPredictedGrade(String predictedGrade) {
        this.predictedGrade = predictedGrade;
    }

    public String getActualGrade() {
        return actualGrade;
    }

    public void setActualGrade(String actualGrade) {
        this.actualGrade = actualGrade;
    }

    public LocalDateTime getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(LocalDateTime predictionDate) {
        this.predictionDate = predictionDate;
    }
}
