package com.sergeev.controlpanel.model;

import javax.persistence.*;

/**
 * Created by dmitry-sergeev on 22.09.15.
 * Example: name = "MySQL 5.4.3", componentType = "DATABASE"
 */
@Entity @Table(name = "components")
public class Component extends AbstractModel {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = ComponentType.class)
    private ComponentType componentType;

    public Component() {
    }
}
