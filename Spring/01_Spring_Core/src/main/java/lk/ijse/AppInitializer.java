package lk.ijse;

import lk.ijse.bean.SpringBean;
import lk.ijse.bean.TestBean1;
import lk.ijse.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInitializer {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();


        SpringBean bean = context.getBean(SpringBean.class);
        System.out.println(bean);
        bean.testBean();
        TestBean1 testBean1 = context.getBean(TestBean1.class);
        System.out.println(testBean1);

        context.close();
    }
}
