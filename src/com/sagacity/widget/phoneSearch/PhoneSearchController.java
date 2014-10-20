package com.sagacity.widget.phoneSearch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import net.sf.json.JSONObject;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sagacity.widget.comm.BaseController;
import com.sagacity.widget.comm.CommOpration;
import com.sagacity.widget.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/phoneSearch")
public class PhoneSearchController extends BaseController implements CommOpration{
	
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
		render("../phoneSearch/index.html");
	}
	
	public void search() {
		String userID = getPara("userID");
		String corpID = getPara("corpID");
		String keyword = getPara("keyword");
		int pageIndex = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",5);
		String sqlSelect = "select c.CorpName,d.DeptName,cc.Name,cc.PosName,cc.MobilePhone1 \n";
		String sqlFrom = "from zq_corpcontact cc \n"
					+"left join zq_corpinfo c on c.CorpID=cc.CorpID \n"
					+"left join zq_deptinfo d on d.DeptID=cc.DeptID \n"
					+"where (cc.Name like '%"+keyword+"%' or cc.MobilePhone1 like '%"+keyword+"%') and cc.CorpID=?";
		Page<Record> results = Db.paginate(
				pageIndex, pageSize,
				sqlSelect, sqlFrom, corpID);
		renderJson(results);
	}
	
}
