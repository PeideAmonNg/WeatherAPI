package weather.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import weather.Weather;
import weather.WeatherDao;
import weather.WeatherService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class WeatherServiceUnitTest {

	@Autowired
	private WeatherService weatherService;

	@MockBean
	private WeatherDao weatherdao;

	@Test
	public void testGetAllWeather() throws Exception {

		List<Weather> list = new ArrayList<>();
		list.add(new Weather(1, "Wellington", new Date(1560402514799l), 15f, 10, 2));
		list.add(new Weather(2, "Auckland", new Date(1560402514799l), 20f, 11, 5));

		given(weatherdao.getAllWeather(null)).willReturn(list);

		List<Weather> weatherList = weatherService.getAllWeather(null);

		assertEquals(list.size(), weatherList.size());
		assertThat(list, is(weatherList));
	}
}
