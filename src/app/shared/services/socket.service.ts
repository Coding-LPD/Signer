import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import * as io from 'socket.io-client';

import { API } from './api';

@Injectable()
export class SocketService {

  private socket: SocketIOClient.Socket;
  private events = {
    // sign
    teacherIn: 'teacher-in',
    sign: 'sign',

    // chatroom
    roomList: 'room-list',
    msgList: 'msg-list',
    newMsg: 'new-msg'
  };
  private signSource = new Subject<any>();
  private roomListSource = new Subject<any>();
  private msgListSource = new Subject<any>();
  private newMsgSource = new Subject<any>();

  sign$ = this.signSource.asObservable();
  roomList$ = this.roomListSource.asObservable();
  msgList$ = this.msgListSource.asObservable();
  newMsg$ = this.newMsgSource.asObservable();  

  /**
   * 开启websocket连接
   */
  connect(name: string) {
    if (!this.socket) {
      this.socket = io.connect(this.getDomain() + '/' + name);

      // 监听事件
      this.socket.on('connect',     () => this.onConnect());
      this.socket.on('disconnect',  () => this.onDisconnect());
      this.socket.on('error',       (err: any) => this.onError);
      this.socket.on(this.events.sign,      (data: any) => this.signSource.next(data));
      this.socket.on(this.events.roomList,  (data: any) => this.roomListSource.next(data));
      this.socket.on(this.events.msgList,   (data: any) => this.msgListSource.next(data));
      this.socket.on(this.events.newMsg,    (data: any) => this.newMsgSource.next(data));            
    }
    return this;
  }

  /**
   * 为教师提供对应签到的提醒
   */
  setTeacherIn(signId: string) {
    this.socket.emit(this.events.teacherIn, signId);
  }

  /**
   * 获取聊天室列表
   */
  getRoomList(teacherId: string) {
    // room-list所需参数：studentId，teacherId
    this.socket.emit(this.events.roomList, '', teacherId);
  }

  /**
   * 获取聊天信息列表
   */
  getMsgList(courseId: string, page: number, limit?: number) {
    // msg-list所需参数：courseId，page，limit
    this.socket.emit(this.events.msgList, courseId, page, limit);
  }

  /**
   * 发送新消息
   */
  sendMsg(courseId: string, teacherId: string, content: string) {
    // new-msg所需参数：courseId，studentId，content，teacherId
    this.socket.emit(this.events.newMsg, courseId, '', content, teacherId);
  }

  private onConnect() {
    console.log('connect');
  }

  private onDisconnect() {
    console.log('disconnect');
  }

  private onError(err: any) {
    console.log(err);
  }

  /**
   * 去除API.domain中的/api路径
   */
  private getDomain(): string {
    var index = API.domain.lastIndexOf('/');
    return API.domain.substring(0, index);    
  }

}