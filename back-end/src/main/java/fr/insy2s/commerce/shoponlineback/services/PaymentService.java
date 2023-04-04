package fr.insy2s.commerce.shoponlineback.services;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;

import fr.insy2s.commerce.shoponlineback.beans.Account;
import fr.insy2s.commerce.shoponlineback.beans.Address;
import fr.insy2s.commerce.shoponlineback.beans.DeliveryMode;
import fr.insy2s.commerce.shoponlineback.beans.KeyOfOrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.OrderDetails;
import fr.insy2s.commerce.shoponlineback.beans.Ordered;
import fr.insy2s.commerce.shoponlineback.beans.Product;
import fr.insy2s.commerce.shoponlineback.dtos.PaymentCreationDTO;
import fr.insy2s.commerce.shoponlineback.dtos.PaymentCreationDTO.PaymentCreationItem;
import fr.insy2s.commerce.shoponlineback.enums.OrderedStatus;
import fr.insy2s.commerce.shoponlineback.repositories.AccountRepository;
import fr.insy2s.commerce.shoponlineback.repositories.AddressRepository;
import fr.insy2s.commerce.shoponlineback.repositories.DeliveryModeRepository;
import fr.insy2s.commerce.shoponlineback.repositories.OrderDetailsRepository;
import fr.insy2s.commerce.shoponlineback.repositories.OrderedRepository;
import fr.insy2s.commerce.shoponlineback.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final ProductRepository productRepository;

	private final DeliveryModeRepository deliveryModeRepository;

	private final OrderedRepository orderedRepository;

	private final OrderDetailsRepository orderDetailsRepository;

	private final AccountRepository accountRepository;
	
	private final UuidService uuidService;
	
	private final AddressRepository addressRepository;

	public long calculateOrderAmount(PaymentCreationDTO paymentCreationDTO) {
		double total = 0;

		for (PaymentCreationItem item : paymentCreationDTO.getItems()) {
			Product product = productRepository.findById(item.getProductId())
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
							String.format("product with id %d doesn't exist", item.getProductId())));

			// TODO: appliquer les promotions
			total += (product.getPriceTTC() * item.getQuantity());
		}

		DeliveryMode deliveryMode = deliveryModeRepository.findById(paymentCreationDTO.getDeliveryModeName())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String
						.format("delivery mode with name %s doesn't exist", paymentCreationDTO.getDeliveryModeName())));

		total += deliveryMode.getPrice();

		total *= 100;

		return (long) total;
	}

	@Transactional
	public void handlePaymentIntentEvent(final Event event) {
		// Deserialize the nested object inside the event
		EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
		StripeObject stripeObject = dataObjectDeserializer.getObject().orElseThrow(
				(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "unable to deserialize stripe object")));
		// Handle the event
		switch (event.getType()) {
		case "payment_intent.succeeded": {
			try {
				ObjectMapper objectMapper = new ObjectMapper();
				TypeFactory typeFactory = objectMapper.getTypeFactory();
				PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

				// Deserialize metadata
				List<PaymentCreationItem> items = objectMapper.readValue(paymentIntent.getMetadata().get("items"),
						typeFactory.constructCollectionType(List.class, PaymentCreationItem.class));
				Long accountId = Long.parseLong(paymentIntent.getMetadata().get("accountId"));
				String deliveryModeName = paymentIntent.getMetadata().get("deliveryModeName");
				Long billingAddressId = Long.parseLong(paymentIntent.getMetadata().get("billingAddressId"));
				Long deliveryAddressId = Long.parseLong(paymentIntent.getMetadata().get("deliveryAddressId"));

				// création de la commande
				Ordered ordered = new Ordered();
				final Account account = accountRepository.findById(accountId).orElseThrow();
				ordered.setAccount(account);
				ordered.setStatut(OrderedStatus.PAID);
				
				ordered.setTotalPrice(paymentIntent.getAmount() / 100d);
				ordered.setRefOrdered(uuidService.generateUuid());
				ordered.setOrderedDate(Instant.now());
				
				// TODO: a revoir, déterminer la date par rapport au mode de livraison choisi
				ordered.setDeliveryDate(Instant.now());
				
				final Address billingAddress = addressRepository.findById(billingAddressId).orElseThrow();
				ordered.setBillingAddress(billingAddress);
				
				final Address deliveryAddress = addressRepository.findById(deliveryAddressId).orElseThrow();
				ordered.setDeliveryAddress(deliveryAddress);

				orderedRepository.save(ordered);

				items.stream().forEach(item -> {
					final OrderDetails orderDetails = new OrderDetails();
					final Product product = productRepository.findById(item.getProductId()).orElseThrow();
					orderDetails.setPrice(product.getPriceTTC());
					orderDetails.setAmount(item.getQuantity());
					orderDetails.setOrdered(ordered);
					orderDetails.setProduct(product);
					orderDetails.setKeyOfOrderDetails(new KeyOfOrderDetails(product.getIdProduct(), ordered.getIdOrdered()));
					orderDetailsRepository.save(orderDetails);
				});

			} catch (JsonProcessingException e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
			}
			break;
		}
		// ... handle other event types
		default:
			System.out.println("Unhandled event type: " + event.getType());
		}
	}
}
