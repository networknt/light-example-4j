import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import Nav from './Nav';

class Home extends Component {

  render() {
    return (
      <div>
        <Nav />
        Everyone can access home page.
      </div>
    );
  }
}

export default Home;
