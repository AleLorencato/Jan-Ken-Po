package compass.uol.msmatch.dtos;
import compass.uol.msmatch.enums.MatchStatus;
import compass.uol.msmatch.enums.Move;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchResponse {
    private Long matchId;
    private String firstPlayer;
    private String secondPlayer;
    private Move firstPlayerMove;
    private Move secondPlayerMove;
    private MatchStatus status;
    private String winner;
    private String result;
}
