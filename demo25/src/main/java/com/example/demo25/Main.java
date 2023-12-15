package com.example.demo25;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class SalesData {
    private String region;
    private String country;
    private String itemType;
    private String salesChannel;
    private String orderPriority;
    private String orderDate;
    private int unitsSold;
    private double totalProfit;

    public SalesData(String region, String country, String itemType, String salesChannel, String orderPriority,
                     String orderDate, int unitsSold, double totalProfit) {
        this.region = region;
        this.country = country;
        this.itemType = itemType;
        this.salesChannel = salesChannel;
        this.orderPriority = orderPriority;
        this.orderDate = orderDate;
        this.unitsSold = unitsSold;
        this.totalProfit = totalProfit;
    }

    public String getRegion() {
        return region;
    }

    public String getCountry() {
        return country;
    }

    public String getItemType() {
        return itemType;
    }

    public String getSalesChannel() {
        return salesChannel;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public int getUnitsSold() {
        return unitsSold;
    }

    public double getTotalProfit() {
        return totalProfit;
    }
}

public class Main {
    public static void main(String[] args) {
        List<SalesData> salesDataList = parseCSV("C:\\Users\\79050\\IdeaProjects\\demo25\\src\\main\\java\\com\\example\\demo25\\prodaja.csv ");

        if (salesDataList != null && !salesDataList.isEmpty()) {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:sales.db")) {
                Statement statement = connection.createStatement();

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS Sales (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "region TEXT," +
                        "country TEXT," +
                        "itemType TEXT," +
                        "salesChannel TEXT," +
                        "orderPriority TEXT," +
                        "orderDate TEXT," +
                        "unitsSold INTEGER," +
                        "totalProfit REAL)");

                for (SalesData data : salesDataList) {
                    String sqlCheck = "SELECT COUNT(*) AS count FROM Sales WHERE orderDate = ? AND region = ?";
                    PreparedStatement preparedStatementCheck = connection.prepareStatement(sqlCheck);
                    preparedStatementCheck.setString(1, data.getOrderDate());
                    preparedStatementCheck.setString(2, data.getRegion());
                    ResultSet resultSetCheck = preparedStatementCheck.executeQuery();

                    if (resultSetCheck.next()) {
                        int count = resultSetCheck.getInt("count");
                        if (count == 0) {
                            String sql = "INSERT INTO Sales (region, country, itemType, salesChannel, orderPriority, " +
                                    "orderDate, unitsSold, totalProfit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, data.getRegion());
                            preparedStatement.setString(2, data.getCountry());
                            preparedStatement.setString(3, data.getItemType());
                            preparedStatement.setString(4, data.getSalesChannel());
                            preparedStatement.setString(5, data.getOrderPriority());
                            preparedStatement.setString(6, data.getOrderDate());
                            preparedStatement.setInt(7, data.getUnitsSold());
                            preparedStatement.setDouble(8, data.getTotalProfit());
                            preparedStatement.executeUpdate();
                        }
                    }
                    resultSetCheck.close();
                    preparedStatementCheck.close();
                }

                String query1 = "SELECT SUM(UnitsSold) AS TotalUnits, Region FROM Sales GROUP BY Region";
                ResultSet resultSet = statement.executeQuery(query1);

                while (resultSet.next()) {
                    int totalUnits = resultSet.getInt("TotalUnits");
                    String region = resultSet.getString("Region");
                    System.out.println("Region: " + region + ", Total Units Sold: " + totalUnits);
                }

                String query3 = "SELECT Country, SUM(TotalProfit) AS TotalIncome " +
                        "FROM Sales " +
                        "WHERE Region IN ('Europe', 'Asia') " +
                        "GROUP BY Country " +
                        "ORDER BY TotalIncome DESC LIMIT 1";
                ResultSet resultSet3 = statement.executeQuery(query3);

                if (resultSet3.next()) {
                    String countryMaxIncome = resultSet3.getString("Country");
                    double totalIncome = resultSet3.getDouble("TotalIncome");
                    System.out.println("Country with the highest total income in Europe and Asia: " + countryMaxIncome + ", Total Income: " + totalIncome);
                }

                String query4 = "SELECT Country, SUM(TotalProfit) AS TotalIncome " +
                        "FROM Sales " +
                        "WHERE Region IN ('Middle East and North Africa', 'Sub-Saharan Africa') " +
                        "GROUP BY Country " +
                        "HAVING TotalIncome >= 420000 AND TotalIncome <= 440000 " +
                        "ORDER BY TotalIncome DESC LIMIT 1";

                ResultSet resultSet4 = statement.executeQuery(query4);

                if (resultSet4.next()) {
                    String countryInRange = resultSet4.getString("Country");
                    double totalIncomeInRange = resultSet4.getDouble("TotalIncome");
                    System.out.println("Country with total income between 420k and 440k in Middle East, North Africa, and Sub-Saharan Africa: " + countryInRange + ", Total Income: " + totalIncomeInRange);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<SalesData> parseCSV(String filename) {
        List<SalesData> salesDataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }

                String[] data = line.split(",");

                SalesData salesData = new SalesData(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        Integer.parseInt(data[6]),
                        Double.parseDouble(data[7])
                );

                salesDataList.add(salesData);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return salesDataList;
    }
}
