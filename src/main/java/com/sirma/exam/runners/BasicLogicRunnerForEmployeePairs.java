package com.sirma.exam.runners;

import com.sirma.exam.models.Exam;
import com.sirma.exam.repositories.ExamRepository;
import com.sirma.exam.utils.ReadFromCsv;
import com.sirma.exam.utils.ReadLabelledMonths;
import com.sirma.exam.utils.StringToDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class BasicLogicRunnerForEmployeePairs implements CommandLineRunner {
    private ExamRepository repository;

    // EmployeeId, EmployeeId, ProjectId, Time
    private final Map<Long, Map<Long, Map<Long, Long>>> timeSpentTogether = new HashMap<>();

    @Autowired
    public BasicLogicRunnerForEmployeePairs(ExamRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        saveTestJobs();
        Long[] twoEmployees = findEmployees();
        List<Long[]> projects = findProjects(twoEmployees);
        System.out.println("Top team workers are: " + twoEmployees[0] + " and " + twoEmployees[1] + ". They work together for: " + twoEmployees[2] + " days.");
        System.out.println("They work on projects:");
        for (Long[] project : projects) {
            System.out.println("Project ID: " + project[0] + " for time: " + project[1] + " days");
        }
    }

    private Long[] findEmployees() {
        List<Exam> allRecords = repository.findAll();

        for (Exam record : allRecords) {
            for (Exam otherRecord : allRecords) {
                if (record.getProjectId().equals(otherRecord.getProjectId()) &&
                        (record.getStartDate().isEqual(otherRecord.getStartDate()) || record.getStartDate().isBefore(otherRecord.getStartDate())) &&
                        (record.getEndDate().isEqual(otherRecord.getEndDate()) || record.getEndDate().isAfter(otherRecord.getEndDate()))) {
                    if (timeSpentTogether.containsKey(record.getEmployeeId())) {
                        if (timeSpentTogether.get(record.getEmployeeId()).containsKey(otherRecord.getEmployeeId())) {
                            if (timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).containsKey(record.getProjectId())) {

                                timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(),
                                        timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).get(record.getProjectId()) +
                                                ChronoUnit.DAYS.between(otherRecord.getStartDate(), otherRecord.getEndDate()));
                            } else {
                                timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(),
                                        ChronoUnit.DAYS.between(otherRecord.getStartDate(), otherRecord.getEndDate()));
                            }
                        } else {
                            timeSpentTogether.get(record.getEmployeeId()).put(otherRecord.getEmployeeId(), new HashMap<>());
                            timeSpentTogether.get(record.getEmployeeId()).get(otherRecord.getEmployeeId()).put(record.getProjectId(),
                                    ChronoUnit.DAYS.between(otherRecord.getStartDate(), otherRecord.getEndDate()));

                        }
                    } else {
                        timeSpentTogether.put(record.getEmployeeId(), new HashMap<>());
                    }
                }
            }
        }

        Long maxTimeTogether = 0L;
        Long employee1 = 0L;
        Long employee2 = 0L;

        for (Map.Entry<Long, Map<Long, Map<Long, Long>>> entry : timeSpentTogether.entrySet()) {
            Long currentEmployee1 = entry.getKey();
            Map<Long, Map<Long, Long>> innerMap = entry.getValue();

            for (Map.Entry<Long, Map<Long, Long>> innerEntry : innerMap.entrySet()) {
                Long currentEmployee2 = innerEntry.getKey();
                Map<Long, Long> projectMap = innerEntry.getValue();

                for (Map.Entry<Long, Long> projectEntry : projectMap.entrySet()) {
                    Long timeTogether = projectEntry.getValue();

                    if (timeTogether > maxTimeTogether) {
                        maxTimeTogether = timeTogether;
                        employee1 = currentEmployee1;
                        employee2 = currentEmployee2;
                    }
                }
            }
        }

        return new Long[]{employee1, employee2, maxTimeTogether};
    }

    private List<Long[]> findProjects(Long[] employeeIds) {
        if (timeSpentTogether.isEmpty() || employeeIds.length == 0) {
            employeeIds = findEmployees();
        }
        List<Long[]> projectTimeSpent = new ArrayList<>();
        Long employee1 = employeeIds[0];
        Long employee2 = employeeIds[1];

        Map<Long, Long> otherMap = timeSpentTogether.get(employee1).get(employee2);

        for (Map.Entry<Long, Long> projects : otherMap.entrySet()) {
            projectTimeSpent.add(new Long[]{projects.getKey(), projects.getValue()});
        }
        return projectTimeSpent;
    }

    private void saveTestJobs() {
        List<String[]> info = ReadFromCsv.read("import.csv");
        for (String[] line : info) {
            Long empId = null;
            Long projId = null;
            LocalDate sDate = null;
            LocalDate eDate = null;
            String employeeId = line[0];
            String projectId = line[1];
            String startDate = line[2];
            String endDate = line[3];

            if (employeeId.matches("[\\d]")) {
                empId = Long.parseLong(employeeId);
            }
            if (projectId.matches("[\\d]")) {
                projId = Long.parseLong(projectId);
            }
            if (startDate.matches(getRegex())) {
                sDate = StringToDate.toLocalDate(startDate);
            }
            if (!endDate.equalsIgnoreCase("null") && endDate.matches(getRegex())) {
                eDate = StringToDate.toLocalDate(endDate);
            }
            if (Objects.nonNull(empId) && Objects.nonNull(projId) && Objects.nonNull(sDate)) {
                repository.save(new Exam(empId, projId, sDate, eDate));
            }
        }
    }

    private static String getRegex() {
        StringBuilder builder = new StringBuilder();
        String delemiter = "((\\s)|(.)|(-)|(/)|(,)|(\\\\))";

        builder.append("(((3[01])|([12][0-9])|((0)?[1-9]))");
        builder.append(delemiter);
        builder.append("(((1[1,2])|((0)?[1-9]))|(");
        builder.append(ReadLabelledMonths.monthsToRegex() + "))");
        builder.append(delemiter);
        builder.append("(2[0-9]{3})");
        builder.append("(((\\s)?г(.)?)?))|");

        builder.append("(" + ReadLabelledMonths.monthsToRegex() + ")");
        builder.append(delemiter);
        builder.append("((3[01])|([12][0-9])|((0)?[1-9]))");
        builder.append(delemiter);
        builder.append("(2[0-9]{3})");
        builder.append("(((\\s)?г(.)?)?))|");

        builder.append("((2[0-9]{3})");
        builder.append(delemiter);
        builder.append("(((1[1,2])|((0)?[1-9]))|(");
        builder.append(ReadLabelledMonths.monthsToRegex() + "))");
        builder.append(delemiter);
        builder.append("((3[01])|([12][0-9])|((0)?[1-9])))");

        return builder.toString();
    }
}
