## Compile
To compile the project run:m

```
ant compile
```

## Testing (JUnit)
To test the model and generate a testing report run:

```
ant test
```

Then navigate to `report/html/index.html` with a web browser. 

```
open report/html/index.html
```

## Build a Distributable (JAR file)
To build an executable jar file, run:

```
ant jar
```


## Running

### Without Ant, Command line
To run the application via the portable jar file (this is by default created at `dist/ImageViewer.jar` after building) run:

```
java -jar <jar file>
```

Example:

```
java -jar dist/ImageViewer.jar # This is the default jar location after build
```
### With Ant
To run the application through `ant` (also performs application build if necessary), navigate
to the root of this repository and run:

```
ant
```

## JavaDoc
To create the backend (API-level) documentation run:

```
ant javadoc
```

Then navigate to `doc/index.html` with a web browser. 

```
open doc/index.html
```

## Cleaning
To clean created files from building, navigate to the root of this repository and run:

```
ant clean
```
