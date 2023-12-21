package com.sirma.exam.runners;

import com.sirma.exam.models.Exam;
import com.sirma.exam.repositories.ExamRepository;
import com.sirma.exam.utils.ReadFromCsv;
import com.sirma.exam.utils.ReadLabelledMonths;
import com.sirma.exam.utils.StringToDate;
import com.sirma.exam.utils.TripleMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class AdvanceLogicRunnerForEmployeePairs implements CommandLineRunner {
    private ExamRepository repository;
    // EmployeeId, EmployeeId, Time
    private final TripleMap<Long, Long, Long> pairTimes;
    // EmployeeId, EmployeeId, ProjectId
    private final TripleMap<Long, Long, List<Long>> projectsPairing;
    private final Set<Long> employeeIds;
    private List<Exam> allRecords;

    @Autowired
    public AdvanceLogicRunnerForEmployeePairs(ExamRepository repository) {
        this.repository = repository;
        this.pairTimes = new TripleMap<>();
        this.projectsPairing = new TripleMap<>();
        this.employeeIds = new HashSet<>();
        this.allRecords = repository.findAll();
    }

    @Override
    public void run(String... args) throws Exception {
        saveTestJobs();
        Long[] twoEmployees = findEmployees();
        List<Long[]> projects = findProjects(twoEmployees);
        if (Objects.nonNull(twoEmployees) && Objects.nonNull(projects)) {
            System.out.println("Top team workers are: " + twoEmployees[0] + " and " + twoEmployees[1] + ". They work together for: " + twoEmployees[2] + " days.");
            System.out.println("They work on projects:");
            for (Long[] project : projects) {
                System.out.println("Project ID: " + project[0] + " for time: " + project[1] + " days");
            }
        }
    }

    private Long[] findEmployees() {
        if (Objects.nonNull(allRecords)) {
            allRecords.forEach(record -> {
                employeeIds.add(record.getEmployeeId());
                allRecords.forEach(otherRecord -> {
                    if (record != otherRecord && recordsOverlap(record, otherRecord)) {
                        Long time = ChronoUnit.DAYS.between(otherRecord.getStartDate(), otherRecord.getEndDate());
                        if (pairTimes.contains(record.getEmployeeId(), otherRecord.getEmployeeId())) {
                            Long temp = pairTimes.get(record.getEmployeeId(), otherRecord.getEmployeeId()) + time;
                            pairTimes.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), temp);
                        } else {
                            pairTimes.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), time);
                        }
                    }
                });
            });
            return findMaxTimeTogether();
        } else return null;
    }

    private boolean recordsOverlap(Exam record1, Exam record2) {
        long time1 = ChronoUnit.DAYS.between(record1.getStartDate(), record1.getEndDate());
        long time2 = ChronoUnit.DAYS.between(record2.getStartDate(), record2.getEndDate());
        if (time1 >= time2) {
            return record1.getProjectId().equals(record2.getProjectId()) &&
                    (record1.getStartDate().isBefore(record2.getStartDate()) ||
                            record1.getStartDate().isEqual(record2.getStartDate())) &&
                    (record2.getStartDate().isBefore(record1.getEndDate()) ||
                            record2.getEndDate().isBefore(record1.getEndDate())
                            || record2.getEndDate().isEqual(record1.getEndDate()));
        } else {
            return record2.getProjectId().equals(record1.getProjectId()) &&
                    (record2.getStartDate().isBefore(record1.getStartDate()) ||
                            record2.getStartDate().isEqual(record1.getStartDate())) &&
                    (record1.getStartDate().isBefore(record2.getEndDate()) ||
                            record1.getEndDate().isBefore(record2.getEndDate())
                            || record1.getEndDate().isEqual(record2.getEndDate()));
        }
    }

    private Long[] findMaxTimeTogether() {
        long maxTimeTogether = 0L;
        Long employee1 = 0L;
        Long employee2 = 0L;
        if (Objects.nonNull(employeeIds)) {
            for (Long empId : employeeIds) {
                for (Long secondEmpl : employeeIds) {
                    Long currentTime = pairTimes.get(empId, secondEmpl);
                    if (maxTimeTogether < currentTime) {
                        maxTimeTogether = currentTime;
                        employee1 = empId;
                        employee2 = secondEmpl;
                    }
                }
            }
            return new Long[]{employee1, employee2, maxTimeTogether};
        } else return null;
    }

    private List<Long[]> findProjects(Long[] employeeIds) {
        if (pairTimes.isEmpty() || employeeIds.length == 0) {
            employeeIds = findEmployees();
        }
        if (Objects.nonNull(allRecords) && !allRecords.isEmpty() && Objects.nonNull(employeeIds)) {
            List<Long[]> projectTimeSpent = new ArrayList<>();
            Long employee1 = employeeIds[0];
            Long employee2 = employeeIds[1];

            allRecords.forEach(record -> {
                allRecords.forEach(otherRecord -> {
                    if (record != otherRecord && recordsOverlap(record, otherRecord)) {
                        if (!projectsPairing.contains(record.getEmployeeId(), otherRecord.getEmployeeId())) {
                            List<Long> projects = new ArrayList<>();
                            projects.add(record.getProjectId());
                            projectsPairing.addValue(record.getEmployeeId(), otherRecord.getEmployeeId(), projects);
                        } else {
                            projectsPairing.get(record.getEmployeeId(), otherRecord.getEmployeeId()).add(record.getProjectId());
                        }
                    }
                });
            });

            Map<Long, Long> timeOnProjects = new HashMap<>();
            for (Long projectId : projectsPairing.get(employee1, employee2)) {
                for (Exam record : allRecords) {
                    if (record.getProjectId().equals(projectId) && record.getEmployeeId().equals(employee2)) {
                        timeOnProjects.put(projectId, ChronoUnit.DAYS.between(record.getStartDate(), record.getEndDate()));
                    }
                }
            }

            for (Map.Entry<Long, Long> projects : timeOnProjects.entrySet()) {
                projectTimeSpent.add(new Long[]{projects.getKey(), projects.getValue()});
            }
            return projectTimeSpent;
        } else return null;
    }

    private void saveTestJobs() {
        Path path = Paths.get("input_data.csv");
        List<String[]> info = ReadFromCsv.read(path.toAbsolutePath().toString());
        if (Objects.nonNull(info)) {
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
