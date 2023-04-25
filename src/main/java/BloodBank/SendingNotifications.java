package BloodBank;

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

@Component
@EnableScheduling
public class SendingNotifications {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    @Scheduled(fixedRate = 10000)
    public void sendNotification() throws IOException {
        HttpPost httpPost = new HttpPost("http://localhost:16177/api/BloodDonationNotificationsControllercs");
        httpPost.setHeader("Accept", "application/json, text/plain, */*");
        httpPost.setHeader("Authorization", "Bearer");
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
            /*String json = "{\r\n" +
                    "  \"id\":122332,\r\n" +
                    "  \"title\":\"Odrzace se davanje krvi tad i tad\",\r\n" +
                    "  \"text\":\"Obavestavamo vas\",\r\n" +
                    "}";*/
        String json ="{\"id\":0,\"title\":\"Obavestenje.\",\"text\":\"Obavestavamo vas da ces se davanje krvi odrzati tad i tada tu i tu\",\"startTime\":\"2023-04-28T08:00:00+02:00\",\"endTime\":\"2023-04-28T16:00:00+02:00\",\"location\":\"Kac.\"}";

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
