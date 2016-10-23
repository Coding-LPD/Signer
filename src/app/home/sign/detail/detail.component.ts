import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { LoginService } from '../../../login';
import { SignRecord, PositionService, SignRecordService, PopUpComponent } from '../../../shared';

@Component({
  selector: 'detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

  @ViewChild(PopUpComponent) popup: PopUpComponent;

  radiosInactive = [false, true];
  isLargeQRCode = false;
  records: any[] = [
    { 
      studentAvatar: 'http://localhost:3000/images/user/1474517916567756.png',
      studentName: '周晓华',
      state: 0,
      distance: 100
    },
    { 
      studentAvatar: 'http://localhost:3000/images/user/1474517916567756.png',
      studentName: '周晓华',
      state: 0,
      distance: 50
    },
    { 
      studentAvatar: 'http://localhost:3000/images/user/1474517916567756.png',
      studentName: '周晓华',
      state: 0,
      distance: 60
    },
    { 
      studentAvatar: 'http://localhost:3000/images/user/1474517916567756.png',
      studentName: '周晓华',
      state: 1,
      distance: 120
    },
    { 
      studentAvatar: 'http://localhost:3000/images/user/1474517916567756.png',
      studentName: '周晓华',
      state: 2,
      distance: 1000
    }
  ];

  constructor(
    private _route: ActivatedRoute,
    private _loginService: LoginService,
    private _positionService: PositionService,
    private _signRecordService: SignRecordService) {}

  ngOnInit() {         
    this._route.params.forEach((params: Params) => {
      // 获取签到详情
      this._signRecordService.search({signId: params['id']})
        .subscribe(body => {
          if (+body.code == 200) {
            this.records = body.data;
          } else {
            this.popup.show(body.msg);
          }
        });

      // 定位
      this._loginService.getTeacherInfo().subscribe(body => {
        var teacherId = body.data[0]._id;
        this._positionService.getIP()
          .subscribe(body => {
            if (body.ip.trim()) {
              this._positionService.locate(body.ip, teacherId, params['id'])
                .subscribe(body => {
                  if (+body.code == 200) {
                    this.popup.show('定位成功');
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
  }

  selectRadio(index: number) {
    this.radiosInactive.forEach((value: boolean, index: number) => {
      this.radiosInactive[index] = true;
    })
    this.radiosInactive[index] = false;
  }

  ToggleQRCodeSize() {
    this.isLargeQRCode = !this.isLargeQRCode;
  }

}