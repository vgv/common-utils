package me.vgv.common.utils;

import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.testng.annotations.Test;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Vasily Vasilkov (vgv@vgv.me)
 */
public class CloseUtilsTest {

	@Test
	public void testCloseCloseable() throws Exception {
		// проверка на null
		CloseUtils.close((Closeable) null);

		// нормальное закрытие ресурса
		Closeable closeable = Mockito.mock(Closeable.class);
		CloseUtils.close(closeable);
		Mockito.verify(closeable, VerificationModeFactory.times(1)).close();

		// ресурс бросает IOException
		closeable = Mockito.mock(Closeable.class);
		Mockito.doThrow(new IOException()).when(closeable).close();
		CloseUtils.close(closeable);
	}

	@Test
	public void testCloseSqlConnection() throws Exception {
		// проверка на null
		CloseUtils.close((Connection) null);

		// нормальное закрытие ресурса
		Connection connection = Mockito.mock(Connection.class);
		CloseUtils.close(connection);
		Mockito.verify(connection, VerificationModeFactory.times(1)).close();

		// ресурс бросает SQLException
		connection = Mockito.mock(Connection.class);
		Mockito.doThrow(new SQLException()).when(connection).close();
		CloseUtils.close(connection);
	}

	@Test
	public void testCloseSqlStatement() throws Exception {
		// проверка на null
		CloseUtils.close((Statement) null);

		// нормальное закрытие ресурса
		Statement statement = Mockito.mock(Statement.class);
		CloseUtils.close(statement);
		Mockito.verify(statement, VerificationModeFactory.times(1)).close();

		// ресурс бросает SQLException
		statement = Mockito.mock(Statement.class);
		Mockito.doThrow(new SQLException()).when(statement).close();
		CloseUtils.close(statement);
	}

	@Test
	public void testCloseSqlResultSet() throws Exception {
		// проверка на null
		CloseUtils.close((ResultSet) null);

		// нормальное закрытие ресурса
		ResultSet resultSet = Mockito.mock(ResultSet.class);
		CloseUtils.close(resultSet);
		Mockito.verify(resultSet, VerificationModeFactory.times(1)).close();

		// ресурс бросает SQLException
		resultSet = Mockito.mock(ResultSet.class);
		Mockito.doThrow(new SQLException()).when(resultSet).close();
		CloseUtils.close(resultSet);
	}


}
