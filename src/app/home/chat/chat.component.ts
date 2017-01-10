import { Component, ViewChild, ElementRef, OnInit, OnDestroy, AfterViewChecked } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription'

import { Teacher, Course, ChatRoom, ChatMsg, SocketService, LoadingComponent, PopUpComponent } from '../../shared';
import { LoginService } from '../../login';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit, OnDestroy, AfterViewChecked {

  @ViewChild('msgList') msgListElem: ElementRef;
  @ViewChild(LoadingComponent) loading: LoadingComponent; 
  @ViewChild(PopUpComponent) popup: PopUpComponent;

  // 通用
  rooms: ChatRoom[];
  teacher: Teacher;
  isOpenSelect = false;
  tip = '请在右上角选择聊天室';
  isLoading = true;
  completeText = '已加载完所有信息';

  // 当前聊天室
  msgs: ChatMsg[] = [];
  selectedRoom: ChatRoom;  
  content = '';                   // 要发送的内容   
  page = 0;                       // 第几页消息  
  isToBottom = false;             // 是否滚动到内容底部
  isKeepPos = false;              // 是否保持当前位置
  lastScrollHeight = -1;          // 加载新信息前，记录当前可滚动高度
  isLoadAll = false;              // 是否加载完所有信息  

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
          this.popup.show(body.msg);
        }
      });

    this.sub.add(this._socketService.roomList$.subscribe(body => this.onRoomList(body) ));
    this.sub.add(this._socketService.msgList$.subscribe(body => this.onMsgList(body) ));
    this.sub.add(this._socketService.newMsg$.subscribe(body => this.onNewMsg(body) ));
  }

  ngAfterViewChecked() {
    if (this.isToBottom) {
      this.scrollToBottom();
    } else if (this.isKeepPos) {
      this.msgListElem.nativeElement.scrollTop = this.msgListElem.nativeElement.scrollHeight - this.lastScrollHeight;
      this.isKeepPos = false;
    }
  }

  ngOnDestroy() {    
    this.sub.unsubscribe();
  }

  toggleSelect() {
    this.isOpenSelect = !this.isOpenSelect;
  }

  selectRoom(room: ChatRoom) {
    this.isOpenSelect = false;
    // 初始化聊天室
    this.msgs = [];
    this.selectedRoom = room;
    this.content = '';
    this.page = 0;
    this.isToBottom = false;
    this.isKeepPos = false;
    this.lastScrollHeight = -1;
    this.isLoadAll = false;    
    if (room) {
      this.tip = '';
      this._socketService.getMsgList(this.selectedRoom.courseId, this.page++, 9);
    } else {
      this.tip = '请在右上角选择聊天室';      
    }    
  }

  sendMsg() {
    if (!this.content.trim()) {
      this.popup.show('待发送的内容不能为空');
      return;
    }
    this._socketService.sendMsg(this.selectedRoom.courseId, this.teacher._id, this.content);
    this.msgs.push(new ChatMsg('', this.selectedRoom.courseId, this.teacher._id, '', this.content, 
                              this.teacher.avatar, this.teacher.name, new Date().toString()));
    this.isToBottom = true;
    this.content = '';    
  }

  contentKeyUp($event: any) {
    if ($event.ctrlKey && $event.keyCode == 13) {
      this.sendMsg();
    }
  }

  /**
   * room-list响应
   */
  onRoomList(body: any) {
    if (+body.code == 200) {
      this.rooms = body.data;
    } else {
      this.popup.show(body.msg);
    }
  }

  /**
   * msg-list响应
   */
  onMsgList(body: any) {
    if (+body.code == 200) {                        
      // 加载了所有信息，则显示加载完毕
      // 还有新信息，则停止在当前位置
      if (body.data.length <= 0) {        
        this.isLoadAll = true;
      } else {
        this.isKeepPos = true;
      }
      // 默认加载第一页时，滚动到底部
      if (this.page == 0) {
        this.isToBottom = true;
      }
      this.msgs = body.data.reverse().concat(this.msgs);      
    } else {
      this.popup.show(body.msg);
    }
  }

  /**
   * new-msg响应
   */
  onNewMsg(body: any) {
    if (+body.code == 200) {
      // 不是当前聊天室的信息，则不添加
      if (body.data.courseId !== this.selectedRoom.courseId) return;
      
      this.msgs.push(body.data);
      // 当前位置靠近底部，则有新信息时跳到底部
      // 否则有新信息时，则只是提示下
      if (this.msgListElem.nativeElement.scrollHeight - this.msgListElem.nativeElement.scrollTop <= 440) {
        this.isToBottom = true;
      } else {
        this.popup.show('有新消息');
      }
    } else {
      this.popup.show(body.msg);
    }
  }

  /**
   * 根据信息的发布者来决定信息的停靠位置（左边、右边）
   */
  msgClass(msg: ChatMsg) {
    return msg.teacherId == this.teacher._id ? 'right' : 'left';
  }

  /**
   * 滚动到信息列表底部
   */
  scrollToBottom() {
    if (this.msgListElem) {
      this.msgListElem.nativeElement.scrollTop = this.msgListElem.nativeElement.scrollHeight;      
    }
    this.isToBottom = false;
  }

  /**
   * 用户滚动信息列表
   */
  scrollMsg($event: any) {
    if ($event.target.scrollTop <= 10 && !this.isLoadAll) {
      this._socketService.getMsgList(this.selectedRoom.courseId, this.page++, 9);
      this.lastScrollHeight = this.msgListElem.nativeElement.scrollHeight;
    }
  }

}