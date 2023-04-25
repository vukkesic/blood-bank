package BloodBank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BloodDonationNotificationDTO {
    private String title;
    private String text;
    private Date startTime;
    private Date endTime;
    private String location;
}