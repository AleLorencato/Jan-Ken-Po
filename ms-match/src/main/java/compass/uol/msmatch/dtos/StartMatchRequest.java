package compass.uol.msmatch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartMatchRequest {
    @NotBlank(message = "Username do primeiro jogador é obrigatório")
    private String firstPlayerUsername;

    @NotBlank(message = "Username do segundo jogador é obrigatório")
    private String secondPlayerUsername;
}

