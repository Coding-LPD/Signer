import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject'

import { Course } from '../../shared';

@Injectable()
export class StatisticsService {

  private selectedCourseSource = new Subject<Course>();

  selectedCourse$ = this.selectedCourseSource.asObservable();

  selectCourse(course: Course) {
    this.selectedCourseSource.next(course);
  }

}