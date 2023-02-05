## Exchange Rates API

Tests for Exchange Rates API focused on `/latest` endpoint. For documentation
of the API visit https://exchangeratesapi.io/documentation

### Configuration

To properly configure project edit `config.properties` file located in
`/src/test/resources`. Example configuration contains two main properties, 
`baseUrl` for default URI to API and `apiKey` for invidual access key to API.
Example:

#### Configuration file (/src/test/resources/config.properties)
```
baseUrl=http://api.exchangeratesapi.io/v1
apiKey=7c2f911641bd26da43ec931c0ee885ea
```

### Run tests

To start tests just use this command in project main path:
```
./gradlew clean cucumber
```