package cn.wq.persistence.jpa_plus;

import cn.wq.persistence.jpa_plus.model.McIdentifier;
import cn.wq.persistence.jpa_plus.service.McIdentifierService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class JpaPlusApplicationTests {

    @Autowired
    private McIdentifierService mcIdentifierService;

    @Test
    public void test(){
        McIdentifier mcIdentifier = new McIdentifier();
        mcIdentifier.setCollectionID(1);
        mcIdentifier.setAttributeID(2);
        mcIdentifier.setAttributeName("Road");
        mcIdentifier.setConfidenceID(3);
        mcIdentifier.setConfidenceType(1);
        mcIdentifier.setConfidenceTypeName("Position");

        mcIdentifierService.save(mcIdentifier);
    }



}
