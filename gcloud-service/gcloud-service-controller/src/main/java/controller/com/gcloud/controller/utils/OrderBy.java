package com.gcloud.controller.utils;

public class OrderBy implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String field;
    private OrderType orderType;

    protected OrderBy(String field, OrderType orderType) {
        this.field = field;
        this.orderType = orderType;
    }

    public static OrderBy asc(String field) {
        return new OrderBy(field, OrderType.ASC);
    }

    public static OrderBy desc(String field) {
        return new OrderBy(field, OrderType.DESC);
    }

    public enum OrderType {
        ASC, DESC;

        public String value(){
            return name();
        }

    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public String toSqlString() {
        return toSqlString(false, false);
    }

    public String toSqlString(boolean IsOutSqlKeyword) {
        return toSqlString(IsOutSqlKeyword, false);
    }

    public String toSqlString(boolean IsOutSqlKeyword, boolean isFrist) {
        return (IsOutSqlKeyword ? (isFrist ? "order by " : ", ") : "") + field + " " + (orderType.equals(OrderType.DESC) ? "desc" : "asc");
    }

}
