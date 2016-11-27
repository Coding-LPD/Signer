import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Subscription } from 'rxjs/Subscription'
import * as moment from 'moment';

import { Course } from '../../../shared';
import { CourseService } from '../../course';
import { StatisticsService } from '../statistics.service';

@Component({
  selector: 'all',
  templateUrl: './all.component.html',
  styleUrls: ['./all.component.css']
})
export class AllComponent implements OnInit, OnDestroy {

  // 没有数据时的提示语
  tip = '请在右上角选择课程';

  // 图例
  signInRatioLabels = ['10-03','10-10','10-17','10-24','10-31','11-7','11-14'];

  // 图的数据
  signInRatioData = [{
    label: '参与签到的比例', 
    data: [60, 80, 75, 40, 50, 10, 90],
    fill: false,
    lineTension: 0
  }];

  // 图的配置
  commonOptions = {
    responsive: true
  };  
  signInRatioOptions = Object.assign({}, this.commonOptions, {
    scales: {
      yAxes: [{
        ticks: {
          max: 100,
          min: 0
        }
    }]
    }
  });

  // 先隐藏后显示，使一些图表能适应父元素大小
  showChart = false;

  sub: Subscription;

  constructor(
    private _courseService: CourseService,
    private _statisticsService: StatisticsService
  ) {}

  ngOnInit() {
    // 先隐藏后显示，使一些图表能适应父元素大小
    setTimeout(() => this.showChart = true, 1);

    this.sub = this._statisticsService.selectedCourse$
      .flatMap((course: Course) => {
        if (!course) {
          return Observable.of<any>({
            code: 600,
            msg: '请在右上角选择课程'
          });
        }
        return this._courseService.getAllStatistics(course._id);
      })
      .subscribe(body => {
        if (+body.code == 200) {
          this.tip = '';
          this.extractData(body.data);
        } else {
          this.tip = body.msg;
        }
      })
  }

  ngOnDestroy() {
    this.sub.unsubscribe();
  }

  extractData(data: any) {
    // 签到比例
    this.signInRatioLabels = [];
    this.signInRatioData[0].data = [];    
    data.signIn.forEach((val: any) => {
      this.signInRatioLabels.push(moment(val.time).format('MM-DD'));
      this.signInRatioData[0].data.push(val.ratio);
    });    
  }

}