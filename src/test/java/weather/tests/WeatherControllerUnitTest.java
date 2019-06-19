package weather.tests;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import weather.Weather;
import weather.WeatherController;
import weather.WeatherService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(WeatherController.class)
public class WeatherControllerUnitTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private WeatherService service;

	@Test
	public void whenGetAllWeatherNotEmpty_thenReturnJSONArray() throws Exception {    	
		Weather weather = new Weather(1, "Wellington", new Date(1560402514799l), 15f, 10, 2);

		List<Weather> allWeather = Arrays.asList(weather);

		given(service.getAllWeather(null)).willReturn(allWeather);

		mvc.perform(get("/weather")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].city", is(weather.getCity())));

	}

	@Test
	public void whenGetAllWeatherEmpty_thenReturnJSONArray() throws Exception {
		List<Weather> allWeather = Arrays.asList();

		given(service.getAllWeather(null)).willReturn(allWeather);

		mvc.perform(get("/weather")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$", hasSize(0)));        
	}

	@Test
	public void findWeatherById_200() throws Exception {    	
		Weather weather = new Weather(1, "Wellington", new Date(1560402514799l), 15f, 10, 2);

		given(service.getWeatherById(1)).willReturn(weather);

		mvc.perform(get("/weather/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$['city']", is(weather.getCity())))
		.andExpect(jsonPath("$['date']", is(weather.getDate().toString())))
		.andExpect(jsonPath("$['rain']", is(weather.getRain())));
	}

	@Test
	public void findWeatherById_404() throws Exception {
		given(service.getWeatherById(1)).willReturn(null);

		mvc.perform(get("/weather/{id}", 1)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	public void createWeather_200() throws Exception {
		Weather weather = new Weather(null, "AC", new Date(1560402514799l), 15f, 10, 2);
		Weather createdWeather = new Weather(1, "AC", new Date(1560402514799l), 15f, 10, 2);

		class WeatherMatcher implements ArgumentMatcher<Weather> {
			public boolean matches(Weather w) {
				return w.getCity().equals(weather.getCity()) &&
						w.getTemperature().equals(weather.getTemperature());
			}
			public String toString() {
				return "Weather Matcher";
			}
		}

		given(service.createWeather(argThat(new WeatherMatcher()))).willReturn(createdWeather);

		mvc.perform(post("/weather")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(weather)))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$['id']", is(1)));
	}

	@Test
	public void createWeather_500() throws Exception {
		Weather weather = new Weather(null, "AC", new Date(1560402514799l), 15f, 10, 2);

		given(service.createWeather(Mockito.any(Weather.class))).willThrow(SQLException.class);

		mvc.perform(post("/weather")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(weather)))
		.andExpect(status().isInternalServerError());
	}

	@Test
	public void deleteWeather_200() throws Exception {

		given(service.deleteWeather(1)).willReturn(true);

		mvc.perform(delete("/weather/{id}", 1))
		.andExpect(status().isOk());		
	}

	@Test
	public void deleteWeather_404() throws Exception {

		given(service.deleteWeather(1)).willReturn(false);

		mvc.perform(delete("/weather/{id}", 1))
		.andExpect(status().isNotFound());	
	}

}
