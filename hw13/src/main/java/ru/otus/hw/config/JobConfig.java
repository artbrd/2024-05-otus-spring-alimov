package ru.otus.hw.config;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.MongoPagingItemReader;
import org.springframework.batch.item.data.builder.MongoPagingItemReaderBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.services.AuthorTransformService;

import java.util.HashMap;

@SuppressWarnings("unused")
@Configuration
public class JobConfig {
    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private static final int CHUNK_SIZE = 5;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Bean
    public MongoPagingItemReader<MongoBook> bookReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<MongoBook>()
                .name("mongoBookItemReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(MongoBook.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public MongoPagingItemReader<MongoAuthor> authorReader(MongoTemplate mongoTemplate) {
        return new MongoPagingItemReaderBuilder<MongoAuthor>()
                .name("mongoAuthorItemReader")
                .template(mongoTemplate)
                .jsonQuery("{}")
                .targetType(MongoAuthor.class)
                .sorts(new HashMap<>())
                .build();
    }

    @Bean
    public ItemProcessor<MongoBook, JpaBook> bookProcessor(AuthorTransformService authorTransformService) {
        return (book) -> new JpaBook(0, book.getTitle(),
                authorTransformService.getAuthor(book.getAuthor().getId()));
    }

    @Bean
    public ItemProcessor<MongoAuthor, Void> authorProcessor(AuthorTransformService authorTransformService) {
        return (author) -> {
            authorTransformService.process(author);
            return null;
        };
    }

    @Bean
    public JpaItemWriter<JpaBook> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<JpaBook>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean
    public Job importLibraryJob(Step transformAuthorStep, Step transformBooksStep) {
        return new JobBuilder(IMPORT_LIBRARY_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transformAuthorStep)
                .next(transformBooksStep)
                .build();
    }

    @Bean
    public Step transformBooksStep(ItemReader<MongoBook> reader, JpaItemWriter<JpaBook> writer,
                                   ItemProcessor<MongoBook, JpaBook> processor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<MongoBook, JpaBook>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transformAuthorStep(ItemReader<MongoAuthor> reader, ItemProcessor<MongoAuthor, Void> processor) {
        return new StepBuilder("transformBooksStep", jobRepository)
                .<MongoAuthor, Void>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(chunk -> { })
                .build();
    }
}
