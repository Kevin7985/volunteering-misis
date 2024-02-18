package ru.misis.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String misisId;
    private String email;
    private String firstName;
    private String lastName;
    private String middleName;
    private Boolean isModerator;
    private Boolean isStaff;
}
