package onest.dy.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
	// ����SqlServer��������Ϣ
	private static String driver;
	private static String connStr;
	private static String user;
	private static String pwd;
	// ���Ӷ���
	private static Connection conn = null;
	private static DBUtil dbUtil;

	// ��̬�����
	static {
		// ���������ļ�����ʼ��SqlServer��������
		try {
			loadDBProperty("DBConfig.properties");
			// �������ݿ�
			connectToDB();
		} catch (ClassNotFoundException | IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	private DBUtil() {

	}

	// �ṩ��ȡ��ǰ��Ķ���Ľӿ�
	// ����ģʽ
	public static DBUtil getInstance() throws ClassNotFoundException, SQLException {
		// ֻ����һ�����󼴿�
		if (null == dbUtil) {
			dbUtil = new DBUtil();
		}
		return dbUtil;
	}

	/**
	 * �������ݿ������ļ�
	 * 
	 * @param pFile
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	private static void loadDBProperty(String pFile) throws IOException, ClassNotFoundException, SQLException {
		// ����Properties����
		Properties prop = new Properties();
		// ���������ļ�
		prop.load(DBUtil.class.getClassLoader().getResourceAsStream(pFile));
		driver = prop.getProperty("DRIVER");
		connStr = prop.getProperty("CONN_STR");
		user = prop.getProperty("USER");
		pwd = prop.getProperty("PWD");
	}

	// ��ȡ���Ӷ���
	private static void connectToDB() throws SQLException, ClassNotFoundException {
		if (null == conn || conn.isClosed()) {
			Class.forName(driver);
			conn = DriverManager.getConnection(connStr, user, pwd);
		}
	}

	/**
	 * ��ѯ����
	 * 
	 * @param sql ��ѯ���ݵ�sql���
	 * @return ��ѯ�������ݼ�
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public ResultSet queryDate(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// ִ�в�ѯ
		ResultSet rs = pstmt.executeQuery();
		return rs;
	}

	/**
	 * �ж������Ƿ����
	 * 
	 * @param sql ��ѯ��sql���
	 * @return �����򷵻�true�����򷵻�false
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public boolean isExist(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// ִ�в�ѯ
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	}

	/**
	 * ��������
	 * 
	 * @param sql ִ�в����sql���
	 * @return �����¼������
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public int addDataToTable(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// ִ�в���
		return pstmt.executeUpdate();
	}

	/**
	 * �޸�����
	 * 
	 * @param sql ��������SQL���
	 * @return �޸Ļ�ɾ���ļ�¼����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int updateData(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// ִ���޸Ļ�ɾ��
		return pstmt.executeUpdate();
	}
	
	/**
	 * ɾ������
	 * 
	 * @param sql ��������SQL���
	 * @return �޸Ļ�ɾ���ļ�¼����
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public int deleteData(String sql) throws ClassNotFoundException, SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		// ִ���޸Ļ�ɾ��
		return pstmt.executeUpdate();
	}

	/**
	 * �ر����ݿ�����
	 * 
	 * @throws SQLException
	 */
	public void closeDB() throws SQLException {
		if (null != conn && !conn.isClosed()) {
			conn.close();
		}
	}
}
