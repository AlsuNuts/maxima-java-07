package org.example;
/*

Описать базовый интерфейс BaseRepository для реализации паттерна DAO
с CRUD операциями и выборкой всех объектов (для любого класса) findAll()
 */


import java.sql.*;

public class App
{
    public static final String DB_URL = "jdbc:h2:mem:test";
    public static final String DB_DRIVER = "";

    public static void main( String[] args )
    {

        try {
            Connection connection = DriverManager.getConnection(DB_URL);

            Statement statement = connection.createStatement();
            //создаем таблицу
            statement.executeUpdate("CREATE TABLE cats (Name VARCHAR(45), Weight INT)");
            //добавляем элементы в таблицу
            statement.executeUpdate("INSERT INTO cats(Name, Weight) VALUES ('Пушок', 10)");
            statement.executeUpdate("INSERT INTO cats(Name, Weight) VALUES ('Кусец', 5)");
            statement.executeUpdate("INSERT INTO cats(Name, Weight) VALUES ('Черныш', 7)");
            //изменение данных
            statement.executeUpdate("UPDATE cats SET Name = 'Снежок' WHERE Name = 'Черныш'");
            //добавляем в таблицу новый столбец
            statement.executeUpdate("ALTER TABLE cats ADD isAngry BIT");
            //возвращаем значения и выводим данные
            ResultSet resultSet = statement.executeQuery("SELECT * FROM cats");
            while (resultSet.next()){
                String name = resultSet.getString("Name");
                int weight = resultSet.getInt("Weight");
                boolean isAngry = resultSet.getBoolean("isAngry");
                String template = (isAngry ? "Сердитый" :"добродушный" + " кот %s весом %d кг");
                System.out.println(String.format(template, name, weight, isAngry));
            }




            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
