package fr.insy2s.commerce.shoponlineback.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PictureDTO {
	private Long idPicture;
	
	private String name;
	
	private String url;
}
