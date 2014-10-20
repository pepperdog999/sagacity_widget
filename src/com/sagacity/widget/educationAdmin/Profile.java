package com.sagacity.widget.educationAdmin;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_profile",pkName="ProfileID")
public class Profile extends Model<Profile>{
	private static final long serialVersionUID = 1L;
	public final static Profile dao = new Profile();
		
}
