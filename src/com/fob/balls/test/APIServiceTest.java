package com.fob.balls.test;

import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.alibaba.fastjson.JSONArray;
import com.fob.balls.net.APIRequestServers;
import com.fob.balls.net.bean.AuthuserBean;
import com.fob.balls.net.bean.CodeListBean;
import com.fob.balls.net.bean.CourtDetailBean;
import com.fob.balls.net.bean.CourtListBean;
import com.fob.balls.net.bean.CourtNumREQ;
import com.fob.balls.net.bean.GetOrderListRES;
import com.fob.balls.net.bean.OrderHeartBeatRES;
import com.fob.balls.net.bean.ResultBasicBean;
import com.fob.balls.net.bean.SportBean;
import com.fob.util.JsonParseTool;
import com.fob.util.L;

public class APIServiceTest extends AndroidTestCase {
	private static final String TAG = "APIServiceTest";

	/**
	 * 1.3 获取码表
	 * 
	 * @throws Exception
	 */
	public void getCodelist() throws Exception {
		String str = APIRequestServers.getCodeList();
		L.d(TAG, "getCodelist str=" + str);
		CodeListBean mc = (CodeListBean) JsonParseTool.dealComplexResult(str,
				CodeListBean.class);
		L.d(TAG, "getCodelist Curt=" + mc.getCurt() + ",ret=" + mc.getRet());
		// {"ansheartbeatrate":"10","bookperiod":"8","citycodes":[{"city":"北京"},{"city":"石家庄"}],"favorgymbeatrate":"30","favorgymnum":"3","managerchatbeatrate":"5","orderheartbeatrate":"3","ordermaxnum":"3","sportcodes":[{"item":"羽毛球"},{"item":"网球"},{"item":"乒乓球"}],"unconfirmedmaxnum":"1","ret":"1","curt":1401549735324,"err":""}
		List<SportBean> list = mc.getSportcodes();
		for (SportBean bean : list) {
			L.d(TAG, "getCodelist bean=" + bean.getItem());
		}
	}

	/**
	 * 1.4 用户注册
	 * 
	 * @throws Exception
	 */
	public void reguser() throws Exception {
		String str = APIRequestServers.reguser("18610041095");
		L.d(TAG, "reguser str=" + str);
		ResultBasicBean mc = (ResultBasicBean) JsonParseTool.dealSingleResult(
				str, ResultBasicBean.class);
		// {"token":"d41503ca5e42496cb11133ff0da70b95","username":"5850f7579802e569ae914e2a2f14a2de","ret":"1","err":""}
	}

	/**
	 * 1.5 忘记密码
	 * 
	 * @throws Exception
	 */
	public void forgotpwd() throws Exception {
		String str = APIRequestServers.forgotpwd("18610041095");
		L.d(TAG, "forgotpwd str=" + str);
		ResultBasicBean mc = (ResultBasicBean) JsonParseTool.dealSingleResult(
				str, ResultBasicBean.class);
		L.d(TAG, "forgotpwd Curt=" + mc.getCurt());
		// {"ret":"1","curt":1401553436406,"err":""}
	}

	/**
	 * 1.6 认证短信验证码
	 * 
	 * @throws Exception
	 */
	public void authuser() throws Exception {
		String str = APIRequestServers.authuser("18610041095", "301337");
		L.d(TAG, "authuser str=" + str);
		AuthuserBean mc = (AuthuserBean) JsonParseTool.dealSingleResult(str,
				AuthuserBean.class);
		L.d(TAG, "authuser Curt=" + mc.getCurt());
		// {"token":"b0dac8362dbdcbbb5b47448f5bde3900","username":"5850f7579802e569ae914e2a2f14a2de","ret":"1","curt":1402065854551,"err":""}
		// {"token":"4fa359f10b4f694643318635ea606f60","username":"9d63f90a431612076ef67df5700cf1bf","ret":"1","curt":1402132374479,"err":""}
	}

	/**
	 * 1.7 设置昵称
	 * 
	 * @throws Exception
	 */
	public void setNick() throws Exception {
		String str = APIRequestServers.setNick("老牛");
		L.d(TAG, "setNick str=" + str);
		ResultBasicBean mc = (ResultBasicBean) JsonParseTool.dealSingleResult(
				str, ResultBasicBean.class);
		// {"ret":"1","curt":1401665167940,"err":""}
	}

	/**
	 * 1.8 设置城市
	 * 
	 * @throws Exception
	 */
	public void setCity() throws Exception {
		APIRequestServers.setCity("北京", 0.0, 0.0);
		// {"ret":"1","curt":1401665650727,"err":""}
	}

