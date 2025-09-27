package com.yassir.bank.user;

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
@RequestMapping("/api/v1/users")
public class UserController {



    @Autowired
    private UserService userService;
    @Operation(summary = "Get all users", description = "Fetch a list of all users from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = User.class)))),
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Returning all users");
        return ResponseEntity.ok(userService.findAll());
    }

    @Operation(summary = "Get all users with the specified id", description = "Fetch 1 user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("Returning user with ID : "+id);
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(summary = "creates a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation failed)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Email must be valid\" }"))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Email 'test@example.com' is already used\" }"))),
    })
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("creating new user : "+user.toString());
        return ResponseEntity.ok(userService.createUser(user));
    }

    @Operation(summary = "updates a user ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body (validation failed)",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Email must be valid\" }"))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"User not found with id 99\" }"))),
            @ApiResponse(responseCode = "409", description = "Email already exists",
                    content = @Content(schema = @Schema(example = "{ \"message\": \"Email 'existing@example.com' is already used\" }"))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        log.info("Updating user with ID : "+id+" to : "+user.toString());
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "deletes a user of the specified ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with ID "+id);
        userService.deleteById(id);
        log.info("the user with ID "+id+" was deleted successfully");
        return ResponseEntity.ok("User "+ id +" was deleted successfully ");
    }
}
