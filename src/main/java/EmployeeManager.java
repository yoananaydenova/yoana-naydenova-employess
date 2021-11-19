import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class EmployeeManager {

    final static String DATE_FORMAT = "yyyy-MM-dd";
    final static String DIRECTORY_NAME = "employees";
    final static String FILE_NAME = "data.txt";


    public static void main(String[] args) {

        try {

            Path directoryOnDesktop = createDirectoryOnDesktop(DIRECTORY_NAME);

            Map<Integer, Project> projectsWithEmployees = filesReader(directoryOnDesktop);

            Map<String, Long> teamsOfTwo = teamsWithDays(projectsWithEmployees);

            System.out.println(teamWithMaxDays(teamsOfTwo));
        } catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println(e.getMessage());
        }

    }

    private static Path createDirectoryOnDesktop(String directoryName) {
        String absolutePath = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();
        String pathWithDir = absolutePath.concat("/Desktop/").concat(directoryName);
        Path directory = null;
        try {
            directory = Files.createDirectories(Paths.get(pathWithDir));
        } catch (IOException ex) {
            System.out.println("You don't have write permission in the directory Desktop!");
        }
        return directory;
    }

    public static String teamWithMaxDays(Map<String, Long> teamsOfTwo) {

        if (teamsOfTwo.size() == 0) {
            return "There is no data for a team working together!";
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
        return "There is no data for a team working together!";

    }


    public static Map<String, Long> teamsWithDays(Map<Integer, Project> projectsWithEmployees) {
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

    private static LocalDate findBeforeDate(LocalDate dateOne, LocalDate dateTwo) {
        if (dateOne.isBefore(dateTwo)) {
            return dateOne;
        }
        return dateTwo;
    }

    private static LocalDate findAfterDate(LocalDate dateOne, LocalDate dateTwo) {
        if (dateOne.isAfter(dateTwo)) {
            return dateOne;
        }
        return dateTwo;
    }


    private static Map<Integer, Project> filesReader(Path path) throws NoSuchFileException {

        if (isEmpty(path)) {
            throw new NoSuchFileException("Please create file with needed data in folder \"employees\" on your Desktop. Try again!");
        }

        Map<Integer, Project> projects = new HashMap<>();

        File folder = path.toFile();
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    fileReader(file.getAbsolutePath(), projects);
                }
            }
        }
        return projects;
    }

    public static boolean isEmpty(Path path) {
        return path.toFile().listFiles().length == 0;
    }


    private static void fileReader(String filePath, Map<Integer, Project> projects) {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String record;

            while ((record = br.readLine()) != null) {

                String[] data = record.split("\\s*,\\s*");

                if (data.length < 4) {
                    System.out.printf("The record %s can not be save. There is errors in it!\n", record);
                    continue;
                }


                Integer empId = parseIdNumber(data[0]);
                Integer projectId = parseIdNumber(data[1]);
                LocalDate dateFrom = parseDateFormat(data[2]);
                LocalDate dateTo = parseDateFormat(data[3]);

                if (empId == null || projectId == null || dateFrom == null || dateTo == null) {
                    System.out.printf("The record %s can not be save. There is errors in it!\n", record);
                } else {
                    if (dateFrom.isAfter(dateTo)) {
                        System.out.printf("The record %s can not be save. The dateFrom is after the dateTo!\n", record);
                    }

                    Project project = new Project(projectId);
                    Employee employee = new Employee(empId, dateFrom, dateTo);

                    if (projects.containsKey(projectId)) {
                        projects.get(projectId).addEmployee(employee);
                    } else {
                        project.addEmployee(employee);
                        projects.put(projectId, project);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File is not found!\n Please add text file with records of employees in format: EmpID/number/, ProjectID/number/, DateFrom/yyyy-MM-dd/, DateTo/yyyy-MM-dd/.\n You must give name of the file \"data.txt\" and save it into directory src/main/resources/ of the project.\n After that you can Run the project again.");
        } catch (IOException e) {
            System.out.println("File read error!");
        }

    }

    private static Integer parseIdNumber(String inputNumber) {
        try {
            return Integer.parseInt(inputNumber);
        } catch (NumberFormatException e) {
            System.out.printf("The format of the id number \"%s\" is broken!\n", inputNumber);
            return null;
        }
    }

    private static LocalDate parseDateFormat(String inputDate) {
        LocalDate correctDate = null;
        if (inputDate.toUpperCase().equals("NULL")) {
            correctDate = LocalDate.now();
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
            try {
                correctDate = LocalDate.parse(inputDate, formatter);
            } catch (DateTimeParseException e) {
                System.out.printf("The format of the date \"%s\" is broken!\n", inputDate);
            }
        }

        return correctDate;
    }
}
