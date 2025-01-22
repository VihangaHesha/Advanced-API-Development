package lk.ijse.config;

import lk.ijse.bean.MyConnection;
import lk.ijse.bean.SpringBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackages = "lk.ijse.bean")
public class AppConfig {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    MyConnection getMyConnection(){
        return new MyConnection();
    }
}
