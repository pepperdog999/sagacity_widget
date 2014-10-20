package com.sagacity.widget.bigWheel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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
import com.sagacity.widget.utility.DateUtils;
import com.sagacity.widget.utility.PropertiesFactoryHelper;
import com.sagacity.widget.utility.StringTool;

@ControllerBind(controllerKey = "/bigWheel")
public class BigWheelController extends BaseController implements CommOpration{
	
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
		setAttr("ac",PropertiesFactoryHelper.getInstance().getConfig("game.lottery"));
		render("../bigWheel/index.html");
	}
	
	@Before(Tx.class)
	public void lottery() {
		JSONObject map = new JSONObject();
		Map<String,Object> data = new HashMap<String,Object>();		
		int result = 0;
		int ac = Integer.parseInt(PropertiesFactoryHelper.getInstance().getConfig("game.lottery").toString());
		String userID = getPara("userID");
		List <Record> list = Db.find("select KeyID from sys_userlottery where UserID=? and DATE_FORMAT(AddTime,'%Y-%m-%d')=?",userID,DateUtils.nowDate());
		if(list.size() >= ac){
			data.put("Result", result);
		}else{
			result = (int)(Math.random()*2)+1;		
			int prizeType = result==2 ? 0 : (int)(Math.random()*9)+1;
			new UserLottery().set("UserID", userID).set("AddTime", DateUtils.nowDateTime()).set("PrizeType", prizeType).save();
			data.put("Result", result);
			data.put("PrizeType", prizeType);
			int integral = 0;
			switch(prizeType){
			case 0:
				integral=1;
				break;
			case 1:
				integral=50;
				break;
			case 2:
				integral=2;
				break;
			case 3:
				integral=5;
				break;
			case 4:
				integral=10;
				break;
			case 5:
				integral=2;
				break;
			case 6:
				integral=5;
				break;
			case 7:
				integral=20;
				break;
			case 8:
				integral=2;
				break;
			case 9:
				integral=5;
				break;
			default :
				integral=0;
				break;
			}
			//UserInfo.dao.setUserIntegral(userID, integral);
			String urlString =PropertiesFactoryHelper.getInstance()
					.getConfig("interface.url")+"ChangeUserIntegral";
			try{
				urlString+="?userID="+userID+"&integral="+integral;
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
			}catch(Exception e){
				e.printStackTrace();
			}
				
		}
		renderJson(data);
		
	}

}
