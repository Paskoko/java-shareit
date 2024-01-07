package ru.practicum.shareit.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

/**
 * Class with user's components
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int id;
    private String name;
    @Email
    private String email;

    public User compare(User user) {
        return User.builder()
                .id(user.getId())
                .name(user.getName() != null ? user.getName() : this.getName())
                .email(user.getEmail() != null ? user.getEmail() : this.getEmail())
                .build();
    }
}
