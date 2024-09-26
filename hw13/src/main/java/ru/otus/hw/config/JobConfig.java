package ru.otus.hw.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.support.builder.CompositeItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.dto.BookDto;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;

import javax.sql.DataSource;
import java.util.HashMap;

@SuppressWarnings("unused")
@Configuration
public class JobConfig {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private static final int CHUNK_SIZE = 5;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MongoAuthorRepository mongoAuthorRepository;

    @Autowired
    private MongoBookRepository mongoBookRepository;

    @Bean
    public Job importLibraryJob(Step createTempAuthorTable,
                                Step authorMigrationStep,
                                Step bookMigrationStep,
                                Step dropTempAuthorTable
    ) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(createTempAuthorTable)
                .next(authorMigrationStep)
                .next(bookMigrationStep)
                .next(dropTempAuthorTable)
                .build();
    }

    @Bean
    public Step createTempAuthorTable() {
        return new StepBuilder("createTempAuthorTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "CREATE TABLE temp_authors (id_mongo VARCHAR(255) NOT NULL, id_jpa BIGSERIAL NOT NULL)"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }

    @Bean
    public Step authorMigrationStep(RepositoryItemReader<MongoAuthor> reader,
                                    CompositeItemWriter<MongoAuthor> writer,
                                    ItemProcessor<MongoAuthor, MongoAuthor> processor) {
        return new StepBuilder("authorMigrationStep", jobRepository)
                .<MongoAuthor, MongoAuthor>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoAuthor> authorReader() {
        return new RepositoryItemReaderBuilder<MongoAuthor>()
                .name("authorReader")
                .repository(mongoAuthorRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public ItemProcessor<MongoAuthor, MongoAuthor> authorProcessor() {
        return (author) -> author;
    }

    @Bean
    public CompositeItemWriter<MongoAuthor> compositeAuthorWriter(
            JdbcBatchItemWriter<MongoAuthor> authorInsertTempTable,
            JdbcBatchItemWriter<MongoAuthor> authorJdbcBatchItemWriter
    ) {
        return new CompositeItemWriterBuilder<MongoAuthor>()
                .delegates(authorInsertTempTable, authorJdbcBatchItemWriter)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<MongoAuthor> authorInsertTempTable() {
        return new JdbcBatchItemWriterBuilder<MongoAuthor>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO temp_authors (id_mongo, id_jpa) " +
                        "VALUES (:id, SELECT nextval('author_id_seq'))")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<MongoAuthor> authorJdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<MongoAuthor>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO authors(id, full_name) " +
                        "VALUES ((SELECT id_jpa FROM temp_authors WHERE id_mongo = :id), :fullName)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step bookMigrationStep(RepositoryItemReader<MongoBook> reader,
                                  CompositeItemWriter<BookDto> writer, ItemProcessor<MongoBook, BookDto> processor) {
        return new StepBuilder("bookMigrationStep", jobRepository)
                .<MongoBook, BookDto>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public RepositoryItemReader<MongoBook> bookReader() {
        return new RepositoryItemReaderBuilder<MongoBook>()
                .name("bookReader")
                .repository(mongoBookRepository)
                .methodName("findAll")
                .pageSize(100)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public ItemProcessor<MongoBook, BookDto> bookProcessor() {
        return (book) -> new BookDto(book.getId(), book.getTitle(), book.getAuthor().getId());
    }

    @Bean
    public CompositeItemWriter<BookDto> compositeBookWriter(JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter) {
        return new CompositeItemWriterBuilder<BookDto>()
                .delegates(bookJdbcBatchItemWriter)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<BookDto> bookJdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<BookDto>()
                .itemPreparedStatementSetter((bookDto, statement) -> {
                    statement.setString(1, bookDto.getTitle());
                    statement.setString(2, bookDto.getAuthorId());
                })
                .sql("INSERT INTO books(title, id, author_id) " +
                        "VALUES (?, " +
                        "(SELECT nextval('book_id_seq')), " +
                        "(SELECT id_jpa FROM temp_authors WHERE id_mongo = ?))")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step dropTempAuthorTable() {
        return new StepBuilder("dropTempAuthorTable", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((contribution, chunkContext) -> {
                    new JdbcTemplate(dataSource).execute(
                            "DROP TABLE temp_authors"
                    );
                    return RepeatStatus.FINISHED;
                }), platformTransactionManager)
                .build();
    }
}
