package compass.uol.msmatch.clients;

import compass.uol.msmatch.dtos.HistoryRequest;
import compass.uol.msmatch.dtos.UserExistsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service", url = "${feign.user-service.url}")
public interface UserServiceClient {

    @GetMapping("/users/username/{username}")
    UserExistsResponse checkUserExists(@PathVariable("username") String username);

    @PostMapping("/users/internal/match-result")
    void addToHistory(@RequestBody HistoryRequest historyRequest);
}
