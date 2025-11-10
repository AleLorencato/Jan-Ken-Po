package compass.uol.jankenpo.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultRequest {
    private String username;
    private Long matchId;
    private String opponent;
    private String playerMove;
    private String opponentMove;
    private String result;
}
