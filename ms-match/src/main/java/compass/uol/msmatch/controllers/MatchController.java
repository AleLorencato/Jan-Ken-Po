package compass.uol.msmatch.controllers;

import compass.uol.msmatch.dtos.MatchResponse;
import compass.uol.msmatch.dtos.MoveRequest;
import compass.uol.msmatch.dtos.StartMatchRequest;
import compass.uol.msmatch.services.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/start")
    public ResponseEntity<MatchResponse> startMatch(@Valid @RequestBody StartMatchRequest request) {
        MatchResponse response = matchService.startMatch(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{matchId}/move")
    public ResponseEntity<MatchResponse> makeMove(
            @PathVariable(name = "matchId") Long matchId,
            @Valid @RequestBody MoveRequest moveRequest) {
        MatchResponse response = matchService.makeMove(matchId, moveRequest);
        return ResponseEntity.ok(response);
    }
}