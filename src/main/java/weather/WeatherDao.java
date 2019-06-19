package weather;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import weather.helper.DatabaseHelper;

@Service
public class WeatherDao {

	private Connection conn;

	public WeatherDao() {
		try {
			DatabaseHelper.initialiseDB();
			this.conn = DatabaseHelper.getConnection(); 			
		} catch(SQLException e) {

		}
	}

	public List<Weather> getAllWeather(Date date) throws SQLException {
		List<Weather> allWeather = new ArrayList<Weather>();
		String sql = "SELECT * FROM cities_weather ";

		PreparedStatement s;

		if(date != null) {
			sql += "WHERE date = ?";
			s = conn.prepareStatement(sql);
			s.setDate(1, date);
		} else {
			s = conn.prepareStatement(sql);
		}

		ResultSet rs = s.executeQuery();

		while (rs.next()) {
			Weather weather = new Weather(rs.getInt("id"),
					rs.getString("city"),
					rs.getDate("date"),
					rs.getFloat("temperature"),
					rs.getInt("wind"),
					rs.getInt("rain"));

			allWeather.add(weather);
		}

		return allWeather;
	}

	public Weather getWeatherById(int id) throws SQLException {
		String sql = "SELECT * FROM cities_weather WHERE id = ?";

		PreparedStatement s = conn.prepareStatement(sql);
		s.setInt(1, id);
		ResultSet rs = s.executeQuery();

		while (rs.next()) {
			Weather weather = new Weather(rs.getInt("id"),
					rs.getString("city"),
					rs.getDate("date"),
					rs.getFloat("temperature"),
					rs.getInt("wind"),
					rs.getInt("rain"));

			return weather;
		}

		return null;
	}

	public Weather getWeatherByTemp(String temp) throws SQLException, IllegalArgumentException {
		String sql = "SELECT * FROM cities_weather ORDER BY temperature ";

		if(temp.equals("coldest") || temp.equals("warmest")) {
			sql += temp.equals("coldest") ? "ASC" : "DESC";
			sql += " fetch first 1 row only";
			ResultSet rs = conn.createStatement().executeQuery(sql);
			if (!rs.next()) {
				return null;
			} else {
				Weather weather = new Weather(rs.getInt("id"),
						rs.getString("city"),
						rs.getDate("date"),
						rs.getFloat("temperature"),
						rs.getInt("wind"),
						rs.getInt("rain"));

				return weather;
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Weather createWeather(Weather weather) throws SQLException {
		String sql = "INSERT INTO cities_weather VALUES (DEFAULT, ?, ?, ?, ?, ?)";

		// Use PreparedStatement to prevent SQL injection
		PreparedStatement s = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

		s.setString(1, weather.getCity());
		s.setDate(2, weather.getDate());
		s.setFloat(3, weather.getTemperature());
		s.setInt(4, weather.getWind());
		s.setInt(5, weather.getRain());

		s.executeUpdate();

		ResultSet rs = s.getGeneratedKeys();

		if (rs.next()) {
			int key = rs.getInt(1);
			weather.setId(key);				
		}

		return weather;
	}

	public Weather updateWeather(int id, Weather weather) throws SQLException {
		weather.setId(id);


		String sql = "UPDATE cities_weather SET city = ?, date = ?, temperature = ?, wind = ?, rain = ? WHERE id = ?";
		PreparedStatement s = conn.prepareStatement(sql);

		s.setString(1, weather.getCity());
		s.setDate(2, weather.getDate());
		s.setFloat(3, weather.getTemperature());
		s.setInt(4, weather.getWind());
		s.setInt(5, weather.getRain());
		s.setInt(6, id);

		int updated = s.executeUpdate();
		if (updated == 0) {
			// Tells client that the resource did not exist
			return null;
		} else {
			return weather;
		}
	}

	public boolean deleteWeather(int id) throws SQLException {
		String sql = "DELETE FROM cities_weather WHERE id = ?";

		PreparedStatement s = conn.prepareStatement(sql);
		s.setInt(1, id);
		int deleted = s.executeUpdate();

		if (deleted == 0) {
			// Tells client that the resource did not exist
			return false;    	    	
		} else {
			return true;
		}	
	}
}
