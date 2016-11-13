import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'all',
  templateUrl: './all.component.html',
  styleUrls: ['./all.component.css']
})
export class AllComponent implements OnInit {

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

  ngOnInit() {
    setTimeout(() => this.showChart = true, 1);
  }

}