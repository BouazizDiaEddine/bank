package com.yassir.bank.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Operation(
            summary = "Get accounts by user ID",
            description = "Retrieves all accounts that belong to the specified user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of accounts retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Account.class)))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"User not found with id 99\" }"))),
    })
    @GetMapping("user/{id}")
    public ResponseEntity<List<Account>> getAllAccounts(@PathVariable Long id) {
        log.info("Returning Accounts for user with ID : "+id);
        return ResponseEntity.ok(accountService.findByUser(id));
    }
    @Operation(
            summary = "Get account by ID",
            description = "Fetches an account by its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Account not found with id 99\" }"))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        log.info("Returning Account with ID : "+id);
        return ResponseEntity.ok(accountService.findById(id));
    }

    @Operation(
            summary = "Create a new account",
            description = "Creates a new account for a user in a specific currency. Ensures that a user cannot have multiple accounts in the same currency and validates initial balance rules."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account created successfully",
                    content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (balance too low)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"initial balance in EURO should be 10 greater than 5\" }"))),
            @ApiResponse(responseCode = "404", description = "User or Currency not found"),
            @ApiResponse(responseCode = "409", description = "Account already exists for this user and currency"),
    })
    @PostMapping
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account) {
        log.info("creating new account  : "+account.toString());
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @Operation(
            summary = "Update an account's currency",
            description = "Updates the currency of an existing account. The account balance is recalculated using the appropriate exchange rate. Only currency changes are allowed; other fields cannot be modified."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account updated successfully",
                    content = @Content(schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input (e.g., attempted to update non-currency fields)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"You can only change currency here\" }"))),
            @ApiResponse(responseCode = "404", description = "Account not found"),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @Valid @RequestBody Account account) {
        log.info("updating account with ID "+id+" to "+account.toString());
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @Operation(
            summary = "Delete an account",
            description = "Deletes an account by its ID. If the account does not exist, a 404 error is returned."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"account not found with id 12\" }"))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id) {
        log.info("Deleting account with ID "+id);
        accountService.deleteById(id);
        log.info("the account with ID "+id+" was deleted successfully");
        return ResponseEntity.ok("account "+ id +" has been deleted successfully ");
    }

}
