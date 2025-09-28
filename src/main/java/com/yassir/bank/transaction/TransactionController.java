package com.yassir.bank.transaction;

import com.yassir.bank.exchange.Exchange;
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
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @Operation(
            summary = "Get transaction history",
            description = "Retrieves all transactions linked to a specific account ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of transactions retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Account Not Found 101\" }"))),
    })
    @GetMapping(("/account/{id}"))
    public ResponseEntity<List<Transaction>> TransactionHistory(@PathVariable Long id) {
        log.info("getting transactions for account :"+id);
        return ResponseEntity.ok(transactionService.accountTransactions(id));
    }

    @Operation(
            summary = "Send money between accounts",
            description = "Creates a send transaction between two accounts. Validates that both accounts exist and that the transaction type is SEND."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input (wrong transaction type)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"the transaction type should be SEND\" }"))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"account not found 1\" }"))),
            @ApiResponse(responseCode = "400", description = "Invalid input (wrong transaction type)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"the 2 accounts must have the same currency\" }"))),
    })
    @PostMapping(("/send"))
    public ResponseEntity<List<Transaction>> createTransactionSend(@Valid @RequestBody Transaction transaction) {
        log.info("sending from account :"+transaction.getFromAccount().getAccountId()+"to account"+transaction.getFromAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionSend(transaction));
    }

    @Operation(
            summary = "Deposit money into an account",
            description = "Creates a deposit transaction for a specific account. Validates that the ToAccount exists and that the transaction type is DEPOSIT."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit transaction created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input (wrong transaction type)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"the transaction type should be DEPOSIT\" }"))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"account not found 10\" }"))),
    })
    @PostMapping(("/deposit"))
    public ResponseEntity<List<Transaction>> createTransactionDeposit(@Valid @RequestBody Transaction transaction) {
        log.info("Deposit to account :"+transaction.getToAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionDeposit(transaction));
    }

    @Operation(
            summary = "Withdraw money from an account",
            description = "Creates a withdrawal transaction for a specific account. Validates that the account exists and that the transaction type is WITHDRAWAL."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Withdrawal transaction created successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "400", description = "Invalid input (wrong transaction type)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"the transaction type should be WITHDRAWAL\" }"))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"account not found 20\" }"))),
    })
    @PostMapping(("/withdrawal"))
    public ResponseEntity<List<Transaction>> createTransactionWithdrawal(@Valid @RequestBody Transaction transaction) {
        log.info("withdrawal from account :"+transaction.getToAccount().getAccountId());
        return ResponseEntity.ok(transactionService.insertTransactionWithdrawal(transaction));
    }
}
