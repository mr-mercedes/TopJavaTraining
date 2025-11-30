package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;

class AdminUIControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminUIController.BASE_URL + '/';

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.patch(REST_URL + ADMIN_ID)
                .queryParam("enable", "false")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNoContent());

        perform(MockMvcRequestBuilders.patch(REST_URL + ADMIN_ID)
                .queryParam("enable", "true")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andExpect(status().isNoContent());
    }

}