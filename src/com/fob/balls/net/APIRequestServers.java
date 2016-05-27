package com.fob.balls.net;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fob.balls.dialog.App;
import com.fob.tools.MD5EncodeUtil;
import com.fob.util.L;

public class APIRequestServers {
	private static final String TAG = "APIRequestServers";

	private static final String URL = "http://s-82631.gotocdn.com:8081/DYL001D/mobile/";
	public static final String URL2 = "http://s-82631.gotocdn.com:8081/DYL001D/image/";

	// http://www.yueqiuku.com:8081/

	private static Map<String, String> setHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		String nonce = (new Random().nextInt(899999) + 100000) + "";// 888888
		String expires = System.currentTimeMillis() + "";// 1393086533785
		String token = App.preferences.getStringMessage("user", "Token", "");
		String accessKey = App.preferences.getStringMessage("user", "Username",
				"");
		String signature = MD5EncodeUtil.sign(token, expires, nonce);
		L.i(TAG, "setHeaders nonce=" + nonce + ",expires=" + expires
				+ ",accessKey=" + accessKey + ",signature=" + signature
				+ ",User-Agent=" + android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		headers.put("Nonce", nonce);
		headers.put("Signature", signature);// zVQYSreD1vmi5uWJz+hdL7krwto=
		headers.put("Expires", expires);
		headers.put("YQKAccessKey", accessKey);
		headers.put("Content-Type", "application/json; charset=utf-8");
		return headers;
	}

