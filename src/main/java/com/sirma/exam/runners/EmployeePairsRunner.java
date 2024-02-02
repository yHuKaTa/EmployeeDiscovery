package com.sirma.exam.runners;

import com.sirma.exam.models.History;
import com.sirma.exam.models.Pair;
import com.sirma.exam.repositories.HistoryRepository;
import com.sirma.exam.repositories.PairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class EmployeePairsRunner implements CommandLineRunner {
    private HistoryRepository historyRepository;

    private PairRepository pairRepository;

    private List<History> allHistories;

    private List<Pair> allPairs;

    @Autowired
    public EmployeePairsRunner(HistoryRepository historyRepository, PairRepository pairRepository) {
        this.historyRepository = historyRepository;
        this.pairRepository = pairRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Long[] topEmployeesWithTime = findEmployees();
        List<Long[]> projects = findProjects(topEmployeesWithTime);
        if (Objects.nonNull(topEmployeesWithTime) && Objects.nonNull(projects)) {
            System.out.println("Top team workers are: " + topEmployeesWithTime[0] + " and " + topEmployeesWithTime[1] + ". They work together for: " + topEmployeesWithTime[2] + " days.");
            System.out.println("They work on projects:");
            for (Long[] project : projects) {
                System.out.println("Project ID: " + project[0] + " for time: " + project[1] + " days");
            }
        }
    }

    private Long[] findEmployees() {
        allHistories = historyRepository.findAll();
        // For all possible cases to optimize speed of this method
        if (allHistories.isEmpty() || allHistories.size() == 1) {
            return null;
        } else if (allHistories.size() == 2 && !(allHistories.get(0).getProjectId().equals(allHistories.get(1).getProjectId()))) {
            return null;
        } else if (allHistories.size() == 2 && allHistories.get(0).getProjectId().equals(allHistories.get(1).getProjectId()) && !recordsOverlap(allHistories.get(0), allHistories.get(1))) {
            return null;
        } else if (allHistories.size() == 2 && allHistories.get(0).getProjectId().equals(allHistories.get(1).getProjectId()) && recordsOverlap(allHistories.get(0), allHistories.get(1))) {
            return new Long[]{allHistories.get(0).getEmployeeId(), allHistories.get(1).getEmployeeId(), calculateDates(allHistories.get(0), allHistories.get(1))};
        }

        for (History history : allHistories) {
            for (History otherHistory : allHistories) {
                if (!(history.getEmployeeId().equals(otherHistory.getEmployeeId())) &&
                        history.getProjectId().equals(otherHistory.getProjectId()) &&
                        recordsOverlap(history, otherHistory)) {
                    if (pairRepository.notExistsInDB(history.getEmployeeId(), otherHistory.getEmployeeId(), history.getProjectId(), calculateStartDate(history, otherHistory), calculateEndDate(history, otherHistory))) {
                        pairRepository.save(new Pair(history.getEmployeeId(), otherHistory.getEmployeeId(), history.getProjectId(), calculateStartDate(history, otherHistory), calculateEndDate(history, otherHistory)));
                    }
                }
            }
        }

        allPairs = pairRepository.findAll();
        long maxTime = 0L;
        long employee1 = 0L;
        long employee2 = 0L;
        for (Pair pair : allPairs) {
            long currentTime = 0L;
            for (Pair otherPair : allPairs) {
                if (pair.getEmployeeId().equals(otherPair.getEmployeeId()) && pair.getOtherEmployeeId().equals(otherPair.getOtherEmployeeId())) {
                    currentTime += otherPair.getTimeStamp();
                }
            }
            if (maxTime < currentTime) {
                maxTime = currentTime;
                employee1 = pair.getEmployeeId();
                employee2 = pair.getOtherEmployeeId();
            }
        }
        // Pair of employees and time they work together for the result
        return new Long[]{employee1, employee2, maxTime};
    }

    private LocalDate calculateStartDate(History history, History otherHistory) {
        if (history.getStartDate().isAfter(otherHistory.getStartDate())) {
            return history.getStartDate();
        } else {
            return otherHistory.getStartDate();
        }
    }

    private LocalDate calculateEndDate(History history, History otherHistory) {
        if (Objects.isNull(history.getEndDate()) && Objects.isNull(otherHistory.getEndDate())) {
            return null;
        } else if (Objects.isNull(history.getEndDate())) {
            return otherHistory.getEndDate();
        } else if (Objects.isNull(otherHistory.getEndDate())) {
            return history.getEndDate();
        } else if (history.getEndDate().isAfter(otherHistory.getEndDate())) {
            return otherHistory.getEndDate();
        } else {
            return history.getEndDate();
        }
    }

    private Long calculateDates(History history, History otherHistory) {
        if (isPeriodGraterThen(history, otherHistory)) {
            return ChronoUnit.DAYS.between(otherHistory.getStartDate(), history.getEndDate());
        } else {
            return ChronoUnit.DAYS.between(history.getStartDate(), otherHistory.getEndDate());
        }
    }

    private boolean isPeriodGraterThen(History history, History otherHistory) {
        long time1 = ChronoUnit.DAYS.between(history.getStartDate(), history.getValidEndDate());
        long time2 = ChronoUnit.DAYS.between(otherHistory.getStartDate(), otherHistory.getValidEndDate());
        return (time1 >= time2);
    }

    private boolean recordsOverlap(History history, History otherHistory) {
        if (history.getValidEndDate().isBefore(otherHistory.getStartDate())) {
            return false;
        }
        if (otherHistory.getValidEndDate().isBefore(history.getStartDate())) {
            return false;
        }
        if (isPeriodGraterThen(history, otherHistory)) {
            return (history.getStartDate().isBefore(otherHistory.getStartDate()) ||
                    history.getStartDate().isEqual(otherHistory.getStartDate()));
        } else {
            return (otherHistory.getStartDate().isBefore(history.getStartDate()) ||
                    otherHistory.getStartDate().isEqual(history.getStartDate()));
        }
    }

    private List<Long[]> findProjects(Long[] employeeIds) {
        if (Objects.isNull(employeeIds)) {
            return null;
        } else if (Objects.isNull(allPairs) || allPairs.isEmpty()) {
            return Collections.singletonList(new Long[]{allHistories.get(0).getProjectId(), calculateDates(allHistories.get(0), allHistories.get(1))});
        }

        List<Long[]> timeInProject = new ArrayList<>();
        long employee1 = employeeIds[0];
        long employee2 = employeeIds[1];

        List<Pair> projectsList = pairRepository.findByEmployeesId(employee1, employee2);

        for (Pair project : projectsList) {
            timeInProject.add(new Long[]{project.getProjectId(), project.getTimeStamp()});
        }
        return timeInProject;
    }
}
