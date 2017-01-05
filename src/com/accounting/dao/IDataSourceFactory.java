package com.accounting.dao;

import javax.activation.DataSource;

public interface IDataSourceFactory {
	public DataSource getDataSource() throws Exception;
}
