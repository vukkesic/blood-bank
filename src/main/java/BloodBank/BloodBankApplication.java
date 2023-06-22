package BloodBank;

import BloodBank.dto.BloodDonationNotificationDTO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class BloodBankApplication {
    public static void main(String[] args) throws Exception{
        SpringApplication.run(BloodBankApplication.class, args);
        String s1 = "15-May-2023 9:00:00";
        String s2 = "15-May-2023 13:00:00";
        String s3 = "27-Aug-2023 7:00:00";
        String s4 = "27-Aug-2023 15:00:00";
        SimpleDateFormat formatter=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        BloodDonationNotificationDTO bdn = new BloodDonationNotificationDTO("Obavestenje", "Obavestavamo vas da ce dana 07.007.2023. od 9-13h organizovati akcija ddk u mestu Lok.", formatter.parse(s1), formatter.parse(s2), "Lok");
        BloodDonationNotificationDTO bdn2 = new BloodDonationNotificationDTO("Obavestenje", "Obavestavamo vas da ce dana 27.08.2023. od 7-15h organizovati akcija ddk u mestu Futog.", formatter.parse(s3), formatter.parse(s4), "Futog");
        try {
            BloodBank.SendingNotifications.sendNotification(bdn);
            BloodBank.SendingNotifications.sendNotification(bdn2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
