import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { Sign } from '../../../../shared';
import { SignService } from '../../sign.service';

@Component({
  selector: 'complete',
  templateUrl: './complete.component.html',
  styleUrls: ['./complete.component.css']
})
export class CompleteComponent implements OnInit {

  sign: Sign;

  constructor(
    private _router: Router,
    private _signService: SignService) {}

  ngOnInit() {
    this.sign = this._signService.sign;
  }

  toCalendar() {
    this._router.navigate(['/home/calendar']);
  }

}