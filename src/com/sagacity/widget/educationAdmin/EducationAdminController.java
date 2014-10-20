package com.sagacity.widget.educationAdmin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sagacity.widget.comm.BaseController;
import com.sagacity.widget.comm.CommOpration;
import com.sagacity.widget.educationAdmin.CorpContact;
import com.sagacity.widget.educationAdmin.Profile;
import com.sagacity.widget.utility.PropertiesFactoryHelper;
import com.sagacity.widget.utility.StringTool;

@ControllerBind(controllerKey = "/educationAdmin")
public class EducationAdminController extends BaseController implements CommOpration{
	
	@Override
	public void index() {
		String userID = getPara("userID");
		String corpID = getPara("corpID");
		JSONObject map = new JSONObject();
		
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"GetUserInfo";
		try{
			urlString+="?userID="+userID+"&corpID="+corpID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        map = JSONObject.fromObject(reader.readLine());
	        reader.close();
	        httpConn.disconnect();
	        if(map.getInt("QueryState")==1){
	        	setAttr("cc",map.get("UserInfo"));
	        }
		}catch(Exception e){
			e.printStackTrace();
		}
		render("../educationAdmin/index.html");
	}
	
	public void enrollment() {
		setAttr("interface",PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url"));
		setAttr("contactInfo",CorpContact.dao.findById(getPara("CContactID")));
		setAttr("profile",Profile.dao.findFirst("select * from zq_profile where CContactID=?",getPara("CContactID")));
		render("../educationAdmin/enrollment.html");
	}
	
	@Before(Tx.class)
	public void updateProfile() {
		boolean result = false;
		Map<String,Object> data = new HashMap<String,Object>();
		CorpContact c = getModel(CorpContact.class, "info");
		Profile p = getModel(Profile.class,"profile");
		result=c.update();
		if(result){
			//增加或更新profile信息
			if(p.get("ProfileID")==null){
				//新增
				result=p.set("CContactID", c.get("CContactID")).save();
			}else{
				//更新
				result=p.update();
			}
		}
		data.put("Result", result);
		if(result){
			data.put("Msg", "个人信息更新成功！");
			data.put("ProfileID", p.get("ProfileID"));
		}
		else{
			data.put("Msg", "个人信息更新失败！");
		}
		if(StringTool.notNull(getPara("Callbackparam"))){
			renderJson(getPara("Callbackparam")+"("+JSONObject.fromObject(data)+")");
		}else{
			renderJson(data);
		}
	}
	
}
