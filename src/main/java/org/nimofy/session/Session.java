package org.nimofy.session;

import lombok.SneakyThrows;
import org.nimofy.annotations.Column;
import org.nimofy.annotations.Id;
import org.nimofy.annotations.Table;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;

public class Session {
    private final DataSource dataSource;
    private final String SELECT_BY_ID = "select * from %s where id = ?";

    public Session(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private <T> String resolveTableName(Class<T> clazz) {
        return clazz.getAnnotation(Table.class).name();
    }

    public <T> T findById(Class<T> clazz, Long id) {
        String tableName = resolveTableName(clazz);
        String formattedQuery = String.format(SELECT_BY_ID, tableName);
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(formattedQuery);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return buildFromResultSet(resultSet, clazz);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private <T> T buildFromResultSet(ResultSet resultSet, Class<T> clazz)  {
        resultSet.next();
        var newInstance = clazz.getConstructor().newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            var fieldClazz = field.getType();
            if (field.isAnnotationPresent(Column.class)){
                var sqlQueryName = field.getAnnotation(Column.class).name();
                var value = resultSet.getObject(sqlQueryName);
                if (value instanceof Timestamp){
                    field.set(newInstance, ((Timestamp) value).toLocalDateTime());
                } else {
                    field.set(newInstance, fieldClazz.cast(resultSet.getObject(sqlQueryName)));

                }
            } else if (field.isAnnotationPresent(Id.class)) {

                field.set(newInstance, fieldClazz.cast(resultSet.getObject("id")));
            }
        }
        return newInstance;

    }


}
