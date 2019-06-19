package weather;

import java.sql.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Weather {

	private Integer id;

	@NotNull(message="City cannot be missing or empty")
	@NotEmpty(message="City cannot be missing or empty")
	private String city;

	@NotNull(message="Date cannot be missing or empty")
	private Date date;

	@NotNull(message="Temperature cannot be missing or empty")
	@Min(-100)
	@Max(100)
	private Float temperature;

	@NotNull(message="Wind cannot be missing or empty")
	@Min(0)
	@Max(1000)
	private Integer wind;

	@NotNull(message="Rain cannot be missing or empty")
	@Min(0)
	@Max(1000)
	private Integer rain;

	public Weather(Integer id, String city, Date date, Float temperature, Integer wind, Integer rain) {
		this.id = id;
		this.city = city;
		this.date = date;
		this.temperature = temperature;
		this.wind = wind;
		this.rain = rain;
	}

	public Integer getId() {
		return id;
	}
	public String getCity() {
		return city;
	}
	public Date getDate() {
		return date;
	}
	public Float getTemperature() {
		return temperature;
	}
	public Integer getWind() {
		return wind;
	}
	public Integer getRain() {
		return rain;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public void setWind(int wind) {
		this.wind = wind;
	}

	public void setRain(int rain) {
		this.rain = rain;
	}


}