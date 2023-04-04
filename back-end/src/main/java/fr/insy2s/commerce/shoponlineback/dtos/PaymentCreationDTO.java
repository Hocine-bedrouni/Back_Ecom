package fr.insy2s.commerce.shoponlineback.dtos;

import java.util.List;

import lombok.Data;

@Data
public class PaymentCreationDTO {
	private String deliveryModeName;
	
	private List<PaymentCreationItem> items;
	
	private Long accountId;
	
	private Long billingAddressId;
	 
	private Long deliveryAddressId;
	
	@Data
	public static class PaymentCreationItem {
		private Long productId;
		
		private Long quantity;
	}
}
