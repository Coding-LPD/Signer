import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'big-qrcode',
  templateUrl: './big-qrcode.component.html',
  styleUrls: ['./big-qrcode.component.css']
})
export class BigQRCodeComponent implements OnInit {

  code: string;

  constructor(private _route: ActivatedRoute) {}

  ngOnInit() {
    this._route.params
      .subscribe(params => this.code = params['code']);
  }

  goBack() {
    history.back();
  }

}