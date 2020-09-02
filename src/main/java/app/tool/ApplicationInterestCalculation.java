package app.tool;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

public class ApplicationInterestCalculation {

    private static int totalMonths = 36;
    private static double perDayInterest = 0.08d / 365.00d;

    public static void main(String[] args) throws Exception {

        Connection c = PostgreSqlConnect.connectToDatabaseOrDie();

        Map<Integer, Application> applicationMap = getAllLoanData(c);

        applicationMap.forEach(new BiConsumer<Integer, Application>() {
            @Override
            public void accept(Integer integer, Application application) {

                Map<String, Double> interestCalculation = new HashMap<>();

                LocalDate startDate = application.getSanctionedDate();
                LocalDate endDate = application.getSanctionedDate().plusMonths(totalMonths - 1);
                LocalDate itrDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

                long monthDays = ChronoUnit.DAYS.between(startDate, itrDate) + 1; //Month 1 Days
                double cumulativeInterest = 0;

                System.out.println(startDate.toString());

                for (int i = 1; i < totalMonths; i++) {
                    double interestAmount = round(perDayInterest * monthDays * (application.getSanctionedAmount() + cumulativeInterest));
                    interestCalculation.put("" + itrDate.getYear() + "_" + monthString(itrDate), interestAmount);
                    cumulativeInterest += interestAmount;
                    System.out.println(itrDate.toString() + " " + interestAmount);
                    LocalDate nextDate = itrDate.plusMonths(1);
                    nextDate = nextDate.withDayOfMonth(nextDate.lengthOfMonth());
                    monthDays = ChronoUnit.DAYS.between(itrDate, nextDate); //Month 1 Days
                    itrDate = nextDate;
                }

                itrDate = itrDate.withDayOfMonth(1);
                monthDays = ChronoUnit.DAYS.between(itrDate, endDate) + 1; //Month End Days
                double interestAmount = round(perDayInterest * monthDays * (application.getSanctionedAmount() + cumulativeInterest));
                interestCalculation.put("" + itrDate.getYear() + "_" + monthString(itrDate), interestAmount);
                System.out.println(endDate.toString() + " " + interestAmount);

                application.setInterestCalculation(interestCalculation);
            }
        });


        insertInterest(c,applicationMap);

        c.close();

        System.out.println("asd");

    }


    public static Map<Integer, Application> getAllLoanData(Connection connection) throws SQLException {

        String SQL = "select aa.applicant_id,aa.sanctioned_loan_amount,ai.disbursed_date_time from tbl_applicant_detail_master as aa inner join tbi_loan_status as ai on aa.applicant_id=ai.applicant_detail_id where ai.is_disbursed = true;";

        PreparedStatement pstmt = connection.prepareStatement(SQL);

        Map<Integer, Application> applicationMap = new HashMap<>();

        try (ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {

                Application application = new Application();

                application.setApplicantId((int) resultSet.getLong(1));
                application.setSanctionedAmount((double) resultSet.getFloat(2));
                Timestamp timestamp = resultSet.getTimestamp(3);
                if (timestamp != null) {
                    application.setSanctionedDate(timestamp.toLocalDateTime().toLocalDate());
                }
                applicationMap.put(application.getApplicantId(), application);
            }
        }

        return applicationMap;
    }

