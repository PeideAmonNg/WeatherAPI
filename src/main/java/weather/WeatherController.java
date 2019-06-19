package weather;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/weather")
@Api("Set of endpoints for Creating, Retrieving, Updating and Deleting of Weather Data Points.")
public class WeatherController {

	@Autowired
	private WeatherService weatherService;

	@GetMapping
	@ApiOperation("Returns list of all weather data points in the system.")
	public ResponseEntity<List<Weather>> findAllWeather(@RequestParam(required=false) Date date) {
		try {
			return ResponseEntity.ok(weatherService.getAllWeather(date));
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("{id}")
	@ApiOperation("Returns a specific weather data point by their identifier. 404 if does not exist.")
	@ApiResponse(code = 400, message = "weather id must be an integer")
	public ResponseEntity<Weather> findWeather(@PathVariable int id) {
		try {
			Weather weather = weatherService.getWeatherById(id);
			if(weather != null)
				return ResponseEntity.ok(weather);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@GetMapping(value="temp/{temp}")
	@ApiOperation("Returns a coldest or warmest weather data point.")
	public ResponseEntity<Weather> findWeatherByTemp(@PathVariable String temp) {
		if(!temp.equals("coldest") || !temp.equals("warmest")) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			Weather weather = weatherService.getWeatherByTemp(temp);
			if(weather != null)
				return ResponseEntity.ok(weather);
			else
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	@PostMapping
	@ApiOperation("Creates a new weather data point.")
	public ResponseEntity<Weather> createWeather(@Valid @RequestBody Weather weather) {    	
		try {
			Weather createdWeather = weatherService.createWeather(weather);

			return ResponseEntity.ok(createdWeather);
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("{id}")
	@ApiOperation("Updates a weather data point. 404 if not found.")
	public ResponseEntity<Weather> updateWeather(@PathVariable int id, @Valid @RequestBody Weather weather) {
		try {
			Weather updatedWeather = weatherService.updateWeather(id, weather); 
			if(updatedWeather != null) {
				return ResponseEntity.ok(updatedWeather);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("{id}")
	@ApiOperation("Deletes a weather data point from the system. 404 if not found.")
	public ResponseEntity<Weather> deleteWeather(@PathVariable int id) {
		try {
			return new ResponseEntity<>(weatherService.deleteWeather(id) ? HttpStatus.OK : HttpStatus.NOT_FOUND);
		} catch(SQLException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}