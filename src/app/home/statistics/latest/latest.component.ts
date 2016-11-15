import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';

import { Course } from '../../../shared';
import { CourseService } from '../../course';
import { StatisticsService } from '../statistics.service';

@Component({
  selector: 'latest',
  templateUrl: './latest.component.html',
  styleUrls: ['./latest.component.css']
})
export class LatestComponent implements OnInit {

  // 没有数据时的提示语
  tip = '请选择课程';

  // 图例
  signInLabels = ['未签', '已签'];
  batteryLabels = ['-100~0%', '0~30%', '30~50%', '50~70%', '70~90%', '90~100%'];
  top10BatteryLabels = ['ID1', 'ID2', 'ID3', 'ID4', 'ID5', 'ID6', 'ID7', 'ID8', 'ID9', 'ID10'];
  last10BatteryLabels = ['ID1', 'ID2', 'ID3', 'ID4', 'ID5', 'ID6', 'ID7', 'ID8', 'ID9', 'ID10'];

  // 图的数据
  beforeSignInData = [10, 40];
  afterSignInData = [30, 20];
  batteryData = [
    { label: '人数', data: [5, 5, 10, 15, 12, 3] }
  ];
  top10BatteryData = [
    { label: '电量消耗', data: [100, 90, 85, 70, 70, 66, 50, 48, 42, 39] }
  ];
  last10BatteryData = [ 
    { label: '电量消耗', data: [-60, -55, -52, -49, -45, -35, -20, -10, 0, 10] }
  ];

  // 图的配置
  commonOptions = {
    responsive: true
  };
  batteryOptions = Object.assign({}, this.commonOptions, {
    scales: {
      xAxes: [{
        gridLines: {
          display: false,
        }
      }]
    }
  });
  batteryBoardOptions = Object.assign({}, this.commonOptions, {
    scales: {
      xAxes: [{
        gridLines: {
          display: false,
        }
      }],
      yAxes: [{
        ticks: {
          max: 100,
          min: -100
        }
      }]
    }
  });

  // 先隐藏后显示，使一些图表能适应父元素大小
  showChart = false;

  constructor(
    private _courseService: CourseService,
    private _statisticsService: StatisticsService
  ) {}

  ngOnInit() {
    // 先隐藏后显示，使一些图表能适应父元素大小
    setTimeout(() => this.showChart = true, 1);

    this._statisticsService.selectedCourse$
      .distinctUntilChanged()
      .flatMap((course: Course) => {
        if (!course) {
          return Observable.of<any>({
            code: 600,
            msg: '请选择课程'
          });
        }
        return this._courseService.getLatestStatistics(course._id);
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.tip = '';
          this.extractData(body.data)
        } else {
          this.tip = body.msg;
        }
      });
  }

  extractData(data: any) {
    // 签到比例
    this.beforeSignInData = [data.studentCount - data.beforeSignIn, data.beforeSignIn];
    this.afterSignInData = [data.studentCount - data.afterSignIn, data.afterSignIn];
    // 电量消耗
    this.batteryData[0].data = data.batteryCost;
    this.batteryData = JSON.parse(JSON.stringify(this.batteryData));
    // 电量消耗前十
    this.top10BatteryLabels = [];
    this.top10BatteryData[0].data = [];
    data.top10BatteryCost.forEach((val: any) => {
      this.top10BatteryLabels.push(val.studentId);
      this.top10BatteryData[0].data.push(val.batteryCost);
    });
    // 电量消耗后十
    this.last10BatteryLabels = [];
    this.last10BatteryData[0].data = [];
    data.last10BatteryCost.forEach((val: any) => {
      this.last10BatteryLabels.push(val.studentId);
      this.last10BatteryData[0].data.push(val.batteryCost);
    });
  }

}