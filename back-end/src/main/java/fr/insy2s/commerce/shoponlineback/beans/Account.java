package fr.insy2s.commerce.shoponlineback.beans;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    private Long id;

    @Column(name = "ref_account", length = 250, nullable = true) // TODO mettre en nullable false après changement en BDD
    @NotNull
    private String refAccount;

    @Column(name = "name", nullable = false, length = 100)
    @NotEmpty
    @NotBlank(message = "sorry name is required")
    private String name;

    @Column(name = "first_name", nullable = false, length = 100)
    @NotEmpty
    @NotBlank(message = "sorry firstname is required")
    private String firstName;

    @Column(name = "password", nullable = false, length = 250)
    @NotEmpty
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @NotEmpty
    @Email
    private String email;
    
    @Column(name = "reset_password_token", nullable = true)
    private String resetPasswordToken;
    
    @Column(name = "reset_password_token_creation_date", nullable = true)
    private Timestamp resetPasswordTokenCreationDate;

    @Column(name = "civility", nullable = true)
    private String civility;

    @Column(name = "active")
    private Boolean active;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @Column(name = "id_role", nullable = false)
    @JoinTable(name = "account_role",
            joinColumns = @JoinColumn(name = "id_account"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    List<Role> roles;

    @ToString.Exclude
    @OneToMany(mappedBy = "account")
    @JsonIgnoreProperties({"account", "deliveryAdress", "billingAdress"})
    private List<Ordered> ordereds;
    
    @OneToMany(mappedBy = "account")
    private List<Address> addresses;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
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
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
