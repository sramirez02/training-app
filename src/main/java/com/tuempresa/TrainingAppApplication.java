package com.tuempresa;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.catalina.startup.Tomcat;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.tuempresa.config.DataConfig;
import com.tuempresa.config.WebConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ComponentScan(basePackages = "com.tuempresa")
public class TrainingAppApplication {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        
        Context ctx = tomcat.addContext("", null);
        
//        Tomcat.addServlet(ctx, "default", new DefaultServlet());
//        ctx.addServletMappingDecoded("/", "default");
       
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(WebConfig.class, DataConfig.class);
        
        DispatcherServlet servlet = new DispatcherServlet(appContext);
        Wrapper wrapper = Tomcat.addServlet(ctx, "dispatcher", servlet);
        wrapper.setLoadOnStartup(1);
        ctx.addServletMappingDecoded("/*", "dispatcher");
        
        tomcat.start();
        int port = tomcat.getConnector().getPort();
        log.info("Aplicación iniciada en http://localhost:{}", port);
        tomcat.getServer().await();
    }
    
    
    
}