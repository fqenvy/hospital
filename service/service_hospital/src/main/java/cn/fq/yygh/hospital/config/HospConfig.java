package cn.fq.yygh.hospital.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.fq.yygh.hospital.mapper")
public class HospConfig {
}
