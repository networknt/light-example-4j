import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TodoService} from 'shared/todo.service';

@Component({
  selector: 'list',
  templateUrl: 'app/list/list.component.html',
  styleUrls: ['app/list/list.component.css']
})
export class ListComponent implements OnInit {
  contacts: {};
  private isAdd:number = 1;

  constructor(
    private _router: Router,
    private _todoService: TodoService
  ) {}

  ngOnInit() {
    //this.getTodos();
  }



  addContact() {
    this._router.navigate(['edit']);
  }


}
