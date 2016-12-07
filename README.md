### Overview

The Cucumber Parallel Testing project allows parallel testing of Cucumber JVM tests. 
Multiple threads are executed in parallel, each triggering a Cucumber test runner which in turn runs 
features that have not been run by the other threads. 

Each test runner attempts to run a feature file. If the feature file has not already been run it will add it to 
a register and run it. If the feature file has already been registered it will skip it and 
attempt to run the next one in the same manner. In this manner feature files are run in turn by processing
threads.

#### Comparison with other techniques

Other techniques involve invasive management of tags or branches in order to organise feature files/scenarios
in such a way that runners will specifically target groupings tests. The disadvantages of this approach are -

* Developers need to be aware of this strategy and ensure they implement feature files/scenarios in such a way that the load will be spread equally
* It is likely that one group of tests will take an unnecessarily long period of time to execute whilst others are idle, as the are specifically targeting tests

The advantage of this approach is that 

* Once the parallel runners are set-up there is no need for the developer to have to worry about how feature files/scenario are branched/tagged. The test framework simply processes the next feature file that has not been run.
* Because feature files are run in-turn, thread idle time is limited and threads are likely to only be idle towards the end of testing when all features files are either being processed or have been processed

The disadvantage of this approach is that it works on a per-feature basis. It is not appropriate for cases where there are many scenarios in few feature files (tagging may be better in this case).

#### Implementation

Create a test runner for every thread to be run. The report locations should be unique.

eg. 

```
@RunWith(ParallelCucumber.class)
@CucumberOptions(glue = {"uk.co.hmtt.parallel.bdd.tests"},
        format = {"pretty", "html:target/cucumber/cucumber-html-report-1", "json:target/cucumber/json/cucumber-1.json"},
        features = {"src/test/resources/features"},
        tags = {"~@wip"})
public class FirstParallelTestRunner {}
```

```
@RunWith(ParallelCucumber.class)
@CucumberOptions(glue = {"uk.co.hmtt.parallel.bdd.tests"},
        format = {"pretty", "html:target/cucumber/cucumber-html-report-2", "json:target/cucumber/json/cucumber-2.json"},
        features = {"src/test/resources/features"},
        tags = {"~@wip"})
public class SecondParallelTestRunner {}
```

Update your POM file to ensure that multiple threads are run. The number of threads should match
the number of runners. Ensure you incude all parallel runners; and exclude any other tests

eg.

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-failsafe-plugin</artifactId>
    <version>2.16</version>
    <configuration>
        <forkCount>2</forkCount>
        <reuseForks>true</reuseForks>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>integration-test</goal>
                <goal>verify</goal>
            </goals>
            <configuration>
                <includes>
                    <include>**/*ParallelTestRunner.java</include>
                </includes>
            </configuration>
        </execution>
    </executions>
</plugin>
```

By default one report will be produced per thread. In order to consolidate all reports under a single
thread, use the maven cucumber reporting plugin and point it to the output location of the Json report files.

eg.

```
<plugin>
    <groupId>net.masterthought</groupId>
    <artifactId>maven-cucumber-reporting</artifactId>
    <version>2.8.0</version>
    <executions>
        <execution>
            <id>execution</id>
            <phase>verify</phase>
            <goals>
                <goal>generate</goal>
            </goals>
            <configuration>
                <projectName>cucumber-jvm-example</projectName>
                <outputDirectory>${project.build.directory}</outputDirectory>
                <cucumberOutput>${project.build.directory}/cucumber/json/</cucumberOutput>
            </configuration>
        </execution>
    </executions>
</plugin>
```

You should now be able to run your Cucumber tests as per normal. Note, Cucumber tests must 
 be run using Maven in order to be run in parallel; and you should also use "clean" in order
 to remove the test record file between test runs. eg. mvn clean install

#### Cucumber Report

http://hmtt.co.uk/reports/cucumber-parallel-testing/feature-overview.html

#### Sonar Report

https://sonarqube.com/dashboard/index?id=uk.co.hmtt%3Acucumber-parallel-testing