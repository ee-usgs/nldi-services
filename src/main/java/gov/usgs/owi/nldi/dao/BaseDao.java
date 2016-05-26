package gov.usgs.owi.nldi.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseDao extends SqlSessionDaoSupport {
	private static final Logger LOG = LoggerFactory.getLogger(BaseDao.class);

	public static final String DATA_SOURCES = "dataSources";
	public static final String FEATURE = "feature";
	public static final String FEATURES = "features";
	public static final String FLOW_LINES = "flowLines";

	public BaseDao(SqlSessionFactory sqlSessionFactory) {
		LOG.trace(getClass().getName());
		setSqlSessionFactory(sqlSessionFactory);
	}

}
