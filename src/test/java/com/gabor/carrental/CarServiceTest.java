package com.gabor.carrental;

import com.gabor.carrental.data.CarEntity;
import com.gabor.carrental.models.CarModel;
import com.gabor.carrental.services.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class CarServiceTest {


    @Autowired
    private CarService carService;


    @Test
    void shouldFindAllCars() {
        List<CarModel> cars = carService.findAll();

        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(3);
        assertThat(cars.get(0).getName()).isEqualTo("Audi");
    }

    @Test
    void shouldFindACarWithRequestedId() {
        CarModel car = carService.findById(1);

        assertThat(car).isNotNull();
        assertThat(car.getName()).isEqualTo("Audi");
    }

    @Test
    void shouldReturnNullIfCarWithIdDoesNotExist() {
        CarModel car = carService.findById(10);

        assertThat(car).isNull();
    }

    @Test
    void shouldFindCarsAvailableBetweenTwoDates() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);

        List<CarEntity> cars = carService.findBetweenDates(today, calendar.getTime());

        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(1);
        assertThat(cars.get(0).getName()).isEqualTo("BMW");
    }

    @Test
    void shouldReturnEmptyListIfCarsNotAvailableBetweenTwoDates() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrow = calendar.getTime();

        List<CarEntity> cars = carService.findBetweenDates(tomorrow, today);
        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(0);
    }

    @Test
    @Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldSaveANewCar() {
        List<CarModel> cars = carService.findAll();
        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(3);

        CarModel saveCar = cars.get(0);
        saveCar.setId(4);

        CarModel res = carService.save(saveCar);
        assertThat(res).isNotNull();

        cars = carService.findAll();
        assertThat(cars.size()).isEqualTo(4);
    }

    @Test
    @Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldDeleteACarById() {
        List<CarModel> cars = carService.findAll();
        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(3);

        carService.deleteById(cars.get(0).getId());

        cars = carService.findAll();
        assertThat(cars.size()).isEqualTo(2);
    }

    @Test
    @Sql(scripts = "classpath:setup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void shouldUpdateACarsData() {
        List<CarModel> cars = carService.findAll();
        assertThat(cars).isNotNull();
        assertThat(cars.size()).isEqualTo(3);

        CarModel car = new CarModel(cars.get(0));
        car.setName("Nissan");

        CarModel updatedReturned = carService.update(cars.get(0).getId(), car, true);

        CarModel updatedCar = carService.findById(cars.get(0).getId());
        assertThat(updatedCar.getName()).isEqualTo(car.getName());
        assertThat(updatedReturned).isNotNull();

        cars = carService.findAll();
        assertThat(cars.size()).isEqualTo(3);
    }
}
