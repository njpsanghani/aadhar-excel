package app.tool;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class AddOrgDistrict {

    //private static final String FILE_NAME = "BankALL.xlsx";
    private static final String FILE_NAME = "CreditALL.xlsx";
    private static final String ORGANIZATION_TABLE = "tbl_organization_master";
    private static final String DISTRICT_TABLE = "tbl_district_master";
    private static final String BRANCH_TABLE = "tbl_branch_master";
    private static final String USER_TABLE = "tbi_users";

    public static void main(String[] args) throws Exception {


        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        Workbook workbook = new XSSFWorkbook(excelFile);
        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();


        List<Data> dataList = new ArrayList<Data>();

        iterator.next();

        while (iterator.hasNext()) {

            Iterator<Cell> cellIterator = null;

            Row currentRow = iterator.next();
            cellIterator = currentRow.iterator();

            Data data = new Data();


            try {
                data.setDistrictName(currentRow.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue().trim().replace(",", "").replace("\"", "").replace("'", ""));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                data.setUserMobileNo(String.valueOf((long) currentRow.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getNumericCellValue()));
                data.setUserUsername(String.valueOf((long) currentRow.getCell(8, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL).getNumericCellValue()));
            } catch (Exception e) {
                e.printStackTrace();
            }


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

            dataList.add(data);

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


                try {
                    data = getOrgDistrict(c, data);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                if (data.getOrganizationId()!=0 ){

                    if (data.getOrganizationDistrictId()== 0) {

                        try {
                            data = updateOrgDistrict(c, data);
                            System.out.println("-------------ORG DISTRICT UPDATED");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }else {
                        System.out.println("ORG DISTRICT EXISTS");
                    }

                }else {

                    System.out.println("ORG NOT EXISTS");

                }


            }
        });

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


    public static Data getOrgDistrict(Connection connection, Data data) throws SQLException {

        String SQL = "" +
                "select o.org_id,o.organization_district_id,* from tbl_organization_master as o  " +
                "left join tbl_branch_master as b on b.org_id = o.org_id " +
                "left join tbi_users as u on u.branch_id = b.branch_id " +
                "inner join tbl_district_master as d on d.district_id = b.district_id " +
                "inner join tbl_organization_type_master as t on t.id = o.organization_type_id " +
                "where u.username = ?;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        pstmt.setString(1, data.getUserUsername());
        /*int affectedRows = pstmt.executeQuery();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }*/

        try (ResultSet resultSet = pstmt.executeQuery()) {
            if (resultSet.next()) {
                data.setOrganizationId((int) resultSet.getLong(1));
                int nValue = (int)resultSet.getLong(2);
                data.setOrganizationDistrictId(resultSet.wasNull() ? 0 : nValue);
            } else {
                throw new SQLException("Could not get USER ORG");
            }
        }

        return data;
    }

    public static Data updateOrgDistrict(Connection connection, Data data) throws SQLException {

        String SQL = "UPDATE public.tbl_organization_master SET organization_district_id=? WHERE org_id=?;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        pstmt.setInt(1, data.getBranchDistrictId());
        pstmt.setInt(2, data.getOrganizationId());

        int affectedRows = pstmt.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }

        /*try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                System.out.println("ORG District UPDATED Updated");
                // data.setBranchId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("ORG District UPDATED Update FAILED");
            }
        }*/

        return data;
    }

}
