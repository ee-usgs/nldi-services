package gov.usgs.owi.nldi.dao;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NavigationDao extends BaseDao {
	public static final String NAVIGATE = "navigate";
	private static final String NS = NAVIGATE + ".";
	
	@Autowired
	public NavigationDao(SqlSessionFactory sqlSessionFactory) {
		super(sqlSessionFactory);
	}
	
	public Map<String, String> navigate(Map<String, Object> parameterMap) {
		return getSqlSession().selectOne(NS + NAVIGATE, parameterMap);
	}

}
