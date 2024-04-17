package com.tienda.login.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tienda.login.validation.ExistsByUserName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    //@ExistsByUserName //mala inyección
    @Column(unique = true)
    @NotBlank
    @Size(min = 4,max = 12)
   private String username;

    @NotBlank
   @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
   private String password;
    @PrePersist
    public void prePersist(){
            enable=true;
        }
   private boolean enable;

    @Transient
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private boolean admin;

   // Relación Unidireccional
    @ManyToMany
    @JoinTable(
            name = "rel_users_roles",
            joinColumns = @JoinColumn(name = "FK_USER", nullable = false),
            inverseJoinColumns = @JoinColumn(name="FK_ROLE", nullable = false),
            uniqueConstraints= {@UniqueConstraint(columnNames={"FK_USER","FK_ROLE"})}
    )
    List<Role> roles;


}
