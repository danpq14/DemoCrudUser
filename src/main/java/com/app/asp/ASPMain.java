/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.app.asp;

import com.app.util.LoggerInterface;
import java.util.logging.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author hungdt
 */
public class ASPMain implements LoggerInterface {

    public static ApplicationContext appContext;
//    public static PortalService portalService;

    public ASPMain() {
    }

    public static void main(String[] args) {
//        List list;
        try {
            appContext = new ClassPathXmlApplicationContext("app-context.xml");

//            portalService = appContext.getBean("portalService", PortalService.class);
//            list = portalService.listData(0, 10);
//            System.out.println("list size = " + list.size());
//            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
//                Object next = iterator.next();
//                logger.info(next.toString());
//            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
        }
    }
}
