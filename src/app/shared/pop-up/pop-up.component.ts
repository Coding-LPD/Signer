import { Component,
  trigger, transition, style, animate, state
} from '@angular/core';

@Component({
  selector: 'pop-up',
  template: `
    <div class="pop-up" *ngIf="showing" @bomb>{{content}}</div>    
  `,
  styles: [`
    .pop-up {
      position: fixed;
      z-index: 1000;
      top:0; bottom: 0; left: 0; right: 0;
      width: 300px;
      height: 50px;      
      margin: auto;      
      border-radius: 10px;
      text-align: center;
      line-height: 50px;
      color: white;
      background-color: rgba(0, 0, 0, 0.4);      
    }
  `],
  animations: [
    trigger('bomb', [
      transition('void => *', [
        style({ transform: 'scale(0)'}),
        animate(200)
      ]),
      transition('* => void', [
        animate(200, style({opacity: 0}))
      ])
    ])
  ]
})
export class PopUpComponent {  

  private _defaultContent = '提示'
  content = this._defaultContent;
  showing = false;

  show(content?: string) {
    this.content = content || this.content;
    this.showing = true;
    setTimeout(() => this.showing = false, this.getShowTime(this.content));
  }

  hide() {
    this.showing = false;
  }

  isShow() {
    return this.showing;
  }

  toggle(content?: string) {
    this.content = content || this.content;
    this.showing = !this.showing;
  }

  private getShowTime(content: string) {
    if (content.length <= 6) {
      return 1500;
    } else if (content.length <= 15) {
      return 2500;    
    } else {
      return 3500;
    }
  }

}