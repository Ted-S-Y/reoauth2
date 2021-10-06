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

/**
 * DB 의 User 테이블
 *
 */
@Data
@Entity
@Table(name = "RBS001_MBR", uniqueConstraints = {
        @UniqueConstraint(columnNames = "id")
})
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String mbrNm;
 
    @Column(nullable = false)
    private String mobl;
 
    private String imageUrl;
 
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
 
    private String providerId;
 
    @Builder(builderClassName= "social", builderMethodName = "socialBuilder")
    private User(String mbrNm, String mobl, String imageUrl, @NotNull AuthProvider provider, String providerId) {
        this.mbrNm = mbrNm;
        this.mobl = mobl;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.providerId = providerId;
    }
 
    public void updateNameAndImage(String mbrNm, String imageUrl) {
    	this.mbrNm = mbrNm;
        this.imageUrl = imageUrl;
    }
}
