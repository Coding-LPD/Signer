import { Component } from '@angular/core';

import { SignRecord } from '../../../shared';

@Component({
  selector: 'detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent {

  radiosInactive = [false, true];
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

  selectRadio(index: number) {
    this.radiosInactive.forEach((value: boolean, index: number) => {
      this.radiosInactive[index] = true;
    })
    this.radiosInactive[index] = false;
  }

}