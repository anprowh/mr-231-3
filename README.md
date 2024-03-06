# MR-231-3 Parser
Implementation of MR-231-3 parser

### Main funciton
Main function of the project prints result of parsing example messages. Messages are hardcoded into [`App.java`](https://github.com/anprowh/mr-231-3/blob/main/startApp/src/main/java/org/example/App.java) file.

### Parser
Parser class is implemented in `mr-231-3` directory. Example usage can be found in main function in [`App.java`](https://github.com/anprowh/mr-231-3/blob/main/startApp/src/main/java/org/example/App.java) file.

To connect parser as module to your project you can refer to [`pom.xml`](https://github.com/anprowh/mr-231-3/blob/main/build/pom.xml) in `build` directory. Add `<module>../mr-231-3</module>` under `<modules>` section.
Then you need to add it as dependency under `<dependencies>` section to `pom.xml` file of module that is actually going to use it. Example in [`pom.xml`](https://github.com/anprowh/mr-231-3/blob/main/startApp/pom.xml) in startApp module.
```xml
<dependency>
    <groupId>org.example</groupId>
    <artifactId>mr-231-3</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Docker
To build docker image run the following command:

`docker build . --tag mr2313`

You can choose tag to your liking. To run project's main function in container run command:

`docker run mr2313`
