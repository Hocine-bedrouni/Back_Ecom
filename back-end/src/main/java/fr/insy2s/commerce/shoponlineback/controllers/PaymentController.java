package fr.insy2s.commerce.shoponlineback.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.JsonSyntaxException;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCreateParams;

import fr.insy2s.commerce.shoponlineback.dtos.PaymentCreationDTO;
import fr.insy2s.commerce.shoponlineback.dtos.PaymentCreationDTO.PaymentCreationItem;
import fr.insy2s.commerce.shoponlineback.services.PaymentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
public class PaymentController {
	
	@Autowired
	private final PaymentService paymentService;

	@Value("${stripe.secret-key}")
	private String stripeSecretKey;

	@Value("${stripe.webhook-secret}")
	private String webhookSecret;

	@Getter
	private final static class PaymentCreationResponse {
		private final String clientSecret;

		public PaymentCreationResponse(String clientSecret) {
			this.clientSecret = clientSecret;
		}
	}

	@PostMapping("/create-payment-intent")
	public ResponseEntity<PaymentCreationResponse> createPayment(
			@Valid @RequestBody PaymentCreationDTO paymentCreationDTO) {
		try {
			Stripe.apiKey = stripeSecretKey;
			
			ObjectMapper mapper = new ObjectMapper();

			PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
					.setAmount(Long.valueOf(paymentService.calculateOrderAmount(paymentCreationDTO))).setCurrency("eur")
					.setAutomaticPaymentMethods(
							PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
					// Configuration des metadata
					.putMetadata("items", mapper.writeValueAsString(paymentCreationDTO.getItems()))
					.putMetadata("deliveryModeName", paymentCreationDTO.getDeliveryModeName())
					.putMetadata("accountId", paymentCreationDTO.getAccountId().toString())
					.putMetadata("billingAddressId", paymentCreationDTO.getBillingAddressId().toString())
					.putMetadata("deliveryAddressId", paymentCreationDTO.getDeliveryAddressId().toString())
					.build();

			// Create a PaymentIntent with the order amount and currency
			PaymentIntent paymentIntent = PaymentIntent.create(params);

			PaymentCreationResponse paymentResponse = new PaymentCreationResponse(paymentIntent.getClientSecret());
			return ResponseEntity.ok(paymentResponse);
		} catch (StripeException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), e);
		} catch(JsonProcessingException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
		}
	}
	
	@PostMapping("/no-role/handle-payment-intent-event")
	public ResponseEntity<String> handlePaymentIntentEvent(@RequestHeader HttpHeaders headers, @RequestBody String body) {
        // The library needs to be configured with your account's secret key.
        // Ensure the key is kept out of any version control system you might be using.
		Stripe.apiKey = stripeSecretKey;
        
        // This is your Stripe CLI webhook secret for testing your endpoint locally.
        String endpointSecret = webhookSecret;
		
        String payload = body;
        String sigHeader = headers.getFirst("Stripe-Signature");
        Event event = null;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (JsonSyntaxException e) {
            // Invalid payload
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (SignatureVerificationException e) {
            // Invalid signature
        	throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

        paymentService.handlePaymentIntentEvent(event);
        
		return ResponseEntity.ok("");
	}
}
