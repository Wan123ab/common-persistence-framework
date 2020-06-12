package cn.wq.persistence.mybatis_plus.service;

import cn.wq.persistence.mybatis_plus.dao.McValueMapper;
import cn.wq.persistence.sql.model.McValue;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/6/10 17:34
 * @desc
 */
@Service
public class McValueService extends BaseService<McValue, McValueMapper>{

    public int insert(McValue entity){
        return mapper.insert(entity);
    }
}
 
