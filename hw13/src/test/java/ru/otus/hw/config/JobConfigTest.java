package ru.otus.hw.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.otus.hw.models.jpa.JpaAuthor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.repositories.jpa.JpaAuthorRepository;
import ru.otus.hw.repositories.jpa.JpaBookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
class JobConfigTest {

    private static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JpaAuthorRepository jpaAuthorRepository;

    @Autowired
    private JpaBookRepository jpaBookRepository;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
                .extracting(Job::getName)
                .isEqualTo(IMPORT_LIBRARY_JOB_NAME);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters());

        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        var authors = jpaAuthorRepository.findAll();
        assertThat(authors)
                .isNotEmpty()
                .isEqualTo(getExpectedAuthors());

        var books = jpaBookRepository.findAll();
        assertThat(books)
                .isNotEmpty()
                .isEqualTo(getExpectedBooks());
    }

    private List<JpaAuthor> getExpectedAuthors() {
        return List.of(
                new JpaAuthor(1000L, "Author_1"),
                new JpaAuthor(1001L, "Author_2"),
                new JpaAuthor(1002L, "Author_3"));
    }

    private List<JpaBook> getExpectedBooks() {
        return List.of(
                new JpaBook(1000L, "BookTitle_1", new JpaAuthor(1000L, "Author_1")),
                new JpaBook(1001L, "BookTitle_2", new JpaAuthor(1001L, "Author_2")),
                new JpaBook(1002L, "BookTitle_3", new JpaAuthor(1002L, "Author_3")));
    }
}