package BloodBank;

import BloodBank.dto.BloodDonationRequestDTO;
import BloodBank.model.BloodDonationAppointment;
import BloodBank.repository.BloodDonationAppointmentRepository;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import io.grpc.reflection.v1alpha.ErrorResponse;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import rs.ac.uns.ftn.grpc.*;

import java.util.Date;
import java.util.List;

@GrpcService
@AllArgsConstructor
public class SpringGrpcService extends SpringGrpcServiceGrpc.SpringGrpcServiceImplBase{
    private final BloodDonationAppointmentRepository bloodDonationAppointmentRepository;

    public BloodDonationAppointment createAppointment(BloodDonationRequestDTO dto) {
        Date tempStartTime = dto.getStartTime();
        BloodDonationAppointment appointment = null;
        List<BloodDonationAppointment> appointments = getAppointmentsForAction(dto.getStartTime(), dto.getEndTime(), dto.getLocation());
        System.out.println(appointments);
        if (appointments.size() == 0) {
            appointment = new BloodDonationAppointment(0L, dto.getStartTime(), dto.getPatient(), dto.getLocation());
            bloodDonationAppointmentRepository.save(appointment);
            return appointment;
        }
        for (BloodDonationAppointment a : appointments) {
            if (a.getStartTime().after(tempStartTime)) {
                tempStartTime = a.getStartTime();
            }
        }
        long curTimeInMs = tempStartTime.getTime();
        Date newStartTime = new Date(curTimeInMs
                + (30 * 60000));
        if (newStartTime.before(dto.getEndTime())) {
            appointment = new BloodDonationAppointment(0L, newStartTime, dto.getPatient(), dto.getLocation());
        }
        if(appointment != null) {
            bloodDonationAppointmentRepository.save(appointment);
        }
        return appointment;
    }

    public List<BloodDonationAppointment> getAppointmentsForAction(Date startTime, Date endTime, String location) {
        return bloodDonationAppointmentRepository.getAppointmentsForAction(startTime, endTime, location);
    }

    @Override
    public void makeBloodDonationAppointment (Model.BloodDonationRequest request, StreamObserver<rs.ac.uns.ftn.grpc.Model.BloodDonationAppointment> responseObserver){
        BloodDonationAppointment appointment = createAppointment(new BloodDonationRequestDTO(new Date(request.getStartTime().getSeconds() * 1000), new Date(request.getEndTime().getSeconds()*1000), request.getPatientName(), request.getLocation()));

        if(appointment == null){
            Metadata.Key<ErrorResponse> errorResponseKey = ProtoUtils.keyForProto(ErrorResponse.getDefaultInstance());
            ErrorResponse errorResponse = ErrorResponse.newBuilder()
                    .setErrorMessage("Popunjeni su svi termini za izabranu akciju davanja krvi.")
                    .build();
            Metadata metadata = new Metadata();
            metadata.put(errorResponseKey, errorResponse);
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription("Popunjeni su svi termini za izabranu akciju davanja krvi.")
                    .asRuntimeException(metadata));
        }

        rs.ac.uns.ftn.grpc.Model.BloodDonationAppointment responseAppointment = rs.ac.uns.ftn.grpc.Model.BloodDonationAppointment.newBuilder()
                .setPatientName(appointment.getPatientName())
                .setLocation(appointment.getLocation())
                .build();
        responseObserver.onNext(responseAppointment);
        responseObserver.onCompleted();
    }
}
