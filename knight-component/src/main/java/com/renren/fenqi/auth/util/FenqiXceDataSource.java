package com.renren.fenqi.auth.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.xiaonei.xce.XceAdapter;

/**
 * xce数据源服务
 * 
 * @author jie.li6@renren-inc.com
 * @created 2016年9月19日 上午11:20:32
 * @version 1.0
 * 
 */

public class FenqiXceDataSource extends AbstractDataSource {

    // 数据源名称
    private String bizName;

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getCurrentDataSource().getConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getCurrentDataSource().getConnection(username, password);
    }

    private boolean isCurrentTransactionReadOnly() {
        return TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    }

    private DataSource getReadDataSource() throws SQLException {
        return XceAdapter.getInstance().getReadDataSource(bizName);
    }

    /**
     * get write data source
     *
     * @return
     * @throws SQLException
     */
    private DataSource getWriteDataSource() throws SQLException {
        return XceAdapter.getInstance().getWriteDataSource(bizName);
    }

    /**
     * get current data source
     *
     * @return
     * @throws SQLException
     */
    private DataSource getCurrentDataSource() throws SQLException {
        return isCurrentTransactionReadOnly() ? getReadDataSource() : getWriteDataSource();
    }

}
