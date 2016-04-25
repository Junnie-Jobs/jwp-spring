package next.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import next.model.User;

@Repository
public class UserDao {

	@Resource
	private JdbcTemplate jdbcTemplate;

	public void insert(User user) {
		String sql = "INSERT INTO USERS VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, user.getUserId(), user.getPassword(), user.getName(), user.getEmail());
	}

	public User findByUserId(String userId) {
		String sql = "SELECT userId, password, name, email FROM USERS WHERE userid=?";

		RowMapper<User> rm = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
						rs.getString("email"));
			}
		};
		Map<String, Object> user = jdbcTemplate.queryForMap(sql, rm, userId);

		return (User) user.get(userId);
	}

	public List<Map<String, Object>> findAll() throws SQLException {
		String sql = "SELECT userId, password, name, email FROM USERS";

		RowMapper<User> rm = new RowMapper<User>() {
			@Override
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				return new User(rs.getString("userId"), rs.getString("password"), rs.getString("name"),
						rs.getString("email"));
			}
		};

		return jdbcTemplate.queryForList(sql, rm);
	}

	public void update(User user) {
		String sql = "UPDATE USERS set password = ?, name = ?, email = ? WHERE userId = ?";
		jdbcTemplate.update(sql, user.getPassword(), user.getName(), user.getEmail(), user.getUserId());
	}
}
