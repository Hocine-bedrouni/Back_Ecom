package fr.insy2s.commerce.shoponlineback.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insy2s.commerce.shoponlineback.dtos.AccountDTO;
import fr.insy2s.commerce.shoponlineback.dtos.AddressDTO;
import fr.insy2s.commerce.shoponlineback.services.AccountService;
import fr.insy2s.commerce.shoponlineback.services.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/shopping-online/no-role")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AddressControler {
	private final AddressService addressService;
	
	private final AccountService accountService;
	
	@PostMapping("/accounts/{accountId}/addresses")
	public ResponseEntity<String> addAddress(@PathVariable Long accountId, @RequestBody AddressDTO addressDTO) {
		final AccountDTO accountDTO = accountService.getById(accountId).get();
		addressDTO.setAccount(accountDTO);
		addressService.add(addressDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body("created");
	}
	
	@PutMapping("/accounts/{accountId}/addresses/{id}")
	public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long accountId, @PathVariable Long id, @RequestBody AddressDTO addressDTO) {
		final AccountDTO accountDTO = accountService.getById(accountId).get();
		addressDTO.setAccount(accountDTO);
		return ResponseEntity.status(HttpStatus.OK).body(addressService.update(id, addressDTO));
	}
	
	@GetMapping("/accounts/{accountId}/addresses/{id}")
	public ResponseEntity<AddressDTO> getAddress(@PathVariable Long accountId, @PathVariable Long id) {
		accountService.getById(accountId).get();
		return ResponseEntity.status(HttpStatus.OK).body(addressService.getById(id).get());
	}
	
	@GetMapping("/accounts/{accountId}/addresses")
	public ResponseEntity<List<AddressDTO>> getAddressesByAccountId(@PathVariable Long accountId) {
		accountService.getById(accountId).get();
		return ResponseEntity.status(HttpStatus.OK).body(addressService.getByAccountId(accountId));
	}
}