	/**
	 * 1.9 设置运动项目
	 * 
	 * @throws Exception
	 */
	public void setFavorSport() throws Exception {
		APIRequestServers.setFavorSport("网球");
		// {"ret":"1","curt":1401665784770,"err":""}
	}

	/**
	 * 1.10 设置密码
	 * 
	 * @throws Exception
	 */
	public void setPasswd() throws Exception {
		APIRequestServers.setPasswd("12345678");
		// {"ret":"1","curt":1401665784770,"err":""}
	}

	/**
	 * 1.11 登录
	 * 
	 * @throws Exception
	 */
	public void login() throws Exception {
		APIRequestServers.login("18610041095", "12345678");
		// {"ret":"1","curt":1401665784770,"err":""}
	}

	/**
	 * 1.12 订单心跳 心跳接口，用于更新小红点状态及播放提示音。
	 * 
	 * @throws Exception
	 */
	public void orderHeartBeat() throws Exception {
		String str = APIRequestServers.orderHeartBeat("网球", 0);
		// {"ucs":"1","cs":"0","ret":"1","curt":1401666160805,"err":""}
		OrderHeartBeatRES ohb = (OrderHeartBeatRES) JsonParseTool
				.dealSingleResult(str, OrderHeartBeatRES.class);
	}

	/**
	 * 1.13 公告心跳 心跳接口，用于更新小红点状态及播放提示音
	 * 
	 * @throws Exception
	 */
	public void ansHeartBeat() throws Exception {
		APIRequestServers.ansHeartBeat("网球");
		// TODO
	}

	/**
	 * 1.14 常用场地心跳 常用场地刷新
	 * 
	 * @throws Exception
	 */
	public void cusedHeartBeat() throws Exception {
		APIRequestServers.cusedHeartBeat("网球");
		// TODO
	}

	// public void searchParnter() throws Exception {
	// APIRequestServers.searchParnter("[{'l':'初级'},{'l':'中级'}]", "男", "true",
	// "116.388176", "39.995376");
	// //
	// }
	//
	// public void searchParnterExact() throws Exception {
	// APIRequestServers.searchParnterExact("13911372350");
	// //
	// }
	//
	// public void makeFriend() throws Exception {
	// APIRequestServers.makeFriend("ec7e71ce4d36d1945c27ec26b09e99f3", "老牛",
	// "dear6");
	// //
	// }
	//
	// public void recvNotice() throws Exception {
	// APIRequestServers.recvNotice("1394985323788", "0", "0", "5");
	// //
	// {"pagecount":0,"pageno":0,"pagecontentlist":[{"abstime":1400378128617,"image":null,"msg":"老于已通过您的请求","msgmime":null,"msgtype":"申请结果","sendernick":"老于","sender":"7e791ef5b7fb3459a90973792ff9db2d"}],"ret":"1","curt":1401671731977,"err":""}
	// }
	//
	// public void decideMakeFriend() throws Exception {
	// APIRequestServers.decideMakeFriend("通过",
	// "7e791ef5b7fb3459a90973792ff9db2d", "ok", "老于", "网球");
	// //
	// }

	/**
	 * 1.21 获取待确认订单列表
	 * 
	 * @throws Exception
	 */
	public void getUnconfirmed() throws Exception {
		String str = APIRequestServers.getConfirmed("网球", "1", "5");
		// {"list":[{"detail":"888","executestatus":"未处理","lastupdate":1403365673002,"sportcusername":"0f7f337faa00ca3b87796af896aff074","captionmobile":"18610041095","captionnickname":"玄飞","captionusername":null,"chatroomid":"","courtnoreqs":[],"courtnumreqs":[{"endgap":23,"startgap":22,"courtnum":3,"mtype":"室内"}],"mnickname":"","orderdate":"20140621","courtname":"测试场馆2.网球","courtorderid":"656730024612592","status":"待确认"},{"detail":"888","executestatus":"未处理","lastupdate":1403365663615,"sportcusername":"0f7f337faa00ca3b87796af896aff074","captionmobile":"18610041095","captionnickname":"玄飞","captionusername":null,"chatroomid":"","courtnoreqs":[],"courtnumreqs":[{"endgap":15,"startgap":14,"courtnum":3,"mtype":"室内"}],"mnickname":"","orderdate":"20140621","courtname":"测试场馆2.网球","courtorderid":"656636150992592","status":"待确认"},{"detail":"888","executestatus":"未处理","lastupdate":1403364635530,"sportcusername":"0f7f337faa00ca3b87796af896aff074","captionmobile":"18610041095","captionnickname":"玄飞","captionusername":null,"chatroomid":"","courtnoreqs":[],"courtnumreqs":[{"endgap":8,"startgap":7,"courtnum":1,"mtype":"室内"}],"mnickname":"","orderdate":"20140621","courtname":"测试场馆2.网球","courtorderid":"646355306412592","status":"待确认"},{"detail":"888","executestatus":"已处理","lastupdate":1402804728915,"sportcusername":"4bbfc05597752dd244a24fa88d1858da","captionmobile":"18610041095","captionnickname":"玄飞","captionusername":null,"chatroomid":"","courtnoreqs":[],"courtnumreqs":[{"endgap":16,"startgap":14,"courtnum":1,"mtype":"室外"}],"mnickname":"","orderdate":"20140616","courtname":"测试场馆1.网球","courtorderid":"18632460882592","status":"未接受"},{"detail":"888","executestatus":"已处理","lastupdate":1402804722341,"sportcusername":"4bbfc05597752dd244a24fa88d1858da","captionmobile":"18610041095","captionnickname":"玄飞","captionusername":null,"chatroomid":"","courtnoreqs":[],"courtnumreqs":[{"endgap":16,"startgap":14,"courtnum":1,"mtype":"室外"}],"mnickname":"","orderdate":"20140616","courtname":"测试场馆1.网球","courtorderid":"17192961442592","status":"未接受"}],"pagecount":1,"pageno":0,"ret":"1","curt":1403366219890,"err":""}
		GetOrderListRES ols = (GetOrderListRES) JsonParseTool
				.dealComplexResult(str, GetOrderListRES.class);
	}

