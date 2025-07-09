package com.twothree.backend.batch;

import com.twothree.backend.entity.Church;
import com.twothree.backend.enums.ChurchStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.item.ItemProcessor;
// import org.springframework.batch.item.ItemReader;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.database.JdbcBatchItemWriter;
// import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
// import org.springframework.batch.item.file.FlatFileItemReader;
// import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.io.ClassPathResource;
// import org.springframework.transaction.PlatformTransactionManager;

// import javax.sql.DataSource;

// @Configuration
// @RequiredArgsConstructor
// @Slf4j
// public class ChurchBatchConfig {
// 
//     private final JobRepository jobRepository;
//     private final PlatformTransactionManager transactionManager;
//     private final DataSource dataSource;
// 
//     @Bean
//     public Job importChurchJob() {
//         return new JobBuilder("importChurchJob", jobRepository)
//                 .start(importChurchStep())
//                 .build();
//     }
// 
//     @Bean
//     public Step importChurchStep() {
//         return new StepBuilder("importChurchStep", jobRepository)
//                 .<Church, Church>chunk(100, transactionManager)
//                 .reader(churchItemReader())
//                 .processor(churchItemProcessor())
//                 .writer(churchItemWriter())
//                 .build();
//     }
// 
//     @Bean
//     public FlatFileItemReader<Church> churchItemReader() {
//         return new FlatFileItemReaderBuilder<Church>()
//                 .name("churchItemReader")
//                 .resource(new ClassPathResource("data/churches.csv"))
//                 .delimited()
//                 .names("name", "address", "phone", "email", "pastorName", "status")
//                 .fieldSetMapper(fieldSet -> {
//                     Church church = new Church();
//                     church.setName(fieldSet.readString("name"));
//                     church.setAddress(fieldSet.readString("address"));
//                     church.setPhone(fieldSet.readString("phone"));
//                     church.setEmail(fieldSet.readString("email"));
//                     church.setPastorName(fieldSet.readString("pastorName"));
//                     church.setStatus(ChurchStatus.valueOf(fieldSet.readString("status")));
//                     return church;
//                 })
//                 .build();
//     }
// 
//     @Bean
//     public ItemProcessor<Church, Church> churchItemProcessor() {
//         return church -> {
//             log.info("Processing church: {}", church.getName());
//             // 여기서 데이터 검증 및 변환 로직 추가
//             return church;
//         };
//     }
// 
//     @Bean
//     public JdbcBatchItemWriter<Church> churchItemWriter() {
//         return new JdbcBatchItemWriterBuilder<Church>()
//                 .dataSource(dataSource)
//                 .sql("INSERT INTO churches (name, address, phone, email, pastor_name, status, created_at, updated_at) " +
//                      "VALUES (:name, :address, :phone, :email, :pastorName, :status, NOW(), NOW())")
//                 .beanMapped()
//                 .build();
//     }
// } 