import { Component, OnInit } from '@angular/core';

import { SignRecord, IPService } from '../../../shared';

@Component({
  selector: 'detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {

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

  constructor(private _ipService: IPService) {}

  ngOnInit() {
    this._ipService.getIP()
      .subscribe(body => {
        if (body.ip.trim()) {
          alert(body.ip);
        } else {
          alert('获取本机ip失败');
          console.log(`body: ${JSON.stringify(body)}`);
        }
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