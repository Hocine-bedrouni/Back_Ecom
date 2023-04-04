package fr.insy2s.commerce.shoponlineback.dtos;

import lombok.Data;

@Data
public class UpdatePasswordDTO {
	private String oldPassword;
	
	private String newPassword;
} 
