import React, {Component} from 'react';
import {
    BrowserRouter,
    Link,
    Route,
    Switch,
} from 'react-router-dom';

import './App.css';
import ChannelsListWithData from './components/ChannelsListWithData';
import NotFound from './components/NotFound';
import ChannelDetails from './components/ChannelDetails';

import {SubscriptionClient, addGraphQLSubscriptions} from 'subscriptions-transport-ws';

import {
    ApolloClient,
    ApolloProvider,
    createNetworkInterface,
    toIdValue,
} from 'react-apollo';


const networkInterface = createNetworkInterface({uri: 'http://localhost:8080/graphql'});
networkInterface.use([{
    applyMiddleware(req, next) {
        setTimeout(next, 500);
    },
}]);

const wsClient = new SubscriptionClient(`ws://localhost:8080/subscriptions`, {
    reconnect: true
});
//
const networkInterfaceWithSubscriptions = addGraphQLSubscriptions(
    networkInterface,
    wsClient
);

function dataIdFromObject(result) {
    if (result.__typename) {
        if (result.id !== undefined) {
            return `${result.__typename}:${result.id}`;
        }
    }
    return null;
}

const client = new ApolloClient({
    networkInterface: networkInterfaceWithSubscriptions,
    customResolvers: {
        Query: {
            channel: (_, args) => {
                return toIdValue(dataIdFromObject({__typename: 'Channel', id: args['id']}))
            },
        },
    },
    dataIdFromObject,
});

class App extends Component {
    render() {
        return (
            <ApolloProvider client={client}>
                <BrowserRouter>
                    <div className="App">
                        <Link to="/" className="navbar">React + GraphQL Tutorial</Link>
                        <Switch>
                            <Route exact path="/" component={ChannelsListWithData}/>
                            <Route path="/channel/:channelId" component={ChannelDetails}/>
                            <Route component={NotFound}/>
                        </Switch>
                    </div>
                </BrowserRouter>
            </ApolloProvider>
        );
    }
}

export default App;
