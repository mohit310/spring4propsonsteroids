package com.db;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");
        MidasPropertyLoader bean = (MidasPropertyLoader) context.getBean("prop-loader");
        Map<String, String> myMap = bean.getMap("irds");
        System.out.println(myMap);

        Map<String, Map<String, String>> myNewMap = bean.getMapOfMap("midas.country");
        System.out.println(myNewMap);
        context.close();
    }
}
