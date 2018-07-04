import { Injectable } from '@angular/core';
import {ApolloLink, from} from 'apollo-link';
import {InMemoryCache} from 'apollo-cache-inmemory';
import {HttpLink} from 'apollo-angular-link-http';
import {Apollo} from 'apollo-angular';
import {CookieService} from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class GraphQlService {
  public apollo: Apollo;

  constructor(apollo: Apollo, private httpLink: HttpLink, private cookieService: CookieService) {
    this.apollo = apollo;
    console.log('constructing graphql service');
    if (this.cookieService.get('csrf')) {
      this.initApollo(this.cookieService.get('csrf'));
    } else {
      window.open('https://localhost:6881/oauth2/code?response_type=code&client_id=2015b892-8171-498d-92d8-19f92c448e5f', '_self');
    }
  }

  initApollo(xsrfToken) {
    const http = this.httpLink.create({
      uri: 'https://localhost:8443/graphql',
      withCredentials: true
    });

    const authMiddleware = new ApolloLink((operation, forward) => {
      operation.setContext(({headers = {}}) => ({
        headers: {
          ...headers,
          'X-CSRF-TOKEN': xsrfToken
        }
      }));
      return forward(operation);
    });

    this.apollo.create({
      link: from([authMiddleware, http]),
      cache: new InMemoryCache()
    });
    console.log('Apollo initialized.');
  }
}
