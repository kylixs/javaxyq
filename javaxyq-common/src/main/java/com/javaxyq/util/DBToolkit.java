package com.javaxyq.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

@Slf4j
public class DBToolkit {

    private static DataSource datasource;
    private static boolean forceInit;

    synchronized public static DataSource getDataSource() {
        if (datasource == null) {
            datasource = getDataSource(false);
        }
        return datasource;
    }

    public static DataSource getDataSource(boolean create) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("org.sqlite.JDBC");
        hikariConfig.setJdbcUrl("jdbc:sqlite:test.db");
        return new HikariDataSource(hikariConfig);
    }

    public static void prepareDatabase() {
        log.info("[db]starting DB");
        try {
            DBToolkit.getDataSource().getConnection();
            if (forceInit) {
                initDataBase();
            }
        } catch (SQLException e) {
            log.error("启动数据库失败", e);
            initDataBase();
        }

        log.info("[db]started DB" );
    }

    protected static void initDataBase() {
        try {
            log.info("[db]初始化数据库...");
            // 创建数据库
            DataSource ds = getDataSource(true);
            Connection _conn = ds.getConnection();
            ScriptRunner runner = new ScriptRunner(_conn, true, false);
            File[] files = IoUtil.loadFiles("classpath:sql/*.sql");
            log.info("length is:" + files.length);
            for (int i = 0; i < files.length; i++) {
                log.info("[db]导入: " + files[i].getName() + " ..");
                //importSQL(_conn, new FileInputStream(files[i]));
                runner.runScript(new FileReader(files[i]));
            }

            // 导入初始数据
            log.info("[db]导入初始化数据...");
            File[] datafiles = IoUtil.loadFiles("classpath:sql/*.csv");
            for (File file : datafiles) {
                log.info("[db]导入：{} ...", file.getName());
                PreparedStatement preStatment = null;
                String tableName = file.getName().replace(".csv", "");
                Scanner scanner = new Scanner(file, "utf-8");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    if (values.length > 1) {
                        preStatment = createInsertStatement(_conn, preStatment, tableName, values);
                        preStatment.clearParameters();
                        for (int i = 0; i < values.length; i++) {
                            preStatment.setObject(i + 1, strip(values[i]));
                        }
                        preStatment.execute();
                    }
                }
                if (preStatment != null) {
                    preStatment.close();
                }
            }
            log.info("[db]初始化数据库完毕.");
        } catch (Exception e) {
            log.error("[db]初始化数据库失败", e);
        }
    }

    private static String strip(String str) {
        str = str.replaceAll("'", "").trim();
        return str;
    }

    private static PreparedStatement createInsertStatement(Connection conn, PreparedStatement statment, String tableName, String[] values)
            throws SQLException {
        if (statment == null) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("insert into ").append(tableName).append(" values(");
            for (int i = 0; i < values.length; i++) {
                sbSQL.append("?");
                if (i < values.length - 1) {
                    sbSQL.append(",");
                }
            }
            sbSQL.append(")");
            statment = conn.prepareStatement(sbSQL.toString());
        }
        return statment;
    }

    private static String readFileContent(File file) {
        try {
            //System.out.println("[db]loadFile: " + file.getPath());
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            byte[] buf = new byte[dis.available()];
            dis.readFully(buf);
            return new String(buf);
        } catch (Exception e) {
            log.warn("读取文件失败：" + file.getName() + ", " + e.getMessage());
        }
        return null;
    }

    public static boolean isForceInit() {
        return forceInit;
    }

    public static void setForceInit(boolean forceInit) {
        DBToolkit.forceInit = forceInit;
    }

//	public static void importSQL(Connection conn, InputStream in) throws SQLException {
//		Scanner s = new Scanner(in);
//		s.useDelimiter("(;(\r)?\n)|(--\n)");
//		Statement st = null;
//		try {
//			st = conn.createStatement();
//			while (s.hasNext()) {
//				String line = s.next();
//				if (line.startsWith("/*!") && line.endsWith("*/")) {
//					int i = line.indexOf(' ');
//					line = line.substring(i + 1, line.length() - " */".length());
//				}
//
//				if (line.trim().length() > 0) {
//					st.execute(line);
//				}
//			}
//		} finally {
//			if (st != null)
//				st.close();
//		}
//	}

}

