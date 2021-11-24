package model.dto;

import java.time.LocalDate;

public class RecordDetails {
    private Integer empId;
    private Integer projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public RecordDetails() {
    }

    public RecordDetails(Integer empId, Integer projectId, LocalDate dateFrom, LocalDate dateTo) {
        this.empId = empId;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Integer getEmpId() {
        return empId;
    }

    public RecordDetails setEmpId(Integer empId) {
        this.empId = empId;
        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public RecordDetails setProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public RecordDetails setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public RecordDetails setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

}
