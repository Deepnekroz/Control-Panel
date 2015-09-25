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

    @Column(name = "install_command")
    private String installCommand;

    @ManyToOne
    @JoinColumn(name = "node_id", nullable = false)
    private Node node;

    @OneToOne(fetch = FetchType.LAZY, targetEntity = ComponentType.class)
    private ComponentType componentType;


    public Component() {
    }

    public Component(String name, String installCommand, ComponentType componentType) {
        this.name = name;
        this.installCommand = installCommand;
        this.componentType = componentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstallCommand() {
        return installCommand;
    }

    public void setInstallCommand(String installCommand) {
        this.installCommand = installCommand;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Component component = (Component) o;

        if (name != null ? !name.equals(component.name) : component.name != null) return false;
        if (installCommand != null ? !installCommand.equals(component.installCommand) : component.installCommand != null) return false;
        if (componentType != null ? !componentType.equals(component.componentType) : component.componentType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (installCommand != null ? installCommand.hashCode() : 0);
        result = 31 * result + (componentType != null ? componentType.hashCode() : 0);
        return result;
    }
}
