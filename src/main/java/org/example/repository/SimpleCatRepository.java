package org.example.repository;

//Описать класс SimpleCatRepository для реализации этого интерфейса.
//URL базы данных и имя таблицы задайте в конструкторе класса выбраным Вами способом

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleCatRepository implements CatRepository{
    public static final String DB_URL = "jdbc:h2:mem:test";
    public static final String DB_DRIVER = "org.h2.Driver";
    public SimpleCatRepository() {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);

            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE cats (Id LONG,Name VARCHAR(45), Weight INT, IsAngry BIT)");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean create(Cat element) {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL); //установили соединение
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("INSERT INTO cats VALUES ('%d', '%s','%d','%b')", element.getId(), element.getName(), element.getWeight(), element.isAngry())); //для себя https://hr-vector.com/java/formatirovanie-chisel-strok                    String request = String.format("INSERT INTO cats VALUES ('%d','%s','%d','%b')",

            connection.close();
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cat read(Long id) {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM cats WHERE Id = '%d'", id));
            resultSet.next();
            Cat cat = new Cat(resultSet.getLong("Id"), resultSet.getString("Name"), resultSet.getInt("Weight"), resultSet.getBoolean("IsAngry"));

            connection.close();
            return cat;
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int update(Long id, Cat element) {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            int changes = statement.executeUpdate(String.format("UPDATE cats SET Id = '%d', Name = '%s', Weight = '%d', IsAngry = '%b' WHERE Id = '%d'",
                    element.getId(), element.getName(), element.getWeight(), element.isAngry(), id));
            connection.close();
            return changes;
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            statement.executeUpdate(String.format("DELETE FROM cats WHERE Id = '%d'", id));

            connection.close();

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Cat> findAll() {
        try {
            Class.forName(DB_DRIVER);
            Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            List<Cat> listOfCAts= new ArrayList<>();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cats");
            while (resultSet.next()){
                Cat cat = new Cat(resultSet.getLong("Id"), resultSet.getString("Name"), resultSet.getInt("Weight"), resultSet.getBoolean("IsAngry"));
                listOfCAts.add(cat);
            }

            connection.close();
            return listOfCAts;

        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
