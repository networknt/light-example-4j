In this stateless single page application with React, the client is a React application running with Nodejs server for development. For production, the SPA should be served by the light-router instance instead so that there is no need for light-router to enable CORS handler.

There are several configuration changes that need to be done for light-router.

* Enable CORS handler in service.yml for development environment only.
* Add StatelessAuthHandler to the path provider so that it can handler the authorization code redirected from the OAuth 2.0 provider.
* Add StatelessCsrfHandler middleware handler to service.yml to check the csrf header agains csrf token in jwt.


For the detailed configuration, please refer to light-config-test/light-router/spa-stateless
