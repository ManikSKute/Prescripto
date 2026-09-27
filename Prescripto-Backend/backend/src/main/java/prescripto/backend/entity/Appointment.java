package prescripto.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    private String slotDate;
    private String slotTime;

    private Double amount;
    private Long date = System.currentTimeMillis();

    private Boolean cancelled = false;
    private Boolean payment = false;
    private Boolean isCompleted = false;
}