package com.example.spring_batch_learning.tasklet;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.core.io.ClassPathResource;

import java.nio.file.Files;
import java.nio.file.Path;

@StepScope
public class FileValidationTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext) {

        String inputFile = chunkContext
                .getStepContext()
                .getJobParameters()
                .get("inputFile")
                .toString();

        Path file = Path.of(inputFile);

        if (!Files.exists(file)) {
            throw new RuntimeException("Input file not found: " + inputFile);
        }

        System.out.println("File validation successful: " + inputFile);
        System.out.println("File Size: " + file.toFile().length());

        return RepeatStatus.FINISHED;
    }
}
