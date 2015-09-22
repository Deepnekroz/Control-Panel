package com.sergeev.controlpanel.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sergeev.controlpanel.model.user.User;

import javax.persistence.*;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */

@Entity(name = "Node") @Table(name = "nodes")
public class Node {

    @Id @GeneratedValue
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "inetaddr")
    private InetAddress inetAddress;

    @Column(name = "osName")
    private String osName;

    @Column(name = "osVersion")
    private String osVersion;

    @ElementCollection(targetClass = Component.class)
    private Set<Component> components;

    @ManyToMany(mappedBy = "nodeList")
    private List<User> users;

    public Node() {
    }

    public Node(String name, InetAddress inetAddress, String osName, String osVersion) {
        this.name = name;
        this.inetAddress = inetAddress;
        this.osName = osName;
        this.osVersion = osVersion;
    }

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

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public void setComponents(Set<Component> components) {
        this.components = components;
    }

    @OneToMany(mappedBy = "nodes", orphanRemoval = true)
    @JoinColumn(name = "id")
    public Set<Component> getComponents(){
        return components;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (id != node.id) return false;
        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (inetAddress != null ? !inetAddress.equals(node.inetAddress) : node.inetAddress != null) return false;
        if (osName != null ? !osName.equals(node.osName) : node.osName != null) return false;
        if (osVersion != null ? !osVersion.equals(node.osVersion) : node.osVersion != null) return false;
        return !(components != null ? !components.equals(node.components) : node.components != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (inetAddress != null ? inetAddress.hashCode() : 0);
        result = 31 * result + (osName != null ? osName.hashCode() : 0);
        result = 31 * result + (osVersion != null ? osVersion.hashCode() : 0);
        result = 31 * result + (components != null ? components.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", id);
        jsonObject.addProperty("inetAddress", inetAddress.toString());
        jsonObject.addProperty("osName", osName);
        jsonObject.addProperty("osVersion", osVersion);
        jsonObject.add("components", new Gson().toJsonTree(components));


        return jsonObject.toString();
    }
}
