package app.tool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class UpdateUserDistrictFromBranch {

    private static final String ORGANIZATION_TABLE = "tbl_organization_master";
    private static final String DISTRICT_TABLE = "tbl_district_master";
    private static final String BRANCH_TABLE = "tbl_branch_master";
    private static final String USER_TABLE = "tbi_users";

    public static void main(String[] args) throws Exception {

        Connection c = PostgreSqlConnect.connectToDatabaseOrDie();

        Map<Integer,Data> userDistrictMap = getAllUsers(c);
        Map<Integer,Data> branchDistrictMap = getAllBranchesORG(c);

        Map<Integer,Integer> userUpdateMap = new HashMap<>();

        userDistrictMap.forEach(new BiConsumer<Integer, Data>() {
            @Override
            public void accept(Integer integer, Data data) {

                if (data.getUser_district_id()==0){
                    try {
                        data.setUser_district_id( branchDistrictMap.get(data.getBranchId()).getBranchDistrictId());

                        userUpdateMap.put(integer,data.getUser_district_id());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        updateUserDistrict(c,userUpdateMap);

        c.close();

        System.out.println("asd");


    }

    private static void updateUserDistrict(Connection c, Map<Integer, Integer> orgUpdateMap) throws Exception {

        String SQL = "UPDATE public.tbi_users SET district_id=? WHERE id=?;";
        PreparedStatement pstmt = c.prepareStatement(SQL);

        orgUpdateMap.forEach(new BiConsumer<Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer integer2) {

                try {
                    pstmt.setInt(1, integer2);
                    pstmt.setInt(2, integer);


                    pstmt.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        int[] result = pstmt.executeBatch();
        System.out.println(result);


    }


    public static Map<Integer,Data> getAllUsers(Connection connection) throws SQLException {

        String SQL = "select id,branch_id,district_id from tbi_users where role_id in (1,2);";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        Map<Integer,Data> orgMap =new HashMap<>();

        try (ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Data data = new Data();

                data.setUserId((int) resultSet.getLong(1));
                data.setBranchId((int) resultSet.getLong(2));
                int nValue = (int)resultSet.getLong(3);
                data.setUser_district_id(resultSet.wasNull() ? 0 : nValue);

                orgMap.put(data.getUserId(),data);
            }
        }

        return orgMap;
    }

    public static Map<Integer,Data> getAllBranchesORG(Connection connection) throws SQLException {

        String SQL = "select branch_id,district_id from tbl_branch_master;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        Map<Integer,Data> orgBranchMap =new HashMap<>();

        try (ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Data data = new Data();

                data.setBranchId((int) resultSet.getLong(1));
                int nValue = (int)resultSet.getLong(2);
                data.setBranchDistrictId(resultSet.wasNull() ? 0 : nValue);

                orgBranchMap.put(data.getBranchId(),data);
            }
        }

        return orgBranchMap;
    }

}
