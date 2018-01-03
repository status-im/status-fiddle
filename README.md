# status-fiddle

Online UI editor for status-react

## Development Mode

### Run application:

```
lein figwheel-repl
```

Figwheel will automatically push cljs changes to the browser.

Wait a bit, then browse to [http://localhost:3449](http://localhost:3449).

## Production Mode

### Build CLJS:

```
lein build-prod
```

Open `resources/public/index.html` in the browser.
