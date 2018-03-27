# status-fiddle

Online UI editor for status-react https://status-im.github.io/fiddle/

## Setup

Checkout `https://github.com/status-im/status-react` into `status-react` folder 

You should have `status-react` and `status-fiddle` in the same directory

```
+-- status-dev-folder
|   +-- status-react // status-react repo 
|   +-- status-fiddle // this repo

```

Make simlink to icons  

```
ln -s /Users/*/status-dev-folder/status-react/resources/icons /Users/*/status-dev-folder/status-fiddle/resources/icons
```

Make simlink to components  

```
ln -s /Users/*/status-dev-folder/status-react/src/status_im/ui/components /Users/*/status-dev-folder/status-fiddle/src/cljs/status_im/ui/components
```

### Installation:

```
npm install
npm run build
```

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
