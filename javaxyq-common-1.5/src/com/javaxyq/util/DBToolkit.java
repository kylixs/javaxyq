package com.javaxyq.util;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;

public class DBToolkit {

	private static Connection conn;
	private static DataSource datasource;
	private static boolean forceInit;

	public static Connection getConnection(String username, String password) throws ClassNotFoundException,
			SQLException {
		if (conn == null || conn.isClosed()) {
			synchronized (DBToolkit.class) {
				if (conn == null || conn.isClosed()) {
					Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
					String url = "jdbc:derby:xyqdb";
					conn = DriverManager.getConnection(url, username, password);
				}
			}
		}
		return conn;
	}

	synchronized public static DataSource getDataSource() {
		if (datasource == null) {
			datasource = getDataSource(false);
		}
		return datasource;
	}

	public static DataSource getDataSource(boolean create) {
		EmbeddedDataSource _datasource = new EmbeddedDataSource();
		//String dbdir = System.getProperty("user.home") + "/javaxyq/xyqdb";
		String dbdir = "xyqdb";
		_datasource.setDatabaseName(dbdir);
		if (create) {
			_datasource.setCreateDatabase("create");
		}
		return _datasource;
	}

	public static void prepareDatabase() {
		System.out.println("[db]starting DB at: " + new java.util.Date());
		try {
			DBToolkit.getDataSource().getConnection();
			if(forceInit) {
				initDataBase();
			}
		} catch (SQLException e) {
			System.err.println("启动数据库失败：" + e.getMessage());
			// e.printStackTrace();
			initDataBase();
		}
		System.out.println("[db]started DB at: " + new java.util.Date());
	}

	protected static void initDataBase() {
		try {
			System.out.println("[db]初始化数据库...");
			File dir = new File("sql");
			// 创建数据库
			DataSource ds = getDataSource(true);
			Connection _conn = ds.getConnection();
			ScriptRunner runner = new ScriptRunner(_conn, true, false);
			File[] files = dir.listFiles(new SuffixFilenameFilter(".sql"));
			for (int i = 0; i < files.length; i++) {
				System.out.println("[db]导入: "+files[i].getName()+" ..");
				//importSQL(_conn, new FileInputStream(files[i]));
				runner.runScript(new FileReader(files[i]));
			}

			// 导入初始数据
			System.out.println("[db]导入初始化数据...");
			File[] datafiles = dir.listFiles(new SuffixFilenameFilter(".csv"));
			for (File file : datafiles) {
				System.out.println("[db]导入："+file.getName()+" ...");
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
				if(preStatment != null) {
					preStatment.close();
				}
			}

			System.out.println("[db]初始化数据库完毕.");
		} catch (Exception e) {
			System.err.println("[db]初始化数据库失败: " + e.getMessage());
			e.printStackTrace();
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
			System.out.println("读取文件失败：" + file.getName() + ", " + e.getMessage());
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

