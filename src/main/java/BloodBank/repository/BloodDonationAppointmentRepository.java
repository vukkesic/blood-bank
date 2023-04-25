package BloodBank.repository;

import BloodBank.model.BloodDonationAppointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Date;
import java.util.List;

@Repository
public interface BloodDonationAppointmentRepository extends JpaRepository<BloodDonationAppointment,Long>{
    @Query(value = "SELECT a.* FROM blood_donation_appointment a WHERE CAST(a.start_time as date) >= CAST(?1 as date) AND CAST(a.start_time as date) <= CAST(?2 as date) AND a.location = ?3", nativeQuery = true)
    List<BloodBank.model.BloodDonationAppointment> getAppointmentsForAction(Date startTime, Date endTime, String location);
}
