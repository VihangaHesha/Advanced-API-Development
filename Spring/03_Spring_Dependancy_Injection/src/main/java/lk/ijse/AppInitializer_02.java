package lk.ijse;

import lk.ijse.bean.Boy;
import lk.ijse.config.AppConfig;
import lk.ijse.di.Test_02;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AppInitializer_02 {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(AppConfig.class);
        context.refresh();

        Test_02 test02 = context.getBean(Test_02.class);
        System.out.println(test02);

        context.registerShutdownHook();
    }
}