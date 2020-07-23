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
import java.util.stream.Collectors;

public class App1 {

    //private static final String FILE_NAME = "C:\\Users\\njpsa\\Downloads\\Test1Bank.xlsx";
    //private static final String FILE_NAME = "D:\\Workspace\\aadhar-excel\\Test1Bank.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_11.xlsx";
    //private static final String FILE_NAME = "Book1_2020_06_09.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_08.xlsx";
    //private static final String FILE_NAME = "Book1_2020_06_09.xlsx";
    //private static final String FILE_NAME = "Bank_Sheet_2020_06_10.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_10.xlsx";
    //private static final String FILE_NAME = "Bank_Sheet_2020_06_11_3.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_12.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_16.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_17.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_19.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_20.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_25.xlsx";
    //private static final String FILE_NAME = "Bank_2020_06_29.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_02.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_06.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_08.xlsx";ss
    //private static final String FILE_NAME = "Bank_2020_07_09.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_13.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_14.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_16.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_17.xlsx";
    //private static final String FILE_NAME = "Bank_2020_07_20.xlsx";
    private static final String FILE_NAME = "Bank_2020_07_23.xlsx";
    private static final String ORGANIZATION_TABLE = "tbl_organization_master";
    private static final String DISTRICT_TABLE = "tbl_district_master";
    private static final String BRANCH_TABLE = "tbl_branch_master";
    private static final String USER_TABLE = "tbi_users";
    private static final String PASS = "$2a$10$3GOmZk/WR9hN/MXEvkhCtOBMaJusnrbAa3g9ywwWdwf2ctQUvNj5m";//ANGSY@123

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

