import {Component, OnInit, Input} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {TodoService} from 'shared/todo.service';


@Component({
  selector: 'my-operate',
  templateUrl: 'app/edit/edit.component.html',
  styleUrls: ['app/edit/edit.component.css']
})
export class EditComponent implements OnInit {
  isAdd: boolean;
  operateTitle: string;
  jsonResult:string
  editId: number;
  todo:any = {};



  constructor(
    private _router: Router,
    private _activatedRoute:ActivatedRoute,
    private _todoService: TodoService,
    private _location:Location,
    ) {}

  ngOnInit() {

    this.operateTitle  = 'Create Todo Event';
    //
  }

  submitForm() {

      this.addTodo();

  }




  addTodo() {

    let new_todo = {
      "title": this.todo.title,
      "comleted": this.todo.completed,
      "order": this.todo.order
    };

    this.jsonResult = JSON.stringify(new_todo);
    this._todoService.addTodo(new_todo);

   // this._router.navigate(['']);
  }

 

  cancleOperate() {
    this._location.back();
  }

}
