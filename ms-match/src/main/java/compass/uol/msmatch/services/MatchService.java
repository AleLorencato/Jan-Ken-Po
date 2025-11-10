package compass.uol.msmatch.services;

import compass.uol.msmatch.clients.UserServiceClient;
import compass.uol.msmatch.dtos.*;
import compass.uol.msmatch.enums.MatchStatus;
import compass.uol.msmatch.enums.Move;
import compass.uol.msmatch.exceptions.*;
import compass.uol.msmatch.models.Match;
import compass.uol.msmatch.repositories.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserServiceClient userServiceClient;

    @Transactional
    public MatchResponse startMatch(StartMatchRequest request) {

        if (request.getFirstPlayerUsername().equals(request.getSecondPlayerUsername())) {
            throw new InvalidPlayerException("Os jogadores devem ser diferentes");
        }
        validateUserExists(request.getFirstPlayerUsername());
        validateUserExists(request.getSecondPlayerUsername());

        Match match = new Match();
        match.setFirstPlayer(request.getFirstPlayerUsername());
        match.setSecondPlayer(request.getSecondPlayerUsername());
        match.setStatus(MatchStatus.WAITING_MOVES);
        match = matchRepository.save(match);

        return convertToResponse(match);
    }

    @Transactional
    public MatchResponse makeMove(Long matchId, MoveRequest moveRequest) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Partida não encontrada com ID: " + matchId));

        if (match.getStatus() == MatchStatus.FINISHED) {
            throw new InvalidPlayerException("A partida já foi finalizada");
        }

        String username = moveRequest.getUsername();
        Move move = moveRequest.getMove();

        if (!username.equals(match.getFirstPlayer()) && !username.equals(match.getSecondPlayer())) {
            throw new InvalidPlayerException("Jogador não pertence a esta partida");
        }

        if (username.equals(match.getFirstPlayer())) {
            if (match.getFirstPlayerMove() != null) {
                throw new DuplicateMoveException("O primeiro jogador já fez seu movimento");
            }
            match.setFirstPlayerMove(move);
        } else {
            if (match.getSecondPlayerMove() != null) {
                throw new DuplicateMoveException("O segundo jogador já fez seu movimento");
            }
            match.setSecondPlayerMove(move);
        }

        if (match.getFirstPlayerMove() != null && match.getSecondPlayerMove() != null) {
            finishMatch(match);
        }

        match = matchRepository.save(match);
        return convertToResponse(match);
    }

    private void finishMatch(Match match) {
        String result = determineWinner(match.getFirstPlayerMove(), match.getSecondPlayerMove());

        match.setStatus(MatchStatus.FINISHED);
        match.setResult(result);

        if (result.equals("DRAW")) {
            match.setWinner("DRAW");
        } else if (result.equals("PLAYER1")) {
            match.setWinner(match.getFirstPlayer());
        } else {
            match.setWinner(match.getSecondPlayer());
        }

        saveHistory(match);
    }

    private String determineWinner(Move firstMove, Move secondMove) {
        if (firstMove == secondMove) {
            return "DRAW";
        }

        boolean firstPlayerWins = (firstMove == Move.JAN && secondMove == Move.KEN) ||
                (firstMove == Move.KEN && secondMove == Move.PO) ||
                (firstMove == Move.PO && secondMove == Move.JAN);

        return firstPlayerWins ? "PLAYER1" : "PLAYER2";
    }

    private void saveHistory(Match match) {
        HistoryRequest firstPlayerHistory = new HistoryRequest();
        firstPlayerHistory.setUsername(match.getFirstPlayer());
        firstPlayerHistory.setMatchId(match.getId());
        firstPlayerHistory.setOpponent(match.getSecondPlayer());
        firstPlayerHistory.setPlayerMove(match.getFirstPlayerMove().toString());
        firstPlayerHistory.setOpponentMove(match.getSecondPlayerMove().toString());

        if (match.getResult().equals("DRAW")) {
            firstPlayerHistory.setResult("DRAW");
        } else if (match.getResult().equals("PLAYER1")) {
            firstPlayerHistory.setResult("WIN");
        } else {
            firstPlayerHistory.setResult("LOSE");
        }

        userServiceClient.addToHistory(firstPlayerHistory);

        HistoryRequest secondPlayerHistory = new HistoryRequest();
        secondPlayerHistory.setUsername(match.getSecondPlayer());
        secondPlayerHistory.setMatchId(match.getId());
        secondPlayerHistory.setOpponent(match.getFirstPlayer());
        secondPlayerHistory.setPlayerMove(match.getSecondPlayerMove().toString());
        secondPlayerHistory.setOpponentMove(match.getFirstPlayerMove().toString());

        if (match.getResult().equals("DRAW")) {
            secondPlayerHistory.setResult("DRAW");
        } else if (match.getResult().equals("PLAYER2")) {
            secondPlayerHistory.setResult("WIN");
        } else {
            secondPlayerHistory.setResult("LOSE");
        }

        userServiceClient.addToHistory(secondPlayerHistory);
    }

    private void validateUserExists(String username) {
        try {
            UserExistsResponse response = userServiceClient.checkUserExists(username);
            if (response == null) {
                throw new UserNotFoundException("Usuário não encontrado: " + username);
            }
        } catch (feign.FeignException.NotFound ex) {
            throw new UserNotFoundException("Usuário não encontrado: " + username);
        } catch (Exception ex) {
            throw new UserNotFoundException("Erro ao verificar usuário: " + username + " - " + ex.getMessage());
        }
    }

    private MatchResponse convertToResponse(Match match) {
        MatchResponse response = new MatchResponse();
        response.setMatchId(match.getId());
        response.setFirstPlayer(match.getFirstPlayer());
        response.setSecondPlayer(match.getSecondPlayer());
        response.setFirstPlayerMove(match.getFirstPlayerMove());
        response.setSecondPlayerMove(match.getSecondPlayerMove());
        response.setStatus(match.getStatus());
        response.setWinner(match.getWinner());
        response.setResult(match.getResult());
        return response;
    }
}