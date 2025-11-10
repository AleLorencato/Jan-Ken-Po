package compass.uol.msmatch.dtos;

import compass.uol.msmatch.enums.Move;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveRequest {
    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotNull(message = "Movimento é obrigatório")
    private Move move;
}
