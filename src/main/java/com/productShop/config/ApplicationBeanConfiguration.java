package com.productShop.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.productShop.utils.FileIOUtil;
import com.productShop.utils.FileIOUtilImpl;
import com.productShop.utils.ValidationUtil;
import com.productShop.utils.ValidationUtilImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .create();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ValidationUtil validationUtil() {
        return new ValidationUtilImpl();
    }

    @Bean
    public FileIOUtil fileIOUtil() {
        return new FileIOUtilImpl();
    }

    @Bean
    public BufferedReader reader() {
        return new BufferedReader(new InputStreamReader(System.in));
    }


}