	/**
	 * 1.15 获得全部场地
	 * 
	 * @throws Exception
	 */
	public void getAllCourt() throws Exception {
		String str = APIRequestServers.getAllCourt("", "网球", "0", "5");
		L.d(TAG, "getAllCourt str=" + str);
		CourtListBean list = (CourtListBean) JsonParseTool.dealComplexResult(
				str, CourtListBean.class);
		// {"list":[{"courtid":"536e2e1a0cf23955d1d628d2","courtname":"国家网球场"},{"courtid":"5385e4a50cf299a0fd922af6","courtname":"联调测试场馆1"}],"pagecount":0,"pageno":0,"ret":"1","curt":1401672666276,"err":""}
	}

	/**
	 * 1.16 获取场地详情
	 * 
	 * @throws Exception
	 */
	public void getCourtDetail() throws Exception {
		String str = APIRequestServers
				.getCourtDetail("4bbfc05597752dd244a24fa88d1858da");
		L.d(TAG, "getAllCourt str=" + str);
		CourtDetailBean cdb = (CourtDetailBean) JsonParseTool
				.dealComplexResult(str, CourtDetailBean.class);
		// {"courtDetail":{"courtname":"测试场馆1网球馆","courtinfos":["室外: 2片","室内: 1片"],"lat":39.997398,"lon":116.364672,"opentime":"9:00-21:00","productprices":["标准价: 非黄金时间40~120元 黄金时间40~120元"],"tel":null,"courtid":null},"ret":"1","curt":1402760215781,"err":""}
	}

	/**
	 * 1.16 加为常用场地
	 * 
	 * @throws Exception
	 */
	public void addMyCourt() throws Exception {
		// {"courtid":"4bbfc05597752dd244a24fa88d1858da","sport":"网球"}
		APIRequestServers.addMyCourt("536e2e1a0cf23955d1d628d2", "网球");
		// {"ret":"1","curt":1401673014025,"err":""}
	}

	/**
	 * 1.17 获取常用场地
	 * 
	 * @throws Exception
	 */
	public void getMyCourt() throws Exception {
		String str = APIRequestServers.getMyCourt("网球");
		L.d(TAG, "getAllCourt str=" + str);
		CourtListBean list = (CourtListBean) JsonParseTool.dealComplexResult(
				str, CourtListBean.class);
		// {"list":[{"courtid":"536e2e1a0cf23955d1d628d2","courtname":"国家网球场."}],"pagecount":0,"pageno":0,"ret":"1","curt":1401675034671,"err":""}
	}

	/**
	 * 1.18 获取某日某场地空闲状态
	 * 
	 * @throws Exception
	 */
	public void getAvList() throws Exception {
		// 4bbfc05597752dd244a24fa88d1858da
		APIRequestServers.getAvList("4bbfc05597752dd244a24fa88d1858da",
				"20140620");
		// {"list":[{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":0},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":1},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":2},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":3},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":4},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":5},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":6},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":7},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":8},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":9},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":10},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":11},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":12},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":13},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":14},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":15},{"list":[{"n":1,"t":"室内"},{"n":1,"t":"室外"}],"g":16},{"list":[{"n":1,"t":"室内"},{"n":1,"t":"室外"}],"g":17},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":18},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":19},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":20},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":21},{"list":[{"n":2,"t":"室内"},{"n":1,"t":"室外"}],"g":22}],"ret":"1","curt":1401694800124,"err":""}
	}

