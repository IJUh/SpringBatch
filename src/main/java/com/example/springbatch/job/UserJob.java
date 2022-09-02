package com.example.springbatch.job;

import com.example.springbatch.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@EnableBatchProcessing
public class UserJob {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

    private final SqlSessionFactory sqlSessionFactory;

    private final int fetchSize = 500;


    public UserJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource, SqlSessionFactory sqlSessionFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    // job regist
    @Bean
    public Job UserInfoJob() {
        return this.jobBuilderFactory.get("UserInfoJob")
                .start(startStep())
                //.on("FAILED")
                  //  .to(failOfStep())
                    //.on("*")
                    //.to(writeStep())
                    //.on("*")
                   // .end()
                .build();
    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get("startStep")
                .<User,User>chunk(100)
                .reader(readUserInfo())
                .writer(writer())
                .build();
    }

    /*@Bean
    public Step failOfStep() {
        return stepBuilderFactory.get("failOfStep")
                        .<User,String>chunk(100)
                        .reader(items -> {

                            return null;
                        })
                        .writer(list -> list.stream().map)
                        .build();
    }*/

    @Bean
    public ItemReader<User> readUserInfo() {

        return new MyBatisPagingItemReaderBuilder<User>()
                .sqlSessionFactory(sqlSessionFactory)
                .queryId("com.example.springbatch.mapper.MemberMapper.getUserInfo")
                .pageSize(200)
                .build();

       /* Map<String, Object> parameters = new HashMap<>();
                parameters.put("cst_cd", "01109");

        return new JdbcCursorItemReaderBuilder<User>()
                .fetchSize(fetchSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .sql("select cst_cd as id from tb_cst where cst_cd = :cst_cd")
                .name("readUser")
                .build();*/
    }

    @Bean
        public ItemWriter<User> writer() {

            return items -> {
                log.info("itemWriter ....");
            };
            /*return new JdbcBatchItemWriterBuilder<User>()
                    .dataSource(dataSource)
                    .sql("update tb_cst set cst_cd = :id where cst_cd = :id")
                    .beanMapped()
                    .build();*/
        }
}
