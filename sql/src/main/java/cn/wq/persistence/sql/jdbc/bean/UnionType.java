package cn.wq.persistence.sql.jdbc.bean;

/**
 * Union类型
 */
public enum UnionType {

    UNION("UNION"),
    UNION_ALL("UNION ALL");

    private final String type;

    UnionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
