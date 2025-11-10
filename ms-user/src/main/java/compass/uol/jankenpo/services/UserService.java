package compass.uol.jankenpo.services;

import compass.uol.jankenpo.enums.ResultEnum;
import compass.uol.jankenpo.models.MatchResult;
import compass.uol.jankenpo.models.User;
import compass.uol.jankenpo.repositories.UserRepository;
import compass.uol.jankenpo.requests.MatchResultRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById((long) id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User updateUser(int id, User userDetails) {
        return userRepository.findById((long) id)
                .map(user -> {
                    user.setUsername(userDetails.getUsername());
                    return userRepository.save(user);
                })
                .orElse(null);
    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById((long) id)) {
            userRepository.deleteById((long) id);
            return true;
        }
        return false;
    }

    public void saveMatchResult(MatchResultRequest request) {
        Date matchDateTime = new Date();

        Optional<User> userOptional = getUserByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado: " + request.getUsername());
        }

        User user = userOptional.get();
        MatchResult matchResult = new MatchResult();
        matchResult.setDateTime(matchDateTime);
        matchResult.setOpponent(request.getOpponent());
        matchResult.setOwnMove(request.getPlayerMove());
        matchResult.setOpponentMove(request.getOpponentMove());
        matchResult.setUser(user);

        switch (request.getResult()) {
            case "WIN":
                matchResult.setResult(ResultEnum.WIN);
                break;
            case "LOSE":
                matchResult.setResult(ResultEnum.LOSE);
                break;
            case "DRAW":
                matchResult.setResult(ResultEnum.DRAW);
                break;
            default:
                throw new IllegalArgumentException("Resultado inválido: " + request.getResult());
        }

        user.getMatchResults().add(matchResult);
        userRepository.save(user);
    }
}
