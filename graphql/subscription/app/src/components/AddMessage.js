import React from 'react';
import {gql, graphql} from 'react-apollo';
import {channelDetailsQuery} from './ChannelDetails';
import {withRouter} from 'react-router';

const AddMessage = ({mutate, match}) => {
    const handleKeyUp = (evt) => {
        if (evt.keyCode === 13) {
            mutate({
                variables: {
                    message: {
                        channelId: match.params.channelId,
                        text: evt.target.value
                    }
                },
                optimisticResponse: {
                    addMessage: {
                        text: evt.target.value,
                        id: Math.round(Math.random() * -1000000),
                        __typename: 'Message',
                    },
                },
                update: (store, {data: {addMessage}}) => {
                    // Read the data from the cache for this query.
                    const data = store.readQuery({
                        query: channelDetailsQuery,
                        variables: {
                            channelId: match.params.channelId,
                        }
                    });

                    if (!data.channel.messages.find((msg) => msg.id === addMessage.id)) {
                        // Add our Message from the mutation to the end.
                        data.channel.messages.push(addMessage);
                    }

                    // Write the data back to the cache.
                    store.writeQuery({
                        query: channelDetailsQuery,
                        variables: {
                            channelId: match.params.channelId,
                        },
                        data
                    });
                },
            });
            evt.target.value = '';
        }
    };

    return (
        <div className="messageInput">
            <input
                type="text"
                placeholder="New message"
                onKeyUp={handleKeyUp}
            />
        </div>
    );
};

const addMessageMutation = gql`
    mutation addMessage($message: MessageInput!) {
        addMessage(message: $message) {
            id
            text
        }
    }
`;


const AddMessageWithMutation = graphql(
    addMessageMutation,
)(withRouter(AddMessage));

export default AddMessageWithMutation;
