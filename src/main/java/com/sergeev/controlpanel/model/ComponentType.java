package com.sergeev.controlpanel.model;

import javax.persistence.*;

/**
 * Created by dmitry-sergeev on 22.09.15.
 * Example of class entities: DATABASE, MAINTAINER, etc
 */
@Entity @Table(name = "component_type")
public class ComponentType {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ComponentType() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentType that = (ComponentType) o;

        if (id != that.id) return false;
        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ComponentType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
