package compass.uol.jankenpo.models;

import compass.uol.jankenpo.enums.ResultEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    private String opponent;

    private String ownMove;

    private String opponentMove;

    @Enumerated(EnumType.STRING)
    private ResultEnum result;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
