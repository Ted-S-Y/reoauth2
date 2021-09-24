package com.rbros.oauth2.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String name;
 
    @Email
    @Column(nullable = false)
    private String email;
 
    private String imageUrl;
 
    @Column(nullable = false)
    private Boolean emailVerified = false;
 
    @JsonIgnore
    private String password;
 
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
 
    private String providerId;
 
    @Builder(builderClassName= "social", builderMethodName = "socialBuilder")
    private User(String name, @Email String email, String imageUrl, @NotNull AuthProvider provider, String providerId) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }
 
    @Builder(builderClassName = "local",builderMethodName = "localBuilder")
    public User(String name, @Email String email, String imageUrl, String password, @NotNull AuthProvider provider, String providerId) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }
 
    public void updateNameAndImage(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }
}
