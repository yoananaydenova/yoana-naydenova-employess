import java.util.ArrayList;
import java.util.List;

public class Project {
    private Integer projectId;
    private List<Employee> employees;

    public Project(Integer projectId) {
        this.projectId = projectId;
        this.employees = new ArrayList<>();
    }

    public Integer getProjectId() {
        return projectId;
    }

    public List<Employee> getEmployees() {
        return employees;
    }


    // Additional method
    public void addEmployee(Employee employee) {
        if (this.employees.contains(employee)) {
            System.out.printf("The record of employee \"%s\" exists in the project %d and wont be save again!\n", employee, this.projectId);
        } else {
            this.employees.add(employee);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;

        Project project = (Project) o;

        return getProjectId().equals(project.getProjectId());
    }

    @Override
    public int hashCode() {
        return getProjectId().hashCode();
    }
}
