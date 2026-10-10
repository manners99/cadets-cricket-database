package uk.co.cadetscricket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.dao.DataAccessResourceFailureException;
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

    /**
     * Test to verify that the getAllPlayers endpoint returns a list of players.
     * This test mocks the PlayerService to return a predefined list of players and checks that the
     * response from the controller matches the expected output.
     * 
     * This does not test the actual service layer or database interactions, but rather focuses on the controller's behavior.
     * @throws Exception
     */
    @Test
    void getAllPlayersReturnsPlayers() throws Exception {
        when(playerService.getAllPlayers()).thenReturn(List.of(player("John", "Smith", "J")));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].displayName").value("John Smith"));
    }

    /**
     * Test to verify that the getPlayerById endpoint correctly delegates to the PlayerService.
     * This test mocks the PlayerService to return a specific player when queried by ID and checks 
     * that the controller returns the expected JSON response.
     * It also verifies that the service method was called with the correct parameter.
     * 
     * This test does not cover the actual service logic or database interactions, 
     * but rather focuses on the controller's behavior and its interaction with the service layer.
     * @throws Exception
     */
    @Test
    void getPlayerByIdDelegatesToService() throws Exception {
        when(playerService.getPlayerById(1L)).thenReturn(player("John", null, "M"));

        mockMvc.perform(get("/api/players/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("John M"));

        verify(playerService).getPlayerById(1L);
    }

    /**
     * Test to verify that the createPlayer endpoint accepts JSON input and correctly delegates to the PlayerService.
     * This test mocks the PlayerService to return a specific player when a new player is created
     * and checks that the controller returns the expected JSON response.
     * 
     * This test does not cover the actual service logic or database interactions, 
     * but rather focuses on the controller's behavior and its interaction with the service layer.
     * @throws Exception
     */
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.displayName").value("John M"));

        verify(playerService).createPlayer(any(Player.class));
    }

    @Test
    void createPlayerReturnsFailureWhenSaveFails() throws Exception {
        when(playerService.createPlayer(any(Player.class)))
                .thenThrow(new DataAccessResourceFailureException("database unavailable"));

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "firstName": "John",
                                  "initial": "M",
                                  "gender": "OTHER"
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("The player could not be created."));
    }


    /**
     * Test to verify that the searchSuggestions endpoint correctly delegates to the PlayerService and returns the expected results.
     * This test mocks the PlayerService to return a list of players matching a search query and checks that the controller returns the expected JSON response.
     * 
     * This test does not cover the actual service logic or database interactions, 
     * but rather focuses on the controller's behavior and its interaction with the service layer.
     * @throws Exception
     */
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


    /**
     * Test to verify that the deletePlayer endpoint correctly delegates to the PlayerService.
     * This test mocks the PlayerService to ensure that when a delete request is made to the controller, 
     * the service's deletePlayer method is called with the correct player ID.
     * 
     * This test does not cover the actual service logic or database interactions, 
     * but rather focuses on the controller's behavior and its interaction with the service layer.
     * @throws Exception
     */
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