	/**
	 * 1.3 获取码表
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getCodeList() throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		return HttpGetPost.getHttp(URL + "getcodelist", headers);
	}

	/**
	 * 1.4 用户注册
	 * 
	 * @param mobile
	 *            手机
	 * @param mac
	 *            MD5（手机网卡mac地址）
	 * @return
	 * @throws Exception
	 */
	public static String reguser(String mobile) throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		JSONObject json = new JSONObject();
		json.put("mobile", mobile);
		L.i(TAG, "reguser mobile=" + mobile);
		return HttpGetPost.post(URL + "reguser", json, headers);
	}

	/**
	 * 1.5 忘记密码
	 * 
	 * @param mobile
	 *            手机
	 * @return
	 * @throws Exception
	 */
	public static String forgotpwd(String mobile) throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		JSONObject json = new JSONObject();
		json.put("mobile", mobile);
		L.i(TAG, "forgotpwd mobile=" + mobile);
		return HttpGetPost.post(URL + "forgotpwd", json, headers);
	}

	/**
	 * 1.6 认证短信验证码
	 * 
	 * @param mobile
	 *            手机
	 * @param mobileAuth
	 *            MD5（4位手机数字验证码，例子用“1234”）
	 * @return
	 * @throws Exception
	 */
	public static String authuser(String mobile, String mobileauth)
			throws Exception {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json; charset=utf-8");
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		JSONObject json = new JSONObject();
		json.put("mobile", mobile);
		json.put("mobileauth", MD5EncodeUtil.MD5Encode(mobileauth));
		L.i(TAG, "authuser mobile=" + mobile + ",mobileauth=" + mobileauth);
		return HttpGetPost.post(URL + "authuser", json, headers);
	}

	/**
	 * 1.7 设置昵称
	 * 
	 * @param nick
	 *            昵称
	 * @return
	 * @throws Exception
	 */
	public static String setNick(String nick) throws Exception {
		JSONObject json = new JSONObject();
		json.put("nick", nick);
		L.i(TAG, "setNick nick=" + nick);
		return HttpGetPost.post(URL + "setnick", json, setHeaders());
	}

	/**
	 * 1.8 设置城市
	 * 
	 * @param city
	 *            城市
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	public static String setCity(String city, double x, double y)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("city", city);
		json.put("x", x);
		json.put("y", y);
		L.i(TAG, "setCity city=" + city + ",x=" + x + ",y=" + y);
		return HttpGetPost.post(URL + "setcity", json, setHeaders());
	}

	/**
	 * 1.9 设置运动项目
	 * 
	 * @param favorsport
	 *            运动项目
	 * @return
	 * @throws Exception
	 */
	public static String setFavorSport(String favorsport) throws Exception {
		JSONObject json = new JSONObject();
		json.put("favorsport", favorsport);
		L.i(TAG, "setFavorSport favorsport=" + favorsport);
		return HttpGetPost.post(URL + "setfavorsport", json, setHeaders());
	}

	/**
	 * 1.10 设置空闲场地刷新
	 * 
	 * @param usedisvalid
	 * @param freshtime1
	 * @param freshtime2
	 * @return
	 * @throws Exception
	 */
	public static String setUsedRresh(String usedisvalid, int freshtime1,
			int freshtime2) throws Exception {
		JSONObject json = new JSONObject();
		json.put("usedisvalid", usedisvalid);
		json.put("freshtime1", freshtime1);
		json.put("freshtime2", freshtime2);
		L.i(TAG, "setpasswd usedisvalid=" + usedisvalid + ",freshtime1="
				+ freshtime1 + ",freshtime2=" + freshtime2);
		return HttpGetPost.post(URL + "setusedfresh", json, setHeaders());
	}

	/**
	 * 1.11 设置个人信息
	 * 
	 * @param nickname
	 * @param favorsportlevel
	 * @param sex
	 * @param role
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static String setSelfInfo(String nickname, String favorsportlevel,
			String sex, String role, String email) throws Exception {
		JSONObject json = new JSONObject();
		json.put("nickname", nickname);
		json.put("favorsportlevel", favorsportlevel);
		json.put("sex", sex);
		json.put("role", role);
		json.put("email", email);
		L.i(TAG, "setpasswd nickname=" + nickname + ",favorsportlevel="
				+ favorsportlevel + ",sex=" + sex + ",role=" + role + ",email="
				+ email);
		return HttpGetPost.post(URL + "setselfinfo", json, setHeaders());
	}

	/**
	 * 1.12 获取个人信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String getselfinfo() throws Exception {
		return HttpGetPost.getHttp(URL + "getselfinfo", setHeaders());
	}

	/**
	 * 1.10 设置密码
	 * 
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public static String uploadmyhead(String passwd) throws Exception {
		JSONObject json = new JSONObject();
		json.put("ReqEntityFilePart", MD5EncodeUtil.MD5Encode(passwd));
		L.i(TAG, "setpasswd passwd=" + passwd);

		Map<String, String> headers = new HashMap<String, String>();
		String nonce = (new Random().nextInt(899999) + 100000) + "";// 888888
		String expires = System.currentTimeMillis() + "";// 1393086533785
		String token = App.preferences.getStringMessage("user", "Token", "");
		String accessKey = App.preferences.getStringMessage("user", "Username",
				"");
		String signature = MD5EncodeUtil.sign(token, expires, nonce);
		L.i(TAG, "setHeaders nonce=" + nonce + ",expires=" + expires
				+ ",accessKey=" + accessKey + ",signature=" + signature
				+ ",User-Agent=" + android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		headers.put("User-Agent", android.os.Build.MODEL + ";"
				+ android.os.Build.VERSION.RELEASE);
		headers.put("Nonce", nonce);
		headers.put("Signature", signature);// zVQYSreD1vmi5uWJz+hdL7krwto=
		headers.put("Expires", expires);
		headers.put("YQKAccessKey", accessKey);
		headers.put("Content-Type", "multipart/form-data");

		return HttpGetPost.post(URL2 + "uploadmyhead", json, headers);
	}

	/**
	 * 1.10 设置密码
	 * 
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public static String setPasswd(String passwd) throws Exception {
		JSONObject json = new JSONObject();
		json.put("passwd", MD5EncodeUtil.MD5Encode(passwd));
		L.i(TAG, "setpasswd passwd=" + passwd);
		return HttpGetPost.post(URL + "setpasswd", json, setHeaders());
	}

	/**
	 * 1.11 登录
	 * 
	 * @param passwd
	 * @return
	 * @throws Exception
	 */
	public static String login(String mobile, String passwd) throws Exception {
		JSONObject json = new JSONObject();
		json.put("mobile", mobile);
		json.put("passwd", MD5EncodeUtil.MD5Encode(passwd));
		L.i(TAG, "login passwd=" + passwd);
		return HttpGetPost.post(URL + "login", json, setHeaders());
	}

	/**
	 * 1.12 订单心跳 心跳接口，用于更新小红点状态及播放提示音。
	 * 
	 * @param sport
	 * @return
	 * @throws Exception
	 */
	public static String orderHeartBeat(String sport, long lasttime)
			throws Exception {
		L.i(TAG, "orderHeartBeat sport=" + sport);
		return HttpGetPost.getHttp(URL + "orderheartbeat?sport=" + sport
				+ "&lasttime=" + lasttime, setHeaders());
	}

	/**
	 * 1.13 公告心跳 心跳接口，用于更新小红点状态及播放提示音
	 * 
	 * @param sport
	 * @return 304没有更新，200有更新
	 * @throws Exception
	 */
	public static int ansHeartBeat(String sport) throws Exception {
		L.i(TAG, "ansHeartBeat sport=" + sport);
		return HttpGetPost.getHttpStatus(URL + "ansheartbeat?sport=" + sport,
				setHeaders());
	}

	/**
	 * 1.14 常用场地心跳 常用场地刷新
	 * 
	 * @param sport
	 * @return 304没有更新，200有更新
	 * @throws Exception
	 */
	public static int cusedHeartBeat(String sport) throws Exception {
		L.i(TAG, "cusedHeartBeat sport=" + sport);
		return HttpGetPost.getHttpStatus(URL + "cusedheartbeat?sport=" + sport,
				setHeaders());
	}

	// /**
	// *
	// * @param favorsportlevelList
	// * 初级、中级、高级
	// * @param sex
	// * 男、女
	// * @param searchGeo
	// * “附近的人”开关
	// * @param x
	// * 经度
	// * @param y
	// * 纬度
	// * @return
	// * @throws Exception
	// */
	// public static String searchParnter(String favorsportlevelList, String
	// sex,
	// String searchGeo, String x, String y) throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("favorsportlevelList", favorsportlevelList);
	// json.put("sex", sex);
	// json.put("searchGeo", searchGeo);
	// json.put("x", x);
	// json.put("y", y);
	// L.i(TAG, "searchParnter favorsportlevelList=" + favorsportlevelList
	// + ",searchGeo=" + searchGeo + ",x=" + x + ",y=" + y);
	// return HttpGetPost.post(URL + "searchparnter", json, setHeaders());
	// }
	//
	// /**
	// *
	// * @param exact
	// * 手机号或昵称
	// * @return
	// * @throws Exception
	// */
	// public static String searchParnterExact(String exact) throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("exact", exact);
	// L.i(TAG, "searchParnterExact exact=" + exact);
	// return HttpGetPost.post(URL + "searchparnterexact", json, setHeaders());
	// }
	//
	// /**
	// *
	// * @param receiver
	// * @param sendernick
	// * @param validateText
	// * 验证文字
	// * @return
	// * @throws Exception
	// */
	// public static String makeFriend(String receiver, String sendernick,
	// String validateText) throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("receiver", receiver);
	// json.put("sendernick", sendernick);
	// json.put("validateText", validateText);
	// L.i(TAG, "makeFriend receiver=" + receiver + ",sendernick="
	// + sendernick + ",validateText=" + validateText);
	// return HttpGetPost.post(URL + "makefriend", json, setHeaders());
	// }
	//
	// /**
	// *
	// * @param since_id
	// * @param max_id
	// * @param pageno
	// * 页码（0为第一页）
	// * @param pagesize
	// * 页记录条数（随意定制）
	// * @return
	// * @throws Exception
	// */
	// public static String recvNotice(String since_id, String max_id,
	// String pageno, String pagesize) throws Exception {
	// L.i(TAG, "recvNotice since_id=" + since_id + ",max_id=" + max_id
	// + ",pageno=" + pageno + ",pagesize=" + pagesize);
	// return HttpGetPost.getHttp(URL + "recvnotice?since_id=" + since_id
	// + "&max_id=" + max_id + "&pageno=" + pageno + "&pagesize="
	// + pagesize, setHeaders());
	// }
	//
	// /**
	// *
	// * @param success
	// * 是否通过
	// * @param receiver
	// * 通过的好友ID
	// * @param validateText
	// * 验证文字
	// * @param sendernick
	// * 发送方昵称
	// * @param sport
	// * 运动分类
	// * @return
	// * @throws Exception
	// */
	// public static String decideMakeFriend(String success, String receiver,
	// String validateText, String sendernick, String sport)
	// throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("success", success);
	// json.put("receiver", receiver);
	// json.put("validateText", validateText);
	// json.put("sendernick", sendernick);
	// json.put("sport", sport);
	// L.i(TAG, "decideMakeFriend success=" + success + ",receiver="
	// + receiver + ",validateText=" + validateText + ",sendernick="
	// + sendernick + ",sport=" + sport);
	// return HttpGetPost.post(URL + "decidemakefriend", json, setHeaders());
	// }

	/**
	 * 1.15 获得全部场地
	 * 
	 * @param city
	 *            城市名
	 * @param sport
	 * @param pageno
	 *            页码（0为第一页）
	 * @param pagesize
	 *            页记录条数（随意定制）
	 * @return
	 * @throws Exception
	 */
	public static String getAllCourt(String city, String sport, String pageno,
			String pagesize) throws Exception {
		L.i(TAG, "getAllCourt city=" + city + ",sport=" + sport + ",pageno="
				+ pageno + ",pagesize=" + pagesize);
		return HttpGetPost.getHttp(URL + "getallcourt?city=" + city + "&sport="
				+ sport + "&pageno=" + pageno + "&pagesize=" + pagesize,
				setHeaders());
	}

	/**
	 * 1.19 搜索场地
	 * 
	 * @param keyword
	 * @param city
	 * @param sport
	 * @return
	 * @throws Exception
	 */
	public static String searchallcourt(String keyword, String city,
			String sport) throws Exception {
		return HttpGetPost.getHttp(URL + "searchallcourt?keyword=" + keyword
				+ "&city=" + city + "&sport=" + sport, setHeaders());
	}

	/**
	 * 1.16 查看场地详情
	 * 
	 * @param courtid
	 * @return
	 * @throws Exception
	 */
	public static String getCourtDetail(String courtid) throws Exception {
		L.i(TAG, "getCourtdDetail courtid=" + courtid);
		return HttpGetPost.getHttp(URL + "getcourtdetail?courtid=" + courtid,
				setHeaders());
	}

	/**
	 * 1.17 加为常用场地
	 * 
	 * @param courtid
	 *            场馆编号
	 * @param sport
	 *            运动分类
	 * @return
	 * @throws Exception
	 */
	public static String addMyCourt(String courtid, String sport)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("courtid", courtid);
		json.put("sport", sport);
		L.i(TAG, "addMyCourt courtid=" + courtid + ",sport=" + sport);
		return HttpGetPost.post(URL + "addmycourt", json, setHeaders());
	}

	/**
	 * 1.17 取消常用场地
	 * 
	 * @param courtid
	 *            场馆编号
	 * @param sport
	 *            运动分类
	 * @return
	 * @throws Exception
	 */
	public static String removeMyCourt(String courtid, String sport)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("courtid", courtid);
		json.put("sport", sport);
		L.i(TAG, "addMyCourt courtid=" + courtid + ",sport=" + sport);
		return HttpGetPost.post(URL + "removemycourt", json, setHeaders());
	}

	/**
	 * 1.18 获取常用场地
	 * 
	 * @param sport
	 * @return
	 * @throws Exception
	 */
	public static String getMyCourt(String sport) throws Exception {
		L.i(TAG, "getAllCourt sport=" + sport);
		return HttpGetPost.getHttp(URL + "getmycourt?sport=" + sport,
				setHeaders());
	}

	/**
	 * 1.19 获取某日某场地空闲状态
	 * 
	 * @param courtid
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getAvList(String courtid, String date)
			throws Exception {
		L.i(TAG, "getAvList courtid=" + courtid + ",date=" + date);
		return HttpGetPost.getHttp(URL + "getavlist?courtid=" + courtid
				+ "&date=" + date, setHeaders());
	}

	/**
	 * 1.20 提交预订
	 * 
	 * @param courtnumreqs
	 *            请求list{startgap：订场时间,mtype：类型,courtnum：数量信息}
	 * @param sportcusername
	 *            运动项目ID
	 * @param detail
	 * @param captionnickname
	 *            预订人昵称
	 * @param captionmobile
	 *            预订人手机号
	 * @param orderdate
	 *            预订日期
	 * @param sport
	 * @return
	 * @throws Exception
	 */
	public static String book(JSONArray courtnumreqs, String sportcusername,
			String detail, String orderdate) throws Exception {
		JSONObject json = new JSONObject();
		json.put("courtnumreqs", courtnumreqs);
		json.put("sportcusername", sportcusername);
		json.put("detail", detail);
		json.put("orderdate", orderdate);
		L.i(TAG, "book courtnumreqs=" + courtnumreqs + ",sportcusername="
				+ sportcusername + ",detail=" + detail + ",orderdate="
				+ orderdate);
		return HttpGetPost.post(URL + "book", json, setHeaders());
	}

	/**
	 * 1.21 获取待确认订单列表
	 * 
	 * @param sport
	 * @param pageno
	 *            页码（0为第一页）
	 * @param pagesize
	 *            页记录条数（随意定制）
	 * @return
	 * @throws Exception
	 */
	public static String getUnconfirmed(String sport, String pageno,
			String pagesize) throws Exception {
		L.i(TAG, "getAllCourt sport=" + sport + ",pageno=" + pageno
				+ ",pagesize=" + pagesize);
		return HttpGetPost.getHttp(URL + "getunconfirmed?sport=" + sport
				+ "&pageno=" + pageno + "&pagesize=" + pagesize, setHeaders());
	}

	/**
	 * 1.22 获取已确认订单列表
	 * 
	 * @param sport
	 * @param pageno
	 *            页码（0为第一页）
	 * @param pagesize
	 *            页记录条数（随意定制）
	 * @return
	 * @throws Exception
	 */
	public static String getConfirmed(String sport, String pageno,
			String pagesize) throws Exception {
		L.i(TAG, "getAllCourt sport=" + sport + ",pageno=" + pageno
				+ ",pagesize=" + pagesize);
		return HttpGetPost.getHttp(URL + "getconfirmed?sport=" + sport
				+ "&pageno=" + pageno + "&pagesize=" + pagesize, setHeaders());
	}

	// /**
	// * 1.21 获取订单列表（待确认/已确认） 说明：返回某时间段内待确认或已确认的订单列表
	// *
	// * @param sport
	// * @param status
	// * 待确认/已确认
	// * @param lasttime
	// * @param pageno
	// * 页码（0为第一页）
	// * @param pagesize
	// * 页记录条数（随意定制）
	// * @return
	// * @throws Exception
	// */
	// public static String getOrderList(String sport, String status,
	// String lasttime, String pageno, String pagesize) throws Exception {
	// L.i(TAG, "getOrderList sport=" + sport + ",status=" + status
	// + ",lasttime=" + lasttime + ",pageno=" + pageno + ",pagesize="
	// + pagesize);
	// return HttpGetPost.getHttp(URL + "getorderlist?sport=" + sport
	// + "&status=" + status + "&lasttime=" + lasttime + "&pageno="
	// + pageno + "&pagesize=" + pagesize, setHeaders());
	// }

	/**
	 * 1.23 取消预订（取消未确认订单）
	 * 
	 * @param courtorderid
	 *            订单号
	 * @return
	 * @throws Exception
	 */
	public static String bookCancel(String courtorderid) throws Exception {
		JSONObject json = new JSONObject();
		json.put("courtorderid", courtorderid);
		L.i(TAG, "bookCancel courtorderid=" + courtorderid);
		return HttpGetPost.post(URL + "bookcancel", json, setHeaders());
	}

	/**
	 * 1.24 申请退订（退已成功确认订单）
	 * 
	 * @param courtorderid
	 *            订单号
	 * @return
	 * @throws Exception
	 */
	public static String confirmCancel(String courtorderid) throws Exception {
		JSONObject json = new JSONObject();
		json.put("courtorderid", courtorderid);
		L.i(TAG, "confirmCancel courtorderid=" + courtorderid);
		return HttpGetPost.post(URL + "confirmcancel", json, setHeaders());
	}

	/**
	 * 1.26 发送预订聊天消息
	 * 
	 * @param groupid
	 *            聊天室ID
	 * @param msgmime
	 *            消息类型，目前为text
	 * @param msg
	 *            文本消息内容
	 * @return
	 * @throws Exception
	 */
	public static String sendbookmsg(String groupid, String msgmime, String msg)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("groupid", groupid);
		json.put("msgmime", msgmime);
		json.put("msg", msg);
		L.i(TAG, "sendbookmsg groupid=" + groupid + ",msgmime=" + msgmime
				+ ",msg=" + msg);
		return HttpGetPost.post(URL + "sendbookmsg", json, setHeaders());
	}

	/**
	 * 1.27 接收预订聊天消息
	 * 
	 * @param groupid
	 *            聊天室ID
	 * @param lasttime
	 * @param pageno
	 *            页码（0为第一页）
	 * @param pagesize
	 *            页记录条数（随意定制）
	 * @return
	 * @throws Exception
	 */
	public static String recvbookmsg(String groupid, String lasttime,
			String pageno, String pagesize) throws Exception {
		L.i(TAG, "recvbookmsg groupid=" + groupid + ",lasttime=" + lasttime
				+ ",pageno=" + pageno + ",pagesize=" + pagesize);
		return HttpGetPost.getHttp(URL + "recvbookmsg?groupid=" + groupid
				+ "&lasttime=" + lasttime + "&pageno=" + pageno + "&pagesize="
				+ pagesize, setHeaders());
	}

	/**
	 * 1.30 接收公告
	 * 
	 * @param pagesize
	 * @return
	 * @throws Exception
	 */
	public static String recvannounce(String pagesize, long lasttime)
			throws Exception {
		L.i(TAG, "recvannounce pagesize=" + pagesize);
		return HttpGetPost.getHttp(URL + "recvannounce?lasttime=" + lasttime
				+ "&pagesize=" + pagesize, setHeaders());
	}

	/**
	 * 1.31 关于
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String about() throws Exception {
		return HttpGetPost.getHttp(URL + "about", setHeaders());
	}

	/**
	 * 1.33 获取最新版本信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String updateversion() throws Exception {
		return HttpGetPost.getHttp(URL + "updateversion", setHeaders());
	}

	// /**
	// * 1.25 发送消息
	// *
	// * @param username
	// * @param chatroomid
	// * @param text
	// * @param token
	// * @return
	// * @throws Exception
	// */
	// public static String mobileSendText(String username, String chatroomid,
	// String text, String token) throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("username", username);
	// json.put("chatroomid", chatroomid);
	// json.put("text", text);
	// json.put("token", token);
	// L.i(TAG, "mobileSendText username=" + username + ",chatroomid="
	// + chatroomid + ",text=" + text + ",token=" + token);
	// return HttpGetPost.post(URL + "mobilesendtext", json, setHeaders());
	// }
	//
	// /**
	// * 1.26 接收消息
	// *
	// * @param username
	// * @param chatroomid
	// * @param msgno
	// * @param token
	// * @return
	// * @throws Exception
	// */
	// public static String mobileRecvText(String username, String chatroomid,
	// String msgno, String token) throws Exception {
	// JSONObject json = new JSONObject();
	// json.put("username", username);
	// json.put("chatroomid", chatroomid);
	// json.put("msgno", msgno);
	// json.put("token", token);
	// L.i(TAG, "mobileRecvText username=" + username + ",chatroomid="
	// + chatroomid + ",msgno=" + msgno + ",token=" + token);
	// return HttpGetPost.post(URL + "mobilerecvtext", json, setHeaders());
	// }
}
