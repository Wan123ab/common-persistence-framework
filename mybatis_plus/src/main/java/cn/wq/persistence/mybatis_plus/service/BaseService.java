package cn.wq.persistence.mybatis_plus.service;

import cn.wq.persistence.mybatis_plus.dao.BaseMapper;
import cn.wq.persistence.sql.jdbc.utils.SpringContextUtils;
import cn.wq.persistence.sql.model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/10 16:23
 * @desc
 */
public class BaseService<T extends Model, M extends BaseMapper<T>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected M mapper;

    @PostConstruct
    public void init() {
        Field[] fields = getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object v = field.get(this);
                Class<?> cls = field.getType();
                if (v == null && cls.getSimpleName().toLowerCase().contains("service")) {
                    v = SpringContextUtils.getBean(cls);
                    field.set(this, v);
                }
                field.setAccessible(false);
            }
        } catch (Exception e) {
            logger.error("Class{}初始化出现异常", getClass().getSimpleName(), e);
        }
    }

}
 
