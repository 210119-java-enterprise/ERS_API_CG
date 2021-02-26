package com.revature.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.models.User;


import java.util.Objects;

/**
 * A DTO to facilitate easy transferring of user credentials
 */
public class Principal {

    private int id;
    private int role;
    private String email;

    public Principal() {
        super();
    }

    public Principal(User user) {
        this.id = user.getUserId();
        this.role = user.getUserRole();
        this.email = user.getEmail();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String stringify() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Principal principal = (Principal) o;
        return id == principal.id &&
                Objects.equals(email, principal.email) &&
                Objects.equals(role, principal.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, role);
    }

    @Override
    public String toString() {
        return "Principal{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

}
