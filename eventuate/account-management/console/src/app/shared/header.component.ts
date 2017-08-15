import {Component, OnInit, Input} from '@angular/core';

@Component({
  selector: 'my-header',
  templateUrl: 'app/shared/header.component.html',
  styleUrls: ['app/shared/header.component.css']

})

export class HeaderComponent implements OnInit {
  @Input() title: string;
  @Input() isShowCreateButton: boolean;

  constructor() {}

  ngOnInit() {

  }

}
