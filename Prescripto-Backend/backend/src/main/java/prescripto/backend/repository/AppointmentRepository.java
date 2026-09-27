package prescripto.backend.repository;

import prescripto.backend.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByDoctorId(Long doctorId);
    boolean existsByDoctorIdAndSlotDateAndSlotTimeAndCancelledFalse(Long doctorId, String slotDate, String slotTime);
}