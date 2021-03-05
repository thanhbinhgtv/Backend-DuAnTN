package duantn.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContext;

@SpringBootTest
class DuantnBackendApplicationTests {
    @Autowired
    ServletContext servletContext;
    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        System.out.println("context path:" +servletContext.getContextPath());
        System.out.println("getServletContextName():" +servletContext.getServletContextName());
        System.out.println("getServerInfo(): "+servletContext.getServerInfo());

        System.out.println(applicationContext.getApplicationName());
        System.out.println(applicationContext.getDisplayName());
    }

}
