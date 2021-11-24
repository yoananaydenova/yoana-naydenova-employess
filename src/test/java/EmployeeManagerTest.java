import org.junit.jupiter.api.Test;

class EmployeeManagerTest {

    //TODO tests of all classes
//    @Test
//    public void run_shouldReturnCorrectResult() {
//        EmployeeManager employeeManager = new EmployeeManager();
//        employeeManager.run();
//    }


//    @Test
//    public void teamWithMaxDays_shouldReturnNegativeResponse_withEmptyMap() {
//        Map<String, Long> teamsOfTwo = new HashMap<>();
//        EmployeeManager employeeManager = new EmployeeManager();
//        String result = employeeManager.teamWithMaxDays(teamsOfTwo);
//        assertEquals(result, "There is no data for a team working together!");
//    }
//
//    @Test
//    public void teamWithMaxDays_withEqualMapValue_shouldReturnPositiveResponseWithFirstEqualsTeam() {
//        Map<String, Long> teamsOfTwo = new HashMap<>();
//        teamsOfTwo.put("1-2", 10L);
//        teamsOfTwo.put("2-3", 10L);
//        teamsOfTwo.put("1-3", 10L);
//        EmployeeManager employeeManager = new EmployeeManager();
//        String result = employeeManager.teamWithMaxDays(teamsOfTwo);
//        assertEquals( "The team that has worked together the longest on common projects is 1 and 2. They worked together: 10 days.",
//                result);
//    }
//
//    @Test
//    public void teamWithMaxDays_shouldReturnPositiveResponse() {
//        Map<String, Long> teamsOfTwo = new HashMap<>();
//        teamsOfTwo.put("1-2", 10L);
//        teamsOfTwo.put("1-3", 15L);
//        teamsOfTwo.put("1-4", 20L);
//
//        EmployeeManager employeeManager = new EmployeeManager();
//        String result = employeeManager.teamWithMaxDays(teamsOfTwo);
//
//        assertEquals("The team that has worked together the longest on common projects is 1 and 4. They worked together: 20 days.",
//                result);
//    }
//
//    @Test
//    public void teamsWithDays_shouldReturnCorect (){
//        Map<Integer, model.entity.Project> projectsWithEmployees = new HashMap<>();
//
//        model.entity.Project project1 = new model.entity.Project(1);
//        project1.addEmployee(new model.entity.Employee(1, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));
//        project1.addEmployee(new model.entity.Employee(2, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));
//
//        model.entity.Project project2 = new model.entity.Project(2);
//        project2.addEmployee(new model.entity.Employee(1, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));
//        project2.addEmployee(new model.entity.Employee(2, LocalDate.of(2020,1,1),LocalDate.of(2020,3,2)));
//
//
//        projectsWithEmployees.put(1, project1);
//        projectsWithEmployees.put(2, project2);
//
//        EmployeeManager employeeManager = new EmployeeManager();
//        Map<String, Long> result = employeeManager.teamsWithDays(projectsWithEmployees);
//
//        assertEquals( 1, result.size());
//        assertEquals(2L,result.get("1-2"));
//
//    }

}