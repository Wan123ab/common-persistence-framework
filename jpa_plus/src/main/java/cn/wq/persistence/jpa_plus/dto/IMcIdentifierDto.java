package cn.wq.persistence.jpa_plus.dto;

import org.springframework.beans.factory.annotation.Value;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/3 14:14
 * @desc
 */
public interface IMcIdentifierDto {

    Integer getConfidenceID();

    String getConfidenceTypeName();

    Integer getCollectionID();

    String getAttributeName();

    /**
     * 支持@Value和SPEL
     * @return
     */
    @Value("#{target.confidenceTypeName + ':' + target.attributeName}")
    String getConfidenceTypeNameAndAttributeName();

    /**
     * 通过Spel表达式获取方法里面的参数值
     * @param prefix
     * @return
     */
    @Value("#{args[0] + '---' + target.confidenceTypeName + '!!!'}")
    String getPrefixAndConfidenceTypeName(String prefix);

}
 
