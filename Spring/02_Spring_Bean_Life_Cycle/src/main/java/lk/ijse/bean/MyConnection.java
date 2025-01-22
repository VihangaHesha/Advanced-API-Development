package lk.ijse.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MyConnection implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean, DisposableBean {

    @Override
    public void destroy() throws Exception {
        System.out.println("MyConnection Disposable Bean");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("MyConnection bean factory set");
    }

    public MyConnection() {
        System.out.println("MyConnection bean created");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("MyConnection bean name set" );
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("MyConnection Initializing Bean");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("MyConnection Application Context");
    }
}
