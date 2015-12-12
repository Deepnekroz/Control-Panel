package com.sergeev.controlpanel.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.JsonObject;
import com.sergeev.controlpanel.model.AbstractModel;
import com.sergeev.controlpanel.model.Node;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
@Entity
@Table(name = "users")
public class User extends AbstractModel {

    @Id @Column(nullable = false, unique = true)
    @GeneratedValue
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Node.class)
    @JoinTable(name = "users_nodes",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "node_id")})
    private Set<Node> nodeList = null;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    public User() {
    }

    public User(String name, String password, UserRole role, Set<Node> nodeList, boolean enabled) {
        this.name = name;
        this.password = password;
        this.role = role;
        this.nodeList = nodeList;
        this.enabled = enabled;
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

    public Set<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(Set<Node> nodeList) {
        this.nodeList = nodeList;
    }

    public Set<Node> addNode(Node node){
        if (!nodeList.contains(node)) {
            nodeList.add(node);
            node.addUser(this);
        }
        return nodeList;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