    private static void insertInterest(Connection c, Map<Integer, Application> applicationMap) throws Exception {

        String SQL = "INSERT INTO public.tbl_loan_interest(" +
                " applicant_id, \"2020_06\", \"2020_07\", \"2020_08\", \"2020_09\", \"2020_10\", \"2020_11\", \"2020_12\", \"2021_01\", \"2021_02\", \"2021_03\", \"2021_04\", \"2021_05\", \"2021_06\", \"2021_07\", \"2021_08\", \"2021_09\", \"2021_10\", \"2021_11\", \"2021_12\", \"2022_01\", \"2022_02\", \"2022_03\", \"2022_04\", \"2022_05\", \"2022_06\", \"2022_07\", \"2022_08\", \"2022_09\", \"2022_10\", \"2022_11\", \"2022_12\", \"2023_01\", \"2023_02\", \"2023_03\", \"2023_04\", \"2023_05\", \"2023_06\", \"2023_07\", \"2023_08\", \"2023_09\", \"2023_10\")\n" +
                " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);;";
        PreparedStatement pstmt = c.prepareStatement(SQL);

        applicationMap.forEach(new BiConsumer<Integer, Application>() {
            @Override
            public void accept(Integer integer, Application application) {
                try {
                    pstmt.setInt(1, application.getApplicantId());

                    if (application.getInterestCalculation().containsKey("2020_06")){
                        pstmt.setDouble(2, application.getInterestCalculation().get("2020_06"));
                    }else {
                        pstmt.setNull(2, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_07")){
                        pstmt.setDouble(3, application.getInterestCalculation().get("2020_07"));
                    }else {
                        pstmt.setNull(3, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_08")){
                        pstmt.setDouble(4, application.getInterestCalculation().get("2020_08"));
                    }else {
                        pstmt.setNull(4, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_09")){
                        pstmt.setDouble(5, application.getInterestCalculation().get("2020_09"));
                    }else {
                        pstmt.setNull(5, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_10")){
                        pstmt.setDouble(6, application.getInterestCalculation().get("2020_10"));
                    }else {
                        pstmt.setNull(6, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_11")){
                        pstmt.setDouble(7, application.getInterestCalculation().get("2020_11"));
                    }else {
                        pstmt.setNull(7, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2020_12")){
                        pstmt.setDouble(8, application.getInterestCalculation().get("2020_12"));
                    }else {
                        pstmt.setNull(8, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_01")){
                        pstmt.setDouble(9, application.getInterestCalculation().get("2021_01"));
                    }else {
                        pstmt.setNull(9, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_02")){
                        pstmt.setDouble(10, application.getInterestCalculation().get("2021_02"));
                    }else {
                        pstmt.setNull(10, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_03")){
                        pstmt.setDouble(11, application.getInterestCalculation().get("2021_03"));
                    }else {
                        pstmt.setNull(11, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_04")){
                        pstmt.setDouble(12, application.getInterestCalculation().get("2021_04"));
                    }else {
                        pstmt.setNull(12, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_05")){
                        pstmt.setDouble(13, application.getInterestCalculation().get("2021_05"));
                    }else {
                        pstmt.setNull(13, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_06")){
                        pstmt.setDouble(14, application.getInterestCalculation().get("2021_06"));
                    }else {
                        pstmt.setNull(14, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_07")){
                        pstmt.setDouble(15, application.getInterestCalculation().get("2021_07"));
                    }else {
                        pstmt.setNull(15, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_08")){
                        pstmt.setDouble(16, application.getInterestCalculation().get("2021_08"));
                    }else {
                        pstmt.setNull(16, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_09")){
                        pstmt.setDouble(17, application.getInterestCalculation().get("2021_09"));
                    }else {
                        pstmt.setNull(17, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_10")){
                        pstmt.setDouble(18, application.getInterestCalculation().get("2021_10"));
                    }else {
                        pstmt.setNull(18, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_11")){
                        pstmt.setDouble(19, application.getInterestCalculation().get("2021_11"));
                    }else {
                        pstmt.setNull(19, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2021_12")){
                        pstmt.setDouble(20, application.getInterestCalculation().get("2021_12"));
                    }else {
                        pstmt.setNull(20, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_01")){
                        pstmt.setDouble(21, application.getInterestCalculation().get("2022_01"));
                    }else {
                        pstmt.setNull(21, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_02")){
                        pstmt.setDouble(22, application.getInterestCalculation().get("2022_02"));
                    }else {
                        pstmt.setNull(22, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_03")){
                        pstmt.setDouble(23, application.getInterestCalculation().get("2022_03"));
                    }else {
                        pstmt.setNull(23, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_04")){
                        pstmt.setDouble(24, application.getInterestCalculation().get("2022_04"));
                    }else {
                        pstmt.setNull(24, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_05")){
                        pstmt.setDouble(25, application.getInterestCalculation().get("2022_05"));
                    }else {
                        pstmt.setNull(25, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_06")){
                        pstmt.setDouble(26, application.getInterestCalculation().get("2022_06"));
                    }else {
                        pstmt.setNull(26, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_07")){
                        pstmt.setDouble(27, application.getInterestCalculation().get("2022_07"));
                    }else {
                        pstmt.setNull(27, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_08")){
                        pstmt.setDouble(28, application.getInterestCalculation().get("2022_08"));
                    }else {
                        pstmt.setNull(28, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_09")){
                        pstmt.setDouble(29, application.getInterestCalculation().get("2022_09"));
                    }else {
                        pstmt.setNull(29, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_10")){
                        pstmt.setDouble(30, application.getInterestCalculation().get("2022_10"));
                    }else {
                        pstmt.setNull(30, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_11")){
                        pstmt.setDouble(31, application.getInterestCalculation().get("2022_11"));
                    }else {
                        pstmt.setNull(31, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2022_12")){
                        pstmt.setDouble(32, application.getInterestCalculation().get("2022_12"));
                    }else {
                        pstmt.setNull(32, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_01")){
                        pstmt.setDouble(33, application.getInterestCalculation().get("2023_01"));
                    }else {
                        pstmt.setNull(33, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_02")){
                        pstmt.setDouble(34, application.getInterestCalculation().get("2023_02"));
                    }else {
                        pstmt.setNull(34, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_03")){
                        pstmt.setDouble(35, application.getInterestCalculation().get("2023_03"));
                    }else {
                        pstmt.setNull(35, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_04")){
                        pstmt.setDouble(36, application.getInterestCalculation().get("2023_04"));
                    }else {
                        pstmt.setNull(36, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_05")){
                        pstmt.setDouble(37, application.getInterestCalculation().get("2023_05"));
                    }else {
                        pstmt.setNull(37, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_06")){
                        pstmt.setDouble(38, application.getInterestCalculation().get("2023_06"));
                    }else {
                        pstmt.setNull(38, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_07")){
                        pstmt.setDouble(39, application.getInterestCalculation().get("2023_07"));
                    }else {
                        pstmt.setNull(39, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_08")){
                        pstmt.setDouble(40, application.getInterestCalculation().get("2023_08"));
                    }else {
                        pstmt.setNull(40, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_09")){
                        pstmt.setDouble(41, application.getInterestCalculation().get("2023_09"));
                    }else {
                        pstmt.setNull(41, Types.DOUBLE);
                    }

                    if (application.getInterestCalculation().containsKey("2023_10")){
                        pstmt.setDouble(42, application.getInterestCalculation().get("2023_10"));
                    }else {
                        pstmt.setNull(42, Types.DOUBLE);
                    }


                    pstmt.addBatch();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        int[] result = pstmt.executeBatch();
        System.out.println(result);


    }

    private static double round(double value) {
        int places = 2;
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private static String monthString(LocalDate date) {
        int month = date.getMonthValue();
        String m = "";

        if (month > 9) {
            m = "" + month;
        } else {
            m = "0" + month;
        }

        return m;
    }

}
