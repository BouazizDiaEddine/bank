package com.yassir.bank.user;

import com.yassir.bank.account.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user_acc")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_id_sequence",
            sequenceName = "user_id_sequence",
            initialValue = 1000,
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_id_sequence"
    )
    private Long userId;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String name;

    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    @NotBlank
    private String email;

    @OneToMany(mappedBy = "user",cascade = CascadeType.REMOVE)
    private Set<Account> account;
}
