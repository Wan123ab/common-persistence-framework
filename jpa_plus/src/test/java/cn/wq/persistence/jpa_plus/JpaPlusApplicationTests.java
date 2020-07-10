package cn.wq.persistence.jpa_plus;

import cn.wq.persistence.jpa_plus.model.McIdentifier;
import cn.wq.persistence.jpa_plus.service.McIdentifierService;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.stream.IntStream;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaPlusApplicationTests {

    @Autowired
    private McIdentifierService mcIdentifierService;

    @Test
    public void test() {
        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setCollectionID(1);
        mcIdentifier.setAttributeID(2);
        mcIdentifier.setAttributeName("Road");
        mcIdentifier.setConfidenceType(1);
        mcIdentifier.setConfidenceTypeName("Position");

        /**
         * TODO 循环500次，为啥直插入1个对象？？？因为JPA每次save前都会判断该entity isNew
         *
         */
        IntStream.range(0, 500).forEach(i ->
        {
            mcIdentifier.setConfidenceID(i);
            mcIdentifierService.save(mcIdentifier);
            System.out.println(i);
        });

        while (true) {
        }
    }

    @Test
    public void test1() {

        ArrayList<McIdentifier> list = Lists.newArrayList();
        McIdentifier  mcIdentifier;

        for (int i = 1; i <= 500; i++) {
            mcIdentifier = new McIdentifier();
            mcIdentifier.setCollectionID(1);
            mcIdentifier.setAttributeID(2);
            mcIdentifier.setAttributeName("Road");
            mcIdentifier.setConfidenceType(1);
            mcIdentifier.setConfidenceTypeName("Position");
            mcIdentifier.setConfidenceID(i);
            list.add(mcIdentifier);
        }

        mcIdentifierService.saveAll(list);

        while (true) {
        }
    }


}
