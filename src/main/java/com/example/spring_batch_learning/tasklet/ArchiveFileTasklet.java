package com.example.spring_batch_learning.tasklet;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ArchiveFileTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext) throws Exception {

        String inputFile =
                chunkContext.getStepContext()
                        .getJobParameters()
                        .get("inputFile")
                        .toString();

        String archiveDirectory =
                chunkContext.getStepContext()
                        .getJobParameters()
                        .get("archiveDirectory")
                        .toString();

        Path source = Path.of(inputFile);
        Path archiveDir = Path.of(archiveDirectory);

        Files.createDirectories(archiveDir);

        Path destination =
                archiveDir.resolve(source.getFileName());

        Files.move(
                source,
                destination,
                StandardCopyOption.REPLACE_EXISTING
        );

        System.out.println("File archived successfully: " + destination);

        return RepeatStatus.FINISHED;
    }
}