package compass.uol.msmatch.models;

import compass.uol.msmatch.enums.MatchStatus;
import compass.uol.msmatch.enums.Move;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matches")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstPlayer;

    @Column(nullable = false)
    private String secondPlayer;

    @Enumerated(EnumType.STRING)
    private Move firstPlayerMove;

    @Enumerated(EnumType.STRING)
    private Move secondPlayerMove;

    @Enumerated(EnumType.STRING)
    private MatchStatus status = MatchStatus.WAITING_MOVES;

    private String winner;

    private String result;
}