	/**
	 * 1.19 提交预订
	 * 
	 * @throws Exception
	 */
	public void book() throws Exception {
		// JSONObject json = new JSONObject();
		// json.put("startgap", 16);
		// json.put("endgap", 18);
		// json.put("mtype", "室外");
		// json.put("courtnum", 1);
		CourtNumREQ cnr = new CourtNumREQ();
		cnr.setStartgap(16);
		cnr.setEndgap(18);
		cnr.setMtype("室外");
		cnr.setCourtnum(1);
		ArrayList<CourtNumREQ> list = new ArrayList<CourtNumREQ>();
		list.add(cnr);
		JSONArray array = new JSONArray();
		array.addAll(list);

		// b364e15631e85d3d76309cf6f189ef2b
		APIRequestServers.book(array, "4bbfc05597752dd244a24fa88d1858da",
				"888", "20140616");
	}

	// /**
	// * 1.20 获取订单列表（待确认/已确认） 说明：返回某时间段内待确认或已确认的订单列表
	// *
	// * @throws Exception
	// */
	// public void getOrderList() throws Exception {
	// APIRequestServers.getOrderList("网球", "待确认", "1394985313762", "0", "5");
	// //
	// {"list":[{"captionmobile":"13911111111","captionnickname":"player0","courtorderid":"957268604704676","orderdate":"20140510","lastupdate":1401695726860,"captionusername":null,"courtnumreqs":[{"courtnum":1,"endgap":18,"mtype":"室外","startgap":16}],"detail":"888","sportcusername":"49a510ae34ce5f3da547d3b8497785fc","chatroomid":"","courtnoreqs":[],"mnickname":"","courtname":""},{"captionmobile":"13911111111","captionnickname":"testnickname","courtorderid":"987091672714676","orderdate":"20140529","lastupdate":1401598709167,"captionusername":null,"courtnumreqs":[{"courtnum":1,"endgap":16,"mtype":"室外","startgap":14}],"detail":"888","sportcusername":"19dc2bc563a1f6ae13f204aff29d0f2a","chatroomid":"","courtnoreqs":[],"mnickname":"","courtname":""},{"captionmobile":"13911111111","captionnickname":"testnickname","courtorderid":"986981854334676","orderdate":"20140529","lastupdate":1401598698185,"captionusername":null,"courtnumreqs":[{"courtnum":1,"endgap":16,"mtype":"室外","startgap":14}],"detail":"888","sportcusername":"19dc2bc563a1f6ae13f204aff29d0f2a","chatroomid":"","courtnoreqs":[],"mnickname":"","courtname":""},{"captionmobile":"13911111111","captionnickname":"testnickname","courtorderid":"461224373384676","orderdate":"20140529","lastupdate":1401546122437,"captionusername":null,"courtnumreqs":[{"courtnum":1,"endgap":16,"mtype":"室外","startgap":14}],"detail":"888","sportcusername":"28e24a05b9ed968a34deb6716a5b95eb","chatroomid":"","courtnoreqs":[],"mnickname":"","courtname":""},{"captionmobile":"13911111111","captionnickname":"testnickname","courtorderid":"460757545014676","orderdate":"20140529","lastupdate":1401546075754,"captionusername":null,"courtnumreqs":[{"courtnum":1,"endgap":16,"mtype":"室外","startgap":14}],"detail":"888","sportcusername":"28e24a05b9ed968a34deb6716a5b95eb","chatroomid":"","courtnoreqs":[],"mnickname":"","courtname":""}],"pagecount":3,"pageno":0,"ret":"1","curt":1401696509177,"err":""}
	// }

	// /**
	// * 1.20 发送消息
	// *
	// * @throws Exception
	// */
	// public void mobileSendText() throws Exception {
	// APIRequestServers.mobileSendText("player0", "52766fbee9bef8c7f916fc66",
	// "88888888888", "5888");
	// // {"ret":"1","curt":1401697015880,"err":""}
	// }
	//
	// /**
	// * 1.21 接收消息
	// *
	// * @throws Exception
	// */
	// public void mobileRecvText() throws Exception {
	// APIRequestServers.mobileRecvText("player0", "52766fbee9bef8c7f916fc66",
	// "2", "5888");
	// //
	// {"ret":1,"simpleChatromsgList":[{"username":"testuser1","abstime":1401697064581,"msg":"测试消息1","msgno":null},{"username":"testuser2","abstime":1401697064581,"msg":"测试消息2","msgno":null}],"errorCode":0}
	// }
}
