import { Component, ViewChild, ElementRef, OnInit, OnDestroy, AfterViewChecked } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription'

import { Teacher, Course, ChatRoom, ChatMsg, SocketService } from '../../shared';
import { LoginService } from '../../login';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {

  @ViewChild('msgList') msgListElem: ElementRef;

  msgs: ChatMsg[];
  rooms: ChatRoom[];
  teacher: Teacher;
  selectedRoom: ChatRoom;
  isOpenSelect = false;
  tip = '请在右上角选择聊天室';  
  content = '';                   // 要发送的内容 
  isLoadMore = false;             // 是否正在加载更多

  sub = new Subscription();

  constructor(
    private _loginService: LoginService,
    private _socketService: SocketService
  ) {}

  ngOnInit() {
    this._loginService.getTeacherInfo()
      .subscribe(body => {
        if (+body.code == 200) {
          this.teacher = body.data[0];
          this._socketService.connect('sign').getRoomList(this.teacher._id);
        } else {
          alert(body.msg);
        }
      });

    this.sub.add(this._socketService.roomList$.subscribe(body => this.onRoomList(body) ));
    this.sub.add(this._socketService.msgList$.subscribe(body => this.onMsgList(body) ));
    this.sub.add(this._socketService.newMsg$.subscribe(body => this.onNewMsg(body) ));
  }

  ngAfterViewChecked() {
    if (this.isLoadMore) {
      this.isLoadMore = false;      
    } else {
      this.scrollToBottom();
    }
  }

  ngOnDestroy() {    
    this.sub.unsubscribe();
  }

  toggleSelect() {
    this.isOpenSelect = !this.isOpenSelect;
  }

  selectRoom(room: ChatRoom) {
    this.selectedRoom = room;
    this.isOpenSelect = false;
    if (room) {
      this.tip = '';
      this._socketService.getMsgList(this.selectedRoom.courseId, 0);
    } else {
      this.tip = '请在右上角选择聊天室';
      this.msgs = [];
    }
  }

  sendMsg() {
    this._socketService.sendMsg(this.selectedRoom.courseId, this.teacher._id, this.content);
    this.msgs.push(new ChatMsg('', this.selectedRoom.courseId, this.teacher._id, '', this.content, 
                              this.teacher.avatar, this.teacher.name, new Date().toString()));
    this.content = '';
  }

  /**
   * room-list响应
   */
  onRoomList(body: any) {
    if (+body.code == 200) {
      this.rooms = body.data;
    } else {
      alert(body.msg);
    }
  }

  /**
   * msg-list响应
   */
  onMsgList(body: any) {
    if (+body.code == 200) {
      this.msgs = body.data.reverse();
    } else {
      alert(body.msg);
    }
  }

  /**
   * new-msg响应
   */
  onNewMsg(body: any) {
    if (+body.code == 200) {
      this.msgs.push(body.data);
    } else {
      alert(body.msg);
    }
  }

  /**
   * 根据信息的发布者来决定信息的停靠位置（左边、右边）
   */
  msgClass(msg: ChatMsg) {
    return msg.teacherId == this.teacher._id ? 'right' : 'left';
  }

  scrollToBottom() {
    if (this.msgListElem) {
      this.msgListElem.nativeElement.scrollTop = this.msgListElem.nativeElement.scrollHeight;      
    }
  }

}