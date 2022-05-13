package hu.bme.cah.api.cardsagainsthumanityapi.card.controller;

import static org.hamcrest.Matchers.containsString;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.bme.cah.api.cardsagainsthumanityapi.card.domain.White;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
class WhiteControllerTest {
    ObjectMapper objectMapper = new ObjectMapper();

//    @BeforeEach
//    void setUp(@Autowired WhiteService whiteService) throws IOException {
//        ObjectMapper mapperWhite = new ObjectMapper();
//        TypeReference<List<White>> typeReferenceWhite = new TypeReference<>(){};
//        InputStream inputStreamWhite = TypeReference.class.getResourceAsStream("/database/white.json");
//
//        List<White> whites = mapperWhite.readValue(inputStreamWhite,typeReferenceWhite);
//        whiteService.save(whites);
//    }

    @Test
    void list(@Autowired MockMvc mvc) throws Exception {
        String result = "[{\"whiteId\":1,\"text\":\"\\\"Tweeting.\\\"\",\"pack\":\"Base\"},{\"whiteId\":2,\"text\":\"(I am doing Kegels right now.)\",\"pack\":\"Base\"},{\"whiteId\":3,\"text\":\"10,000 Syri";
        mvc.perform(get("http://localhost:" + "8080" + "/api/white/list")
                        .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(result)));
    }

    @Test
    void getByWhiteId(@Autowired MockMvc mvc) throws Exception {
        String result = "{\"whiteId\":1,\"text\":\"\\\"Tweeting.\\\"\",\"pack\":\"Base\"}";
        mvc.perform(get("http://localhost:" + "8080" + "/api/white/{whiteId}", "1")
                        .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    void create(@Autowired MockMvc mvc) throws Exception {
        White white = new White();
        white.setText("test card");
        white.setPack("test card");
        String body = objectMapper.writeValueAsString(white);

        String result = "{\"whiteId\": 1276, \"text\": \"test card\", \"pack\": \"test card\"}\n";
        mvc.perform(post("http://localhost:" + "8080" + "/api/white/")
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(result));
    }
    @Test
    void updateForbiddenBase(@Autowired MockMvc mvc) throws Exception {
        White update = new White();
        update.setWhiteId(1L);
        update.setText("test card changed");
        update.setPack("test card");
        String body = objectMapper.writeValueAsString(update);
        mvc.perform(put("http://localhost:" + "8080" + "/api/white/{whiteId}", "1")
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateAllowedBase(@Autowired MockMvc mvc) throws Exception {
        White update = new White();
        update.setWhiteId(1L);
        update.setText("test card changed");
        update.setPack("test card");
        String body = objectMapper.writeValueAsString(update);
        String result = "{\"whiteId\":1,\"text\":\"test card changed\",\"pack\":\"test card\"}";
        mvc.perform(put("http://localhost:" + "8080" + "/api/white/{whiteId}", "1")
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    void updateAllowedCustom(@Autowired MockMvc mvc) throws Exception {
        White white = new White();
        white.setText("test card");
        white.setPack("test card");
        String body = objectMapper.writeValueAsString(white);

        mvc.perform(post("http://localhost:" + "8080" + "/api/white/")
                .content(body)
                .accept(MediaType.APPLICATION_JSON)
                .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                .contentType(MediaType.APPLICATION_JSON));

        White update = new White();
        update.setText("test card changed");
        update.setPack("test card");
        String body2 = objectMapper.writeValueAsString(update);
        String result = "{\"whiteId\":1276,\"text\":\"test card changed\",\"pack\":\"test card\"}";
        mvc.perform(put("http://localhost:" + "8080" + "/api/white/{whiteId}", "1276")
                        .content(body2)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    void deleteForbiddenBase(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(delete("http://localhost:" + "8080" + "/api/white/{whiteId}", "1")
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteAllowedBase(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(delete("http://localhost:" + "8080" + "/api/white/{whiteId}", "1")
                        .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAllowedCustom(@Autowired MockMvc mvc) throws Exception {
        White white = new White();
        white.setText("test card");
        white.setPack("test card");
        String body = objectMapper.writeValueAsString(white);

        mvc.perform(post("http://localhost:" + "8080" + "/api/white/")
                .content(body)
                .accept(MediaType.APPLICATION_JSON)
                .with(user("hlev").password("hlev").roles("USER", "ADMIN"))
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(delete("http://localhost:" + "8080" + "/api/white/{whiteId}", "1276")
                        .with(user("hlev").password("hlev").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}