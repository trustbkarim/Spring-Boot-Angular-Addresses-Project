package com.firstapp.firstapp.SpringApplicationContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContext implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        CONTEXT = applicationContext;
    }

    // récuperér (instance d'une classe) n'importe quel object dans cette app en mentionnant le nom de l'objet
    public static Object getBean(String beanName)
    {
        return CONTEXT.getBean(beanName);
    }
}
