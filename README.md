[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=todoDemo&metric=alert_status&token=92c2db6f780ba57f20c338f7537a0f7ba40b54ea)](https://sonarcloud.io/dashboard?id=todoDemo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=todoDemo&metric=coverage&token=92c2db6f780ba57f20c338f7537a0f7ba40b54ea)](https://sonarcloud.io/dashboard?id=todoDemo)
[![geekle-todo](https://img.shields.io/endpoint?url=https://dashboard.cypress.io/badge/simple/gsebq7&style=flat&logo=cypress)](https://dashboard.cypress.io/projects/gsebq7/runs)

## Deployed App on Heroku

https://gbloch-geekle-todo.herokuapp.com/

# JHipster Configuration Options

![conf](https://i.imgur.com/ZGqBwre.png)

# JHipster JDL Schema

![jdl-schema](https://i.imgur.com/YfnFvbI.png)

# JHipster JDL Configuration

![jdl-conf](https://i.imgur.com/9iJI7iT.png)


# JHipster JSON Configuration

```json
{
  "generator-jhipster": {
    "applicationType": "monolith",
    "baseName": "todoDemo",
    "jhipsterVersion": "7.1.0",
    "skipClient": false,
    "skipServer": false,
    "skipUserManagement": false,
    "skipCheckLengthOfIdentifier": false,
    "skipFakeData": false,
    "jhiPrefix": "jhi",
    "entitySuffix": "",
    "dtoSuffix": "DTO",
    "testFrameworks": ["cypress", "gatling", "cucumber"],
    "blueprints": [],
    "otherModules": [],
    "pages": [],
    "creationTimestamp": 1627051217967,
    "serviceDiscoveryType": false,
    "reactive": false,
    "authenticationType": "jwt",
    "packageName": "io.gbloch.tododemo",
    "serverPort": "8080",
    "cacheProvider": "ehcache",
    "enableHibernateCache": false,
    "databaseType": "sql",
    "devDatabaseType": "postgresql",
    "prodDatabaseType": "postgresql",
    "buildTool": "maven",
    "serverSideOptions": [],
    "websocket": false,
    "searchEngine": false,
    "messageBroker": false,
    "enableSwaggerCodegen": false,
    "clientFramework": "angularX",
    "withAdminUi": true,
    "clientTheme": "none",
    "enableTranslation": true,
    "nativeLanguage": "en",
    "packageFolder": "io/gbloch/tododemo",
    "jwtSecretKey": "SECRET",
    "devServerPort": 4200,
    "clientPackageManager": "npm",
    "clientThemeVariant": "",
    "languages": ["en", "fr", "ja"],
    "cypressCoverage": true,
    "entities": ["TodoList", "Todo", "Category", "Tag"],
    "lastLiquibaseTimestamp": 1627142322000
  }
}
```

# todoDemo

This application was generated using JHipster 7.1.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.1.0](https://www.jhipster.tech/documentation-archive/v7.1.0).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Angular CLI][] with [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Edit [src/main/webapp/app/app.module.ts](src/main/webapp/app/app.module.ts) file:

```
import 'leaflet/dist/leaflet.js';
```

Edit [src/main/webapp/content/scss/vendor.scss](src/main/webapp/content/scss/vendor.scss) file:

```
@import '~leaflet/dist/leaflet.css';
```

Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

### Using Angular CLI

You can also use [Angular CLI][] to generate some custom client code.

For example, the following command:

```
ng generate component my-component
```

will generate few files:

```
create src/main/webapp/app/my-component/my-component.component.html
create src/main/webapp/app/my-component/my-component.component.ts
update src/main/webapp/app/app.module.ts
```

## Building for production

### Packaging as jar

To build the final jar and optimize the todoDemo application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

UI end-to-end tests are powered by [Cypress][]. They're located in [src/test/javascript/cypress](src/test/javascript/cypress)
and can be run by starting Spring Boot in one terminal (`./mvnw spring-boot:run`) and running the tests (`npm run e2e`) in a second one.

#### Lighthouse audits

You can execute automated [lighthouse audits][https://developers.google.com/web/tools/lighthouse/] with [cypress audits][https://github.com/mfrachet/cypress-audit] by running `npm run e2e:cypress:audits`.
You should only run the audits when your application is packaged with the production profile.
The lighthouse report is created in `target/cypress/lhreport.html`

### Other tests

Performance tests are run by [Gatling][] and written in Scala. They're located in [src/test/gatling](src/test/gatling).

To use those tests, you must install Gatling from [https://gatling.io/](https://gatling.io/).

For more information, refer to the [Running tests page][].

### E2E Webapp Code Coverage

When using Cypress, you can generate code coverage report by running your dev server with instrumented code:

Build your Angular application with instrumented code:

    npm run webapp:instrumenter

Start your backend without compiling frontend:

    npm run backend:start

Start your Cypress end to end testing:

    npm run e2e:cypress:coverage

The coverage report is generated under `./coverage/lcov-report/`

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

For example, to start a postgresql database in a docker container, run:

```
docker-compose -f src/main/docker/postgresql.yml up -d
```

To stop it and remove the container, run:

```
docker-compose -f src/main/docker/postgresql.yml down
```

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
./mvnw -Pprod verify jib:dockerBuild
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.1.0 archive]: https://www.jhipster.tech/documentation-archive/v7.1.0
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.1.0/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.1.0/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.1.0/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.1.0/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.1.0/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.1.0/setting-up-ci/
[gatling]: https://gatling.io/
[node.js]: https://nodejs.org/
[webpack]: https://webpack.github.io/
[angular cli]: https://cli.angular.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[jasmine]: https://jasmine.github.io/2.0/introduction.html
[cypress]: https://www.cypress.io/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/
