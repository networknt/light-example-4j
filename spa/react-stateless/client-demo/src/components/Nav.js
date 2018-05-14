import React, { Component } from 'react';
import { Link } from 'react-router-dom';
import '../App.css';

class Nav extends Component {

  render() {
    return (
      <nav className="navbar navbar-default">
        <div className="navbar-header">
          <Link className="navbar-brand" to="/">OAuth 2.0 JWT Demo</Link>
        </div>
        <ul className="nav navbar-nav">
          <li>
            <Link to="/">Home</Link>
          </li>
          <li>
           <Link to="/special">API</Link>
          </li>
          <li>
           <Link to="/login">Log In</Link>
          </li>
          <li>
           <Link to="/logout">Log Out</Link>
          </li>
        </ul>
      </nav>
    );
  }
}

export default Nav;