package app.tool;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class UpdateOrgDistrictFromBranch {

    private static final String ORGANIZATION_TABLE = "tbl_organization_master";
    private static final String DISTRICT_TABLE = "tbl_district_master";
    private static final String BRANCH_TABLE = "tbl_branch_master";
    private static final String USER_TABLE = "tbi_users";

    public static void main(String[] args) throws Exception {

        Connection c = PostgreSqlConnect.connectToDatabaseOrDie();

        Map<Integer,Data> orgDistrictMap = getAllOrganizations(c);
        Map<Integer,Data> branchOrgDistrictMap = getAllBranchesORG(c);

        Map<Integer,Integer> orgUpdateMap = new HashMap<>();

        orgDistrictMap.forEach(new BiConsumer<Integer, Data>() {
            @Override
            public void accept(Integer integer, Data data) {

                if (data.getOrganizationDistrictId()==0){
                    try {
                        data.setOrganizationDistrictId( branchOrgDistrictMap.get(data.getOrganizationId()).getBranchDistrictId());

                        orgUpdateMap.put(integer,data.getOrganizationDistrictId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        updateOrgDistrict(c,orgUpdateMap);

        c.close();

        System.out.println("asd");


    }

    private static void updateOrgDistrict(Connection c, Map<Integer, Integer> orgUpdateMap) throws Exception {

        String SQL = "UPDATE public.tbl_organization_master SET organization_district_id=? WHERE org_id=?;";
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


    public static Map<Integer,Data> getAllOrganizations(Connection connection) throws SQLException {

        String SQL = "select org_id,organization_district_id from tbl_organization_master;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        Map<Integer,Data> orgMap =new HashMap<>();

        try (ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Data data = new Data();

                data.setOrganizationId((int) resultSet.getLong(1));
                int nValue = (int)resultSet.getLong(2);
                data.setOrganizationDistrictId(resultSet.wasNull() ? 0 : nValue);

                orgMap.put(data.getOrganizationId(),data);
            }
        }

        return orgMap;
    }

    public static Map<Integer,Data> getAllBranchesORG(Connection connection) throws SQLException {

        String SQL = "select org_id,district_id from tbl_branch_master;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        Map<Integer,Data> orgBranchMap =new HashMap<>();

        try (ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Data data = new Data();

                data.setOrganizationId((int) resultSet.getLong(1));
                int nValue = (int)resultSet.getLong(2);
                data.setBranchDistrictId(resultSet.wasNull() ? 0 : nValue);

                orgBranchMap.put(data.getOrganizationId(),data);
            }
        }

        return orgBranchMap;
    }

}
