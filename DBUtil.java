package com.jcp.automation.common.utils;

import static com.qmetry.qaf.automation.core.ConfigurationManager.getBundle;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SocketOptions;
import com.jcp.transformer.automation.exception.JCPConfigurationFailure;
import com.jcp.transformer.automation.util.Reporter;
import com.qmetry.qaf.automation.core.MessageTypes;

public class DBUtil {
	
	/*private static String db_driver_class=ConfigurationManager.getBundle().getString("db.driver.class");
	private static String db_connection=ConfigurationManager.getBundle().getString("db.connection.url");
	private static String db_user= ConfigurationManager.getBundle().getString("db.user");
	private static String db_password= ConfigurationManager.getBundle().getString("db.pwd");*/
	
	public static String query1="SELECT yoh.order_no,  yoh.customer_emailid,  trim(yoh.extn_customer_vid),   yoh.extn_day_phone,  trim(yoh.extn_prime_dc),   trim(ys.shipment_no),"
			+" trim(concat((CASE WHEN yoh.extn_prime_dc = '02' THEN 'C'  WHEN yoh.extn_prime_dc = '03' THEN 'K' WHEN yoh.extn_prime_dc = '04' THEN 'R'  END),ys.shipment_no)),"
			+" trim(yol.item_id), yol.item_description, yors.status, ysc.tracking_no, ysc.scac"
			+" FROM yfs_order_header yoh, yfs_order_release_status yors, yfs_shipment ys, yfs_shipment_line ysl, yfs_order_line yol"
			+" LEFT JOIN yfs_container_details ycd ON ycd.order_line_key = yol.order_line_key"
			+" LEFT JOIN yfs_shipment_container ysc ON ycd.shipment_container_key = ysc.shipment_container_key"
			+" WHERE yoh.order_header_key    = yol.order_header_key AND ys.shipment_key           = ysl.shipment_key"
			+" AND yol.order_line_key        = ysl.order_line_key AND yol.order_line_key        = yors.order_line_key"
			+" AND yors.status_quantity      >0 AND yoh.document_type         ='0001'"
			+" AND yoh.order_header_key      >'20180225'" 
			+" order by yol.order_line_key";


	public static String SQL_SEARCH_STATUS="SELECT shipment_no from (SELECT ys.shipment_no"
			+" FROM yfs_order_header yoh, yfs_order_release_status yors, yfs_shipment ys, yfs_shipment_line ysl, yfs_order_line yol"
			+" LEFT JOIN yfs_container_details ycd ON ycd.order_line_key=yol.order_line_key"
			+" LEFT JOIN yfs_shipment_container ysc ON ycd.shipment_container_key=ysc.shipment_container_key"
			+" WHERE yoh.order_header_key=yol.order_header_key AND ys.shipment_key=ysl.shipment_key"
			+" AND yol.order_line_key=ysl.order_line_key AND yol.order_line_key=yors.order_line_key"
			+" AND yors.status_quantity>0 AND yoh.document_type='0001' AND yoh.purpose not in ('LOADTEST', 'TESTORDER')"
			+" AND yoh.order_header_key>'%s' AND yoh.order_header_key<'%s'"
			+" AND yors.status='%s') group by shipment_no having count(shipment_no)=1";
	
	
	public static String SQL_SEARCH_ACCOUNID_STATUS="SELECT yoh.extn_profile_id FROM yfs_order_header yoh, yfs_order_release_status yors, yfs_order_line yol"+
			" WHERE yoh.order_header_key=yol.order_header_key AND yol.order_line_key = yors.order_line_key"+ 
			" AND yors.status_quantity>0 AND yoh.document_type='0001' AND yoh.purpose not in ('LOADTEST', 'TESTORDER')"+
			" AND yoh.order_header_key>'%s' AND yoh.order_header_key<'%s' AND yors.status='%s' and yoh.extn_profile_id is not null  order by yoh.extn_profile_id desc ";
	
