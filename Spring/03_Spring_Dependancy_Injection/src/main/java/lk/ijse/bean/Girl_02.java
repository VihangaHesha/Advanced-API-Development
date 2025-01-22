package lk.ijse.bean;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class Girl_02 implements Agreement {
    public Girl_02() {
        System.out.println("Girl Constructor");
    }

    @Override
    public void chat() {
        System.out.println("Girl 02 Chatting .....");
    }
}
