package cn.wq.persistence.sql.jdbc.bean;

/**
 * @author: 万强
 * @date: 2019/7/19 16:08
 * @description: EXITS操作类型枚举
 * @Version 1.0
 */
public enum  ExistsType {

    WHERE_EXISTS("WHERE EXISTS"),
    WHERE_NOT_EXISTS("WHERE NOT EXISTS"),
    AND_EXISTS("AND EXISTS"),
    AND_NOT_EXISTS("AND NOT EXISTS");

    private final String type;

    ExistsType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}
