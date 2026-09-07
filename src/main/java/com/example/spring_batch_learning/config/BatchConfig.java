package com.example.spring_batch_learning.config;

import com.example.spring_batch_learning.model.Trade;
import javax.sql.DataSource;
import com.example.spring_batch_learning.processor.TradeProcessor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class BatchConfig {

    @Bean
    public FlatFileItemReader<Trade> reader() {

        return new FlatFileItemReaderBuilder<Trade>()
                .name("tradeReader")
                .resource(new ClassPathResource("trades.csv"))
                .delimited()
                .names("tradeId", "symbol", "quantity", "price")
                .targetType(Trade.class)
                .build();
    }

    @Bean
    public TradeProcessor processor() {
        return new TradeProcessor();
    }

    @Bean
    public com.example.batch.writer.TradeWriter writer(DataSource dataSource) {
        return new com.example.batch.writer.TradeWriter(dataSource);
    }

    @Bean
    public Step tradeStep(
            JobRepository jobRepository,
            FlatFileItemReader<Trade> reader,
            TradeProcessor processor,
            com.example.batch.writer.TradeWriter writer) {

        return new StepBuilder("tradeStep", jobRepository)
                .<Trade, Trade>chunk(2)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job tradeJob(
            JobRepository jobRepository,
            Step tradeStep) {

        return new JobBuilder("tradeJob", jobRepository)
                .start(tradeStep)
                .build();
    }
}