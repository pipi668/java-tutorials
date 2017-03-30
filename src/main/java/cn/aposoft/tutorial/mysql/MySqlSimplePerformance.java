/**
 * 
 */
package cn.aposoft.tutorial.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * @author LiuJian
 *
 */
public class MySqlSimplePerformance {
    private static final char MAX_CJK_CHAR = '\u9FBF';
    
    private static final char EMOJI_HIGH_CHAR = '\uD800';
    private static final char EMOJI_HIGH_END_CHAR = '\uDBFF';
    
    private static final char EMOJI_LOW_CHAR = '\uDC00';
    private static final char EMOJI_LOW_END_CHAR = '\uDFFF';

    /**
     * @param args
     */
    public static void main(String[] args) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8");
        dataSource.setUser("liujian");
        dataSource.setPassword("liujian");

        testInsert(dataSource);
        // testInsertAtRepeatableReadLevel(dataSource);
    }

    private final static String insert = "INSERT INTO c_encoding_test (c_encoding_test,c_int_value) VALUES (?,?)";

    public static void testInsert(MysqlDataSource dataSource) {
        boolean error = false;
        int errorBegin = -1;
        int errorEnd = -1;

        int errorBegin2 = -1;
        int errorEnd2 = -1;

        int maxValue = 65535;
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);

            System.out.println(insert);
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                long begin = System.currentTimeMillis();
                // char c1 = '\u0000';
                // char c2 = MAX_CJK_CHAR;
                char c1 = EMOJI_HIGH_END_CHAR;
                // char c2 = 0;
                // private static final char EMOJI_LOW_CHAR = '\uDC00';
                char[] charArr = new char[2];
                for (int j = c1; j <= 65535; j++) {
                    char c2 = 0;
                    for (int i = c2; i <= 65535; i++) {
                        charArr[0] = c1;
                        charArr[1] = c2;
                        String s = String.valueOf(charArr);
                        stmt.setString(1, s);
                        stmt.setInt(2, c1);
                        try {
                            int result = stmt.executeUpdate();
                            // System.out.println(i + "," + result + "" + s);
                            if (error == true) {
                                error = false;
                                errorEnd = c1;
                                // errorEnd = c1 - 1;
                                errorEnd2 = c2 - 1;
                                System.out
                                        .println("error region:" + "begin:" + errorBegin + "," + errorBegin2 + ",end:" + errorEnd + "," + errorEnd2);
                            }
                        } catch (SQLException e1) {
                            if (error == false) {
                                error = true;
                                errorBegin = c1;
                                errorBegin2 = c2;
                                System.out.println("error begin:" + errorBegin + "," + errorBegin2);
                            }
                        }

                        // c1++;
                        c2++;
                        // c2--;
                        // if (c2 <= c1) {
                        // break;
                        // }
                        if (i % 1000 == 0) {
                            // System.out.println("commit.");
                            conn.commit();
                        }
                    }
                    c1++;
                }
                long end = System.currentTimeMillis();
                System.out.println("insert " + maxValue + ":" + (end - begin));
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void testInsertAtRepeatableReadLevel(MysqlDataSource dataSource) {

        try (Connection conn = dataSource.getConnection()) {
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            conn.setAutoCommit(false);
            try (Statement stmt = conn.createStatement();) {
                long begin = System.currentTimeMillis();

                for (int i = 0; i < 1000; i++) {

                    boolean result = stmt.execute(insert);
                    conn.commit();
                }
                long end = System.currentTimeMillis();
                System.out.println("insert 1000:" + (end - begin));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
