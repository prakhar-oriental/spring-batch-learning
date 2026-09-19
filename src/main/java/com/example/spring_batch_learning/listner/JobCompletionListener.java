package com.example.spring_batch_learning.listner;


import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;

public class JobCompletionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("JOB STARTED");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println(
                "JOB FINISHED WITH STATUS: "
                        + jobExecution.getStatus()
        );
    }
}