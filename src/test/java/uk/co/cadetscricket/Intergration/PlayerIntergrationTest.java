package uk.co.cadetscricket.Intergration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @AfterEach
    void tearDown() {
        if (john != null && john.getPlayerId() != null) {
            playerRepository.deleteById(john.getPlayerId());
        }
        if (jane != null && jane.getPlayerId() != null) {
            playerRepository.deleteById(jane.getPlayerId());
        }
    }

    @Test
    void testGetAllPlayers() throws Exception {
        john = playerRepository.saveAndFlush(new Player("John", "Doe", "D", Gender.MALE));
        jane = playerRepository.saveAndFlush(new Player("Jane", "", "S", Gender.FEMALE));

        mockMvc.perform(get("/api/players"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[*].firstName", containsInAnyOrder("John", "Jane")))
        .andExpect(jsonPath("$[*].surname", containsInAnyOrder("Doe", "")))
        .andExpect(jsonPath("$[*].initials", containsInAnyOrder("D", "S")));
    }
}
