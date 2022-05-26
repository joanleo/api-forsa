package com.prueba.util;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.prueba.entity.Producto;

//@Configuration
//@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Value("${file.input}")
    private String fileInput;

    @Bean
    public FlatFileItemReader<Producto> reader() {
        return new FlatFileItemReaderBuilder<Producto>().name("productoItemReader")
            .resource(new ClassPathResource(fileInput))
            .delimited().delimiter("|")
            .names(new String[] { "codigoPieza", "descripcion", "area", "orden", "familia", "fabricante", "empresa" })
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Producto>() {{
                setTargetType(Producto.class);
             }})
            .build();
    }

    @Bean
    public ProductoItemProcessor processor() {
        return new ProductoItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Producto> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Producto>()
        		.itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO mov_activos (vccodigopieza, vcnombre, narea, vcorden, nidfamilia, vcnitfabricante, vcnitEmpresa) "
            		+ "VALUES (:vccodigopieza, :vcnombre, :narea, :vcorden, :nidfamilia, :vcnitfabricante, :vcnitEmpresa)")
            .dataSource(dataSource)
            .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Producto> writer) {
        return stepBuilderFactory.get("step1")
            .<Producto, Producto> chunk(10)
            .reader(reader())
            .processor(processor())
            .writer(writer)
            .build();
    }

}
