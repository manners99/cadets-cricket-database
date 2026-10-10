package uk.co.cadetscricket.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.cadetscricket.domain.Player;
import uk.co.cadetscricket.service.PlayerService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public Player getPlayerById(@PathVariable Long id) {
        return playerService.getPlayerById(id);
    }

    @PostMapping
    public ResponseEntity<?> createPlayer(@RequestBody Player player) {
        try {
            Player createdPlayer = playerService.createPlayer(player);
            logger.info("Created player: id={}, firstName={}, surname={}, initial={}, gender={}",
                    createdPlayer.getPlayerId(),
                    createdPlayer.getFirstName(),
                    createdPlayer.getSurname(),
                    createdPlayer.getInitial(),
                    createdPlayer.getGender());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPlayer);
        } catch (DataAccessException exception) {
            logger.error("Could not create player", exception);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "The player could not be created."));
        }
    }

    @PutMapping("/{id}")
    public Player updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
        return playerService.updatePlayer(id, updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
    }

    @GetMapping("/search/name")
    public List<Player> searchPlayersByName(@RequestParam String firstName, @RequestParam String surname) {
        return playerService.searchPlayersByName(firstName, surname);
    }

    @GetMapping("/search/suggestions")
    public List<Player> searchPlayerSuggestions(@RequestParam String query) {
        return playerService.searchPlayersBySubstring(query);
    }
}