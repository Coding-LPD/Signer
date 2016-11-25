import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { SharedModule } from '../../shared';
import { ChatComponent } from './chat.component';

@NgModule({
  imports: [ 
    SharedModule,
    FormsModule
  ],
  declarations: [
    ChatComponent
  ],
  exports: [
    ChatComponent
  ]
})
export class ChatModule {}