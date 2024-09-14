package com.gabor.carrental;

import com.gabor.carrental.models.CarModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:/setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@WithMockUser(roles = {"ADMIN"})
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void showAddCarFormTest() throws Exception {

        mockMvc.perform(get("/admin/cars/add"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andExpect(view().name("admin/addCar"));
    }

    @Test
    void addCarTest() throws Exception {

        mockMvc.perform(post("/admin/cars/add")
                        .flashAttr("car", new CarModel("Ferrari", true, 12.2, "ford.png")))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/cars"));
    }

    @Test
    void showCarsTest() throws Exception {
        mockMvc.perform(get("/admin/cars"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("cars"))
                .andExpect(view().name("admin/cars"));
    }

    @Test
    void selectCarTest() throws Exception {
        mockMvc.perform(post("/admin/cars/edit")
                    .flashAttr("car", new CarModel(2,"Ford", true, 12.2, "ford.png")))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/cars/edit/2"));
    }

    @Test
    void showEditFormTest() throws Exception {
        mockMvc.perform(get("/admin/cars/edit/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andExpect(view().name("/admin/edit"));
    }

    @Test
    void shouldRedirectIfCarIdNotExist() throws Exception {
        mockMvc.perform(get("/admin/cars/edit/40"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/admin/cars"));
    }

    @Test
    void updateCarTest() throws Exception {
        mockMvc.perform(post("/admin/cars/doUpdate")
                        .flashAttr("car", new CarModel(1,"Ferrari", true, 12.2, null)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/cars"));
    }

    @Test
    void deleteCarTest() throws Exception {
        mockMvc.perform(post("/admin/cars/delete")
                        .flashAttr("car", new CarModel(3,"Ford", true, 12.2, null)))
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/admin/cars"));
    }

    @Test
    void showOrdersTest() throws Exception {
        mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("orders"))
                .andExpect(view().name("admin/orders"));
    }


}
