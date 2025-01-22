package lk.ijse.di;

import org.springframework.stereotype.Component;

@Component
public class Test_01 implements  DI{

    public Test_01() {
        System.out.println("Test 01 Constructor");
    }

    public void sayHello() {
        System.out.println("Hello");
    }
}
