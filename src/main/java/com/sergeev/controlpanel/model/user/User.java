package com.sergeev.controlpanel.model.user;

import com.google.gson.JsonObject;
import com.sergeev.controlpanel.model.Node;

import javax.persistence.*;
import java.util.List;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USER_NODES")
    private List<Node> nodeList;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public User() {
    }

    public User(String name, String password, UserRole role, List<Node> nodeList) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.nodeList = nodeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("password", password);
        jsonObject.addProperty("role", role.name());

        return jsonObject.toString();
    }
}
