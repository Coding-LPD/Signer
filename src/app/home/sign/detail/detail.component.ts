import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { SignService } from '../sign.service';
import { LoginService } from '../../../login';
import { 
  Sign, SignRecord, 
  PositionService, SignRecordService, 
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
  signStates = [ 
    { text: '未开始', color: '#797979' }, 
    { text: '进行中', color: '#97CC00' },
    { text: '已结束', color: '#FF6C60' } 
  ];  

  constructor(
    private _route: ActivatedRoute,
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
            console.log(this.sign);            
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
            if (body.ip.trim()) {
              this._positionService.locate(body.ip, teacherId, signId)
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
    this.refreshRecords(this.sign._id, index);
  }

  ToggleQRCodeSize() {
    this.isLargeQRCode = !this.isLargeQRCode;
  }

  agreeSign(record: SignRecord) {
    if (!confirm('确定同意签到吗？')) {
      return;
    }
    this._signRecordService.agree(record._id)
      .subscribe(body => {
        if (+body.code == 200) {
          record.state = 1;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  refuseSign(record: SignRecord) {
    if (!confirm('确定拒绝签到吗？')) {
      return;
    }
    this._signRecordService.refuse(record._id)
      .subscribe(body => {
        if (+body.code == 200) {
          record.state = 2;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

  private refreshRecords(signId: string, type: number) {
    this._signRecordService.search({ signId, type })
      .subscribe(body => {
        if (+body.code == 200) {
          this.records = body.data;
        } else {
          this.popup.show(body.msg);
        }
      });
  }

}