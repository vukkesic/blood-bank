package BloodBank;

import BloodBank.dto.BloodDonationNotificationDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Date;

@SpringBootApplication
public class BloodBankApplication {
    public static void main(String[] args) {
        SpringApplication.run(BloodBankApplication.class, args);
        BloodDonationNotificationDTO bdn = new BloodDonationNotificationDTO("Obavestenje", "Obavestavamo vas da ce dana 15.05.2023. od 9-13h organizovati akcija ddk u mestu Lok.", new Date(),new Date(), "Lok");
        BloodDonationNotificationDTO bdn2 = new BloodDonationNotificationDTO("Obavestenje", "Obavestavamo vas da ce dana 27.08.2023. od 7-15h organizovati akcija ddk u mestu Kacarevo.", new Date(),new Date(), "Kacarevo");
        try {
            BloodBank.SendingNotifications.sendNotification(bdn);
            BloodBank.SendingNotifications.sendNotification(bdn2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
