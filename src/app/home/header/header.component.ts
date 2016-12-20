import { Component, Output, EventEmitter, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { LoginService } from '../../login';
import { Teacher } from '../../shared';

@Component({
  selector: 'header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  
  teacher: Teacher;
  hideMenu = true;
  showSidebar = true;
  @Output() onToggleSidebar = new EventEmitter<boolean>();

  constructor(
    private _router: Router,
    private _loginService: LoginService) {}


  ngOnInit() {
    this._loginService.getTeacherInfo()
      .subscribe(body => {
        if (+body.code == 200) {
          this.teacher = body.data[0];
        }
      });
  }

  toggleMenu() {
    this.hideMenu = !this.hideMenu;    
  }

  toggleSidebar() {
    this.showSidebar = !this.showSidebar;
    this.onToggleSidebar.emit(this.showSidebar);
  }

  logout() {
    this._router.navigate(['/home/login']);
  }

}