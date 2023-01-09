package org.example;

/*
Описать класс AdvancedCatRepository, реализующий интерфейс CatRepository
Использовать RowMapper, Connection Pool и PreparedStatement для решения предыдущей задачи
Все настройки подключения описать в файле application.properties в корне проекта или в папке resources:
db.url — URL базы данных
db.driver — имя драйвера
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.repository.Cat;
import org.example.repository.CatRepository;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class AdvancedCatRepository implements CatRepository {


    private HikariDataSource dataSource;

    public AdvancedCatRepository() {
        //String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String propertiesPath = "database.properties";
        try {
            Properties dbProps = new Properties();
            dbProps.load(new FileInputStream(propertiesPath));
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbProps.getProperty("db.url"));
            config.setDriverClassName(dbProps.getProperty("db.driver"));
            dataSource = new HikariDataSource(config);
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE cats (Id LONG,Name VARCHAR(45), Weight INT, IsAngry BIT)");
            connection.close();
        }
        catch(SQLException | IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean create(Cat element) {
        String query = "INSERT INTO cats(id, Name, Weight, isAngry) VALUES (?, ?, ?, ?)";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, element.getId());//preparedStatement.setString(1, name);
            preparedStatement.setString(2, element.getName());
            preparedStatement.setInt(3, element.getWeight());
            preparedStatement.setBoolean(4, element.isAngry());
            int rows = preparedStatement.executeUpdate();
            connection.close();
            return rows > 0 ? true : false;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cat read(Long id) {
        String query = "SELECT * FROM cats WHERE Id = ?";

            try {
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();

                Function<ResultSet, Cat> catRowMapper = result -> {
                    try{
                        return new Cat(resultSet.getLong("id"),
                                resultSet.getString("name"),
                                resultSet.getInt("weight"),
                                resultSet.getBoolean("isAngry"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                };
                connection.close();
                return catRowMapper.apply(resultSet);

            } catch ( SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

    @Override
    public int update(Long id, Cat element) {
        String query = "UPDATE cats SET Id = ?, Name = ?, Weight = ?, IsAngry = ? WHERE Id = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, element.getId());
            preparedStatement.setString(2, element.getName());
            preparedStatement.setInt(3, element.getWeight());
            preparedStatement.setBoolean(4, element.isAngry());
            preparedStatement.setLong(5, element.getId());
            int rows = preparedStatement.executeUpdate();
            connection.close();
            return rows;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void delete(Long id) {
        String query = "DELETE FROM cats WHERE Id = ?";
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Cat> findAll() {
        try {
            Connection connection = dataSource.getConnection();
            List<Cat> listOfCAts= new ArrayList<>();
            String query = "SELECT * FROM cats";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            Function<ResultSet, Cat> catRowMapper = result -> {
                try{
                    return new Cat(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("weight"),
                            resultSet.getBoolean("isAngry"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };

            connection.close();
            return listOfCAts;

        }
        catch ( SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
