import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { SignService } from '../sign.service';
import { LoginService } from '../../../login';
import { 
  Sign, SignRecord, 
  PositionService, SignRecordService, SocketService, 
  PopUpComponent 
} from '../../../shared';

@Component({
  selector: 'detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  radiosInactive = [false, true];
  isLargeQRCode = false;
  sign: Sign;
  records: SignRecord[] = [];
  positionId: string;
  signIn = 0;
  signStates = [ 
    { text: '未开始', color: '#797979' }, 
    { text: '进行中', color: '#97CC00' },
    { text: '已结束', color: '#FF6C60' } 
  ];  
  // 指定地图中心显示的位置
  map = {
    lat: 0,
    lng: 0,
    zoom: 18
  }
  // 用户选择的位置，不一定显示在地图中心
  changedPosition: any;

  constructor(
    private _route: ActivatedRoute,
    private _socketService: SocketService,
    private _loginService: LoginService,
    private _signService: SignService,
    private _positionService: PositionService,
    private _signRecordService: SignRecordService) {}

  ngOnInit() {          
    this._route.params.forEach((params: Params) => {
      var signId = params['id'];

      // 获取签到详情             
      this._signService.search({ _id: signId })
        .subscribe(body => {          
          if (+body.code == 200) {            
            this.sign = body.data[0];  
          } else {
            this.popup.show(body.msg);
          }
        });

      // 获取签到记录
      var type = this.radiosInactive[0] ? 1 : 0;
      this.refreshRecords(signId, type);

      // 定位
      this._loginService.getTeacherInfo().subscribe(body => {
        var teacherId = body.data[0]._id;
        this._positionService.getIP()
          .subscribe(body => {
            if (+body.code == 200) {
              this._positionService.locate(body.data, teacherId, signId)
                .subscribe(body => {
                  if (+body.code == 200) {
                    this.positionId = body.data._id;                    
                    this.map.lat = body.data.latitude;
                    this.map.lng = body.data.longitude;
                  } else {
                    this.popup.show('定位失败');
                  }
                })
            } else {
              this.popup.show('获取本机ip失败');
              console.log(`body: ${JSON.stringify(body)}`);
            }
          });  
      });        
    });

    this._socketService.get('sign')
      .subscribe(data => {      
        var index = !this.radiosInactive[0] ? 0 : 1;
        if (data.type == index) {
          this.records.push(data);
        }         
      })
  }

  selectRadio(index: number) {
    this.radiosInactive.forEach((value: boolean, index: number) => {
      this.radiosInactive[index] = true;
    })
    this.radiosInactive[index] = false;
    this.signIn = index == 0 ? this.sign.beforeSignIn : this.sign.afterSignIn;
    this.refreshRecords(this.sign._id, index);    
  }

  ToggleQRCodeSize() {
    this.isLargeQRCode = !this.isLargeQRCode;
  }

  confirm(record: SignRecord, type: number = 0) {
    // 要求用户再次确认
    var tip = type == 0 ? '确定同意签到吗？' : '确定拒绝签到吗？';
    if (!confirm(tip)) {
      return;
    }
    var state = type == 0 ? 1 : 2;
    this._signRecordService.confirm(record._id, type)
      .subscribe(body => {
        if (+body.code == 200) {
          record.state = state;
          this.signIn = body.data.signIn;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  confirmAll(type: number = 0) {
    // 要求用户再次确认
    var tip = type == 0 ? '确定同意所有签到吗？' : '确定拒绝所有签到吗？';    
    if (!confirm(tip)) {
      return;
    }
    // 获取所有未批准的签到记录的id
    var indexs = this.getNotSignInRecordIndexs();
    var ids = indexs.map((value: number) => {
      return this.records[value]._id;
    });    
    var state = type == 0 ? 1 : 2;
    this._signRecordService.confirmAll(ids, type)
      .subscribe(body => {
        if (+body.code == 200) {
          // 修改所有未批准的签到记录的状态
          indexs.forEach((value: number) => {
            this.records[value].state = state;
          });
          // 修改完成签到人数
          this.signIn = body.data.signIn;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  getNotSignInRecordIndexs() {
    var results: number[] = [];   
    this.records.forEach((record: SignRecord, index: number) => {
      if (record.state == 0) {
        results.push(index);
      }
    });
    return results;
  }

  // 用户选择位置
  changePosition(point: any) {
    this.changedPosition = {
      latitude: point.lat,
      longitude: point.lng
    };
  }

  // 根据用户的选择，更新用户的位置
  updatePosition() {
    if (!this.changedPosition) {
      this.popup.show('请选择位置后再更新定位');
      return;
    }

    this._loginService.getTeacherInfo()
      .flatMap(body => {
        if (+body.code == 200) {
          return this._positionService.update(this.positionId, this.changedPosition);
        } else {
          return Observable.of(body);
        }
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.positionId = body.data._id;
          this.popup.show('定位更新成功');
        } else {
          this.popup.show(body.msg);
        }
      })
  }

  private refreshRecords(signId: string, type: number) {
    this._signRecordService.search({ signId, type })
      .subscribe(body => {
        this.signIn = 0;
        if (+body.code == 200) {
          this.records = body.data;
          this.signIn = this.records.length - this.getNotSignInRecordIndexs().length;          
        } else {
          this.popup.show(body.msg);
        }
      });
  }  

}