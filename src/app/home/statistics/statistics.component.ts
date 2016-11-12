import { Component } from '@angular/core';

@Component({
  selector: 'statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent {

  isOpenSelect = false;
  selectedCourse = '';  
  courses: string[] = ['软件工程软件工程软件工程软件工程', '安卓程序设计', 'iOS程序设计'];

  toggleSelect() {
    this.isOpenSelect = !this.isOpenSelect;
  }

  selectCourse(course: string) {
    this.isOpenSelect = false;
    this.selectedCourse = course;
  }

}