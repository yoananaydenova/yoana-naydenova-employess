import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmployeeManagerTest {

    @Test
    public void teamWithMaxDays_shouldReturnNegativeResponse_withEmptyMap() {
        Map<String, Long> teamsOfTwo = new HashMap<>();
        String result = EmployeeManager.teamWithMaxDays(teamsOfTwo);
        assertEquals(result, "There is no data for a team working together!");
    }

    @Test
    public void teamWithMaxDays_withEqualMapValue_shouldReturnPositiveResponseWithFirstEqualsTeam() {
        Map<String, Long> teamsOfTwo = new HashMap<>();
        teamsOfTwo.put("1-2", 10L);
        teamsOfTwo.put("2-3", 10L);
        teamsOfTwo.put("1-3", 10L);
        String result = EmployeeManager.teamWithMaxDays(teamsOfTwo);
        assertEquals( "The team that has worked together the longest on common projects is 1 and 2. They worked together: 10 days.",
                result);
    }

    @Test
    public void teamWithMaxDays_shouldReturnPositiveResponse() {
        Map<String, Long> teamsOfTwo = new HashMap<>();
        teamsOfTwo.put("1-2", 10L);
        teamsOfTwo.put("1-3", 15L);
        teamsOfTwo.put("1-4", 20L);

        String result = EmployeeManager.teamWithMaxDays(teamsOfTwo);

        assertEquals("The team that has worked together the longest on common projects is 1 and 4. They worked together: 20 days.",
                result);
    }

    @Test
    public void teamsWithDays_shouldReturnCorect (){
        Map<Integer, Project> projectsWithEmployees = new HashMap<>();

        Project project1 = new Project(1);
        project1.addEmployee(new Employee(1, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));
        project1.addEmployee(new Employee(2, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));

        Project project2 = new Project(2);
        project2.addEmployee(new Employee(1, LocalDate.of(2020,1,1),LocalDate.of(2020,1,2)));
        project2.addEmployee(new Employee(2, LocalDate.of(2020,1,1),LocalDate.of(2020,3,2)));


        projectsWithEmployees.put(1, project1);
        projectsWithEmployees.put(2, project2);


        Map<String, Long> result = EmployeeManager.teamsWithDays(projectsWithEmployees);

        assertEquals( 1, result.size());
        assertEquals(2L,result.get("1-2"));



    }

}