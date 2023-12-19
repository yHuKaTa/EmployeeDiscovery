package com.sirma.exam.runners;

import org.springframework.boot.CommandLineRunner;

public class JobsImporter implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        saveTestEmployees();
        saveTestProjects();
        importJobsFromCSV();
    }

    private void saveTestEmployees() {

    }

    private void saveTestProjects() {

    }

    private void importJobsFromCSV() {

    }
}
