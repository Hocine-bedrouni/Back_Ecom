package fr.insy2s.commerce.shoponlineback.controllers;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.Http;

import fr.insy2s.commerce.shoponlineback.dtos.AccountActivationDTO;
import fr.insy2s.commerce.shoponlineback.dtos.AccountDTO;
import fr.insy2s.commerce.shoponlineback.dtos.ForgetPasswordDTO;
import fr.insy2s.commerce.shoponlineback.dtos.ResetPasswordDTO;
import fr.insy2s.commerce.shoponlineback.dtos.UpdatePasswordDTO;
import fr.insy2s.commerce.shoponlineback.exceptions.beansexptions.AccountNotFountException;
import fr.insy2s.commerce.shoponlineback.secure.beanresponse.AuthenticationResponse;
import fr.insy2s.commerce.shoponlineback.services.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/shopping-online")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AccountController {

    private final AccountService accountService;

  // @Secured("ROLE_ADMIN")
    @GetMapping("/no-role/all-account")
    public ResponseEntity<Page<AccountDTO>> findAllWithPagination(Pageable pageable){
        return ResponseEntity.ok(this.accountService.all(pageable));
    }

    @PostMapping("/no-role/add-account-dto")
    public ResponseEntity<AccountDTO> addAccountDTO(@Valid @RequestBody AccountDTO accountDTO)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.accountService.addNew(accountDTO));
    }
    
    @PostMapping("/no-role/accounts/activate")
    public ResponseEntity<Void> activateAccount(@Valid @RequestBody AccountActivationDTO accountActivationDTO) {
    	accountService.activateAccount(accountActivationDTO);
    	
    	return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/no-role/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AccountDTO accountDTO)
    {
        return ResponseEntity.ok(this.accountService.login(accountDTO));
    }

//    @Secured({"ROLE_ADMIN", "ROLE_CLIENT"})
    @PutMapping("/no-role/update-account-dto/{idAccount}")
    public ResponseEntity<AccountDTO> updateAccountDTO(@Valid @PathVariable Long idAccount, @RequestBody AccountDTO accountDTO)
    {
        return ResponseEntity.status(200).body(this.accountService.update(idAccount, accountDTO));
    }


  @Secured("ROLE_ADMIN")
     @DeleteMapping("/remove-account-dto/{idAccount}")
    public ResponseEntity<String> removeAccountDTO(@Valid @PathVariable Long idAccount)
    {
        this.accountService.remove(idAccount);

        return ResponseEntity.status(200).body("Account with id : "+idAccount +" successfully delete");
    }


//    @RolesAllowed("ADMIN")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Secured("ROLE_ADMIN")
    @GetMapping("/no-role/get-by-id-account-dto/{idAccount}")
    public ResponseEntity<AccountDTO> getByIdAccountDTO(@Valid @PathVariable Long idAccount)
    {
        return this.accountService.getById(idAccount)
                .map(account -> {
                    log.info("Account with id {} was found", idAccount);
                    return new ResponseEntity<>(account, HttpStatus.OK);
                }).orElseThrow(() ->{
                    log.error("Account with id {} was not found", idAccount);
                    return new AccountNotFountException("Account with id " +idAccount+ " was not found");
                });
    }
    
    @PostMapping("/no-role/accounts/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody ForgetPasswordDTO body) {
    	accountService.forgetPassword(body.getEmail());
    	return ResponseEntity.ok("forget password");
    }
    
    @PostMapping("/no-role/accounts/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO body) {
    	accountService.resetPassword(body.getToken(), body.getPassword());
    	return ResponseEntity.ok("reset password");
    }
    
    @PutMapping("/accounts/password")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordDTO body) {
    	accountService.updatePassword(body.getOldPassword(), body.getNewPassword());
    	return ResponseEntity.ok("update password");
    }
}
