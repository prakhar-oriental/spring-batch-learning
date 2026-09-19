package com.example.spring_batch_learning.listner;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.listener.StepExecutionListener;
import org.springframework.batch.core.step.StepExecution;


public class TradeStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println(
                "STEP STARTED: " + stepExecution.getStepName()
        );
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        System.out.println(
                "STEP FINISHED: " + stepExecution.getStepName()
        );

        System.out.println(
                "Read count: " + stepExecution.getReadCount()
        );

        System.out.println(
                "Write count: " + stepExecution.getWriteCount()
        );

        return stepExecution.getExitStatus();
    }
}