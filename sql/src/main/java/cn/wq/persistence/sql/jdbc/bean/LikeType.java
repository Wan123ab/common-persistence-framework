package cn.wq.persistence.sql.jdbc.bean;

/**
 * @author: 万强
 * @date: 2019/7/20 11:07
 * @description: SQL中like操作
 * @Version 1.0
 */
public enum LikeType {

    LIKE("%value%"),
    LEFT_LIKE("%value"),
    RIGHT_LIKE("value%");

    private final String type;

    LikeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }


}
