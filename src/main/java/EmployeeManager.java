import model.dto.RecordDetails;
import model.entity.Employee;
import model.entity.Project;
import utils.EmployeeFileReader;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class EmployeeManager {

    public String run() {
        System.out.println("Enter the name of the folder on the desktop where you saved the employee data files.");
        Scanner in = new Scanner(System.in);
        String folderName = in.nextLine();

        try {
            List<RecordDetails> recordDetails = EmployeeFileReader.filesReader(folderName);

            Map<Integer, Project> projectsWithEmployees = createProjectsWithEmployees(recordDetails);

            Map<String, Long> teamsOfTwo = createTeamsWithDays(projectsWithEmployees);

            return (findTeamsWithMaxDays(teamsOfTwo));

        } catch (Exception e) {
            return (e.getMessage());
        }
    }


    private String findTeamsWithMaxDays(Map<String, Long> teamsOfTwo) {

        String result = "There is no data for a team working together!";
        if (teamsOfTwo.size() == 0) {
            return result;
        }
        Long maxDays = Collections.max(teamsOfTwo.values());
        List<Map.Entry<String, Long>> teamsWithMaxDays = teamsOfTwo
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(maxDays))
                .collect(Collectors.toList());

        if (!teamsWithMaxDays.isEmpty()) {

            StringBuilder sb = new StringBuilder();
            teamsWithMaxDays
                    .forEach(team -> {
                        String[] teamMembers = team.getKey().split("-");
                        Long days = team.getValue();

                        sb.append(String.format("The team that has worked together the longest on common projects is %s and %s. They worked together: %d days.",
                                teamMembers[0], teamMembers[1], days));
                        sb.append(System.lineSeparator());
                    });

            return sb.toString();
        }
        return result;
    }

    private Map<Integer, Project> createProjectsWithEmployees(List<RecordDetails> recordDetails) {
        Map<Integer, Project> projectsWithEmployees = new HashMap<>();
        recordDetails.forEach(entity -> {
            Employee employee = new Employee(entity.getEmpId(), entity.getDateFrom(), entity.getDateTo());

            if (projectsWithEmployees.containsKey(entity.getProjectId())) {
                projectsWithEmployees.get(entity.getProjectId()).addEmployee(employee);
            } else {
                Project project = new Project(entity.getProjectId());
                project.addEmployee(employee);
                projectsWithEmployees.put(entity.getProjectId(), project);
            }
        });
        return projectsWithEmployees;
    }

    private Map<String, Long> createTeamsWithDays(Map<Integer, Project> projectsWithEmployees) {

        Map<String, Long> teamsOfTwo = new HashMap<>();
        projectsWithEmployees.forEach((key, value) -> {
            List<Employee> employeesList = value.getEmployees();

            for (int i = 0; i < employeesList.size() - 1; i++) {
                Employee firstEmp = employeesList.get(i);

                for (int k = i + 1; k < employeesList.size(); k++) {
                    Employee secondEmp = employeesList.get(k);


                    if (firstEmp.getEmpId() == secondEmp.getEmpId() || firstEmp.getDateTo().isBefore(secondEmp.getDateFrom()) ||
                            secondEmp.getDateTo().isBefore(firstEmp.getDateFrom())) {
                        continue;
                    }

                    Long period = 0L;
                    // 1. if start and finish together
                    // 2. if first and second start together - first/second finish, then other finish
                    if (firstEmp.getDateFrom().isEqual(secondEmp.getDateFrom())) {
                        LocalDate secondDate = findBeforeDate(firstEmp.getDateTo(), secondEmp.getDateTo());
                        period = DAYS.between(firstEmp.getDateFrom(), secondDate);
                    } else if (firstEmp.getDateTo().isEqual(secondEmp.getDateTo())) {
                        // 3. if first start/second start, then other start - first and second finish together
                        LocalDate firstDate = findAfterDate(firstEmp.getDateFrom(), secondEmp.getDateFrom());
                        period = DAYS.between(firstDate, firstEmp.getDateTo());
                    } else {
                        LocalDate firstDate = findAfterDate(firstEmp.getDateFrom(), secondEmp.getDateFrom());
                        LocalDate secondDate = findBeforeDate(firstEmp.getDateTo(), secondEmp.getDateTo());
                        period = DAYS.between(firstDate, secondDate);
                    }

                    // Save period for the couple into map
                    if (period != 0) {
                        String teamsId = firstEmp.getEmpId() + "-" + secondEmp.getEmpId();
                        if (teamsOfTwo.containsKey(teamsId)) {
                            teamsOfTwo.put(teamsId, teamsOfTwo.get(teamsId) + period + 1L);
                        } else {
                            teamsOfTwo.put(teamsId, period + 1L);
                        }
                    }
                }
            }
        });
        return teamsOfTwo;
    }

    private LocalDate findBeforeDate(LocalDate dateOne, LocalDate dateTwo) {
        if (dateOne.isBefore(dateTwo)) {
            return dateOne;
        }
        return dateTwo;
    }

    private LocalDate findAfterDate(LocalDate dateOne, LocalDate dateTwo) {
        if (dateOne.isAfter(dateTwo)) {
            return dateOne;
        }
        return dateTwo;
    }


}
