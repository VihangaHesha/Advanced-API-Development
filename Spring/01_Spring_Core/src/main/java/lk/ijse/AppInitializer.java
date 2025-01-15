package lk.ijse;

import lk.ijse.bean.MyConnection;
import lk.ijse.bean.SpringBean;
import lk.ijse.bean.TestBean1;
import lk.ijse.bean.TestBean2;
import lk.ijse.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInitializer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

      /*  Object bean = context.getBean("springBean");
        System.out.println(bean);

        TestBean1 testBean1 = context.getBean("testBean1",TestBean1.class);
        System.out.println(testBean1);

        TestBean2 testBean2 = context.getBean(TestBean2.class);
        System.out.println(testBean2);

        MyConnection myConnectionBean = context.getBean(MyConnection.class);
        System.out.println(myConnectionBean);

        MyConnection myConnectionBean1 = (MyConnection) context.getBean("getConnection");
        System.out.println(myConnectionBean1);*/

        context.registerShutdownHook();
    }
}
