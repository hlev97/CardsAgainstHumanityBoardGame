package hu.bme.cah.api.cardsagainsthumanityapi.room.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class RoomControllerTest {

    @Test
    void createRoom(@Autowired MockMvc mvc) throws Exception {
        String result = "{\"roomId\":\"roomName\",\"whiteIds\":null,\"blackIds\":null,\"connectedUsers\":[\"hlev\"],\"czarId\":\"hlev\",\"rounds\":0,\"currentRound\":0,\"startedRoom\":false,\"turnState\":null,\"userScores\":null,\"userVotes\":null,\"userChosen\":null}";
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"roomId\":\"roomName\"\n" +
                                "}"))
                .andExpect(content().json(result));
    }

    @Test
    void joinRoom(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"roomId\":\"roomName2\"\n" +
                                "}"));

        String result = "{\"roomId\":\"roomName2\",\"whiteIds\":[],\"blackIds\":[],\"connectedUsers\":[\"hlev\",\"polya\"],\"czarId\":\"hlev\",\"rounds\":0,\"currentRound\":0,\"startedRoom\":false,\"turnState\":null,\"userScores\":{},\"userVotes\":{},\"userChosen\":{}}";
        mvc.perform(put("http://localhost:" + "8080" + "/api/room/roomName2/join")
                        .with(user("polya").password("polya").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(result));
    }

    @Test
    void leaveRoom(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                .with(user("hlev").password("hlev").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"roomId\":\"roomName3\"\n" +
                        "}"));

        mvc.perform(put("http://localhost:" + "8080" + "/api/room/roomName3/join")
                        .with(user("polya").password("polya").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON));
        String result = "{\"roomId\":\"roomName3\",\"whiteIds\":[],\"blackIds\":[],\"connectedUsers\":[\"hlev\"],\"czarId\":\"hlev\",\"rounds\":0,\"currentRound\":0,\"startedRoom\":false,\"turnState\":null,\"userScores\":{},\"userVotes\":{},\"userChosen\":{}}";
        mvc.perform(delete("http://localhost:" + "8080" + "/api/room/roomName3/join")
                .with(user("polya").password("polya").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"roomId\":\"roomName\"\n" +
                        "}"))
                .andExpect(content().json(result));


    }

    @Test
    void kickPlayer(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"roomId\":\"roomName4\"\n" +
                        "}"));

        mvc.perform(put("http://localhost:" + "8080" + "/api/room/roomName4/join")
                .with(user("polya").password("polya").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON));

        String result = "{\"roomId\":\"roomName4\",\"whiteIds\":[],\"blackIds\":[],\"connectedUsers\":[\"hlev\"],\"czarId\":\"hlev\",\"rounds\":0,\"currentRound\":0,\"startedRoom\":false,\"turnState\":null,\"userScores\":{},\"userVotes\":{},\"userChosen\":{}}";
        mvc.perform(delete("http://localhost:" + "8080" + "/api/room/roomName4/kick/polya")
                        .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(result));
    }

    @Test
    void initGame(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"roomId\":\"roomName5\"\n" +
                        "}"));


        String result = "\"startedRoom\":true";
        mvc.perform(put("http://localhost:" + "8080" + "/api/room/roomName5/initGame")
                .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"rounds\" : 2\n" +
                        "}"))
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void gameState(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("http://localhost:" + "8080" + "/api/room")
                .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"roomId\":\"roomName6\"\n" +
                        "}"));


        mvc.perform(put("http://localhost:" + "8080" + "/api/room/roomName6/initGame")
                        .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"rounds\" : 2\n" +
                                "}"));

        String result = "\"turnState\":\"TURN_CHOOSING_CARDS\",\"currentRound\":1,\"allRound\":2,";
        mvc.perform(get("http://localhost:" + "8080" + "/api/room/roomName6/gameState")
                .with(user("hlev").password("hlev").roles("USER", "CZAR"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(result)));
    }

}
