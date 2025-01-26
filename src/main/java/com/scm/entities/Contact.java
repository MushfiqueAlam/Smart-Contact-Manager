package com.scm.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Contact {
    @Id
    private String id;
   private String name;
   private String email;
    private String phone;
    private String address;
    @Column(length = 500)
    private String description;
    private String picture;
    private boolean favorite;
    private String websiteLink;
    private String linkedInLink;
    private String cloudinaryImagePublicId;

    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "contact",cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<SocailLink> links=new ArrayList<>();
}
