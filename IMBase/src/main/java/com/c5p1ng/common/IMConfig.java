package com.c5p1ng.common;

public interface IMConfig {
	/** �ͻ������� */
	int CLIENT_VERSION = 1; // �汾��
	/** ��������� */
	String SERVER_HOST = "127.0.0.1"; // ������IP
	int SERVER_PORT = 9090; // �������˿�
	/** ��Ϣ��� */
	int SERVER_ID = 0; // ��ʾ��������Ϣ
	byte APP_IM = 1; // ��ʱͨ��Ӧ��IDΪ1
	byte TYPE_CONNECT = 0; // ���Ӻ��һ����Ϣȷ�Ͻ������Ӻͷ�����֤��Ϣ
	byte TYPE_MSG_TEXT = 1; // �ı���Ϣ
	String MSG_EMPTY = ""; // ����Ϣ
}
