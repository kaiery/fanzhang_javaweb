package com.kaiery.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by 范张 on 2016-10-15 7:46.
 */
public class Factory {
    private Factory() {
        System.err.println("------不能实例化-------"+this.getClass().getName());
    }

    private static class SingletonHolder {
        private final static BeanFactory INSTANCE =  new ClassPathXmlApplicationContext("applicationContext.xml");
    }

    public static BeanFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
