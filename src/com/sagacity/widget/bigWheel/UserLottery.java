package com.sagacity.widget.bigWheel;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="sys_userlottery",pkName="KeyID")
public class UserLottery extends Model<UserLottery>{
	private static final long serialVersionUID = 1L;
	public final static UserLottery dao = new UserLottery();

}