                    //data.setOrganizationId();
                    try {
                        try {
                            data.setOrganizationName(currentRow.getCell(1).getStringCellValue().trim().replace(".", "").replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //data.setOrganizationAddress();
                        //data.setOrganizationEmailId();
                        //data.setOrganizationMobileNo();
                        try {
                            if (data.getOrganizationName().equals("THE GUJARAT STATE CO OPERATIVE BANK LTD")) {
                                data.setOrganizationTypeId(4);
                            } else {

                                if (currentRow.getCell(10).getStringCellValue().trim().equals("Urban Cooperative Bank")) {
                                    data.setOrganizationTypeId(1);
                                } else if (currentRow.getCell(10).getStringCellValue().trim().equals("District Cooperative Bank")) {
                                    data.setOrganizationTypeId(4);
                                } else {
                                    data.setOrganizationTypeId(2);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            data.setOrganizationTypeId(2);
                        }

                        //data.setBranchId();
                        data.setBranchName(currentRow.getCell(3).getStringCellValue().trim().replace(".", "").replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        try {
                            data.setBranchAddress(currentRow.getCell(6, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            data.setDistrictName(currentRow.getCell(4, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            data.setBranchTaluka(currentRow.getCell(5).getStringCellValue().trim().toUpperCase().replace("TA :-", "").replace(".", "").replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //data.setBranchMobileNo();
                        //data.setBranchIFSCCode();
                        //data.setBranchPincodeId();
                        //data.setBranchDistrictId();

                        //data.setUserId();
                        data.setUserName(currentRow.getCell(7).getStringCellValue().trim().replace(".", "").replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        //data.setUserAddress();
                        try {
                            data.setUserEmailId(currentRow.getCell(9, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", "").replace(",", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            data.setUserMobileNo(String.valueOf((long) currentRow.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getNumericCellValue()));
                            data.setUserUsername(String.valueOf((long) currentRow.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getNumericCellValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //data.setUserUsername();
                    //data.setUserPassword();
                    // TODO: 05-06-2020 For Manage Role
                    data.setUserRoleId(1);

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

                    if (data.getOrganizationName() != null && data.getBranchName() != null && data.getUserName() != null && data.getUserMobileNo() != null) {
                        dataList.add(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //org -> branch -> user
            Map<String, Map<String, List<Data>>> allOrganizations = dataList.stream().collect(Collectors.groupingBy(Data::getOrganizationName, Collectors.groupingBy(Data::getBranchName)));

            Connection c = PostgreSqlConnect.connectToDatabaseOrDie();

            //----------------------------------ORGANIZATION----------------------------------------------------------
            allOrganizations.forEach(new BiConsumer<String, Map<String, List<Data>>>() {
                @Override
                public void accept(String s, Map<String, List<Data>> branchMap) {

                    Data dataOrg = new ArrayList<>(branchMap.values()).get(0).get(0);
                    String org = selectAsJson(c, ORGANIZATION_TABLE + " where organizations_name = '" + s + "'"/*++" and organization_type_id = " + dataOrg.getOrganizationTypeId()*/);
                    if (org == null) {
                        try {
                            dataOrg = insertOrganization(c, dataOrg);
                            System.out.println("--Inserted Org " + new Gson().toJson(dataOrg));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else {
                        Data d = new Gson().fromJson(org, Data.class);
                        dataOrg.setOrganizationId(d.getOrganizationId());
                        System.out.println("--Org Exists" + new Gson().toJson(dataOrg));
                    }

                    //---------------------------BRANCH------------------------------------------
                    for (Map.Entry<String, List<Data>> branchEntries : branchMap.entrySet()) {

                        branchEntries.getKey();

                        Data dataBranch = branchEntries.getValue().get(0);
                        dataBranch.setOrganizationId(dataOrg.getOrganizationId());

                        String district = selectAsJson(c, DISTRICT_TABLE + " where district_name = '" + dataBranch.getDistrictName() + "'");

                        if (district == null) {
                            // TODO: 05-06-2020 Insert District
                        } else {
                            int districtId = new Gson().fromJson(district, JsonObject.class).get("district_id").getAsInt();
                            dataBranch.setBranchDistrictId(districtId);
                        }

                        String branchString = selectAsJson(c, BRANCH_TABLE + " where branch_name = '" + dataBranch.getBranchName() + "' and org_id = " + dataBranch.getOrganizationId());

                        if (branchString == null) {
                            try {
                                dataBranch = insertBranch(c, dataBranch);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            System.out.println("$$$$$$Inserted Branch " + new Gson().toJson(dataBranch));

                        } else {
                            JsonObject b = new Gson().fromJson(branchString, JsonObject.class);
                            dataBranch.setBranchId(b.get("branch_id").getAsInt());

                            /*//Update Taluka
                            try {
                                updateBranch(c, dataBranch);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }*/

                            System.out.println("$$$$$$Branch Exists" + new Gson().toJson(dataBranch));
                        }

                        //--------------------------------USER-------------------------------
                        List<Data> dataUsers = branchEntries.getValue();

                        //mobile->Users
                        Map<String, List<Data>> mobileUsers = dataUsers.stream().collect(Collectors.groupingBy(Data::getUserMobileNo));

                        for (List<Data> usersList : mobileUsers.values()) {

                            if (usersList.size() > 1) {
                                System.out.println("############ Duplicate Users" + usersList.size());
                            }

                            Data dataUser = usersList.get(0);
                            dataUser.setOrganizationId(dataBranch.getOrganizationId());
                            dataUser.setBranchId(dataBranch.getBranchId());
                            dataUser.setBranchDistrictId(dataBranch.getBranchDistrictId());

                            String userString = selectAsJson(c, USER_TABLE + " where username = '" + dataUser.getUserUsername() + "'");

                            if (userString == null) {
                                try {
                                    dataUser = insertUser(c, dataUser);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                System.out.println("############ Inserted User " + new Gson().toJson(dataUser));

                            } else {
                                JsonObject b = new Gson().fromJson(userString, JsonObject.class);
                                dataUser.setUserId(b.get("id").getAsInt());
                                System.out.println("############ User Exists" + new Gson().toJson(dataUser));
                            }
                        }
                        System.out.println();
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

    public static Data insertOrganization(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "INSERT INTO tbl_organization_master( organizations_name, organization_type_id)" +
                "VALUES ( ?, ?)";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, data.getOrganizationName());
        pstmt.setInt(2, data.getOrganizationTypeId());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                data.setOrganizationId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return data;
    }

    public static Data insertBranch(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "INSERT INTO tbl_branch_master( branch_name, address,org_id,district_id,branch_taluka)" +
                "VALUES ( ?,?,?,?,?)";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, data.getBranchName());
        pstmt.setString(2, data.getBranchAddress());
        pstmt.setInt(3, data.getOrganizationId());
        pstmt.setInt(4, data.getBranchDistrictId());
        if (data.getBranchTaluka() != null) {
            pstmt.setString(5, data.getBranchTaluka());
        } else {
            pstmt.setNull(5, Types.VARCHAR);
        }


        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                data.setBranchId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return data;
    }

    public static Data updateBranch(Connection connection, Data data) throws SQLException {

        String SQL = "UPDATE public.tbl_branch_master SET branch_taluka=? WHERE branch_id=?;";

        PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, data.getBranchTaluka());
        pstmt.setInt(2, data.getBranchId());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                System.out.println("Taluka Updated");
                // data.setBranchId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }

        return data;
    }

    public static Data insertUser(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "INSERT INTO tbi_users( name, mobile_no, email_id, password, role_id, branch_id, username, org_id, district_id,is_allow_update_loan_status)" +
                "VALUES ( ?,?,?,?,?,?,?,?,?,?)";

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
        pstmt.setBoolean(10, true);
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
}
