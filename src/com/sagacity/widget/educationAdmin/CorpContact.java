package com.sagacity.widget.educationAdmin;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_corpcontact",pkName="CContactID")
public class CorpContact extends Model<CorpContact>{
	private static final long serialVersionUID = 1L;
	public final static CorpContact dao = new CorpContact();
	
	//此方法用于针对企业通讯录的更新计数
	public int updateContactCount(String corpID) {
		String sql="update zq_corpInfo set UpdateContactCount=UpdateContactCount+1 where corpID=?";
		return Db.update(sql,corpID);
	}
	
	
}
