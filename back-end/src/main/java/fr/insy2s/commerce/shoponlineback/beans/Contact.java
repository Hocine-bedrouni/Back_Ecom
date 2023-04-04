package fr.insy2s.commerce.shoponlineback.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact", nullable = false)
    private Long idContact;

    @Column(name = "name", nullable = false, length = 50)
    @NotEmpty
    private String name;

    @Column(name = "email", nullable = false, length=50)
    @Email
    @NotEmpty
    @NotBlank(message = "email is required")
    private String email;

    @Column(name = "message", nullable = false, length = 300)
    @NotEmpty
    private String message;
}
