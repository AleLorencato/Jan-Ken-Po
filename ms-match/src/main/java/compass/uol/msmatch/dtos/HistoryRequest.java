package compass.uol.msmatch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRequest {
    private String username;
    private Long matchId;
    private String opponent;
    private String playerMove;
    private String opponentMove;
    private String result;
}