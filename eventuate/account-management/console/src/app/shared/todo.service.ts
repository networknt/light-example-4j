import {Injectable} from '@angular/core';
import {Http, RequestOptions, Response, Headers} from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

const COMMAND_URL = `http://localhost:8083/v1/todos`;
const QUERY_URL = `http://localhost:8082/v1/todos`;


@Injectable()
export class TodoService {
  constructor( private http:Http ) {}

  request(url:string, opts:any) {
    return this.http.request(url, new RequestOptions(opts))
    .map(res => {
      let _res = res.json();
      if(opts.id){
        for(let i=0; i<_res.length; i++){
          if(_res[i].id == opts.id){
            _res = _res[i];
          }
        }
      }
      if(opts.collection){
        let temp:any = [];
        for(let i=0; i<_res.length; i++){
          if(_res[i].collection == opts.collection){
            temp.push(_res[i]);
          }
        }
        _res = temp;
      }
      return _res;
    })
  }

  get(url:string, opts?:Object) {
    return this.request(url, (<any>Object).assign({
      method: 'get'
    }, opts));
  }

  getTodoData() {

    return this.http.get(QUERY_URL, this.buildRequestOption());
  }

  addTodo(obj:Object = {}) {
    let body = JSON.stringify(obj);

    return this.http.post(COMMAND_URL, body, this.buildRequestOption()).map(res => res.json());
  }

 buildRequestOption(): RequestOptions {
    let headers = new Headers({ 'Content-Type': 'application/json' });
    headers.append('Accept', 'application/json');
    let options = new RequestOptions({ headers: headers });
    return  options;
 }


}
