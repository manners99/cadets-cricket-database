package uk.co.cadetscricket.Intergration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsInAnyOrder;

import uk.co.cadetscricket.domain.Player;
import uk.co.cadetscricket.common.Gender;
import uk.co.cadetscricket.repository.PlayerRepository;

@SpringBootTest
@AutoConfigureMockMvc 
public class PlayerIntergrationTest {
    
    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private PlayerRepository playerRepository;

    private Player john;
    private Player jane;

    @BeforeEach
    void setUp() {
        playerRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        playerRepository.deleteAll();
    }

    @Test
    void testGetAllPlayers() throws Exception {
        john = playerRepository.saveAndFlush(new Player("John", "Doe", "D", Gender.MALE));
        jane = playerRepository.saveAndFlush(new Player("Jane", "", "S", Gender.FEMALE));

        mockMvc.perform(get("/api/players"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("John", "Jane")))
        .andExpect(jsonPath("$[*].surname", containsInAnyOrder("Doe", "")))
        .andExpect(jsonPath("$[*].initial", containsInAnyOrder("D", "S")));
    }

    @Test
    void createPlayerReturnsCreatedPlayer() throws Exception {
        mockMvc.perform(post("/api/players")
                .contentType("application/json")
                .content("""
                        {
                          "firstName": "Chris",
                          "surname": "Cairns",
                          "initial": "C",
                          "gender": "MALE"
                        }
                        """))
                .andDo(result -> System.out.println(
                        "Created player JSON: " + result.getResponse().getContentAsString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.playerId").isNumber())
                .andExpect(jsonPath("$.firstName").value("Chris"))
                .andExpect(jsonPath("$.initial").value("C"));
    }
}
