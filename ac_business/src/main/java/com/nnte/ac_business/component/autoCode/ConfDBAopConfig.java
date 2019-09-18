package com.nnte.ac_business.component.autoCode;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("com.nnte.ac_business.component.autoCode")
@EnableAspectJAutoProxy//开启AspectJ注解
public class ConfDBAopConfig {
}
