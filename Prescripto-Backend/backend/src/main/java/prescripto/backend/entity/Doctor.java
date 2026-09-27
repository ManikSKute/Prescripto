package prescripto.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String image;
    private String speciality;
    private String degree;
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String about;

    private Double fees;
    private Boolean available = true;

    private String addressLine1;
    private String addressLine2;

    private Long date = System.currentTimeMillis();
}