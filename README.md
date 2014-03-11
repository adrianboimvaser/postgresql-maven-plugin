# postgresql-maven-plugin [![Build Status](https://travis-ci.org/adrianboimvaser/postgresql-maven-plugin.png)](https://travis-ci.org/adrianboimvaser/postgresql-maven-plugin)

A [Maven](http://maven.apache.org/) plugin for controlling [PostgreSQL](http://www.postgresql.org/). There are goals for starting, stopping, initializing a DB, etc. The main purpose is running integration tests.

The project is in a very early stage. Currently there are only 4 goals with very few options.

Usage example:

    <plugin>
        <groupId>com.github.adrianboimvaser</groupId>
        <artifactId>postgresql-maven-plugin</artifactId>
        <version>0.2-SNAPSHOT</version>
        <configuration>
            <pgsqlHome>${project.build.directory}/pgsql</pgsqlHome>
            <dataDir>${project.build.directory}/data</dataDir>
            <skip>${skipITs}</skip>
        </configuration>
        <executions>
            <execution>
                <id>init-db</id>
                <phase>pre-integration-test</phase>
                <goals>
                    <goal>initdb</goal>
                </goals>
                <configuration>
                    <username>postgres</username>
                    <passwordFile>${basedir}/password.txt</passwordFile>
                    <encoding>UTF8</encoding>
                    <locale>en_US</locale>
                </configuration>
            </execution>
            <execution>
                <id>start-postgresql</id>
                <phase>pre-integration-test</phase>
                <goals>
                    <goal>start</goal>
                </goals>
            </execution>
            <execution>
                <id>create-db</id>
                <phase>pre-integration-test</phase>
                <goals>
                    <goal>createdb</goal>
                </goals>
                <configuration>
                    <username>postgres</username>
                    <databaseName>products</databaseName>
                </configuration>
            </execution>
            <execution>
                <id>stop-postgresql</id>
                <phase>post-integration-test</phase>
                <goals>
                    <goal>stop</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
