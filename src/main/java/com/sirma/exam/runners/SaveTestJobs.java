package com.sirma.exam.runners;

import com.sirma.exam.models.Exam;
import com.sirma.exam.repositories.ExamRepository;
import com.sirma.exam.utils.ReadFromCsv;
import com.sirma.exam.utils.RegExTemplate;
import com.sirma.exam.utils.StringToDate;
import jakarta.annotation.Priority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
@Priority(1)
public class SaveTestJobs implements CommandLineRunner {
    private ExamRepository repository;

    @Autowired
    public SaveTestJobs(ExamRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        Path path = Paths.get("input_data.csv");
        List<String[]> info = ReadFromCsv.read(path.toAbsolutePath().toString());
        if (Objects.nonNull(info)) {
            String regex = RegExTemplate.getRegex();
            for (String[] line : info) {
                if (line.length == 4) {
                    Long empId = null;
                    Long projId = null;
                    LocalDate sDate = null;
                    LocalDate eDate = null;
                    String employeeId = line[0];
                    String projectId = line[1];
                    String startDate = line[2];
                    String endDate = line[3];

                    if (employeeId.matches("[\\d]+")) {
                        empId = Long.parseLong(employeeId);
                    }
                    if (projectId.matches("[\\d]+")) {
                        projId = Long.parseLong(projectId);
                    }
                    if (startDate.matches(regex)) {
                        sDate = StringToDate.toLocalDate(startDate);
                    }
                    if (!endDate.equalsIgnoreCase("null") && endDate.matches(regex)) {
                        eDate = StringToDate.toLocalDate(endDate);
                    }
                    if (Objects.nonNull(empId) && Objects.nonNull(projId) && Objects.nonNull(sDate) && Objects.isNull(eDate)) {
                        repository.save(new Exam(empId, projId, sDate, eDate));
                    } else if (Objects.nonNull(empId) && Objects.nonNull(projId) && Objects.nonNull(sDate) && eDate.isAfter(sDate)) {
                        repository.save(new Exam(empId, projId, sDate, eDate));
                    }
                }
            }
        }
    }
}
