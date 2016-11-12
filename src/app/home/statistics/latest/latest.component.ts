import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'latest',
  templateUrl: './latest.component.html',
  styleUrls: ['./latest.component.css']
})
export class LatestComponent implements OnInit {

  // 图例
  signInChartLabels = ['已签', '未签'];
  batteryChartLabels = ['0~10%', '10~30%', '30~50%', '50~70%', '70~90%', '90~100%'];

  // 图的数据
  beforeSignInChartData = [40, 10];
  afterSignInChartData = [20, 30];
  batteryChartData = [
    { label: '人数', data: [5, 5, 10, 15, 12, 3] }
  ];

  // 图的配置
  commonChartOptions = {
    responsive: true
  };
  batteryChartOptions = Object.assign({}, this.commonChartOptions, {
    scales: {
      xAxes: [{
        gridLines: {
          display: false,
        }
      }]
    }
  });

  ngOnInit() {
    
  }

}