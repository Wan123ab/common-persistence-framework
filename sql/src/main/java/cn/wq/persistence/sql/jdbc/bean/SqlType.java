package cn.wq.persistence.sql.jdbc.bean;

/**
 * SQL 操作类型
 */
public enum SqlType {

    SELECT("SELECT "),
    INSERT("INSERT INTO "),
    DELETE("DELETE FROM "),
    UPDATE("UPDATE ");

    private final String type;

    SqlType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
