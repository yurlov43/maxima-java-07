package org.example.repository;

import org.example.model.Cat;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleCatRepository implements CatRepository {
    private String dBName;
    private Connection connection;
    private Statement statement;

    public SimpleCatRepository(String dB_URL, String dBName) {
        this.dBName = dBName;
        connectToDB(dB_URL);
        createTable();
    }
    
    private void connectToDB(String dB_URL) {
        try {
            connection = DriverManager.getConnection(dB_URL);
            System.out.println("Соединение к БД выполнено!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL при соединении с базой данных!");
        }
    }

    @Override
    public void closeDBConnection() {
        try {
            connection.close();
            System.out.println("Соединение к БД закрыто!");
        } catch (SQLException e) {
            System.out.println("Ошибка SQL при закрытии соединения с базой данных!");
        }
    }

    private void createTable() {
        try {
            statement = connection.createStatement();
            statement.executeUpdate(String.format(
                    "CREATE TABLE %s (id SMALLINT, Name VARCHAR(45), Weight INT, isAngry BIT)",
                    dBName));
        } catch (SQLException e) {
            System.out.println(String.format(
                    "Ошибка SQL при создании таблицы: %s!",
                    dBName));
        }
    }

    @Override
    public boolean create(Cat element) {
        try {
            statement.executeUpdate(String.format(
                    "INSERT INTO cats (id, Name, Weight, isAngry) VALUES (%d, '%s', %d, %b)",
                    element.getId(), element.getName(), element.getWeight(), element.isAngry()));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Ошибка SQL при добавлении в бд кота: %s!",
                    element.getName()));
        }
        return false;
    }

    @Override
    public Cat read(Long id) {
        Cat cat = null;
        try {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM %s WHERE id=%d",
                    dBName,
                    id));
            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                int weight = resultSet.getInt("Weight");
                Boolean isAngry = resultSet.getBoolean("isAngry");
                cat = new Cat(id, name, weight, isAngry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Ошибка SQL при выборке из базы данных одной записи с id = %d!",
                    id));
        }
        return cat;
    }

    @Override
    public int update(Long id, Cat element) {
        int rows = 0;
        try {
            rows = statement.executeUpdate(String.format(
                    "UPDATE %s SET Name='%s', Weight=%d, isAngry=%b WHERE id=%d",
                    dBName,
                    element.getName(),
                    element.getWeight(),
                    element.isAngry(),
                    id));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format(
                    "Ошибка SQL при обновлении в базе данных записи с id = %d!",
                    id));
        }
        return rows;
    }

    @Override
    public void delete(Long id) {
        try {
            statement.executeUpdate(String.format(
                    "DELETE FROM %s WHERE id=%d",
                    dBName,
                    id));
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(String.format(
                "Ошибка SQL при удалении из базы данных записи с id = %d!",
                id));
        }
    }

    @Override
    public List<Cat> findAll() {
        List<Cat> cats = new ArrayList<>();
        try {
            ResultSet resultSet = statement.executeQuery(String.format(
                    "SELECT * FROM %s",
                    dBName));
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("Name");
                int weight = resultSet.getInt("Weight");
                Boolean isAngry = resultSet.getBoolean("isAngry");
                Cat cat = new Cat(id, name, weight, isAngry);
                cats.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL при выборке всех записей из базы данных!");
        }
        return cats;
    }
}
