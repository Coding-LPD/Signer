import { Component } from '@angular/core';

import { JsEncryptService } from './shared';

@Component({
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [JsEncryptService]
})
export class AppComponent { }
