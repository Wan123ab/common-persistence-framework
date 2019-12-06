package cn.wq.persistence.sql.jdbc.bean;

/**
 * @Auther: wanqiang
 * @Date: 2019/7/23 19:37
 * @Desc: Criteria拼接类型
 * @Version: 1.0
 */
public enum CriteriaType {

    WITH(""),
    WHERE("WHERE"),
    AND("AND"),
    OR("OR");

    private final String type;

    CriteriaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
