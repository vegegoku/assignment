package com.progressoft.induction.tp;

/**
 * Created by Ahmad Y. Saleh on 7/24/17.
 */
public class Violation {

    private int order;
    private String property;
    private String description;

    public Violation(int order, String property) {
        this.order = order;
        this.property = property;
    }

    public Violation(int order, String property, String description) {
        this.order = order;
        this.property = property;
        this.description = description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Violation violation = (Violation) o;

        if (order != violation.order) return false;
        return property != null ? property.equals(violation.property) : violation.property == null;
    }

    @Override
    public int hashCode() {
        int result = order;
        result = 31 * result + (property != null ? property.hashCode() : 0);
        return result;
    }
}
