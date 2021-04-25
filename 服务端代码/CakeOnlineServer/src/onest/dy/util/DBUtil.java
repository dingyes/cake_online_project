package onest.dy.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	// 连接SqlServer的配置信息
	private static String driver;
	private static String connStr;
	private static String user;
	private static String pwd;
	// 连接对象
	private static Connection conn = null;
	private static DBUtil dbUtil;

	// 静态代码块
	static {
		// 加载配置文件，初始化SqlServer配置属性
		try {
			loadDBProperty("DBConfig.properties");
			// 连接数据库
			connectToDB();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private DBUtil() {

	}

	// 提供获取当前类的对象的接口
	// 单例模式
	public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
		// 只产生一个对象即可
		if (null == dbUtil) {
			dbUtil = new DBUtil();
		}
		return dbUtil;
	}

	/**
	 * 加载数据库配置文件
	 * 
	 * @param pFile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
		// 创建Properties对象
		Properties prop = new Properties();
		// 加载配置文件
		prop.load(DBUtil.class.getClassLoader().getResourceAsStream(pFile));
		driver = prop.getProperty("DRIVER");
		connStr = prop.getProperty("CONN_STR");
		user = prop.getProperty("USER");
		pwd = prop.getProperty("PWD");
	}

	// 获取连接对象
	private static void connectToDB() throws SQLException, ClassNotFoundException {
		if (null == conn || conn.isClosed()) {
			Class.forName(driver);
			conn = DriverManager.getConnection(connStr, user, pwd);
		}
	}

	/**
	 * 查询数据
	 * 
	 * @param sql 查询数据的sql语句
	 * @return 查询到的数据集
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// 执行查询
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}

	/**
	 * 判断数据是否存在
	 * 
	 * @param sql 查询的sql语句
	 * @return 存在则返回true，否则返回false
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// 执行查询
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}

	/**
	 * 插入数据
	 * 
	 * @param sql 执行插入的sql语句
	 * @return 插入记录的行数
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int addDataToTable(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// 执行插入
		return pstmt.executeUpdate();
	}

	/**
	 * 修改数据
	 * 
	 * @param sql 待操作的SQL语句
	 * @return 修改或删除的记录行数
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int updateData(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// 执行修改或删除
		return pstmt.executeUpdate();
	}
	
	/**
	 * 删除数据
	 * 
	 * @param sql 待操作的SQL语句
	 * @return 修改或删除的记录行数
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int deleteData(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// 执行修改或删除
		return pstmt.executeUpdate();
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void closeDB() throws SQLException {
		if (null != conn && !conn.isClosed()) {
			conn.close();
		}
	}
}
