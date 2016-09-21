import { Component, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  
  hideMenu = true;
  showSidebar = true;
  @Output() onToggleSidebar = new EventEmitter<boolean>();

  toggleMenu() {
    this.hideMenu = !this.hideMenu;    
  }

  toggleSidebar() {
    this.showSidebar = !this.showSidebar;
    this.onToggleSidebar.emit(this.showSidebar);
  }

}