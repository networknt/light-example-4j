import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private route: ActivatedRoute,
              private router: Router,
              private httpClient: HttpClient) { }

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      console.log(params);
      const code = params['code'];
      this.httpClient.get(`https://localhost:8443/authorization?code=${code}`, {withCredentials: true, }).subscribe(() => {
          this.router.navigate(['/home']);
      });
    });
  }
}
