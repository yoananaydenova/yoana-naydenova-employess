package model.entity;

import java.time.LocalDate;
import java.util.Objects;


public class Employee {
    private int empId;
    private LocalDate dateFrom;
    private LocalDate dateTo;

    public Employee(int empId, LocalDate dateFrom, LocalDate dateTo) {
        this.empId = empId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getEmpId() {
        return empId;
    }


    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    @Override
    public String toString() {
        return "model.entity.Employee{" +
                "empId=" + empId +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;
        Employee employee = (Employee) o;
        return getEmpId() == employee.getEmpId() &&
                getDateFrom().equals(employee.getDateFrom()) &&
                getDateTo().equals(employee.getDateTo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmpId(), getDateFrom(), getDateTo());
    }
}
