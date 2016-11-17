/**
 * 
 */
package com.renren.fenqi.auth.model.role;

import com.renren.fenqi.dbtool.annotation.Colum;
import com.renren.fenqi.dbtool.annotation.TableName;

/**
 * @author knight
 *
 */
@TableName(tableName="system_user_role")
public class SystemUserRole {

	@Colum(columName="id")
	private String id;
	
	@Colum(columName="user_id")
	private int userId;
	
	@Colum(columName="role_id")
	private int roleId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
}
