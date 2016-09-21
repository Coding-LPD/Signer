import { Component } from '@angular/core';

@Component({
  selector: 'detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent {

  radiosInactive = [false, true];

  selectRadio(index: number) {
    this.radiosInactive.forEach((value: boolean, index: number) => {
      this.radiosInactive[index] = true;
    })
    this.radiosInactive[index] = false;
  }

}