package weather;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

	@Autowired
	private WeatherDao weatherdao;

	public List<Weather> getAllWeather(Date date) throws SQLException {
		List<Weather> allWeather = weatherdao.getAllWeather(date);
		return allWeather;
	}

	public Weather getWeatherById(int id) throws SQLException {
		return weatherdao.getWeatherById(id);
	}

	public Weather getWeatherByTemp(String temp) throws SQLException, IllegalArgumentException {
		return weatherdao.getWeatherByTemp(temp);
	}

	public Weather createWeather(Weather weather) throws SQLException {
		return weatherdao.createWeather(weather);
	}

	public Weather updateWeather(int id, Weather weather) throws SQLException {
		return weatherdao.updateWeather(id, weather);
	}

	public boolean deleteWeather(int id) throws SQLException {		    		
		return weatherdao.deleteWeather(id);
	}
}
