package com.c5p1ng.client.config;

public interface BaseConfig {
	/**客户端配置*/  
	int     CLIENT_VERSION = 1;         //版本号  
	  
	/**服务端配置*/  
	String  SERVER_HOST = "127.0.0.1";  //服务器IP  
	int     SERVER_PORT = 9090;         //服务器端口  
	  
	/**消息相关*/  
	int     SERVER_ID   = 0;            //表示服务器消息  
	byte    APP_IM = 1;                 //即时通信应用ID为1  
	String  EMPTY_MSG = "";             //空消息  
	  
	//普通消息类型  
	byte MSG_EMPTY      = 0;    //空消息  
	byte MSG_REGESITER  = 60;   //注册消息  
	byte MSG_LOGIN      = 61;   //登陆消息  
	byte MSG_FILE       = 70;   //文件消息  
	byte MSG_SIMPLETEXT = 80;   //文本消息  
	  
	//控制消息类型  
	byte CMSG_CONTACT   = 90;   //获取联系人列表 
	
	String CLIENT_HOST = "";   //客户端ip
}
