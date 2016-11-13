import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'latest',
  templateUrl: './latest.component.html',
  styleUrls: ['./latest.component.css']
})
export class LatestComponent implements OnInit {

  // 图例
  signInLabels = ['已签', '未签'];
  batteryLabels = ['0~10%', '10~30%', '30~50%', '50~70%', '70~90%', '90~100%'];
  top10BatteryLabels = ['ID1', 'ID2', 'ID3', 'ID4', 'ID5', 'ID6', 'ID7', 'ID8', 'ID9', 'ID10'];
  last10BatteryLabels = ['ID1', 'ID2', 'ID3', 'ID4', 'ID5', 'ID6', 'ID7', 'ID8', 'ID9', 'ID10'];

  // 图的数据
  beforeSignInData = [40, 10];
  afterSignInData = [20, 30];
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

  ngOnInit() {
    setTimeout(() => this.showChart = true, 1);
    console.log(this.batteryBoardOptions);
  }

}