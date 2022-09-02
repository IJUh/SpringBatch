package com.example.springbatch.scheduler;

import com.example.springbatch.job.UserJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@EnableScheduling
public class UserJobScheduler {

    private final JobLauncher jobLauncher;

    private final UserJob userJob;


    public UserJobScheduler(JobLauncher jobLauncher, UserJob userJob) {
        this.jobLauncher = jobLauncher;
        this.userJob = userJob;
    }

    // 스케쥴 설정
    @Scheduled(cron="0 0/1 * * * *")
    public void rundJob() {

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try {
            jobLauncher.run(userJob.UserInfoJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException | JobRestartException e) {

            log.error(e.getMessage());
        }

    }
}
