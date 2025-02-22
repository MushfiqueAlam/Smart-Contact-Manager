
package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.attoparser.dom.Text;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Table(name = "users")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User  implements UserDetails{
    @Id
    private String userId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    private String password;
    @Column(length = 500)
    private String about;
    @Column(length = 500)
    private String profilePic;
    private String phoneNumber;
    @Getter(value = AccessLevel.NONE)
    private boolean enabled=false;
    private boolean emailVerified=false;
    private boolean phoneVerified=false;

    //self,google,facebook,github,twitter,linkedin,instagram
    @Enumerated(value = EnumType.STRING)
    private providers provider=providers.SELF;
    private providers providerId;

    //add more field as per requirement
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Contact> contacts=new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList=new ArrayList<>();

    private String emailToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //List of roles[admin,user,..]
        //Collection of simpleGrantedAuthority  
        Collection<SimpleGrantedAuthority> roles=roleList.stream().map(role->new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    // public void setPicture(String picture) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'setPicture'");
    // }

    // public void setActive(boolean b) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'setActive'");
    // }

    // public void setRoleList(List<Object> of) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'setRoleList'");
    // }

    // public UserDetails orElseThrow(Object object) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'orElseThrow'");
    // }

    


    
}
