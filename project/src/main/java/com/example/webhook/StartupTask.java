package com.example.webhook;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.Map;

@Component
public class StartupTask {
    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String body = "{\"name\": \"John Doe\", \"regNo\": \"REG12347\", \"email\": \"john@example.com\"}";
            HttpEntity<String> entity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map respBody = response.getBody();
            String webhookUrl = (String) respBody.get("webhook");
            String accessToken = (String) respBody.get("accessToken");
            
            String sql = "SELECT e.EMP_ID, e.FIRST_NAME, e.LAST_NAME, d.DEPARTMENT_NAME, (SELECT COUNT(*) FROM EMPLOYEE e2 WHERE e2.DEPARTMENT = e.DEPARTMENT AND e2.DOB > e.DOB) AS YOUNGER_EMPLOYEES_COUNT FROM EMPLOYEE e JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID ORDER BY e.EMP_ID DESC;";
            
            HttpHeaders webhookHeaders = new HttpHeaders();
            webhookHeaders.setContentType(MediaType.APPLICATION_JSON);
            webhookHeaders.set("Authorization", accessToken);
            String webhookBody = String.format("{\"finalQuery\": \"%s\"}", sql.replace("\"", "\\\""));
            HttpEntity<String> webhookEntity = new HttpEntity<>(webhookBody, webhookHeaders);
            restTemplate.exchange(webhookUrl, HttpMethod.POST, webhookEntity, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
