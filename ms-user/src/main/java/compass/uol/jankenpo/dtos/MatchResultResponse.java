package compass.uol.jankenpo.dtos;

import compass.uol.jankenpo.enums.ResultEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResultResponse {
    private int id;
    private Date dateTime;
    private String opponent;
    private String ownMove;
    private String opponentMove;
    private ResultEnum result;
}