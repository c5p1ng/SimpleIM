package com.c5p1ng.common;

import org.msgpack.annotation.Message;

@Message
public class IMMessage {
	// Ӧ��ID
	private byte appId;
	// �汾��
	private int version;
	// �û�ID
	private int uid;
	// ��Ϣ���� 0:��½ 1��������Ϣ
	private byte msgType;
	// ���շ�
	private int receiveId;
	// ��Ϣ����
	private String msg;

	public IMMessage() {
	}

	/**
	 * ���췽��
	 * 
	 * @param appId
	 *            Ӧ��ͨ��
	 * @param version
	 *            Ӧ�ð汾
	 * @param uid
	 *            �û�ID
	 * @param msgType
	 *            ��Ϣ����
	 * @param receiveId
	 *            ��Ϣ������
	 * @param msg
	 *            ��Ϣ����
	 */
	public IMMessage(byte appId, int version, int uid, byte msgType, int receiveId, String msg) {
		this.appId = appId;
		this.version = version;
		this.uid = uid;
		this.msgType = msgType;
		this.receiveId = receiveId;
		this.msg = msg;
	}

	public byte getAppId() {
		return appId;
	}

	public void setAppId(byte appId) {
		this.appId = appId;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public byte getMsgType() {
		return msgType;
	}

	public void setMsgType(byte msgType) {
		this.msgType = msgType;
	}

	public int getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(int receiveId) {
		this.receiveId = receiveId;
	}

	@Override
	public String toString() {
		return "appId:" + this.appId + ",version:" + this.version + ",uid:" + this.uid + ",msgType:" + this.msgType
				+ ",receiveId:" + this.receiveId + ",msg:" + this.msg;
	}
}
