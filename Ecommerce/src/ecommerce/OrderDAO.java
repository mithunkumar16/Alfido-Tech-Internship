package ecommerce;

import java.sql.*;
import java.util.Map;

public class OrderDAO {
    public boolean placeOrder(int userId, Map<Integer, Integer> cartItems) {
        String orderSql = "INSERT INTO orders (user_id, total_price) VALUES (?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        double totalPrice = 0.0;

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            for (Map.Entry<Integer, Integer> entry : cartItems.entrySet()) {
                totalPrice += getProductPrice(entry.getKey()) * entry.getValue();
            }

            try (PreparedStatement orderStmt = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setDouble(2, totalPrice);
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    int orderId = rs.getInt(1);
                    try (PreparedStatement itemStmt = conn.prepareStatement(itemSql)) {
                        for (Map.Entry<Integer, Integer> entry : cartItems.entrySet()) {
                            itemStmt.setInt(1, orderId);
                            itemStmt.setInt(2, entry.getKey());
                            itemStmt.setInt(3, entry.getValue());
                            itemStmt.setDouble(4, getProductPrice(entry.getKey()));
                            itemStmt.addBatch();
                        }
                        itemStmt.executeBatch();
                    }
                }
                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private double getProductPrice(int productId) throws SQLException {
        String sql = "SELECT price FROM products WHERE product_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("price");
            }
        }
        return 0.0;
    }
}


