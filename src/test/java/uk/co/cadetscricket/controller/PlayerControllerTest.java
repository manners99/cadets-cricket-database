package uk.co.cadetscricket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.cadetscricket.common.Gender;
import uk.co.cadetscricket.domain.Player;
import uk.co.cadetscricket.service.PlayerService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlayerController(playerService)).build();
    }

    @Test
    void getAllPlayersReturnsPlayers() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(player("John", "Smith", "J")));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].displayName").value("John Smith"));
    }

    @Test
    void getPlayerByIdDelegatesToService() throws Exception {
        when(playerService.getPlayerById(1L)).thenReturn(player("John", null, "M"));

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("John M"));

        verify(playerService).getPlayerById(1L);
    }

    @Test
    void createPlayerAcceptsJson() throws Exception {
        Player player = player("John", null, "M");
        when(playerService.createPlayer(any(Player.class))).thenReturn(player);

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "John",
                                  "initial": "M",
                                  "gender": "OTHER"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("John M"));

        verify(playerService).createPlayer(any(Player.class));
    }

    @Test
    void searchSuggestionsPassesQueryToService() throws Exception {
        when(playerService.searchPlayersBySubstring("jo"))
                .thenReturn(List.of(player("John", null, "M")));

        mockMvc.perform(get("/api/players/search/suggestions")
                        .param("query", "jo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].displayName").value("John M"));

        verify(playerService).searchPlayersBySubstring("jo");
    }

    @Test
    void deletePlayerDelegatesToService() throws Exception {
        mockMvc.perform(delete("/api/players/1"))
                .andExpect(status().isOk());

        verify(playerService).deletePlayer(1L);
    }

    private Player player(String firstName, String surname, String initial) {
        return new Player(firstName, surname, initial, Gender.OTHER);
    }
}