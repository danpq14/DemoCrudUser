package com.app.asp;

import com.app.util.LoggerInterface;
import java.util.logging.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ASPMain implements LoggerInterface {

    public static ApplicationContext appContext;

    public ASPMain() {
    }

    public static void main(String[] args) {
        try {
            appContext = new ClassPathXmlApplicationContext("app-context.xml");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
