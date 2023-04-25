package BloodBank.service.impl;

import BloodBank.dto.BloodDonationRequestDTO;
import BloodBank.model.BloodDonationAppointment;
import BloodBank.repository.BloodDonationAppointmentRepository;
import BloodBank.service.IBloodDonationAppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class BloodDonationAppointmentServiceImpl implements IBloodDonationAppointmentService {
    private final BloodDonationAppointmentRepository bloodDonationAppointmentRepository;

    @Autowired
    public BloodDonationAppointmentServiceImpl(BloodDonationAppointmentRepository bloodDonationAppointmentRepository) {
        this.bloodDonationAppointmentRepository = bloodDonationAppointmentRepository;
    }

    @Override
    public BloodDonationAppointment createAppointment(BloodDonationRequestDTO dto){
        Date tempStartTime = dto.getStartTime();
        BloodDonationAppointment appointment = null;
        List<BloodDonationAppointment> appointments = getAppointmentsForAction(dto.getStartTime(), dto.getEndTime(), dto.getLocation());
        if(appointments == null){
            appointment = new BloodDonationAppointment(0L, dto.getStartTime(), dto.getPatient(), dto.getLocation());
            bloodDonationAppointmentRepository.save(appointment);
            return  appointment;
        }
        for(BloodDonationAppointment a: appointments){
            if(a.getStartTime().after(tempStartTime)){
                tempStartTime = a.getStartTime();
            }
        }
        long curTimeInMs = tempStartTime.getTime();
        Date newStartTime = new Date(curTimeInMs
                + (30 * 60000));
        if(newStartTime.before(dto.getEndTime()))
        {
            appointment = new BloodDonationAppointment(0L, newStartTime, dto.getPatient(), dto.getLocation());
        }
        bloodDonationAppointmentRepository.save(appointment);
        return appointment;
    }

    @Override
    public List<BloodDonationAppointment> getAll() {
        return bloodDonationAppointmentRepository.findAll();
    }

    @Override
    public List<BloodDonationAppointment> getAppointmentsForAction(Date startTime, Date endTime, String location) {
        return bloodDonationAppointmentRepository.getAppointmentsForAction(startTime, endTime, location) ;
    }

}
