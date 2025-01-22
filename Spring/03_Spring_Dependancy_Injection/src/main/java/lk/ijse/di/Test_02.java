package lk.ijse.di;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Test_02 implements Test{

    DI test01;

    /*@Autowired
    public Test_02(DI test01) {
        this.test01 = test01;
    }*/

    /*@Autowired
    public void setter(DI test01) {
        this.test01 = test01;
    }*/

    @Autowired
    @Override
    public void inject(DI test_01) {
        this.test01 = test_01;
    }
}
