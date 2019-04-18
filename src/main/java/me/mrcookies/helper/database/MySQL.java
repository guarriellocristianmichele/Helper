package me.mrcookies.helper.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.mrcookies.helper.utils.References;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class MySQL {

    private static final HikariConfig hcConfig = new HikariConfig();
    private static final HikariDataSource hds;

    static {
        hcConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hcConfig.setJdbcUrl("jdbc:mysql://" + References.host + ":" + References.port + "/" + References.database);
        hcConfig.setUsername(References.username);
        hcConfig.setPassword(References.password);
        hcConfig.addDataSourceProperty("cachePrepStmts", "true");
        hcConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hcConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hcConfig.setConnectionTimeout(30000);
        hcConfig.setMaximumPoolSize(50);
        hcConfig.setLeakDetectionThreshold(60 * 1000);
        hcConfig.setAutoCommit(true);
        hds = new HikariDataSource(hcConfig);
    }

    private static Connection getConnection() {
        try {
            return hds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initialize() {

        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            connection.prepareStatement("SELECT 1 FROM members LIMIT 1").executeQuery().close();
            connection.prepareStatement("SELECT 1 FROM licenses LIMIT 1").executeQuery().close();
            connection.prepareStatement("SELECT 1 FROM giveaway LIMIT 1").executeQuery().close();
            connection.close();
        } catch (final SQLException e) {
            try {
                PreparedStatement ps = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `members` (\n" +
                                "  `username` text not null,\n" +
                                "  `id_long` bigint not null,\n" +
                                "  `coins` int default 0,\n" +
                                "  `daily_start` bigint default 0,\n" +
                                "  `rob_start` bigint default 0,\n" +
                                "  `warns` int default 0" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                );

                PreparedStatement pss = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `licenses` (\n" +
                                "  `license` text not null,\n" +
                                "  `value` int not null,\n" +
                                "  `created` bigint not null,\n" +
                                "  `redeemed` bigint\n" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                );

                PreparedStatement ga = connection.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `giveaway` (\n" +
                                "  `id` bigint not null,\n" +
                                "  `prize` text not null\n" +
                                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;"
                );

                ps.executeUpdate();
                ps.close();
                pss.executeUpdate();
                pss.close();
                ga.executeUpdate();
                ga.close();
                connection.close();
                System.out.println("Helper > Database Structure Created...");
            } catch (final SQLException e1) {
                System.out.println("Error -> " + e1.getClass().getSimpleName());
                try {
                    connection.close();
                } catch (SQLException e2) {
                    System.out.println("Error -> " + e2.getCause().getMessage());
                }
            }
        }
    }

    public String getString(String table, String key, String where, String value) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String result = rs.getString(key);
                rs.close();
                ps.close();
                connection.close();
                return result;
            }
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public Long getLong(String table, String key, String where, String value) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Long result = rs.getLong(key);
                rs.close();
                ps.close();
                connection.close();
                return result;
            }
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public int getInt(String table, String key, String where, String value) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int result = rs.getInt(key);
                rs.close();
                ps.close();
                connection.close();
                return result;
            }
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return 0;
    }

    public boolean getBool(String table, String key, String where, String value) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement ps = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            ps.setString(1, value);
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                boolean result = rs.getBoolean(key);
                rs.close();
                ps.close();
                connection.close();
                return result;
            }
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return false;
    }

    public void setInt(String table, String key, int value, String where, String wherevalue) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");
        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            check.setString(1, wherevalue);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", table, key, where));
                ps.setInt(1, value);
                ps.setString(2, wherevalue);
            } else {
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", table, where, key));
                ps.setString(1, wherevalue);
                ps.setInt(2, value);
                check.close();
            }

            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void setLong(String table, String key, Long value, String where, String wherevalue) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");
        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            check.setString(1, wherevalue);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", table, key, where));
                ps.setLong(1, value);
                ps.setString(2, wherevalue);
            } else {
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", table, where, key));
                ps.setString(1, wherevalue);
                ps.setLong(2, value);
                check.close();
            }

            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public MySQL setString(String table, String key, String value, String where, String wherevalue) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");
        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            check.setString(1, wherevalue);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", table, key, where));
                ps.setString(1, value);
                ps.setString(2, wherevalue);
            } else {
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", table, where, key));
                ps.setString(1, wherevalue);
                ps.setString(2, value);
                check.close();
            }

            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return this;
    }

    public MySQL setBool(String table, String key, boolean value, String where, String wherevalue) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            check.setString(1, wherevalue);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("UPDATE %s SET %s = ? WHERE %s = ?", table, key, where));
                ps.setInt(1, value ? 1 : 0);
                ps.setString(2, wherevalue);
            } else {
                ps = connection.prepareStatement(String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)", table, where, key));
                ps.setString(1, wherevalue);
                ps.setInt(2, value ? 1 : 0);
            }

            check.close();

            ps.execute();
            ps.close();
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return this;
    }

    public MySQL dropEntry(String table, String where, String wherevalue) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement(String.format("SELECT * FROM %s WHERE %s = ?", table, where));
            check.setString(1, wherevalue);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement(String.format("DELETE FROM %s WHERE %s = ?", table, where));
                ps.setString(1, wherevalue);
                ps.execute();
                ps.close();
            }

            check.close();
            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

        return this;
    }

    public void addMember(String username, Long id) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            Long check = getLong("members", "id_long", "id_long", String.valueOf(id));

            if (check == null) {
                ps = connection.prepareStatement("INSERT INTO members (username, id_long) VALUES (?,?);");
                ps.setString(1, username);
                ps.setLong(2, id);
                ps.execute();
                ps.close();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void createLicense(String license, int value, long created) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            String check = getString("licenses", "license", "license", license);

            if (check == null) {
                ps = connection.prepareStatement("INSERT INTO licenses (license, value, created) VALUES (?,?,?);");
                ps.setString(1, license);
                ps.setInt(2, value);
                ps.setLong(3, created);
                ps.execute();
                ps.close();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public void redeemLicense(String license, long redeemed) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            final PreparedStatement check = connection.prepareStatement("SELECT * FROM licenses WHERE license = ?");
            check.setString(1, license);

            if (check.executeQuery().next()) {
                ps = connection.prepareStatement("UPDATE licenses SET redeemed = ? WHERE license = ?");
                ps.setLong(1, redeemed);
                ps.setString(2, license);
                ps.execute();
                ps.close();
            }

            check.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public boolean isLicenseRedeemed(String license) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement check = connection.prepareStatement("SELECT * FROM licenses WHERE license = ?");
            check.setString(1, license);
            ResultSet rs = check.executeQuery();
            boolean redeemed = true;

            if (rs.next()) {
                rs.getLong("redeemed");
                redeemed = !rs.wasNull();
            }

            check.close();
            connection.close();
            return redeemed;
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    public void removeMember(Long id) {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            PreparedStatement ps;
            Long check = getLong("members", "id_long", "id_long", String.valueOf(id));

            if (check == null) {
                connection.close();
                return;
            }

            ps = connection.prepareStatement("DELETE FROM members WHERE id_long = ?;");
            ps.setLong(1, id);
            ps.execute();
            ps.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }

    }

    public ArrayList<String> getLicenses(int start, int limit) {

        ArrayList<String> licenses = new ArrayList<>();
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {

            final PreparedStatement ps;
            ps = connection.prepareStatement("SELECT license FROM licenses LIMIT ?");
            ps.setInt(1, limit);

            final ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {

                if (count < start) {
                    count++;
                    continue;
                }

                String result = rs.getString("license");
                licenses.add(result);
            }

            rs.close();
            ps.close();
            connection.close();
            return licenses;
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public ArrayList<Long> getTop(int start, int limit, String table, String key, String value, boolean b) {

        ArrayList<Long> ids = new ArrayList<>();
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {

            final PreparedStatement ps;

            if (b) {
                ps = connection.prepareStatement(String.format("SELECT %s FROM %s ORDER BY %s DESC LIMIT ?", key, table, value));
                ps.setInt(1, limit);
            } else {
                ps = connection.prepareStatement(String.format("SELECT %s FROM %s ORDER BY %s DESC", key, table, value));
            }

            final ResultSet rs = ps.executeQuery();

            int count = 0;
            while (rs.next()) {

                if (count < start) {
                    count++;
                    continue;
                }

                Long result = rs.getLong(key);
                ids.add(result);
            }

            rs.close();
            ps.close();
            connection.close();
            return ids;
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public Long getGiveawayID() {
        Connection connection = Objects.requireNonNull(getConnection(), "SQL Connection is null");

        try {
            final PreparedStatement ps = connection.prepareStatement("SELECT id FROM giveaway");
            final ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Long result = rs.getLong("id");
                rs.close();
                ps.close();
                connection.close();
                return result;
            }

            connection.close();
        } catch (final SQLException e) {
            e.printStackTrace();

            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public int getCoins(Long id) {
        return getInt("members", "coins", "id_long", String.valueOf(id));
    }

    public int getWarns(Long id) {
        return getInt("members", "warns", "id_long", String.valueOf(id));
    }

}
