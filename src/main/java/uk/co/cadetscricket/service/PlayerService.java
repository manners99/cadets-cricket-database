package uk.co.cadetscricket.service;

import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uk.co.cadetscricket.domain.Player;  
import uk.co.cadetscricket.repository.PlayerRepository;

import java.util.List;

/**
 * Service class for managing Player entities.
 * This class provides methods to perform CRUD operations on Player entities using the PlayerRepository.
 */
@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves all players from the database.
     *
     * @return a list of all players
     */
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param playerId the ID of the player to retrieve
     * @return the player with the specified ID, or null if not found
     */
    public Player getPlayerById(Long playerId) {
        return playerRepository.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));
    }

    /**
     * Creates a new player in the database.
     *
     * @param player the player to create
     * @return the created player
     */
    public Player createPlayer(Player player) {
        return playerRepository.saveAndFlush(player);
    }

    /**
     * Updates an existing player in the database.
     *
     * @param playerId the ID of the player to update
     * @param updatedPlayer the updated player data
     * @return the updated player
     */
    public Player updatePlayer(Long playerId, Player updatedPlayer) {
        Player existingPlayer = getPlayerById(playerId);
        existingPlayer.setFirstName(updatedPlayer.getFirstName());
        existingPlayer.setSurname(updatedPlayer.getSurname());
        existingPlayer.setInitial(updatedPlayer.getInitial());
        existingPlayer.setGender(updatedPlayer.getGender());
        return playerRepository.save(existingPlayer);
    }

    /**
     * Deletes a player from the database.
     *
     * @param playerId the ID of the player to delete
     */
    public void deletePlayer(Long playerId) {
        Player existingPlayer = getPlayerById(playerId);
        playerRepository.delete(existingPlayer);
    }

    /**
     * Searches for players by their first name and surname.
     *
     * @param firstName the first name to search for
     * @param surname the surname to search for
     * @return a list of players matching the specified first name and surname
     */
    public List<Player> searchPlayersByName(String firstName, String surname) {
        return playerRepository.findByFirstNameIgnoreCaseAndSurnameIgnoreCase(
            firstName,
            surname);
    }

    /**
     * Searches for players by a substring in their first name or surname.
     *
     * @param substring the substring to search for
     * @return a list of players matching the specified substring
     */
    public List<Player> searchPlayersBySubstring(String substring) {
        if (substring == null || substring.isBlank()) {
            return List.of();
        }

        return playerRepository.findSuggestions(
                substring.trim(),
                PageRequest.of(0, 10));
    }
}