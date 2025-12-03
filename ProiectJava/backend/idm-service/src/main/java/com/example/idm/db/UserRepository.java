package com.example.idm.db;

import java.sql.*;

public class UserRepository {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public UserRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    public User findByEmailAndPassword(String email, String rawPassword) {
        String sql = "SELECT id, email, parola, rol FROM utilizatori WHERE email = ? AND parola = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, rawPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String em = rs.getString("email");
                    String pw = rs.getString("parola");
                    String role = rs.getString("rol");
                    return new User(id, em, pw, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
