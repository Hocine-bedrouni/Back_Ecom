package fr.insy2s.commerce.shoponlineback.dtos;

import lombok.Data;

import java.util.List;
import java.util.Optional;

@Data
public class AccountDTO {

    private Long id;

    private String refAccount;

    private String name;

    private String firstName;

    private String password;

    private String email;

    private String resetToken;

    private String civility;

    private Boolean active;

    List<RoleDTO> roles;
}
