package com.gabor.carrental;

import com.gabor.carrental.models.OrderModel;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void selectDateTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("dateRangeSearchModel"))
                .andExpect(view().name("search"));
    }

    @Test
    void saveDateRangeTest() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dateRange", "2024-11-10->2024-11-11");

        mockMvc.perform(post("/saveDateRange")
                        .session(session))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cars"))
                .andExpect(view().name("redirect:/cars"));
    }

    @Test
    void listCarsTest() throws Exception {
        MockHttpSession session = new MockHttpSession();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        session.setAttribute("dateRange", today.format(formatter) + "->" + tomorrow.format(formatter));

        mockMvc.perform(get("/cars")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cars"))
                .andExpect(view().name("order/searchResults"));
    }

    @Test
    void shouldRedirectToHomePageIfDateRangeNotExistsInSession() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void selectCarTest() throws Exception {
        mockMvc.perform(post("/car/3"))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/car/3"));
    }

    @Test
    void showCarReservationFormTest() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dateRange", "2024-11-10->2024-11-11");

        mockMvc.perform(get("/car/1")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("order/orderCar"))
                .andExpect(model().attributeExists("car"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void shouldRedirectToHomePageIfCarIdDoesNotExist() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("dateRange", "2024-11-10->2024-11-11");

        mockMvc.perform(get("/car/6")
                        .session(session))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/"));
    }

    @Test
    void submitOrderTest() throws Exception {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        mockMvc.perform(post("/submitOrder")
                        .flashAttr("order", new OrderModel(1, "Gabor", "email", "addr", "phone", 3, 10,
                                formatter.parse("2024-10-10"), formatter.parse("2024-10-11"), 2)))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }
}
