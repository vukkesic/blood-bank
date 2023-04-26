package BloodBank;

import BloodBank.dto.BloodDonationNotificationDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@EnableScheduling
public class SendingNotifications {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    //@Scheduled(fixedRate = 10000)
    public static void sendNotification(BloodDonationNotificationDTO dto) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:16177/api/BloodDonationController");
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Authorization", "Bearer");
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");

        String startDate = dto.getStartTime().toString();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM d H:mm:ss zzz yyyy");
        ZonedDateTime parsedStartDate = ZonedDateTime.parse(startDate, formatter);
        String endDate = dto.getEndTime().toString();
        ZonedDateTime parsedEndDate = ZonedDateTime.parse(endDate, formatter);
        String json =String.format("{\"id\":0,\"title\":\"%s.\",\"text\":\"%s\",\"startTime\":\"%s\",\"endTime\":\"%s\",\"location\":\"%s.\"}", dto.getTitle(), dto.getText(), parsedStartDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), parsedEndDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), dto.getLocation());

        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);

        System.out.println("Executing request " + httpPost.getRequestLine());

        ResponseHandler< String > responseHandler = response  -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpClient.execute(httpPost, responseHandler);
        System.out.println("----------------------------------------");
        System.out.println(responseBody);
    }
}
