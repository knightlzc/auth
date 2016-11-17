package com.renren.fenqi.auth.model.realm;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

@TableName(tableName="system_realm")
public class Realm {
	
	@Colum(columName = "id")
	private int id;
	
	@Colum(columName = "name")
	private String name;
	
	@Colum(columName = "realm_id")
	private String realmId;
	
	@Colum(columName = "status")
	private int status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRealmId() {
		return realmId;
	}

	public void setRealmId(String realmId) {
		this.realmId = realmId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
