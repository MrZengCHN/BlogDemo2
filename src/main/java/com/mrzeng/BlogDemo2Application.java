package com.mrzeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mrzeng.blogdemo2.mapper")
public class BlogDemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(BlogDemo2Application.class, args);
    }

}