	public static String SQL_SEARCH_STATUS1="SELECT yors.status, trim(ys.shipment_no), trim(concat((CASE WHEN yoh.extn_prime_dc = '02' THEN 'C'  WHEN yoh.extn_prime_dc = '03' THEN 'K' WHEN yoh.extn_prime_dc = '04' THEN 'R'  END),ys.shipment_no))"
			+" FROM yfs_order_header yoh, yfs_order_release_status yors, yfs_shipment ys, yfs_shipment_line ysl, yfs_order_line yol"
			+" LEFT JOIN yfs_container_details ycd ON ycd.order_line_key=yol.order_line_key"
			+" LEFT JOIN yfs_shipment_container ysc ON ycd.shipment_container_key=ysc.shipment_container_key"
			+" WHERE yoh.order_header_key=yol.order_header_key AND ys.shipment_key=ysl.shipment_key"
			+" AND yol.order_line_key=ysl.order_line_key AND yol.order_line_key=yors.order_line_key"
			+" AND yors.status_quantity>0 AND yoh.document_type='0001'"
			+" AND yoh.order_header_key>'20170901'"
			+" AND yors.status=";
	
	public static String SQL_COUNT_SHIPMENT="SELECT trim(concat((CASE WHEN yoh.extn_prime_dc = '02' THEN 'C'  WHEN yoh.extn_prime_dc = '03' THEN 'K' WHEN yoh.extn_prime_dc = '04' THEN 'R'  END),ys.shipment_no)), trim(yol.item_id), yol.PRIME_LINE_NO"
			+" FROM yfs_order_header yoh, yfs_order_release_status yors, yfs_shipment ys, yfs_shipment_line ysl, yfs_order_line yol"
			+" LEFT JOIN yfs_container_details ycd ON ycd.order_line_key=yol.order_line_key"
			+" LEFT JOIN yfs_shipment_container ysc ON ycd.shipment_container_key=ysc.shipment_container_key"
			+" WHERE yoh.order_header_key=yol.order_header_key AND ys.shipment_key=ysl.shipment_key"
			+" AND yol.order_line_key=ysl.order_line_key AND yol.order_line_key=yors.order_line_key"
			+" AND yors.status_quantity>0 AND yoh.document_type='0001'"
			+" AND yoh.order_header_key>'20170901'"
			+" AND ys.shipment_no=";
	
	
	public static Connection getDBConnection() throws Exception {

		//String db_driver_class="oracle.jdbc.xa.client.OracleXADataSource";
		String db_driver_class="";
		String db_connection="";
		String db_user="";
		String db_password="";
		
		
		String uatEnv=getBundle().getString("soma.uat.environment");
		if(uatEnv.equalsIgnoreCase("uat1")) {
			db_driver_class="oracle.jdbc.driver.OracleDriver";
			db_connection="jdbc:Oracle:thin:@comdbter2-scan:1531/OMCOTST2";
			//String db_connection="jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=on)(ADDRESS=(PROTOCOL=TCP)(HOST=comdbter1-scan)(PORT=1531))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=OMCOTST1)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC)(RETRIES=180)(DELAY=5))))";
			db_user= "ster_93_uat";
			db_password= "uat0210";
		}else if(uatEnv.equalsIgnoreCase("uat2")) {
			db_driver_class="oracle.jdbc.driver.OracleDriver";
			db_connection="jdbc:Oracle:thin:@comdbter1-scan.jcpenney.com:1531/OMCOTST1";
			//String db_connection="jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=on)(ADDRESS=(PROTOCOL=TCP)(HOST=comdbter1-scan)(PORT=1531))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=OMCOTST1)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC)(RETRIES=180)(DELAY=5))))";
			db_user= "STER_93_UAT_OH";
			db_password= "uatoh0615";
		}else if(uatEnv.equalsIgnoreCase("prod")) {
			db_driver_class="oracle.jdbc.driver.OracleDriver";
			db_connection="jdbc:Oracle:thin:@lomdbprr1-scan.jcpenney.com:1531/OMLPRD";
			//String db_connection="jdbc:oracle:thin:@(DESCRIPTION=(LOAD_BALANCE=on)(ADDRESS=(PROTOCOL=TCP)(HOST=comdbter1-scan)(PORT=1531))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=OMCOTST1)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC)(RETRIES=180)(DELAY=5))))";
			db_user= "ster_readonly_user";
			db_password= "ster_user";
		}
		
		Connection dbConnection = null;
		System.out.println("Driver class:"+db_driver_class);
		System.out.println("Connection:"+db_connection);
		System.out.println("user:"+db_user);
		System.out.println("password:"+db_password);
		
		try {
			Class.forName(db_driver_class);
			dbConnection=DriverManager.getConnection( db_connection, db_user, db_password);
			return dbConnection;
		} catch (ClassNotFoundException e) {
			Reporter.log(e.getMessage());
			Reporter.log("Connection not happened as driver class not found", MessageTypes.TestStepFail);
		} catch (SQLException e) {
			System.out.println("returning exception:"+e);
			Reporter.log(e.getMessage());
			Reporter.log("Connection not happened due to invalid credentials", MessageTypes.TestStepFail);
		}

		return dbConnection;
	}
	
