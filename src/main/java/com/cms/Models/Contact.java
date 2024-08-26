package com.cms.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Contact {

    @Id
    private String contactId;

    @Column(name = "contact_name")
    private String name;
    private String email;
    private String phone;
    private String address;
    private String contactPic;

    @Column(length = 5000)
    private String about;

    private boolean favorite = false;
    private String websiteLink;
    private String LinkedInLink;
//    private List<SocialLink> socialLink = new ArrayList<SocialLink>();

    @ManyToOne
    @JsonIgnore  // Prevent serialization of the User field
    @ToString.Exclude
    private User user;

//    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
//    private List<SocialLink> links = new ArrayList<>();
}
