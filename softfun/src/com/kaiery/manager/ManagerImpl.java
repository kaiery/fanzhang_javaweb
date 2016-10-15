package com.kaiery.manager;
import com.kaiery.bean.UserBean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ManagerImpl implements ManagerInter {

	
	private JdbcTemplate ds;
	private static UUID uid = null;
	
	public void setDs(DataSource ds) {
		this.ds = new JdbcTemplate(ds);
	}

	
	/**生成唯一id*/
	public static String getUUID() {
		uid = UUID.randomUUID(); 
		return uid.toString().replace("-", "");
	}
	
	
	@Override
	public List<UserBean> queryTest() {
		List<UserBean> list = new ArrayList<UserBean>();
		StringBuffer sql = new StringBuffer();
		sql.setLength(0);
		sql.append("select username,showname from m_user  ");
		List<Map<String, Object>> sqlList = ds.queryForList(sql.toString());
		for (int i = 0; i < sqlList.size(); i++) {
			UserBean ub = new UserBean();
			ub.initBean(sqlList.get(i));
			list.add(ub);
		}
		return list;
	}

}
