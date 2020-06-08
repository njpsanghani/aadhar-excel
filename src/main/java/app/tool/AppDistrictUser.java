package app.tool;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AppDistrictUser {

    private static final String FILE_NAME = "C:\\Users\\njpsa\\Downloads\\District Regiester.xlsx";
    //private static final String ORGANIZATION_TABLE = "tbl_organization_master";
    private static final String DISTRICT_TABLE = "tbl_district_master";
    //private static final String BRANCH_TABLE = "tbl_branch_master";
    private static final String USER_TABLE = "tbi_users";
    private static final String USER_DISTRICT_TABLE = "tbl_user_district";
    private static final String PASS = "$2a$10$3GOmZk/WR9hN/MXEvkhCtOBMaJusnrbAa3g9ywwWdwf2ctQUvNj5m";//RCS@321

    public static void main(String[] args) {
        try {

            FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            List<Data> dataList = new ArrayList<Data>();

            iterator.next();

            while (iterator.hasNext()) {

                Iterator<Cell> cellIterator = null;
                try {
                    Row currentRow = iterator.next();
                    cellIterator = currentRow.iterator();

                    Data data = new Data();

                    data.setOrganizationId(866);
                    data.setBranchId(1585);

                    try {
                        data.setUserName(currentRow.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        data.setUserEmailId(currentRow.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        data.setUserUsername(currentRow.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        data.setUserMobileNo(String.valueOf((long) currentRow.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getNumericCellValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        data.setDistrictName(currentRow.getCell(3, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", ""));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // TODO: 05-06-2020 For Manage Role
                    data.setUserRoleId(5);

                    try {
                        while (cellIterator.hasNext()) {

                            Cell currentCell = cellIterator.next();
                            //getCellTypeEnum shown as deprecated for version 3.15
                            //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
                            if (currentCell.getCellType() == CellType.STRING) {
                                System.out.print(currentCell.getStringCellValue() + "--");
                            } else if (currentCell.getCellType() == CellType.NUMERIC) {
                                System.out.print(currentCell.getNumericCellValue() + "--");
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println();

                    if (data.getUserName() != null && data.getUserMobileNo() != null) {
                        dataList.add(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Connection c = PostgreSqlConnect.connectToDatabaseOrDie();

            dataList.forEach(new Consumer<Data>() {
                @Override
                public void accept(Data data) {

                    String district = selectAsJson(c, DISTRICT_TABLE + " where district_name = '" + data.getDistrictName() + "'");

                    if (district == null) {
                        // TODO: 05-06-2020 Insert District
                    } else {
                        int districtId = new Gson().fromJson(district, JsonObject.class).get("district_id").getAsInt();
                        data.setBranchDistrictId(districtId);
                    }

                    String userString = selectAsJson(c, USER_TABLE + " where username = '" + data.getUserUsername() + "'");

                    if (userString == null) {
                        try {
                            data = insertUser(c, data);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        System.out.println("############ Inserted User " + new Gson().toJson(data));

                    } else {
                        JsonObject b = new Gson().fromJson(userString, JsonObject.class);
                        data.setUserId(b.get("id").getAsInt());
                        System.out.println("############ User Exists" + new Gson().toJson(data));
                    }

                    String userDistrictString = selectAsJson(c, USER_DISTRICT_TABLE + " where user_id = " + data.getUserId() + " and district_id = " + data.getBranchDistrictId());

                    if (userDistrictString == null) {
                        try {
                            data = insertUserDistrict(c, data);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@ Inserted User district " + new Gson().toJson(data));

                    } else {
                        JsonObject b = new Gson().fromJson(userString, JsonObject.class);
                        data.setUserId(b.get("id").getAsInt());
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@ User district Exists" + new Gson().toJson(data));
                    }

                }
            });

            System.out.println();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Data insertUser(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "INSERT INTO tbi_users( name, mobile_no, email_id, password, role_id, branch_id, username, org_id, district_id)" +
                "VALUES ( ?,?,?,?,?,?,?,?,?)";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, data.getUserName());
        pstmt.setString(2, data.getUserMobileNo());
        pstmt.setString(3, data.getUserEmailId());
        pstmt.setString(4, PASS);
        pstmt.setInt(5, data.getUserRoleId());
        pstmt.setInt(6, data.getBranchId());
        pstmt.setString(7, data.getUserUsername());
        pstmt.setInt(8, data.getOrganizationId());
        pstmt.setInt(9, data.getBranchDistrictId());
        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                data.setUserId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return data;
    }

    public static Data insertUserDistrict(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "INSERT INTO tbl_user_district( user_id,  district_id)" +
                "VALUES ( ?,?)";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);


        pstmt.setInt(1, data.getUserId());
        pstmt.setInt(2, data.getBranchDistrictId());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                data.setUser_district_id((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return data;
    }

    public static String selectAsJson(Connection conn, String statement) {
        Statement stmt = null;
        String jsonArray = null;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "select row_to_json(t)" +
                            " from (" +
                            "   select * from " + statement +
                            " ) t");

            if (rs.next())
                jsonArray = rs.getString(1);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
