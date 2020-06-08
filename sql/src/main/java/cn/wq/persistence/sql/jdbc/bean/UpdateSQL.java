package cn.wq.persistence.sql.jdbc.bean;

import cn.wq.persistence.sql.jdbc.function.FieldSupplier;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2019/12/13 09:31
 * @desc
 */
@Data
public class UpdateSQL extends SQL<UpdateSQL> {

    private Class tableClass;

    private List<SetParam> setParams = Lists.newArrayList();

    public UpdateSQL update(Class tableClass){
        sqlType = SqlType.UPDATE;
        this.tableClass = tableClass;
        return this;
    }

    public UpdateSQL set(FieldSupplier<?> supplier, Object value){
        SetParam setParam = new SetParam(supplier.getColumnNameByFieldDefault(), value);
        setParams.add(setParam);
        return this;
    }



}
 
