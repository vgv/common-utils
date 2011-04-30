package me.vgv.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Вспомогательные методы для закрытия различного рода ресурсов
 *
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public final class CloseUtils {

	private CloseUtils() {
	}

	private static final Logger log = LoggerFactory.getLogger(CloseUtils.class);

	public static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				log.error("can't close closeable resource", e);
			}
		}
	}

	public static void close(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("can't close database connection", e);
			}
		}
	}

	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.error("can't close database statement", e);
			}
		}
	}

	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error("can't close database ResultSet");
			}
		}
	}

}
