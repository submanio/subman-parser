# [subman.io](http://subman.io/) [![Build Status](https://travis-ci.org/submanio/subman-parser.svg)](https://travis-ci.org/submanio/subman-parser)

Service for fast subtitle searching.


## Installation

First you need to install lein, bower, mongodb and elasticsearch.

Then install deps:

```bash
lein deps
```

And run with:

```bash
lein run
```

For building jar run:

```bash
lein uberjar
```

For running server side tests run:

```bash
lein test
```

## Deploy

For testing local changes you need to build docker image:

```bash
docker build -t submanio/subman-parser .
```
