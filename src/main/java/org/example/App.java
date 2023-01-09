package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.repository.Cat;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.function.Function;

public class App
{
    //public static final String DB_URL = "jdbc:h2:mem:test";
    //public static final String DB_DRIVER = "org.h2.Driver";

    public static void main( String[] args ) throws IOException {
        //String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String propertiesPath = "database.properties";

        Properties dbProps = new Properties();
        dbProps.load(new FileInputStream(propertiesPath));

        //String appVersion = appProps.getProperty("version");

        try {

            HikariConfig config = new HikariConfig();
            //настройки конфига
            config.setJdbcUrl(dbProps.getProperty("db.url"));
            config.setDriverClassName(dbProps.getProperty("db.driver"));
            DataSource dataSource = new HikariDataSource(config);
            Connection connection = dataSource.getConnection();//здесь можно указать имя и пароль пользователя
            //Connection connection = DriverManager.getConnection(DB_URL);

            //RowMapper на основе интерфейса
            Function<ResultSet, Cat> catRowMapper = resultSet -> {
                try {
                    return new Cat(resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getInt("weight"),
                            resultSet.getBoolean("isAngry"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            };

            Statement statement = connection.createStatement();
            //создаем таблицу
            statement.executeUpdate("CREATE TABLE cats (id LONG, Name VARCHAR(45), Weight INT, isAngry BIT)");

            //добавляем элементы в таблицу
            statement.executeUpdate("INSERT INTO cats(id, Name, Weight, isAngry) VALUES (1L, 'Пушок', 10, true)");
            statement.executeUpdate("INSERT INTO cats(id, Name, Weight, isAngry) VALUES (2L, 'Кусец', 5, false)");
            statement.executeUpdate("INSERT INTO cats(id, Name, Weight, isAngry) VALUES (3L, 'Черныш', 7, true)");

            //String request = String.format("UPDATE cats SET Name = 'Кусец' WHERE Name = '%s'", name);
            //подсчет изменений
            //int rows = statement.executeUpdate(request);

            //SQL Injection
            String name = "Эдуард";
            String query = "UPDATE cats SET Name = ? WHERE Name = 'Кусец' ";//можно добавлять много ?
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            int rows = preparedStatement.executeUpdate();
            System.out.println("Обновлено записей:" + rows);

            //изменение данных
            statement.executeUpdate("UPDATE cats SET Name = 'Черныш' WHERE Name = 'Снежок'");

            //добавляем в таблицу новый столбец
            //statement.executeUpdate("ALTER TABLE cats ADD isAngry BIT");

            //возвращаем значения и выводим данные
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cats");
            while (resultSet.next()){
                Cat cat = catRowMapper.apply(resultSet);
                //String name = resultSet.getString("Name");
                //int weight = resultSet.getInt("Weight");
                //boolean isAngry = resultSet.getBoolean("isAngry");
                String template = (cat.isAngry() ? "Сердитый" :"добродушный") + " кот %s весом %d кг";
                System.out.println(String.format(template, cat.getName(), cat.getWeight(), cat.isAngry()));
            }

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
