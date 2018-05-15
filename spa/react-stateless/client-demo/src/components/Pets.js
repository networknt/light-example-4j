import React, { Component } from 'react';
import { Link } from 'react-router-dom'
import Nav from './Nav';
import { getPetsData } from '../utils/api';
import { instanceOf } from 'prop-types';
import { withCookies, Cookies } from 'react-cookie';

class Pets extends Component {
  
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  constructor() {
    super();
    this.state = { pets: [] };
  }

  getPets(csrf) {
    getPetsData(csrf).then((pets) => {
      this.setState({ pets });
    });
  }
  componentWillMount() {
  }

  componentDidMount() {
    const { cookies } = this.props;
    console.log("csrf = " + cookies.get('csrf'));
    this.getPets(cookies.get('csrf'));
  }

  render() {

    const { pets } = this.state;

    return (
      <div>
        <h3>Privileged Pets</h3>
        <hr/>

        { pets.map((pet, index) => (
              <div className="col-sm-6" key={index}>
                <div className="panel panel-danger">
                  <div className="panel-heading">
                    <h3 className="panel-title"><span className="btn">#{ pet.id }</span></h3>
                  </div>
                  <div className="panel-body">
                    <p> { pet.name } </p>
                    <p> { pet.tag }</p>
                  </div>
                </div>
              </div>
          ))}
      </div>
    );
  }
}

export default withCookies(Pets);