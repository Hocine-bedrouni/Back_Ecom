package fr.insy2s.commerce.shoponlineback.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.insy2s.commerce.shoponlineback.beans.DeliveryMode;
import fr.insy2s.commerce.shoponlineback.services.DeliveryModeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
@CrossOrigin("*")
@Slf4j
public class DeliveryModeController {
	private final DeliveryModeService deliveryModeService;
	
    @GetMapping("/no-role/delivery-modes")
    public ResponseEntity<List<DeliveryMode>> findAll() {
        return ResponseEntity.ok(deliveryModeService.all(Pageable.unpaged()).toList());
    }
}
