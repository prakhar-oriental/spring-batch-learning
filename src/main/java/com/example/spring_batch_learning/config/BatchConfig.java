package com.example.spring_batch_learning.config;

import com.example.spring_batch_learning.listner.JobCompletionListener;
import com.example.spring_batch_learning.listner.TradeStepListener;
import com.example.spring_batch_learning.model.Trade;
import com.example.spring_batch_learning.processor.TradeProcessor;
import com.example.spring_batch_learning.tasklet.ArchiveFileTasklet;
import com.example.spring_batch_learning.tasklet.FileValidationTasklet;
import com.example.batch.writer.TradeWriter;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    // ---------------------------
    // READER
    // ---------------------------

    @Bean
    @StepScope
    public FlatFileItemReader<Trade> reader(@Value("#{jobParameters['inputFile']}") String inputFile) {

        return new FlatFileItemReaderBuilder<Trade>()
                .name("tradeReader")
                .resource(new FileSystemResource(inputFile))
                .delimited()
                .names("tradeId", "symbol", "quantity", "price")
                .targetType(Trade.class)
                .build();
    }


    // ---------------------------
    // PROCESSOR
    // ---------------------------

    @Bean
    public TradeProcessor processor() {
        return new TradeProcessor();
    }


    // ---------------------------
    // WRITER
    // ---------------------------

    @Bean
    public TradeWriter writer(DataSource dataSource) {
        return new TradeWriter(dataSource);
    }


    // ---------------------------
    // TASKLET
    // ---------------------------

    @Bean
    public FileValidationTasklet fileValidationTasklet() {
        return new FileValidationTasklet();
    }


    // ---------------------------
    // TASKLET STEP
    // ---------------------------

    @Bean
    public Step fileValidationStep(
            JobRepository jobRepository,
            FileValidationTasklet fileValidationTasklet,
            TradeStepListener tradeStepListener
    ) {

        return new StepBuilder("fileValidationStep", jobRepository)
                .listener(tradeStepListener)
                .tasklet(fileValidationTasklet)
                .build();
    }


    // ---------------------------
    // CHUNK STEP
    // ---------------------------

    @Bean
    public Step tradeStep(
            JobRepository jobRepository,
            FlatFileItemReader<Trade> reader,
            TradeProcessor processor,
            TradeWriter writer) {

        return new StepBuilder("tradeStep", jobRepository)
                .<Trade, Trade>chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                .skip(RuntimeException.class)
                .skipLimit(2)
                .build();
    }

    @Bean
    public ArchiveFileTasklet archiveFileTasklet() {
        return new ArchiveFileTasklet();
    }

    @Bean
    public Step archiveFileStep(
            JobRepository jobRepository,
            ArchiveFileTasklet archiveFileTasklet) {

        return new StepBuilder("archiveFileStep", jobRepository)
                .tasklet(archiveFileTasklet)
                .build();
    }

    @Bean
    public JobCompletionListener jobCompletionListener() {
        return new JobCompletionListener();
    }

    @Bean
    public TradeStepListener tradeStepListener() {
        return new TradeStepListener();
    }
    // ---------------------------
    // JOB
    // ---------------------------

    @Bean
    public Job tradeJob(
            JobRepository jobRepository,
            Step fileValidationStep,
            Step tradeStep,
            Step archiveFileStep,
            JobCompletionListener jobCompletionListener) {

            return new JobBuilder("tradeJob", jobRepository)
                    .listener(jobCompletionListener)
                    .start(fileValidationStep)
                    .next(tradeStep)
                    .next(archiveFileStep)
                    .build();
    }
}