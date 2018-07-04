import { Component } from '@angular/core';
import {Router} from '@angular/router';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';

  constructor(private router: Router, private cookieService: CookieService) {}

  home() {
    this.router.navigate(['/home']);
  }

  logout() {
    this.cookieService.deleteAll('/', 'localhost');
    this.router.navigate(['/']);
  }
}
