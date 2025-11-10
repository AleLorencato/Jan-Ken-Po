package compass.uol.jankenpo.controllers;

import compass.uol.jankenpo.dtos.MatchResultResponse;
import compass.uol.jankenpo.dtos.UserResponse;
import compass.uol.jankenpo.models.MatchResult;
import compass.uol.jankenpo.models.User;
import compass.uol.jankenpo.requests.MatchResultRequest;
import compass.uol.jankenpo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(convertToUserResponse(createdUser), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(this::convertToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable(name = "id") int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(convertToUserResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable(name = "username") String username) {
        Optional<User> user = userService.getUserByUsername(username);
        return user.map(u -> ResponseEntity.ok(convertToUserResponse(u)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<MatchResultResponse>> getUserHistory(@PathVariable(name = "id") int id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> ResponseEntity.ok(
                u.getMatchResults().stream()
                        .map(this::convertToMatchResultResponse)
                        .collect(Collectors.toList())
        )).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable(name = "id") int id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return updatedUser != null ? ResponseEntity.ok(convertToUserResponse(updatedUser))
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") int id) {
        boolean deleted = userService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    @PostMapping("/internal/match-result")
    public ResponseEntity<Void> saveMatchResult(@RequestBody MatchResultRequest matchResultRequest) {
        userService.saveMatchResult(matchResultRequest);
        return ResponseEntity.ok().build();
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setMatchResults(
                user.getMatchResults().stream()
                        .map(this::convertToMatchResultResponse)
                        .collect(Collectors.toList())
        );
        return response;
    }

    private MatchResultResponse convertToMatchResultResponse(MatchResult matchResult) {
        MatchResultResponse response = new MatchResultResponse();
        response.setId(matchResult.getId());
        response.setDateTime(matchResult.getDateTime());
        response.setOpponent(matchResult.getOpponent());
        response.setOwnMove(matchResult.getOwnMove());
        response.setOpponentMove(matchResult.getOpponentMove());
        response.setResult(matchResult.getResult());
        return response;
    }
}