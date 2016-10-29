package my.app.rest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.lang.System.out;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import my.app.service.Form20Service;

@RestController
public class Hol1Form20Controller {
	private static final Logger logger = LoggerFactory.getLogger(Hol1Form20Controller.class);
	private @Autowired NamedParameterJdbcTemplate hol1EihParamJdbcTemplate;
	private @Autowired Form20Service form20Service;

	@GetMapping("/r/iF20t3220-{m1}-{m2}-{year}")
	public  @ResponseBody Map<String, Object> readIntegrateF20t3220(
			@PathVariable Integer m1
			,@PathVariable Integer m2
			,@PathVariable Integer year
			,Principal userPrincipal) {
		logger.info("\n -------------------------  /r/iF20t3220");
		return readIntegrateF20table(m1, m2, year, "table3220");
	}
	
	private @Value("${sql.hol1Eih.f20t3220.year_month}") String sqlHol1EihF20t3220YearMonth;
	@GetMapping("/r/F20t3220-{m1}-{m2}-{year}")
	public  @ResponseBody Map<String, Object> readF20t3220(
			@PathVariable Integer m1
			,@PathVariable Integer m2
			,@PathVariable Integer year
			,Principal userPrincipal) {
		logger.info("\n -------------------------  /r/F20t3220");
		return readSQL_FOR_F20table(m1, m2, year, sqlHol1EihF20t3220YearMonth);
	}
	
	private Map<String, Object> readIntegrateF20table(Integer m1, Integer m2, Integer year,
			String methodName) {
		StopWatch watch = new StopWatch();
		watch.start();
		Map<String, Object> map = paramForm20Tables(m1, m2, year);
		out.println( map);
		try {
			Method method = form20Service.getClass().getMethod(methodName, Map.class);
			List<Map<String, Object>> list = (List<Map<String, Object>>) method.invoke(form20Service, map);
			map.put("list", list);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalArgumentException | IllegalAccessException | SecurityException  e) {
			e.printStackTrace();
		}
		map.put("m1", m1);
		map.put("m2", m2);
		watch.stop();
		map.put("duration", watch.getTotalTimeSeconds());
		out.println("duration = " + map.get("duration"));
		return map;
	}

	private Map<String, Object> readSQL_FOR_F20table(Integer m1, Integer m2, Integer year,
			String sql) {
		StopWatch watch = new StopWatch();
		watch.start();
		Map<String, Object> map = paramForm20Tables(m1, m2, year);
		logger.info("\n -------------------------  /r/F20t3220- " + map);
		
		//		System.out.println(sqlHol1Eih_TO_F20t3220YearMonth);
		out.println(sql.length());
		List<Map<String, Object>> queryForList = hol1EihParamJdbcTemplate.queryForList(sql, map);
		map.put("list", queryForList);
		map.put("m1", m1);
		map.put("m2", m2);
		watch.stop();
		map.put("duration", watch.getTotalTimeSeconds());
		out.println("duration = " + map.get("duration"));
		return map;
	}

	private Map<String, Object> paramForm20Tables(Integer m1, Integer m2, Integer year) {
		Map<String, Object> map = new HashMap<>();
		map.put("min_month", m1);
		map.put("max_month", m2);
		map.put("year", year);
		return map;
	}
}