	public static Session getcassandraConnection() throws Exception{
		
		 Cluster cluster, cluster1;
		 Session session = null , session1,session2;
		PreparedStatement stmt;
		BoundStatement bound;
		String uatEnv=getBundle().getString("soma.uat.environment");
		System.out.println("Environment on which test cases are executing::::" + uatEnv);
		try {

			if (uatEnv.equalsIgnoreCase("uat")) {
				List<InetSocketAddress> contactpoint = Cluster.builder().getContactPoints();
				for(InetSocketAddress node:contactpoint){
					
					System.out.println(node);
				
					//cluster = Cluster.builder().addContactPointsWithPorts(node).withPort(9042)
					//		.withCredentials("dtdbread", "readonly123").withSSL().build();
					
				}
/*				session = cluster.connect("jcp_order_manage");
				session1 = cluster.connect("jcp_sourcing");
				session2 = cluster.connect("jcp_promise");*/
			} else if (uatEnv.equalsIgnoreCase("uat1")) {
				cluster = Cluster.builder().addContactPoint("shareddb.integration4.dp-dev.jcpcloud2.net").withPort(9042)
						.withCredentials("dtdbread", "readonly123").withSSL().build();
				session = cluster.connect("jcp_omni_orders");

			} else if (uatEnv.equalsIgnoreCase("uat2")) {
				cluster = Cluster.builder().addContactPoint("shareddb.integration4.dp-dev.jcpcloud2.net").withPort(9042)
						.withCredentials("dtdbread", "readonly123").withSSL().build();
				session = cluster.connect("jcp_omni_orders");

			} else if (uatEnv.equalsIgnoreCase("prod")) {
				cluster = Cluster.builder().addContactPoint("postorderdb.prod.dp-prod.jcpcloud2.net").withPort(9042)
						.withCredentials("sjavvaj1", "Jsk@1985").withSSL().build();
				session = cluster.connect("jcp_omni_orders");

			} else {
				cluster = Cluster.builder().addContactPoint("shareddb.integration4.dp-dev.jcpcloud2.net").withPort(9042)
						.withCredentials("dtdbread", "readonly123").withSSL().build();
				session = cluster.connect("jcp_inventory");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return session;

	}
	public static Connection getDB2Connection() throws Exception {

		Connection dbConnection = null;
		
		try {
			
			Class.forName("com.ibm.db2.jcc.DB2Driver");
						
			dbConnection=DriverManager.getConnection("jdbc:db2://10.111.19.223:446/DALG", "tda249", "asura03");
			return dbConnection;
		} catch (ClassNotFoundException e) {
			Reporter.log(e.getMessage());
			Reporter.log("Connection not happened as driver class not found", MessageTypes.TestStepFail);
		} catch (SQLException e) {
			System.out.println("returning exception:"+e);
			Reporter.log(e.getMessage());
			Reporter.log("Connection not happened due to invalid credentials", MessageTypes.TestStepFail);
		}

		return dbConnection;
	}
	
//	public static Connection getCassandraDBConnection() throws Exception
//	{
//		private static Cluster cluster, cluster1;
//		private static Session session, session1;
//		private static PreparedStatement stmt;
//		static ArrayList<String> skuAttributes=null;
//		BoundStatement bound;
//		ResultSet resultSet;
//		static String environment = getBundle().getString("env.baseurl");
//		static int temp = environment.indexOf("/v");
//		static String subenvironment = environment.substring(0, temp);
//		{
//			System.out.println("Environment on which test cases are executing::::" + environment);
//			try {
//				if (subenvironment.equals("https://services.integration.dp-dev.jcpcloud2.net")) {
//					cluster = Cluster.builder().addContactPoint("shareddb.integration.dp-dev.jcpcloud2.net").withPort(9042)
//							.withCredentials("dtdbread", "readonly123").withSSL().build();
//					session = cluster.connect("jcp_inventory");
//					session1 = cluster.connect("jcp_sourcing");
//				} else if (subenvironment.equals("https://services.integration2.dp-dev.jcpcloud2.net")) {
//					cluster = Cluster.builder().addContactPoint("shareddb.integration2.dp-dev.jcpcloud2.net").withPort(9042)
//							.withCredentials("dtdbread", "readonly123").withSSL().build();
//					session = cluster.connect("jcp_inventory");
//					session1 = cluster.connect("jcp_sourcing");
//
//				} else if (subenvironment.equals("https://services.integration3a.dp-dev.jcpcloud2.net")) {
//					cluster = Cluster.builder().addContactPoint("shareddb.integration3.dp-dev.jcpcloud2.net").withPort(9042)
//							.withCredentials("dtdbread", "readonly123").withSSL().build();
//					session = cluster.connect("jcp_inventory");
//					session1 = cluster.connect("jcp_sourcing");
//				} else 
//				{
//					cluster = Cluster.builder().addContactPoint("shareddb.integration4.dp-dev.jcpcloud2.net").withPort(9042)
//							.withCredentials("dtdbread", "readonly123").withSSL().build();
//					session = cluster.connect("jcp_omni_orders");
//		        }
//			}
//			catch (Exception e) 
//			{
//				e.printStackTrace();
//			}
//		
//		}
//	}
	

	
	
	public String fetchAccountId(String query) {
		Connection dbConnection = null;
		Statement statement = null;
		try{
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			System.out.println(query);
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()) {
				if(rs.getString(1)!=null && rs.getString(1).trim()!="" && rs.getString(1).trim()!=" " ) {
					System.out.println("Value:"+rs.getString(1));
					return rs.getString(1);
				}
			}
			
			/*if(rs.next()) {
				String shipment=rs.getString(1);
				System.out.println(SQL_COUNT_SHIPMENT+"'"+shipment+"'");
				Statement statement1 = dbConnection.createStatement();
				ResultSet rs1=statement1.executeQuery(SQL_COUNT_SHIPMENT+"'"+shipment+"'");
				if(rs1.next()) {
					getBundle().setProperty("orderlist.line.primeno",rs1.getString(3));
					return rs1.getString(1);
				}
			}else {
				throw new JCPConfigurationFailure("No Data Avaliable in SOMA Database with the given search criteria");
			}
			while(rs.next()) {
				result.put(rs.getString(2),rs.getString(3));
			}
			
			for(String key: result.keySet()) {
				
				System.out.println(SQL_COUNT_SHIPMENT+"'"+key+"'");
				Statement statement1 = dbConnection.createStatement();
				ResultSet rs1=statement1.executeQuery(SQL_COUNT_SHIPMENT+"'"+key+"'");
				try {
					while(rs1.next()) {
						if(rs1.getInt(1)==1)
							return result.get(key);
					}
				}catch(Exception e){
					System.out.println("Exception:"+e);
				}finally {
					statement1.close();
					rs1.close();
				}
			}*/
			
		}catch(Exception e){
			throw new JCPConfigurationFailure("Exception while fetching data from database: "+e.getMessage());
		}finally {
			try {
				if (statement != null)
					statement.close();
			
				if (dbConnection != null)
					dbConnection.close();
			
			} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
		}
		return null;
	}
	
	

	public int rowCount(String query) throws Exception{
		Connection dbConnection = null;
		Statement statement = null;
		int count=0;
		try{
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
			System.out.println(query);
			ResultSet rs = statement.executeQuery(query);
			while(rs.next()){
				count++;
			}
			
			System.out.println("Count is:"+count);
			
			
		}catch(SQLException e){
			System.out.println("There is some exception:"+e);
		}finally {

			if (statement != null) {
				statement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

		}
		return count;

	}
	
	public ResultSet getResult(String query) throws Exception{
		Connection dbConnection = null;
		Statement statement = null;
		ResultSet rs =null;
		try{
			dbConnection = getDBConnection();
			statement = dbConnection.createStatement();
		    rs = statement.executeQuery(query);
		}catch(SQLException e){
			System.out.println("There is some exception:"+e);
		}
		return rs;
	}
}
