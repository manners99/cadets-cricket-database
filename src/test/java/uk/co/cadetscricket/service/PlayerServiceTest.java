package uk.co.cadetscricket.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import uk.co.cadetscricket.common.Gender;
import uk.co.cadetscricket.domain.Player;
import uk.co.cadetscricket.repository.PlayerRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    private PlayerService playerService;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        playerService = new PlayerService(playerRepository);
    }

    @Test
    void getAllPlayersReturnsAllPlayers() {
        List<Player> players = List.of(player("John", "Smith", "J"));
        when(playerRepository.findAll()).thenReturn(players);

        assertSame(players, playerService.getAllPlayers());
    }

    @Test
    void getPlayerByIdReturnsPlayerWhenFound() {
        Player player = player("John", "Smith", "J");
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        assertSame(player, playerService.getPlayerById(1L));
    }

    @Test
    void getPlayerByIdThrowsWhenPlayerDoesNotExist() {
        when(playerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> playerService.getPlayerById(99L));
    }

    @Test
    void createPlayerSavesPlayer() {
        Player player = player("John", "Smith", "J");
        when(playerRepository.save(player)).thenReturn(player);

        assertSame(player, playerService.createPlayer(player));
        verify(playerRepository).save(player);
    }

    @Test
    void updatePlayerCopiesEditableFieldsAndSavesExistingPlayer() {
        Player existing = player("John", "Smith", "J");
        Player update = player("Jane", null, "M");
        update.setGender(Gender.FEMALE);
        when(playerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(playerRepository.save(existing)).thenReturn(existing);

        Player result = playerService.updatePlayer(1L, update);

        assertSame(existing, result);
        assertEquals("Jane", existing.getFirstName());
        assertNull(existing.getSurname());
        assertEquals("M", existing.getInitial());
        assertEquals(Gender.FEMALE, existing.getGender());
        verify(playerRepository).save(existing);
    }

    @Test
    void deletePlayerDeletesExistingPlayer() {
        Player player = player("John", "Smith", "J");
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.deletePlayer(1L);

        verify(playerRepository).delete(player);
    }

    @Test
    void searchPlayersByNameUsesCaseInsensitiveExactNameQuery() {
        List<Player> players = List.of(player("John", "Smith", "J"));
        when(playerRepository.findByFirstNameIgnoreCaseAndSurnameIgnoreCase("john", "smith"))
                .thenReturn(players);

        assertSame(players, playerService.searchPlayersByName("john", "smith"));
    }

    @Test
    void searchPlayersBySubstringTrimsQueryAndLimitsSuggestions() {
        List<Player> players = List.of(player("John", null, "M"));
        when(playerRepository.findSuggestions("m", PageRequest.of(0, 10)))
                .thenReturn(players);

        assertSame(players, playerService.searchPlayersBySubstring("  m "));
    }

    @Test
    void searchPlayersBySubstringReturnsEmptyForBlankQuery() {
        assertTrue(playerService.searchPlayersBySubstring("  ").isEmpty());

        verifyNoInteractions(playerRepository);
    }

    @Test
    void surnameLessPlayerUsesInitialInDisplayName() {
        Player player = player("John", null, "M");

        assertEquals("John M", player.getDisplayName());
    }

    private Player player(String firstName, String surname, String initial) {
        return new Player(firstName, surname, initial, Gender.OTHER);
    }
}