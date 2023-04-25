package BloodBank.service;

import BloodBank.dto.BloodDonationRequestDTO;
import BloodBank.model.BloodDonationAppointment;

import java.util.Date;
import java.util.List;

public interface IBloodDonationAppointmentService {
    BloodDonationAppointment createAppointment(BloodDonationRequestDTO dto);
    List<BloodDonationAppointment> getAll();
    List<BloodDonationAppointment> getAppointmentsForAction(Date startTime, Date endTime, String location);
}
