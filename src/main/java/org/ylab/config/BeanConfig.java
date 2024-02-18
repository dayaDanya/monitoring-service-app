package org.ylab.config;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.ylab.domain.models.Operation;
import org.ylab.infrastructure.in.db.ConnectionAdapter;
import org.ylab.infrastructure.mappers.*;
import org.ylab.infrastructure.yamlpojos.YmlPojo;

@Configuration
public class BeanConfig {

    @Bean
    public YmlPojo
    @Bean
    public ConnectionAdapter connectionAdapter(){
        return new ConnectionAdapter();
    }
    @Bean
    public CounterMapper counterMapper(){
        return Mappers.getMapper(CounterMapper.class);
    }
    @Bean
    public CounterTypeMapper counterTypeMapper(){
        return Mappers.getMapper(CounterTypeMapper.class);
    }
    @Bean
    public MeasurementInMapper measurementInMapper(){
        return Mappers.getMapper(MeasurementInMapper.class);
    }
    @Bean
    public MeasurementOutMapper measurementOutMapper(){
        return Mappers.getMapper(MeasurementOutMapper.class);
    }
    @Bean
    public OperationOutMapper operationOutMapper(){
        return Mappers.getMapper(OperationOutMapper.class);
    }
    @Bean
    public PersonInputMapper personInputMapper(){
        return Mappers.getMapper(PersonInputMapper.class);
    }

}
