
package compass.uol.msmatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsMatchApplication.class, args);
    }

}