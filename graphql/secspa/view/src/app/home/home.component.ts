import { Component, OnInit } from '@angular/core';
import gql from 'graphql-tag';
import {GraphQlService} from '../graph-ql.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  private theNumber: Number;

  constructor(private graphqlService: GraphQlService) { }

  ngOnInit() {
    this.graphqlService.apollo.query({
      query: gql(`
      {
        numberHolder {
          theNumber
        }
      }
      `)
    }).subscribe((response) => {
      this.theNumber = response.data['numberHolder'].theNumber;
    });
  }
}
