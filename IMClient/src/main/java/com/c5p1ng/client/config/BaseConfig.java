package com.c5p1ng.client.config;

public interface BaseConfig {
	/**�ͻ�������*/  
	int     CLIENT_VERSION = 1;         //�汾��  
	  
	/**���������*/  
	String  SERVER_HOST = "127.0.0.1";  //������IP  
	int     SERVER_PORT = 9090;         //�������˿�  
	  
	/**��Ϣ���*/  
	int     SERVER_ID   = 0;            //��ʾ��������Ϣ  
	byte    APP_IM = 1;                 //��ʱͨ��Ӧ��IDΪ1  
	String  EMPTY_MSG = "";             //����Ϣ  
	  
	//��ͨ��Ϣ����  
	byte MSG_EMPTY      = 0;    //����Ϣ  
	byte MSG_REGESITER  = 60;   //ע����Ϣ  
	byte MSG_LOGIN      = 61;   //��½��Ϣ  
	byte MSG_FILE       = 70;   //�ļ���Ϣ  
	byte MSG_SIMPLETEXT = 80;   //�ı���Ϣ  
	  
	//������Ϣ����  
	byte CMSG_CONTACT   = 90;   //��ȡ��ϵ���б� 
	
	String CLIENT_HOST = "";   //�ͻ���ip
}
